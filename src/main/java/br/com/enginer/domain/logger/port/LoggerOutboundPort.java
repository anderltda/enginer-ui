package br.com.enginer.domain.logger.port;

import br.com.enginer.domain.logger.dto.LoggerDto;

public interface LoggerOutboundPort {

	void info(LoggerDto logger);

	void error(LoggerDto logger);

}
