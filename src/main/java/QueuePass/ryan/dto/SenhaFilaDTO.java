package QueuePass.ryan.dto;

import QueuePass.ryan.model.Senha;

public record SenhaFilaDTO(
        Senha senha,
        long posicaoNaFila,
        double tempoEsperaEstimadoMinutos
) {
}