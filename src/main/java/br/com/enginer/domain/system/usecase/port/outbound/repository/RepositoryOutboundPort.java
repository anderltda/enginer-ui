package br.com.enginer.domain.system.usecase.port.outbound.repository;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.system.usecase.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.page.PageResult;
import br.com.enginer.domain.system.usecase.port.outbound.OutboundPort;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;
import br.com.enginer.infrastructure.adapter.outbound.repository.TypeRepository;

/**
 * Port outbound responsável por acesso a dados (camada repositório).
 * 
 * Agora tipado com <T extends Domain<?>> para garantir segurança de tipos
 * em toda a camada de casos de uso.
 */
public interface RepositoryOutboundPort<T extends Domain<?>> extends OutboundPort {
	
	String FIND_BY_ID = "findById";

	// ------------------------------------------------------------
	// Métodos de busca
	// ------------------------------------------------------------
	T findById(T domain, Object id) throws UncheckedException;
	
	T findById(T domain) throws UncheckedException;
	
	T formId(T domain) throws Exception;

	T findByIdComposite(T domain, Map<String, Object> ids) throws UncheckedException;
	
	T findBySingle(T domain, Map<String, Object> filter, String... method) throws UncheckedException;
	
	T findBySingle(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;
	
	List<T> findAll(T domain, Map<String, Object> filter, String... method) throws UncheckedException;
	
	List<T> findAll(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;
	
	List<T> findAllById(T domain, Object... id) throws UncheckedException;
	
	List<T> findAllById(T domain, List<?> ids) throws UncheckedException;

	// ------------------------------------------------------------
	// Paginação e contagem
	// ------------------------------------------------------------
	PageResult<T> paginator(T domain, Map<String, Object> filter, String... method) throws UncheckedException;
	
	PageResult<T> paginator(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;
	
	Integer count(T domain, Map<String, Object> filter, String... method) throws UncheckedException;
	
	Integer count(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;

	// ------------------------------------------------------------
	// Verificação de existência
	// ------------------------------------------------------------
	boolean existsById(T domain, Object id) throws UncheckedException;
	
	// ------------------------------------------------------------
	// Exclusão
	// ------------------------------------------------------------
	void delete(T domain, Object id) throws UncheckedException;
	
	void delete(T domain, Map<String, Object> ids) throws UncheckedException;
	
	void delete(T domain) throws UncheckedException;
	
	// ------------------------------------------------------------
	// Persistência
	// ------------------------------------------------------------
	T save(T domain, Boolean... flush) throws UncheckedException;
	
	List<T> save(List<T> entities, Boolean... flush) throws UncheckedException;
}