package QueuePass.ryan.dto;

import QueuePass.ryan.model.Enum.PasswordType;

public record CreatePassword(
        PasswordType passwordType
) {
}
