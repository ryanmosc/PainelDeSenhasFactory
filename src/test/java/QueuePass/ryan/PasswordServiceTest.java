package QueuePass.ryan;

import QueuePass.ryan.dto.CreatePassword;
import QueuePass.ryan.factory.SenhaCreator;
import QueuePass.ryan.factory.SenhaFactory;
import QueuePass.ryan.factory.SenhaComumCreator;
import QueuePass.ryan.factory.SenhaVIPCreator;
import QueuePass.ryan.factory.SenhaIdosoCreator;
import QueuePass.ryan.model.Enum.PasswordStatus;
import QueuePass.ryan.model.Enum.PasswordType;
import QueuePass.ryan.model.Senha;
import QueuePass.ryan.service.PasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

//Comentarios

class PasswordServiceTest {

    private PasswordService service;

    @BeforeEach
    void setUp() {
        List<SenhaCreator> creators = List.of(new SenhaComumCreator(), new SenhaVIPCreator(), new SenhaIdosoCreator());
        SenhaFactory factory = new SenhaFactory(creators);
        service = new PasswordService(factory);
    }

    @Test
    void deveCriarSenhasCorretamente() {
        Senha c = service.criarSenha(new CreatePassword(PasswordType.COMUM));
        Senha v = service.criarSenha(new CreatePassword(PasswordType.VIP));
        Senha i = service.criarSenha(new CreatePassword(PasswordType.IDOSO));

        assertThat(c.getCode()).isEqualTo("C001");
        assertThat(v.getCode()).isEqualTo("V001");
        assertThat(i.getCode()).isEqualTo("I001");
    }

    @Test
    void deveChamarProximaSenhaComPrioridadeCorreta() {
        // Idoso > VIP > Comum
        service.criarSenha(new CreatePassword(PasswordType.COMUM));
        service.criarSenha(new CreatePassword(PasswordType.IDOSO));
        service.criarSenha(new CreatePassword(PasswordType.VIP));

        Senha primeira = service.chamarProximaSenha("01");
        assertThat(primeira.getPasswordType()).isEqualTo(PasswordType.IDOSO);

        Senha segunda = service.chamarProximaSenha("02");
        assertThat(segunda.getPasswordType()).isEqualTo(PasswordType.VIP);

        Senha terceira = service.chamarProximaSenha("03");
        assertThat(terceira.getPasswordType()).isEqualTo(PasswordType.COMUM);
    }

    @Test
    void deveListarAguardandoComPrioridade() {
        service.criarSenha(new CreatePassword(PasswordType.COMUM));
        service.criarSenha(new CreatePassword(PasswordType.VIP));
        service.criarSenha(new CreatePassword(PasswordType.IDOSO));

        List<Senha> aguardando = service.listarAguardando();
        assertThat(aguardando.get(0).getPasswordType()).isEqualTo(PasswordType.IDOSO);
        assertThat(aguardando.get(1).getPasswordType()).isEqualTo(PasswordType.VIP);
        assertThat(aguardando.get(2).getPasswordType()).isEqualTo(PasswordType.COMUM);
    }

    @Test
    void deveRetornarNullSeFilaVaziaAoChamar() {
        Senha chamada = service.chamarProximaSenha("01");
        assertThat(chamada).isNull();
    }

    @Test
    void deveFinalizarSenha() {
        Senha criada = service.criarSenha(new CreatePassword(PasswordType.COMUM));
        service.chamarProximaSenha("01");

        Senha finalizada = service.finalizarSenha(criada.getId());
        assertThat(finalizada.getPasswordStatus()).isEqualTo(PasswordStatus.FINALIZADA);
        assertThat(finalizada.getEndDate()).isNotNull();
    }

    @Test
    void deveCancelarSenha() {
        Senha criada = service.criarSenha(new CreatePassword(PasswordType.COMUM));

        Senha cancelada = service.cancelarSenha(criada.getId());
        assertThat(cancelada.getPasswordStatus()).isEqualTo(PasswordStatus.CANCELADA);
    }

    @Test
    void deveResetarSistemaCompleto() {
        service.criarSenha(new CreatePassword(PasswordType.COMUM));
        service.resetar();

        assertThat(service.listarTodas()).isEmpty();

        Senha nova = service.criarSenha(new CreatePassword(PasswordType.COMUM));
        assertThat(nova.getCode()).isEqualTo("C001");
        assertThat(nova.getId()).isEqualTo(0L);
    }
}
