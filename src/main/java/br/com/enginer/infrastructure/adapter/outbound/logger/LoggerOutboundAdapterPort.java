package br.com.enginer.infrastructure.adapter.outbound.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import br.com.enginer.domain.ui.port.outbound.LoggerOutboundPort;

/**
 * 
 */
@Component
public class LoggerOutboundAdapterPort implements LoggerOutboundPort {

	/**
	 *
	 */
	@Override
	public void info(Class<?> clazz, String message) {
		Logger logger_ = LogManager.getLogger(clazz);
		logger_.info(message);
	}

	/**
	 *
	 */
	@Override
	public void error(Class<?> clazz, String message, Throwable throwable) {
		Logger logger_ = LogManager.getLogger(clazz);
		logger_.error(message, throwable);
	}

	/**
	 *
	 */
	@Override
	public void error(Class<?> clazz, Throwable throwable) {
		Logger logger_ = LogManager.getLogger(clazz);
		logger_.error(throwable);
	}

}
