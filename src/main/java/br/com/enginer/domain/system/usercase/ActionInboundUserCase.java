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
import br.com.enginer.domain.system.usercase.port.outbound.storage.FileChunkStorageOutboundPort;
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
    private final FileChunkStorageOutboundPort fileChunkStorageOutboundPort;
    private final HashGeneratorOutboundPort hashGeneratorOutboundPort;

	public ActionInboundUserCase(
			LoggerOutboundPort logger,
			RepositoryOutboundPort<Domain<?>> repositoryOutboundPort,
			PublisherOutboundPort<Domain<?>> publisherOutboundPort,
			FileStorageOutboundPort fileStorageOutboundPort,
			FileChunkStorageOutboundPort fileChunkStorageOutboundPort,
			HashGeneratorOutboundPort hashGeneratorOutboundPort) {
		this.logger = logger;
		this.repositoryOutboundPort = repositoryOutboundPort;
		this.publisherOutboundPort = publisherOutboundPort;
		this.fileStorageOutboundPort = fileStorageOutboundPort;
		this.fileChunkStorageOutboundPort = fileChunkStorageOutboundPort;
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
        		fileChunkStorageOutboundPort,
        		hashGeneratorOutboundPort);
    }

    /**
     * Busca uma entidade pelo seu identificador.
     * @param domain domínio com identificador preenchido
     * @return domínio encontrado (ou nulo se não encontrado)
     * @throws CheckedException em caso de erro de regra de negócio
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
     * Busca uma entidade unica com base em filtros.
     * @param domain domínio com identificador preenchido
     * @return domínio encontrado (ou nulo se não encontrado)
     * @throws CheckedException em caso de erro de regra de negócio
     */
	@Override
	public Domain<?> searchWithBySingleConditions(Domain<?> domain, Map<String, Object> filter) throws CheckedException {
		try {
			Object userCase = injectedDependency(domain);
			return (Domain<?>) ReflectionUtils.executeMethod(userCase, ActionUserCase.buscarPorRegistroUnico, domain, filter);
		} catch (Exception ex) {
			logger.error(ActionInboundUserCase.class, ex);
			throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
		}
	}

    /**
     * Busca entidades com base em filtros dinâmicos.
     * @param domain domínio base
     * @param filter mapa contendo os filtros aplicáveis
     * @return lista de domínios encontrados
     * @throws CheckedException em caso de erro de regra de negócio
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
     * Realiza uma busca paginada com base em filtros.
     * @param domain domínio base
     * @param filter filtros e parâmetros de paginação
     * @return resultado paginado contendo lista e metadados
     * @throws CheckedException em caso de erro de regra de negócio
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
     * Executa uma busca paginada com base em um método específico do UserCase.
     * @param domain domínio base
     * @param filter filtros aplicáveis
     * @param method nome do método a ser executado no caso de uso
     * @return resultado paginado
     * @throws CheckedException em caso de erro de regra de negócio
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
     * Executa uma ação genérica do domínio a partir do método informado.
     * @param domain domínio base
     * @param methodName nome do método a ser executado
     * @param value valores adicionais para o método
     * @return resultado da ação
     * @throws CheckedException em caso de erro de regra de negócio
     */
    @Override
	public Object methodName(Domain<?> domain, String methodName, Object...value) throws CheckedException {
        
    	Object result = null;

    	try {
        	
            Object userCase = injectedDependency(domain);
            
            if(value != null && value.length > 0) {
            	result = (Object) ReflectionUtils.executeMethod(userCase, methodName, value);
            } else {
            	result = (Object) ReflectionUtils.executeMethod(userCase, methodName);
            }

        } catch (Exception ex) {
            logger.error(ActionInboundUserCase.class, ex);
            throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
        }

    	return result;
	}

	/**
     * Executa uma ação genérica do domínio que pode retornar uma instância.
     * @param domain domínio contendo dados de entrada
     * @return domínio atualizado ou resultado da ação
     * @throws CheckedException em caso de erro de regra de negócio
     */
    @Override
    public Domain<?> methodName(Domain<?> domain) throws CheckedException {
        try {
            ActionLogger actionLogger = domain.getActionLogger();
            logger.info(ActionInboundUserCase.class, "Action -> " + actionLogger.getActionName());
            Object userCase = injectedDependency(domain);
            return (Domain<?>) ReflectionUtils.executeMethod(userCase, actionLogger.getActionName(), domain);
        } catch (Exception ex) {
            logger.error(ActionInboundUserCase.class, ex);
            throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
        }
    }

    /**
     * Executa uma ação genérica do domínio sobre uma lista de entidades.
     * @param domain domínio principal
     * @param domains lista de domínios a serem processados
     * @param actionLogger log de auditoria da ação
     * @return lista de domínios processados
     * @throws CheckedException em caso de erro de regra de negócio
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