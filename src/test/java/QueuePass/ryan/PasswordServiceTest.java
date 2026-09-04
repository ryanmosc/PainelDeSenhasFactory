package QueuePass.ryan;

import QueuePass.ryan.dto.CreatePassword;
import QueuePass.ryan.factory.SenhaCreator;
import QueuePass.ryan.factory.SenhaFactory;
import QueuePass.ryan.factory.SenhaNormalCreator;
import QueuePass.ryan.factory.SenhaPrioridadeCreator;
import QueuePass.ryan.model.Enum.PasswordStatus;
import QueuePass.ryan.model.Enum.PasswordType;
import QueuePass.ryan.model.Senha;
import QueuePass.ryan.service.PasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PasswordServiceTest {

    private PasswordService service;

    @BeforeEach
    void setUp() {
        List<SenhaCreator> creators = List.of(new SenhaNormalCreator(), new SenhaPrioridadeCreator());
        SenhaFactory factory = new SenhaFactory(creators);
        service = new PasswordService(factory);
    }

    @Test
    void deveCriarSenhaNormalComCodigoCorreto() {
        Senha senha = service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        assertThat(senha.getCode()).isEqualTo("N001");
        assertThat(senha.getPasswordType()).isEqualTo(PasswordType.NORMAL);
        assertThat(senha.getPasswordStatus()).isEqualTo(PasswordStatus.AGUARDANDO);
    }

    @Test
    void deveCriarSenhaPrioridadeComCodigoCorreto() {
        Senha senha = service.criarSenha(new CreatePassword(PasswordType.PRIORIDADE));
        assertThat(senha.getCode()).isEqualTo("P001");
        assertThat(senha.getPasswordType()).isEqualTo(PasswordType.PRIORIDADE);
    }

    @Test
    void deveIncrementarIdSequencialmente() {
        Senha s1 = service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        Senha s2 = service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        assertThat(s1.getId()).isEqualTo(0L);
        assertThat(s2.getId()).isEqualTo(1L);
    }

    @Test
    void deveCriarMultiplasSenhasComCodigosDistintos() {
        Senha n1 = service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        Senha n2 = service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        Senha p1 = service.criarSenha(new CreatePassword(PasswordType.PRIORIDADE));

        assertThat(n1.getCode()).isEqualTo("N001");
        assertThat(n2.getCode()).isEqualTo("N002");
        assertThat(p1.getCode()).isEqualTo("P001");
    }

    @Test
    void deveLigarSenhasAguardando() {
        service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        service.criarSenha(new CreatePassword(PasswordType.PRIORIDADE));

        List<Senha> aguardando = service.listarAguardando();
        assertThat(aguardando).hasSize(2);
    }

    @Test
    void deveListarAguardandoComPrioridadePrimeiro() {
        service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        service.criarSenha(new CreatePassword(PasswordType.PRIORIDADE));

        List<Senha> aguardando = service.listarAguardando();
        assertThat(aguardando.get(0).getPasswordType()).isEqualTo(PasswordType.PRIORIDADE);
        assertThat(aguardando.get(1).getPasswordType()).isEqualTo(PasswordType.NORMAL);
    }

    @Test
    void deveChamarProximaSenhaComPrioridade() {
        service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        service.criarSenha(new CreatePassword(PasswordType.PRIORIDADE));

        Senha chamada = service.chamarProximaSenha("01");
        assertThat(chamada).isNotNull();
        assertThat(chamada.getPasswordType()).isEqualTo(PasswordType.PRIORIDADE);
        assertThat(chamada.getPasswordStatus()).isEqualTo(PasswordStatus.CHAMADA);
        assertThat(chamada.getGuiche()).isEqualTo("01");
    }

    @Test
    void deveRetornarNullSeFazVaziaAoChamar() {
        Senha chamada = service.chamarProximaSenha("01");
        assertThat(chamada).isNull();
    }

    @Test
    void deveListarSenhasChamadas() {
        service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        service.chamarProximaSenha("01");

        List<Senha> chamadas = service.listarChamadas();
        assertThat(chamadas).hasSize(1);
        assertThat(chamadas.get(0).getPasswordStatus()).isEqualTo(PasswordStatus.CHAMADA);
    }

    @Test
    void deveFinalizarSenha() {
        Senha criada = service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        service.chamarProximaSenha("01");

        Senha finalizada = service.finalizarSenha(criada.getId());
        assertThat(finalizada.getPasswordStatus()).isEqualTo(PasswordStatus.FINALIZADA);
        assertThat(finalizada.getEndDate()).isNotNull();
    }

    @Test
    void deveCancelarSenha() {
        Senha criada = service.criarSenha(new CreatePassword(PasswordType.NORMAL));

        Senha cancelada = service.cancelarSenha(criada.getId());
        assertThat(cancelada.getPasswordStatus()).isEqualTo(PasswordStatus.CANCELADA);
    }

    @Test
    void deveRetornarNullAoFinalizarIdInexistente() {
        Senha resultado = service.finalizarSenha(999L);
        assertThat(resultado).isNull();
    }

    @Test
    void deveResetarSistemaCompleto() {
        service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        service.criarSenha(new CreatePassword(PasswordType.PRIORIDADE));
        service.resetar();

        assertThat(service.listarTodas()).isEmpty();

        Senha nova = service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        assertThat(nova.getCode()).isEqualTo("N001");
        assertThat(nova.getId()).isEqualTo(0L);
    }

    @Test
    void deveBuscarSenhaPorId() {
        Senha criada = service.criarSenha(new CreatePassword(PasswordType.NORMAL));
        Senha encontrada = service.buscarPorId(criada.getId());
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getCode()).isEqualTo(criada.getCode());
    }

    @Test
    void deveRetornarNullParaIdNaoEncontrado() {
        Senha encontrada = service.buscarPorId(999L);
        assertThat(encontrada).isNull();
    }
}
