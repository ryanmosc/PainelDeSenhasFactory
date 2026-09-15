package QueuePass.ryan.model;

import QueuePass.ryan.model.Enum.PasswordStatus;
import QueuePass.ryan.model.Enum.PasswordType;

import java.time.LocalDateTime;

public class SenhaComum extends Senha {
    public SenhaComum(Long id, String code) {
        this.setId(id);
        this.setCode(code);
        this.setPasswordType(PasswordType.COMUM);
        this.setPasswordStatus(PasswordStatus.AGUARDANDO);
        this.setCreatedAt(LocalDateTime.now());
    }
}
