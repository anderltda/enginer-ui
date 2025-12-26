package br.com.enginer.domain.system.usecase.port.outbound.logger;

import br.com.enginer.domain.system.usecase.port.outbound.OutboundPort;

/**
 * Porta de saída responsável por abstrair o mecanismo de logging do sistema.
 * <p>
 * Essa interface define operações para registrar mensagens em diferentes níveis
 * (INFO, WARN, ERROR, DEBUG e TRACE) de forma consistente e contextualizada.
 * Implementações podem redirecionar os logs para o console, arquivos, sistemas
 * externos (como ELK, Loki, CloudWatch) ou outras soluções de observabilidade.
 */
public interface LoggerOutboundPort extends OutboundPort {

    /**
     * Registra uma mensagem de informação.
     *
     * @param source  classe ou componente de origem
     * @param message mensagem a ser registrada
     */
    void info(Class<?> source, String message);

    /**
     * Registra uma mensagem de aviso.
     *
     * @param source  classe ou componente de origem
     * @param message mensagem a ser registrada
     */
    void warn(Class<?> source, String message);

    /**
     * Registra uma mensagem de depuração (útil para desenvolvimento).
     *
     * @param source  classe ou componente de origem
     * @param message mensagem a ser registrada
     */
    void debug(Class<?> source, String message);

    /**
     * Registra uma mensagem de rastreamento detalhada (nível mais detalhado).
     *
     * @param source  classe ou componente de origem
     * @param message mensagem a ser registrada
     */
    void trace(Class<?> source, String message);

    /**
     * Registra um erro com exceção.
     *
     * @param source classe ou componente de origem
     * @param ex     exceção lançada
     */
    void error(Class<?> source, Throwable ex);

    /**
     * Registra um erro com mensagem personalizada e exceção associada.
     *
     * @param source  classe ou componente de origem
     * @param message mensagem personalizada
     * @param ex      exceção lançada
     */
    void error(Class<?> source, String message, Throwable ex);
}