package br.com.enginer.domain.system.usercase;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.system.usercase.exception.CheckedException;
import br.com.enginer.domain.system.usercase.logger.ActionLogger;
import br.com.enginer.domain.system.usercase.page.PageResult;
import br.com.enginer.domain.system.usercase.port.inbound.api.ActionInboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.publisher.PublisherOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.storage.FileStorageOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.storage.HashGeneratorOutboundPort;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.domain.system.usercase.utils.ReflectionUtils;

/**
 * Implementação tipada e genérica do ActionInboundPort.
 * Faz o roteamento dinâmico para o UserCase correto, com segurança de tipo.
 */
public class ActionInboundUserCase<T extends Domain<?>> implements ActionInboundPort<T> {

    private final LoggerOutboundPort logger;
    private final RepositoryOutboundPort<Domain<?>> repositoryOutboundPort;
    private final PublisherOutboundPort<Domain<?>> publisherOutboundPort;
    private final FileStorageOutboundPort fileStorageOutboundPort;
    private final HashGeneratorOutboundPort hashGeneratorOutboundPort;

	public ActionInboundUserCase(LoggerOutboundPort logger,
								 RepositoryOutboundPort<Domain<?>> repositoryOutboundPort,
								 PublisherOutboundPort<Domain<?>> publisherOutboundPort,
								 FileStorageOutboundPort fileStorageOutboundPort,
								 HashGeneratorOutboundPort hashGeneratorOutboundPort) {
		this.logger = logger;
		this.repositoryOutboundPort = repositoryOutboundPort;
		this.publisherOutboundPort = publisherOutboundPort;
		this.fileStorageOutboundPort = fileStorageOutboundPort;
		this.hashGeneratorOutboundPort = hashGeneratorOutboundPort;
	}

    /**
     * Injeta as dependências e instancia o UserCase correto para o domínio.
     */
    private Object injectedDependency(Domain<?> domain) throws Exception {
        return ReflectionUtils.executeInjectedDependencyUserCaseCached(domain.getClass(), 
        		logger,
        		repositoryOutboundPort, 
        		publisherOutboundPort, 
        		fileStorageOutboundPort,
        		hashGeneratorOutboundPort);
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