package br.com.enginer.infrastructure.adapter.outbound.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;

/**
 * Implementação padrão do {@link LoggerOutboundPort} utilizando SLF4J/Logback.
 * <p>
 * Esta implementação centraliza a estratégia de logging, garantindo formatação
 * padronizada e suporte a múltiplos níveis de log (INFO, WARN, DEBUG, TRACE,
 * ERROR).
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
	// Níveis básicos de log
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

}