package br.com.enginer.domain.system.usercase;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.system.usercase.exception.CheckedException;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.page.PageResult;
import br.com.enginer.domain.system.usercase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.publisher.PublisherOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.storage.FileChunkStorageOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.storage.FileStorageOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.storage.HashGeneratorOutboundPort;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.infrastructure.adapter.outbound.repository.TypeRepository;

public interface ActionUserCase<T extends Domain<?>> {
	
	public static final String buscarPorId = "buscarPorId";
	public static final String buscarTodos = "buscarTodos";
	public static final String buscarPorRegistroUnico = "buscarPorRegistroUnico";
	public static final String buscarPorIds = "buscarPorIds";
	public static final String buscarTodosPaginado = "buscarTodosPaginado";
	public static final String existe = "existe";
	public static final String excluir = "excluir";
	public static final String salvar = "salvar";
	public static final String plus = "plus";
	
	// ------------------- Dependências -------------------
	void setLoggerOutboundPort(LoggerOutboundPort loggerOutboundPort);
	
	void setRepositoryOutboundPort(RepositoryOutboundPort<T> repositoryOutboundPort);
	
	void setPublisherOutboundPort(PublisherOutboundPort<T> publisherOutboundPort);
	
	void setHashGeneratorOutboundPort(HashGeneratorOutboundPort hashGeneratorOutboundPort);
	
	void setFileStorageOutboundPort(FileStorageOutboundPort fileStorageOutboundPort);
	
	void setFileChunkStorageOutboundPort(FileChunkStorageOutboundPort fileChunkStorageOutboundPort);
	
	LoggerOutboundPort getLoggerOutboundPort();
	
	RepositoryOutboundPort<T> getRepositoryOutboundPort();
	
	PublisherOutboundPort<T> getPublisherOutboundPort();
	
	HashGeneratorOutboundPort getHashGeneratorOutboundPort();
    
	FileStorageOutboundPort getFileStorageOutboundPort();
	
	FileChunkStorageOutboundPort getFileChunkStorageOutboundPort();

	// ------------------- Ações principais -------------------
	T buscarPorId(T domain) throws CheckedException;
	T buscarPorRegistroUnico(T domain, Map<String, Object> filter) throws UncheckedException;
	T buscarPorRegistroUnico(T domain, Map<String, Object> filter, String method) throws UncheckedException;
	T buscarPorRegistroUnico(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;

	List<T> buscarTodos(T domain, Map<String, Object> filter) throws UncheckedException;
	List<T> buscarTodos(T domain, Map<String, Object> filter, String method) throws UncheckedException;
	List<T> buscarTodos(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;

	List<T> buscarPorIds(T domain, Object... id) throws UncheckedException;
	List<T> buscarPorIds(T domain, List<?> ids) throws UncheckedException;

	PageResult<T> buscarTodosPaginado(T domain, Map<String, Object> filter) throws UncheckedException;
	PageResult<T> buscarTodosPaginado(T domain, Map<String, Object> filter, String method) throws UncheckedException;
	PageResult<T> buscarTodosPaginado(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;

	Integer buscarTotal(T domain, Map<String, Object> filter) throws UncheckedException;
	Integer buscarTotal(T domain, Map<String, Object> filter, String method) throws UncheckedException;
	Integer buscarTotal(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;

	boolean existe(T domain) throws UncheckedException;
	void excluir(T domain) throws UncheckedException;
	void excluir(T domain, List<?> ids) throws UncheckedException;
	void excluir(T domain, Object id) throws UncheckedException;
	void excluirLista(List<T> entities) throws UncheckedException;

	T salvar(T domain) throws UncheckedException;
	T salvar(T domain, Boolean flush) throws UncheckedException;
	List<T> salvarLista(List<T> entities) throws UncheckedException;
	List<T> salvarLista(List<T> entities, Boolean flush) throws UncheckedException;

	T plus(T domain) throws UncheckedException;
	T previous(T domain) throws UncheckedException;
	T next(T domain) throws UncheckedException;
}
