package br.com.enginer.domain.ui.usercase;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.dto.logger.ActionLogger;
import br.com.enginer.domain.ui.port.inbound.ActionInboundPort;
import br.com.enginer.domain.ui.port.outbound.LoggerOutboundPort;
import br.com.enginer.domain.ui.port.outbound.PublisherOutboundPort;
import br.com.enginer.domain.ui.port.outbound.RepositoryOutboundPort;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.domain.ui.usercase.utils.ReflectionUtils;

/**
 * 
 */
public class ActionInboundUserCase implements ActionInboundPort {

	private final LoggerOutboundPort logger;
	private final RepositoryOutboundPort repositoryOutboundPort;
	private final PublisherOutboundPort publisherOutboundPort;

	/**
	 * @param logger
	 * @param repositoryOutboundPort
	 * @param publisherOutboundPort
	 */
	public ActionInboundUserCase(LoggerOutboundPort logger, RepositoryOutboundPort repositoryOutboundPort, PublisherOutboundPort publisherOutboundPort) {
		this.logger = logger;
		this.repositoryOutboundPort = repositoryOutboundPort;
		this.publisherOutboundPort = publisherOutboundPort;
	}

	/**
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	private Object injectedDependency(Domain<?> domain) throws Exception {
		return ReflectionUtils.executeInjectedDependencyUserCase(domain.getClass(), repositoryOutboundPort, publisherOutboundPort);
	}

	/**
	 *
	 */
	@Override
	public Domain<?> searchWithById(Domain<?> domain) throws CheckedException {

		try {
			Object object = injectedDependency(domain);
			return (Domain<?>) ReflectionUtils.executeMethod(object, "buscarPorId", domain);
		} catch (Exception ex) {
			logger.error(ActionInboundUserCase.class, ex);
			throw new CheckedException(ex.getCause().getMessage(), ex.getCause());
		}

	}

	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Domain<?>> searchByConditions(Domain<?> domain, Map<String, Object> filter) throws CheckedException {

		try {
			Object object = injectedDependency(domain);
			return (List<Domain<?>>) ReflectionUtils.executeMethod(object, "buscarTodos", domain, filter);
		} catch (Exception ex) {
			logger.error(ActionInboundUserCase.class, ex);
			throw new CheckedException(ex.getCause().getMessage(), ex.getCause());
		}
	}

	/**
	 *
	 */
	@Override
	public PageResult<?> searchPaginated(Domain<?> domain, Map<String, Object> filter) throws CheckedException {
		PageResult<?> pageResult = null;
		try {
			Object object = injectedDependency(domain);
			pageResult = (PageResult<?>) ReflectionUtils.executeMethod(object, "buscarTodosPaginado", domain, filter);
		} catch (Exception ex) {
			logger.error(ActionInboundUserCase.class, ex);
			throw new CheckedException(ex.getCause().getMessage(), ex.getCause());
		}
		return pageResult;
	}

	/**
	 *
	 */
	@Override
	public PageResult<?> searchPaginatedByMethod(Domain<?> domain, Map<String, Object> filter, String method) throws CheckedException {
		PageResult<?> pageResult = null;
		try {
			Object object = injectedDependency(domain);
			pageResult = (PageResult<?>) ReflectionUtils.executeMethod(object, "buscarTodosPaginado", filter, method);
		} catch (Exception ex) {
			logger.error(ActionInboundUserCase.class, ex);
			throw new CheckedException(ex.getCause().getMessage(), ex.getCause());
		}
		return pageResult;
	}

	/**
	 *
	 */
	@Override
	public Domain<?> methodName(Domain<?> domain) throws CheckedException {
		Domain<?> domainNew = null;
		try {
			ActionLogger actionLogger = domain.getActionLogger();
			logger.info(ActionInboundUserCase.class, "Action -> " + actionLogger.getActionName());
			Object object = injectedDependency(domain);
			domainNew = (Domain<?>) ReflectionUtils.executeMethod(object, actionLogger.getActionName(), domain);
		} catch (Exception ex) {
			logger.error(ActionInboundUserCase.class, ex);
			throw new CheckedException(ex.getCause().getMessage(), ex.getCause());
		}
		return domainNew;
	}

	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Domain<?>> methodName(Domain<?> domain, List<Domain<?>> domains, ActionLogger actionLogger) throws CheckedException {
		List<Domain<?>> domainNews = null;
		try {
			logger.info(ActionInboundUserCase.class, "Action -> " + actionLogger.getActionName());
			Object object = injectedDependency(domain);
			domainNews =  (List<Domain<?>>) ReflectionUtils.executeMethod(object, actionLogger.getActionName(), domains);
		} catch (Exception ex) {
			logger.error(ActionInboundUserCase.class, ex);
			throw new CheckedException(ex.getCause().getMessage(), ex.getCause());
		}
		return domainNews;
	}
	
	
}
