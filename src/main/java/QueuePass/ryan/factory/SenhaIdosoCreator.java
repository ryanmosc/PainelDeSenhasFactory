package QueuePass.ryan.factory;

import QueuePass.ryan.model.Enum.PasswordType;
import QueuePass.ryan.model.Senha;
import QueuePass.ryan.model.SenhaIdoso;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class SenhaIdosoCreator implements SenhaCreator {

    private final AtomicLong contador = new AtomicLong(1);

    @Override
    public Senha criar(Long id) {
        String code = "I" + String.format("%03d", contador.getAndIncrement());
        return new SenhaIdoso(id, code);
    }

    @Override
    public PasswordType getTipo() {
        return PasswordType.IDOSO;
    }

    @Override
    public void resetar() {
        contador.set(1);
    }
}
