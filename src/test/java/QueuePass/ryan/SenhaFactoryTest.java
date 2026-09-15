package QueuePass.ryan;

import QueuePass.ryan.factory.SenhaCreator;
import QueuePass.ryan.factory.SenhaFactory;
import QueuePass.ryan.factory.SenhaComumCreator;
import QueuePass.ryan.factory.SenhaVIPCreator;
import QueuePass.ryan.factory.SenhaIdosoCreator;
import QueuePass.ryan.model.Enum.PasswordStatus;
import QueuePass.ryan.model.Enum.PasswordType;
import QueuePass.ryan.model.Senha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SenhaFactoryTest {

    private SenhaComumCreator comumCreator;
    private SenhaVIPCreator vipCreator;
    private SenhaIdosoCreator idosoCreator;
    private SenhaFactory factory;

    @BeforeEach
    void setUp() {
        comumCreator = new SenhaComumCreator();
        vipCreator = new SenhaVIPCreator();
        idosoCreator = new SenhaIdosoCreator();
        List<SenhaCreator> creators = List.of(comumCreator, vipCreator, idosoCreator);
        factory = new SenhaFactory(creators);
    }

    @Test
    void deveCriarSenhaComumComPrefixoC() {
        Senha senha = comumCreator.criar(0L);
        assertThat(senha.getCode()).startsWith("C");
        assertThat(senha.getPasswordType()).isEqualTo(PasswordType.COMUM);
        assertThat(senha.getPasswordStatus()).isEqualTo(PasswordStatus.AGUARDANDO);
        assertThat(senha.getCreatedAt()).isNotNull();
    }

    @Test
    void deveCriarSenhaVIPComPrefixoV() {
        Senha senha = vipCreator.criar(0L);
        assertThat(senha.getCode()).startsWith("V");
        assertThat(senha.getPasswordType()).isEqualTo(PasswordType.VIP);
    }

    @Test
    void deveCriarSenhaIdosoComPrefixoI() {
        Senha senha = idosoCreator.criar(0L);
        assertThat(senha.getCode()).startsWith("I");
        assertThat(senha.getPasswordType()).isEqualTo(PasswordType.IDOSO);
    }

    @Test
    void deveLancarExcecaoParaTipoNaoSuportado() {
        SenhaFactory emptyFactory = new SenhaFactory(List.of());
        assertThatThrownBy(() -> emptyFactory.criarSenha(PasswordType.COMUM, 0L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deveResetarTodosOsContadoresViaFactory() {
        factory.criarSenha(PasswordType.COMUM, 0L);
        factory.criarSenha(PasswordType.VIP, 0L);
        factory.criarSenha(PasswordType.IDOSO, 0L);
        factory.resetar();

        Senha c = factory.criarSenha(PasswordType.COMUM, 1L);
        Senha v = factory.criarSenha(PasswordType.VIP, 2L);
        Senha i = factory.criarSenha(PasswordType.IDOSO, 3L);

        assertThat(c.getCode()).isEqualTo("C001");
        assertThat(v.getCode()).isEqualTo("V001");
        assertThat(i.getCode()).isEqualTo("I001");
    }
}
