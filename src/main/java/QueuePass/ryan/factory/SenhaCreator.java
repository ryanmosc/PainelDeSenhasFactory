package QueuePass.ryan.factory;

import QueuePass.ryan.model.Enum.PasswordType;
import QueuePass.ryan.model.Senha;

public interface SenhaCreator {

    Senha criar(Long id);

    PasswordType getTipo();

    void resetar();
}
