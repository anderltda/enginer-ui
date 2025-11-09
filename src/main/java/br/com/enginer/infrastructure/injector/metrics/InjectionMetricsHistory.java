package br.com.enginer.infrastructure.injector.metrics;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Mantém histórico e estatísticas acumuladas das injeções de dependências.
 */
public final class InjectionMetricsHistory {

    private static final int MAX_HISTORY = 100; // limite do histórico em memória
    private static final List<InjectionSnapshot> HISTORY = new LinkedList<>();

    private static int totalExecutions = 0;
    private static double cumulativeTimeMs = 0.0;

    private InjectionMetricsHistory() {}

    /** 
     * Adiciona uma nova entrada e atualiza estatísticas acumuladas 
     */
    public static synchronized void addSnapshot(int userCases, int outbounds, double timeMs) {
        InjectionSnapshot snapshot = new InjectionSnapshot(LocalDateTime.now(), userCases, outbounds, timeMs);

        if (HISTORY.size() >= MAX_HISTORY) {
            HISTORY.remove(0); // remove o mais antigo
        }

        HISTORY.add(snapshot);
        totalExecutions++;
        cumulativeTimeMs += timeMs;
    }

    /** 
     * Retorna histórico imutável 
     */
    public static synchronized List<InjectionSnapshot> getHistory() {
        return Collections.unmodifiableList(HISTORY);
    }

    /** 
     * Total de execuções registradas 
     */
    public static synchronized int getTotalExecutions() {
        return totalExecutions;
    }

    /** 
     * Tempo médio de execução (ms) 
     */
    public static synchronized double getAverageTimeMs() {
        return totalExecutions == 0 ? 0 : cumulativeTimeMs / totalExecutions;
    }

    /** 
     * Reset total (para testes ou reinicializações) 
     */
    public static synchronized void reset() {
        HISTORY.clear();
        totalExecutions = 0;
        cumulativeTimeMs = 0.0;
    }

    /** 
     * Snapshot de uma execução 
     */
    public static final class InjectionSnapshot {
    	
        private final LocalDateTime timestamp;
        private final int userCases;
        private final int outbounds;
        private final double timeMs;

        /**
         * @param timestamp
         * @param userCases
         * @param outbounds
         * @param timeMs
         */
        public InjectionSnapshot(LocalDateTime timestamp, int userCases, int outbounds, double timeMs) {
            this.timestamp = timestamp;
            this.userCases = userCases;
            this.outbounds = outbounds;
            this.timeMs = timeMs;
        }

        /**
         * @return
         */
        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        /**
         * @return
         */
        public int getUserCases() {
            return userCases;
        }

        /**
         * @return
         */
        public int getOutbounds() {
            return outbounds;
        }

        /**
         * @return
         */
        public double getTimeMs() {
            return timeMs;
        }

        /**
         *
         */
        @Override
        public String toString() {
            String formatted = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return String.format("[%s] UserCases: %d | Outbounds: %d | Tempo: %.2f ms",
                    formatted, userCases, outbounds, timeMs);
        }
    }
}