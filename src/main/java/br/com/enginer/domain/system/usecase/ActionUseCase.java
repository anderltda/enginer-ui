package br.com.enginer.domain.system.usecase;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.system.usecase.exception.CheckedException;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.page.PageResult;
import br.com.enginer.domain.system.usecase.port.outbound.OutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.publisher.PublisherOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.storage.FileStorageOutboundPort;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;
import br.com.enginer.infrastructure.adapter.outbound.repository.TypeRepository;

public interface ActionUseCase<T extends Domain<?>> {
	
	public static final String buscarPorId = "buscarPorId";
	public static final String buscarTodos = "buscarTodos";
	public static final String buscarPorRegistroUnico = "buscarPorRegistroUnico";
	public static final String buscarPorIds = "buscarPorIds";
	public static final String buscarTodosPaginado = "buscarTodosPaginado";
	public static final String existe = "existe";
	public static final String excluir = "excluir";
	public static final String salvar = "salvar";
	public static final String plus = "plus";
	
    void addOutboundPort(OutboundPort...outboundPorts);
	
	// ------------------- Dependências -------------------
	LoggerOutboundPort getLoggerOutboundPort();
	void setLoggerOutboundPort(LoggerOutboundPort loggerOutboundPort);
	
	RepositoryOutboundPort<T> getRepositoryOutboundPort();
	void setRepositoryOutboundPort(RepositoryOutboundPort<T> repositoryOutboundPort);
	
	PublisherOutboundPort<T> getPublisherOutboundPort();
	void setPublisherOutboundPort(PublisherOutboundPort<T> publisherOutboundPort);
	
	FileStorageOutboundPort getFileStorageOutboundPort();
	void setFileStorageOutboundPort(FileStorageOutboundPort fileStorageOutboundPort);
	
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
