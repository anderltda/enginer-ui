package br.com.enginer.domain.ui.port.inbound;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.InboundPort;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.dto.logger.ActionLogger;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

/**
 * 
 */
public interface ActionInboundPort extends InboundPort {
	
	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Domain<?> searchWithById(Domain<?> domain) throws CheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @return
	 * @throws CheckedException
	 */
	List<Domain<?>> searchByConditions(Domain<?> domain, Map<String, Object> filter) throws CheckedException;

	/**
	 * @param domain
	 * @param filter
	 * @return
	 * @throws CheckedException
	 */
	PageResult<?> searchPaginated(Domain<?> domain, Map<String, Object> filter) throws CheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @param method
	 * @return
	 * @throws CheckedException
	 */
	PageResult<?> searchPaginatedByMethod(Domain<?> domain, Map<String, Object> filter, String method) throws CheckedException;

	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Domain<?> methodName(Domain<?> domain) throws CheckedException;
	
	/**
	 * @param domain
	 * @param domains
	 * @param actionLogger
	 * @return
	 * @throws CheckedException
	 */
	List<Domain<?>> methodName(Domain<?> domain, List<Domain<?>> domains, ActionLogger actionLogger) throws CheckedException;
	
}
