package br.com.enginer.domain.ui.port.outbound;

import br.com.enginer.domain.OutboundPort;

public interface LoggerOutboundPort extends OutboundPort {

	void info(Class<?> clazz, String message);

	void error(Class<?> clazz, String message, Throwable throwable);
	
	void error(Class<?> clazz, Throwable throwable);

}
