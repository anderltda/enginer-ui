package br.com.enginer.infrastructure.adapter.outbound.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.AuditLog;
import br.com.enginer.domain.system.Log;
import br.com.enginer.domain.ui.dto.logger.ActionLogger;
import br.com.enginer.domain.ui.port.outbound.LoggerOutboundPort;

/**
 * Implementação padrão do {@link LoggerOutboundPort} utilizando SLF4J/Logback.
 * <p>
 * Esta implementação centraliza a estratégia de logging, garantindo formatação
 * padronizada e suporte a múltiplos níveis de log (INFO, WARN, DEBUG, TRACE, ERROR).
 * </p>
 */
@Component
public class LoggerOutboundAdapterPort implements LoggerOutboundPort {

    /**
     * Formata a mensagem de log incluindo o nome da classe e thread atual.
     */
    private String format(Class<?> source, String message) {
        return String.format("[%s] %s", source.getSimpleName(), message);
    }

    private Logger getLogger(Class<?> source) {
        return LoggerFactory.getLogger(source);
    }

    // ===========================
    //  Níveis básicos de log
    // ===========================

    @Override
    public void info(Class<?> source, String message) {
        getLogger(source).info(format(source, message));
    }

    @Override
    public void warn(Class<?> source, String message) {
        getLogger(source).warn(format(source, message));
    }

    @Override
    public void debug(Class<?> source, String message) {
        Logger logger = getLogger(source);
        if (logger.isDebugEnabled()) {
            logger.debug(format(source, message));
        }
    }

    @Override
    public void trace(Class<?> source, String message) {
        Logger logger = getLogger(source);
        if (logger.isTraceEnabled()) {
            logger.trace(format(source, message));
        }
    }

    @Override
    public void error(Class<?> source, Throwable ex) {
        getLogger(source).error(format(source, ex.getMessage()), ex);
    }

    @Override
    public void error(Class<?> source, String message, Throwable ex) {
        getLogger(source).error(format(source, message), ex);
    }
    
    // -----------------------------
    // Persistência opcional
    // -----------------------------
    @Override
    public void persist(String level, Class<?> source, String message, String traceId, String user) {
        try {
        	Log log = new Log(level, source.getSimpleName(), message, traceId, user);
            LoggerFactory.getLogger(LoggerOutboundAdapterPort.class).warn("[LoggerOutboundAdapterPort] Log registrado com sucesso.", log);
        } catch (Exception ex) {
            LoggerFactory.getLogger(LoggerOutboundAdapterPort.class).warn("[LoggerOutboundAdapterPort] Falha ao persistir log: " + ex.getMessage());
        }
    }
    
    // ===========================
    //  Log de auditoria (ActionLogger)
    // ===========================

    @Override
    public void audit(Class<?> source, ActionLogger actionLogger, String message, long executionMs) {
        try {
            String traceId = MDC.get("traceId");
            String user = MDC.get("user");

            // Log visual no console
            getLogger(source).info(String.format(
                "[AUDIT] %s.%s executed by %s in %d ms → %s",
                actionLogger.getDomain(),
                actionLogger.getActionName(),
                user,
                executionMs,
                message
            ));

            // Persistência
            AuditLog audit = new AuditLog(
                traceId,
                user,
                actionLogger.getDomain(),
                actionLogger.getActionName(),
                source.getSimpleName(),
                "AUDIT",
                message,
                executionMs
            );
            
            LoggerFactory.getLogger(LoggerOutboundAdapterPort.class).warn("[LoggerOutboundAdapterPort] Auditoria registrada com sucesso.", audit);

        } catch (Exception ex) {
            LoggerFactory.getLogger(LoggerOutboundAdapterPort.class)
                .warn("[LoggerOutboundAdapterPort] Falha ao registrar auditoria: " + ex.getMessage());
        }
    }    

}