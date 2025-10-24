package br.com.enginer.domain.system.usercase.port.inbound.api;

import br.com.enginer.domain.system.usercase.exception.CheckedException;
import br.com.enginer.domain.system.usercase.port.InboundPort;
import br.com.enginer.domain.system.usercase.schema.Form;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;

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
