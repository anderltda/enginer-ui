package br.com.enginer.domain.system.usecase.core.action;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.post.PostAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.post.PostCollectionAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.pre.PreAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.pre.PreCollectionAction;
import br.com.enginer.domain.system.usecase.core.exception.CheckedException;
import br.com.enginer.domain.system.usecase.core.page.PageResult;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.port.inbound.api.ActionInboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.publisher.PublisherOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.TagRepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.UploadFileRepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.storage.FileStorageOutboundPort;
import br.com.enginer.infrastructure.adapter.inbound.audit.ActionLoggerRepositoryOutboundAdapterPort;

/**
 * Implementação tipada e genérica do ActionInboundPort.
 * Faz o roteamento dinâmico para o UseCase correto, com segurança de tipo.
 */
public class ActionInboundUseCase<T extends Domain<?>> implements ActionInboundPort<T> {

    private final LoggerOutboundPort loggerOutboundPort;
    private final ActionLoggerRepositoryOutboundAdapterPort actionLoggerRepositoryOutboundAdapterPort;
    private final UploadFileRepositoryOutboundPort uploadFileRepositoryOutboundPort;
    private final TagRepositoryOutboundPort tagRepositoryOutboundPort;
    private final RepositoryOutboundPort<Domain<?>> repositoryOutboundPort;
    private final PublisherOutboundPort<Domain<?>> publisherOutboundPort;
    private final FileStorageOutboundPort fileStorageOutboundPort;

	/**
	 * Construtor com injeção de dependências.
	 */
	public ActionInboundUseCase(
			LoggerOutboundPort loggerOutboundPort,
			ActionLoggerRepositoryOutboundAdapterPort actionLoggerRepositoryOutboundAdapterPort,
			UploadFileRepositoryOutboundPort uploadFileRepositoryOutboundPort,
			TagRepositoryOutboundPort tagRepositoryOutboundPort,
			RepositoryOutboundPort<Domain<?>> repositoryOutboundPort,
			PublisherOutboundPort<Domain<?>> publisherOutboundPort,
			FileStorageOutboundPort fileStorageOutboundPort) {
		this.loggerOutboundPort = loggerOutboundPort;
		this.actionLoggerRepositoryOutboundAdapterPort = actionLoggerRepositoryOutboundAdapterPort;
		this.uploadFileRepositoryOutboundPort = uploadFileRepositoryOutboundPort;
		this.tagRepositoryOutboundPort = tagRepositoryOutboundPort;
		this.repositoryOutboundPort = repositoryOutboundPort;
		this.publisherOutboundPort = publisherOutboundPort;
		this.fileStorageOutboundPort = fileStorageOutboundPort;
	}


