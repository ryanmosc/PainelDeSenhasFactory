package QueuePass.ryan.model;

import QueuePass.ryan.model.Enum.PasswordStatus;
import QueuePass.ryan.model.Enum.PasswordType;

import java.time.LocalDateTime;

public class SenhaPrioridade extends Senha {

    public SenhaPrioridade(Long id, String code) {
        setId(id);
        setCode(code);
        setPasswordType(PasswordType.PRIORIDADE);
        setPasswordStatus(PasswordStatus.AGUARDANDO);
        setCreatedAt(LocalDateTime.now());
    }
}
