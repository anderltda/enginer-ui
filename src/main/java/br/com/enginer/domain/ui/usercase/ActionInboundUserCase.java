package br.com.enginer.domain.ui.usercase;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.ActionUserCase;
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
 * Implementação tipada e genérica do ActionInboundPort.
 * Faz o roteamento dinâmico para o UserCase correto, com segurança de tipo.
 */
public class ActionInboundUserCase<T extends Domain<?>> implements ActionInboundPort<T> {

    private final LoggerOutboundPort logger;
    private final RepositoryOutboundPort<Domain<?>> repositoryOutboundPort;
    private final PublisherOutboundPort<Domain<?>> publisherOutboundPort;

    /**
     * @param logger
     * @param repositoryOutboundPort
     * @param publisherOutboundPort
     */
    public ActionInboundUserCase(LoggerOutboundPort logger, RepositoryOutboundPort<Domain<?>> repositoryOutboundPort, PublisherOutboundPort<Domain<?>> publisherOutboundPort) {
        this.logger = logger;
        this.repositoryOutboundPort = repositoryOutboundPort;
        this.publisherOutboundPort = publisherOutboundPort;
    }

    /**
     * Injeta as dependências e instancia o UserCase correto para o domínio.
     */
    private Object injectedDependency(Domain<?> domain) throws Exception {
        return ReflectionUtils.executeInjectedDependencyUserCaseCached(domain.getClass(), repositoryOutboundPort, publisherOutboundPort);
    }

    /**
     * @param domain
     * @return T
     */
	@Override
    public Domain<?> searchWithById(Domain<?> domain) throws CheckedException {
        try {
            Object userCase = injectedDependency(domain);
            return (Domain<?>) ReflectionUtils.executeMethod(userCase, ActionUserCase.buscarPorId, domain);
        } catch (Exception ex) {
            logger.error(ActionInboundUserCase.class, ex);
            throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
        }
    }

    /**
	 * @param domain
	 * @param filter
	 * @return List<T>
	 * @throws CheckedException
	 */
    @Override
	@SuppressWarnings("unchecked")
    public List<Domain<?>> searchByConditions(Domain<?> domain, Map<String, Object> filter) throws CheckedException {
        try {
            Object userCase = injectedDependency(domain);
            return (List<Domain<?>>) ReflectionUtils.executeMethod(userCase, ActionUserCase.buscarTodos, domain, filter);
        } catch (Exception ex) {
            logger.error(ActionInboundUserCase.class, ex);
            throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
        }
    }

    /**
     * @param domain
     * @param filter
     * @return PageResult<T>
     * @throws CheckedException
     */
    @Override
	@SuppressWarnings("unchecked")
    public PageResult<Domain<?>> searchPaginated(Domain<?> domain, Map<String, Object> filter) throws CheckedException {
        try {
            Object userCase = injectedDependency(domain);
            return (PageResult<Domain<?>>) ReflectionUtils.executeMethod(userCase, ActionUserCase.buscarTodosPaginado, domain, filter);
        } catch (Exception ex) {
            logger.error(ActionInboundUserCase.class, ex);
            throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
        }
    }

    /**
	 * @param domain
	 * @param filter
	 * @param method
	 * @return PageResult<T>
	 * @throws CheckedException
	 */
    @Override
	@SuppressWarnings("unchecked")
    public PageResult<Domain<?>> searchPaginatedByMethod(Domain<?> domain, Map<String, Object> filter, String method) throws CheckedException {
        try {
            Object userCase = injectedDependency(domain);
            return (PageResult<Domain<?>>) ReflectionUtils.executeMethod(userCase, ActionUserCase.buscarTodosPaginado, domain, filter, method);
        } catch (Exception ex) {
            logger.error(ActionInboundUserCase.class, ex);
            throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
        }
    }

    /**
     * @param domain
     * @return T
     * @throws CheckedException
     */
    @Override
	@SuppressWarnings("unchecked")
    public Domain<?> methodName(Domain<?> domain) throws CheckedException {
        try {
            ActionLogger actionLogger = domain.getActionLogger();
            logger.info(ActionInboundUserCase.class, "Action -> " + actionLogger.getActionName());
            Object userCase = injectedDependency(domain);
            return (T) ReflectionUtils.executeMethod(userCase, actionLogger.getActionName(), domain);
        } catch (Exception ex) {
            logger.error(ActionInboundUserCase.class, ex);
            throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
        }
    }

    /**
	 * @param domain
	 * @param domains
	 * @param actionLogger
	 * @return List<Domain<?>>
	 * @throws CheckedException
	 */
    @Override
	@SuppressWarnings("unchecked")
    public List<Domain<?>> methodName(Domain<?> domain, List<Domain<?>> domains, ActionLogger actionLogger) throws CheckedException {
        try {
            logger.info(ActionInboundUserCase.class, "Action -> " + actionLogger.getActionName());
            Object userCase = injectedDependency(domain);
            return (List<Domain<?>>) ReflectionUtils.executeMethod(userCase, actionLogger.getActionName(), domains);
        } catch (Exception ex) {
            logger.error(ActionInboundUserCase.class, ex);
            throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
        }
    }
}