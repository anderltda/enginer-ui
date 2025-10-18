package br.com.enginer.infrastructure.adapter.outbound.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import br.com.enginer.domain.logger.dto.LoggerDto;
import br.com.enginer.domain.logger.port.LoggerOutboundPort;

@Component
public class LoggerOutboundPortAdapter implements LoggerOutboundPort {

	@Override
	public void info(LoggerDto logger) {
		Logger logger_ = LogManager.getLogger(logger.getClazz());
		logger_.info(logger.getMessage());
	}

	@Override
	public void error(LoggerDto logger) {
		Logger logger_ = LogManager.getLogger(logger.getClazz());
		logger_.error(logger.getClazz(), logger.getThrowable());
	}

}
