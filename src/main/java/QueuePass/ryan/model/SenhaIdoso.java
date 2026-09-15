package QueuePass.ryan.model;

import QueuePass.ryan.model.Enum.PasswordStatus;
import QueuePass.ryan.model.Enum.PasswordType;

import java.time.LocalDateTime;

public class SenhaIdoso extends Senha {
    public SenhaIdoso(Long id, String code) {
        this.setId(id);
        this.setCode(code);
        this.setPasswordType(PasswordType.IDOSO);
        this.setPasswordStatus(PasswordStatus.AGUARDANDO);
        this.setCreatedAt(LocalDateTime.now());
    }
}