	/**
     * Injeta as dependências e instancia o UseCase correto para o domínio.
     */
    private Object injectedDependency(Domain<?> domain) throws Exception {
        return ReflectionUtils.executeInjectedDependencyUseCaseCached(domain.getClass(), 
        		loggerOutboundPort,
        		actionLoggerRepositoryOutboundAdapterPort,
        		uploadFileRepositoryOutboundPort,
        		tagRepositoryOutboundPort,
        		repositoryOutboundPort, 
        		publisherOutboundPort, 
        		fileStorageOutboundPort);
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
        	
        	/** Injected Dependency */
        	Object UseCase = injectedDependency(domain);
        	
            return (Domain<?>) ReflectionUtils.execute(UseCase, ActionUseCase.buscarPorId, domain);
            
        } catch (Exception ex) {
            loggerOutboundPort.error(ActionInboundUseCase.class, ex);
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
			
        	/** Injected Dependency */
        	Object UseCase = injectedDependency(domain);
        	
			return (Domain<?>) ReflectionUtils.execute(UseCase, ActionUseCase.buscarPorRegistroUnico, domain, filter);
			
		} catch (Exception ex) {
			loggerOutboundPort.error(ActionInboundUseCase.class, ex);
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
        	
        	/** Injected Dependency */
        	Object UseCase = injectedDependency(domain);
        	
            return (List<Domain<?>>) ReflectionUtils.execute(UseCase, ActionUseCase.buscarTodos, domain, filter);
            
        } catch (Exception ex) {
            loggerOutboundPort.error(ActionInboundUseCase.class, ex);
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
        	
        	/** Injected Dependency */
        	Object UseCase = injectedDependency(domain);
            
            return (PageResult<Domain<?>>) ReflectionUtils.execute(UseCase, ActionUseCase.buscarTodosPaginado, domain, filter);
            
        } catch (Exception ex) {
            loggerOutboundPort.error(ActionInboundUseCase.class, ex);
            throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
        }
    }

    /**
     * Executa uma busca paginada com base em um método específico do UseCase.
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
        	
        	/** Injected Dependency */
        	Object UseCase = injectedDependency(domain);
            
            return (PageResult<Domain<?>>) ReflectionUtils.execute(UseCase, ActionUseCase.buscarTodosPaginado, domain, filter, method);
            
        } catch (Exception ex) {
            loggerOutboundPort.error(ActionInboundUseCase.class, ex);
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
        	
        	/** Injected Dependency */
        	Object useCase = injectedDependency(domain);
            
            if(value != null && value.length > 0) {
            	result = (Object) ReflectionUtils.execute(useCase, methodName, value);
            } else {
            	result = (Object) ReflectionUtils.execute(useCase, methodName);
            }

        } catch (Exception ex) {
            loggerOutboundPort.error(ActionInboundUseCase.class, ex);
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
            
        	loggerOutboundPort.info(ActionInboundUseCase.class, "Action -> " + actionLogger.getAction());
            
        	/** Injected Dependency */
        	Object useCase = injectedDependency(domain);
        	
        	/** Executa @PreAction */
        	ReflectionUtils.runAnnotatedMethods(useCase, PreAction.class, domain);
            
        	/** Executa o método real */
        	Domain<?> newDomain = (Domain<?>) ReflectionUtils.execute(useCase, actionLogger.getAction(), domain);
        	
        	/** Executa @PostAction */
        	ReflectionUtils.runAnnotatedMethods(useCase, PostAction.class, newDomain);
            
        	return newDomain;
        
        } catch (Exception ex) {
            loggerOutboundPort.error(ActionInboundUseCase.class, ex);
            throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
        }
    }

    /**
     * Executa uma ação genérica do domínio sobre uma lista de entidades.
     * @param domain domínio principal
     * @param domains lista de domínios a serem processados
     * @return lista de domínios processados
     * @throws CheckedException em caso de erro de regra de negócio
     */
    @Override
	@SuppressWarnings("unchecked")
    public List<Domain<?>> methodName(Domain<?> domain, List<Domain<?>> domains) throws CheckedException {
        
    	try {
    		
        	ActionLogger actionLogger = domain.getActionLogger();
        
    		loggerOutboundPort.info(ActionInboundUseCase.class, "Action -> " + actionLogger.getAction());
            
        	/** Injected Dependency */
        	Object useCase = injectedDependency(domain);
        	
        	/** Executa todos @PreCollectionAction */
        	ReflectionUtils.runAnnotatedMethods(useCase, PreCollectionAction.class, domains);
        
        	List<Domain<?>> newDomainList = (List<Domain<?>>) ReflectionUtils.execute(useCase, actionLogger.getAction(), domains);
    		
        	/** Executa @PostCollectionAction */
        	ReflectionUtils.runAnnotatedMethods(useCase, PostCollectionAction.class, newDomainList);
        	
        	return newDomainList;
        	
    	} catch (Exception ex) {
            loggerOutboundPort.error(ActionInboundUseCase.class, ex);
            throw new CheckedException(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(), ex);
        }
    }
}