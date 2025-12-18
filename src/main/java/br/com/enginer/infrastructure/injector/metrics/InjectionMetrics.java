package br.com.enginer.infrastructure.injector.metrics;

/**
 * Armazena as métricas mais recentes e o resumo global.
 * Integra com o histórico de execuções.
 */
public final class InjectionMetrics {

    private static int totalUseCases;
    private static int totalOutboundPorts;
    private static double totalTimeMs;

    /**
     * Construtor privado para nao ser instanciado
     */
    private InjectionMetrics() {}

    /** 
     * Atualiza o estado atual e registra histórico cumulativo 
     */
    public static synchronized void update(int UseCases, int outbounds, double timeMs) {
        totalUseCases = UseCases;
        totalOutboundPorts = outbounds;
        totalTimeMs = timeMs;

        // adiciona também ao histórico cumulativo
        InjectionMetricsHistory.addSnapshot(UseCases, outbounds, timeMs);
    }

    /**
     * 
     */
    public static synchronized void reset() {
        totalUseCases = 0;
        totalOutboundPorts = 0;
        totalTimeMs = 0.0;
        InjectionMetricsHistory.reset();
    }

    /**
     * @return
     */
    public static int getTotalUseCases() {
        return totalUseCases;
    }

    /**
     * @return
     */
    public static int getTotalOutboundPorts() {
        return totalOutboundPorts;
    }

    /**
     * @return
     */
    public static double getTotalTimeMs() {
        return totalTimeMs;
    }

    /** 
     * Retorna o resumo formatado 
     */
    public static String getSummary() {
        return String.format(
            "UseCases: %d | Outbounds: %d | Tempo total: %.2f ms | Injeções: %d | Média: %.2f ms",
            totalUseCases,
            totalOutboundPorts,
            totalTimeMs,
            InjectionMetricsHistory.getTotalExecutions(),
            InjectionMetricsHistory.getAverageTimeMs()
        );
    }
}