package QueuePass.ryan;

import QueuePass.ryan.factory.SenhaCreator;
import QueuePass.ryan.factory.SenhaFactory;
import QueuePass.ryan.factory.SenhaNormalCreator;
import QueuePass.ryan.factory.SenhaPrioridadeCreator;
import QueuePass.ryan.model.Enum.PasswordStatus;
import QueuePass.ryan.model.Enum.PasswordType;
import QueuePass.ryan.model.Senha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SenhaFactoryTest {

    private SenhaNormalCreator normalCreator;
    private SenhaPrioridadeCreator prioridadeCreator;
    private SenhaFactory factory;

    @BeforeEach
    void setUp() {
        normalCreator = new SenhaNormalCreator();
        prioridadeCreator = new SenhaPrioridadeCreator();
        List<SenhaCreator> creators = List.of(normalCreator, prioridadeCreator);
        factory = new SenhaFactory(creators);
    }

    @Test
    void deveCriarSenhaNormalComPrefixoN() {
        Senha senha = normalCreator.criar(0L);
        assertThat(senha.getCode()).startsWith("N");
        assertThat(senha.getPasswordType()).isEqualTo(PasswordType.NORMAL);
    }

    @Test
    void deveCriarSenhaNormalComCodigoFormatado() {
        Senha s1 = normalCreator.criar(0L);
        assertThat(s1.getCode()).isEqualTo("N001");

        Senha s2 = normalCreator.criar(1L);
        assertThat(s2.getCode()).isEqualTo("N002");
    }

    @Test
    void deveResetarContadorNormal() {
        normalCreator.criar(0L);
        normalCreator.criar(1L);
        normalCreator.resetar();

        Senha s = normalCreator.criar(2L);
        assertThat(s.getCode()).isEqualTo("N001");
    }

    @Test
    void deveCriarSenhaNormalComStatusAguardando() {
        Senha senha = normalCreator.criar(0L);
        assertThat(senha.getPasswordStatus()).isEqualTo(PasswordStatus.AGUARDANDO);
    }

    @Test
    void deveCriarSenhaNormalComCreatedAtPreenchido() {
        Senha senha = normalCreator.criar(0L);
        assertThat(senha.getCreatedAt()).isNotNull();
    }

    @Test
    void deveRetornarTipoNormalGetTipo() {
        assertThat(normalCreator.getTipo()).isEqualTo(PasswordType.NORMAL);
    }

    @Test
    void deveCriarSenhaPrioridadeComPrefixoP() {
        Senha senha = prioridadeCreator.criar(0L);
        assertThat(senha.getCode()).startsWith("P");
        assertThat(senha.getPasswordType()).isEqualTo(PasswordType.PRIORIDADE);
    }

    @Test
    void deveCriarSenhaPrioridadeComCodigoFormatado() {
        Senha s1 = prioridadeCreator.criar(0L);
        assertThat(s1.getCode()).isEqualTo("P001");

        Senha s2 = prioridadeCreator.criar(1L);
        assertThat(s2.getCode()).isEqualTo("P002");
    }

    @Test
    void deveResetarContadorPrioridade() {
        prioridadeCreator.criar(0L);
        prioridadeCreator.criar(1L);
        prioridadeCreator.resetar();

        Senha s = prioridadeCreator.criar(2L);
        assertThat(s.getCode()).isEqualTo("P001");
    }

    @Test
    void deveRetornarTipoPrioridadeGetTipo() {
        assertThat(prioridadeCreator.getTipo()).isEqualTo(PasswordType.PRIORIDADE);
    }

    @Test
    void deveCriarSenhaNormalViaFactory() {
        Senha senha = factory.criarSenha(PasswordType.NORMAL, 0L);
        assertThat(senha.getPasswordType()).isEqualTo(PasswordType.NORMAL);
        assertThat(senha.getCode()).startsWith("N");
    }

    @Test
    void deveCriarSenhaPrioridadeViaFactory() {
        Senha senha = factory.criarSenha(PasswordType.PRIORIDADE, 0L);
        assertThat(senha.getPasswordType()).isEqualTo(PasswordType.PRIORIDADE);
        assertThat(senha.getCode()).startsWith("P");
    }

    @Test
    void deveLancarExcecaoParaTipoNaoSuportado() {
        SenhaFactory emptyFactory = new SenhaFactory(List.of());
        assertThatThrownBy(() -> emptyFactory.criarSenha(PasswordType.NORMAL, 0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("NORMAL");
    }

    @Test
    void deveResetarTodosOsContadoresViaFactory() {
        factory.criarSenha(PasswordType.NORMAL, 0L);
        factory.criarSenha(PasswordType.PRIORIDADE, 0L);
        factory.resetar();

        Senha normal = factory.criarSenha(PasswordType.NORMAL, 1L);
        Senha prio = factory.criarSenha(PasswordType.PRIORIDADE, 2L);

        assertThat(normal.getCode()).isEqualTo("N001");
        assertThat(prio.getCode()).isEqualTo("P001");
    }

    @Test
    void deveAtribuirIdCorretamenteViaFactory() {
        Senha senha = factory.criarSenha(PasswordType.NORMAL, 42L);
        assertThat(senha.getId()).isEqualTo(42L);
    }

    @Test
    void deveRetornarCreatorCorretoViaGetCreator() {
        SenhaCreator creator = factory.getCreator(PasswordType.PRIORIDADE);
        assertThat(creator).isInstanceOf(SenhaPrioridadeCreator.class);
    }

    @Test
    void deveLancarExcecaoGetCreatorParaTipoNaoSuportado() {
        SenhaFactory emptyFactory = new SenhaFactory(List.of());
        assertThatThrownBy(() -> emptyFactory.getCreator(PasswordType.PRIORIDADE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deveContadoresSeremIndependentesEntreTipos() {
        factory.criarSenha(PasswordType.NORMAL, 0L);
        factory.criarSenha(PasswordType.NORMAL, 1L);
        factory.criarSenha(PasswordType.PRIORIDADE, 2L);

        Senha n3 = factory.criarSenha(PasswordType.NORMAL, 3L);
        Senha p2 = factory.criarSenha(PasswordType.PRIORIDADE, 4L);

        assertThat(n3.getCode()).isEqualTo("N003");
        assertThat(p2.getCode()).isEqualTo("P002");
    }
}
