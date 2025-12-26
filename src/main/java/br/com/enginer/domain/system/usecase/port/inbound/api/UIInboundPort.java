package br.com.enginer.domain.system.usecase.port.inbound.api;

import br.com.enginer.domain.system.usecase.core.exception.CheckedException;
import br.com.enginer.domain.system.usecase.core.schema.Form;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.port.inbound.InboundPort;

/**
 * 
 */
public interface UIInboundPort<T extends Domain<?>> extends InboundPort {

	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Form form(Domain<?> domain) throws CheckedException;
	
	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Form filter(Domain<?> domain) throws CheckedException;
	
	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Form tab(Domain<?> domain) throws CheckedException;

	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Form row(Domain<?> domain) throws CheckedException;	

}
