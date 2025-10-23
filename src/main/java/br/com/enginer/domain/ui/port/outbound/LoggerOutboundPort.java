package br.com.enginer.domain.ui.port.outbound;

import br.com.enginer.domain.OutboundPort;
import br.com.enginer.domain.ui.dto.logger.ActionLogger;

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
    
    /**
     * Registra log persistente (ex: em banco de dados) com nível definido.
     *
     * @param level   nível de log ("INFO", "ERROR", etc.)
     * @param source  classe de origem
     * @param message mensagem
     * @param traceId identificador da requisição (opcional)
     * @param user    usuário responsável (opcional)
     */
    void persist(String level, Class<?> source, String message, String traceId, String user);
    
    /**
     * Registra auditoria detalhada de uma ação de negócio.
     *
     * @param source       classe onde o log foi gerado
     * @param actionLogger metadados da ação (nome, domínio, id, usuário, etc.)
     * @param message      mensagem adicional de log
     * @param executionMs  tempo total de execução da ação
     */
    void audit(Class<?> source, ActionLogger actionLogger, String message, long executionMs);
}