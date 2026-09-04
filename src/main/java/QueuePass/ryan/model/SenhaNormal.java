package QueuePass.ryan.model;

import QueuePass.ryan.model.Enum.PasswordStatus;
import QueuePass.ryan.model.Enum.PasswordType;

import java.time.LocalDateTime;

public class SenhaNormal extends Senha {

    public SenhaNormal(Long id, String code) {
        setId(id);
        setCode(code);
        setPasswordType(PasswordType.NORMAL);
        setPasswordStatus(PasswordStatus.AGUARDANDO);
        setCreatedAt(LocalDateTime.now());
    }
}
