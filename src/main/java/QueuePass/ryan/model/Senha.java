package QueuePass.ryan.model;

import QueuePass.ryan.model.Enum.PasswordStatus;
import QueuePass.ryan.model.Enum.PasswordType;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class Senha {

    private Long id;

    private String code;

    private PasswordType passwordType;

    private PasswordStatus passwordStatus;

    private LocalDateTime createdAt;

    private LocalDateTime endDate;


}
