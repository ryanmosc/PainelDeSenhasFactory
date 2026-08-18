package QueuePass.ryan.dto;

public record EstatisticasDTO(
        long totalAguardando,
        long totalAtendidasHoje,
        double tempoMedioAtendimentoMinutos,
        double tempoMedioEsperaMinutos
) {
}