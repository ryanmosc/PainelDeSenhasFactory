package QueuePass.ryan.model;

import QueuePass.ryan.model.Enum.PasswordStatus;
import QueuePass.ryan.model.Enum.PasswordType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Senha {
    private Long id;
    private String code;
    private PasswordType passwordType;
    private PasswordStatus passwordStatus;
    private LocalDateTime createdAt;
    private LocalDateTime calledAt;
    private LocalDateTime endDate;
    private String guiche;
}