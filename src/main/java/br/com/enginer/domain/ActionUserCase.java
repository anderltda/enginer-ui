package br.com.enginer.domain;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.port.outbound.PublisherOutboundPort;
import br.com.enginer.domain.ui.port.outbound.RepositoryOutboundPort;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.infrastructure.adapter.outbound.repository.TypeRepository;

public interface ActionUserCase {
	
	public static final String buscarPorId = "buscarPorId";
	public static final String buscarTodos_ = "buscarTodos";
	public static final String buscarPorRegistroUnico = "buscarPorRegistroUnico";
	public static final String buscarPorIds = "buscarPorIds";
	public static final String buscarTodosPaginado = "buscarTodosPaginado";
	public static final String existe = "existe";
	public static final String excluir = "excluir";
	public static final String salvar = "salvar";
	public static final String plus = "plus";
	
	/**
	 * @param repositoryOutboundPort
	 */
	void setRepositoryOutboundPort(RepositoryOutboundPort repositoryOutboundPort);
	
	/**
	 * @param publisherOutboundPort
	 */
	void setPublisherOutboundPort(PublisherOutboundPort publisherOutboundPort);
	
	/**
	 * @return
	 */
	RepositoryOutboundPort getRepositoryOutboundPort();	
	
	/**
	 * @return
	 */
	PublisherOutboundPort getPublisherOutboundPort();
	
	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Domain<?> buscarPorId(Domain<?> domain) throws CheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @return
	 * @throws UncheckedException
	 */
	Domain<?> buscarPorRegistroUnico(Domain<?> domain, Map<String, Object> filter) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @param method
	 * @return
	 * @throws UncheckedException
	 */
	Domain<?> buscarPorRegistroUnico(Domain<?> domain, Map<String, Object> filter, String method) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @param typeRepository
	 * @param queryName
	 * @return
	 * @throws UncheckedException
	 */
	Domain<?> buscarPorRegistroUnico(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @return
	 * @throws UncheckedException
	 */
	List<Domain<?>> buscarTodos(Domain<?> domain, Map<String, Object> filter) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @param method
	 * @return
	 * @throws UncheckedException
	 */
	List<Domain<?>> buscarTodos(Domain<?> domain, Map<String, Object> filter, String method) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @param typeRepository
	 * @param queryName
	 * @return
	 * @throws UncheckedException
	 */
	List<Domain<?>> buscarTodos(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param id
	 * @return
	 * @throws UncheckedException
	 */
	List<Domain<?>> buscarPorIds(Domain<?> domain, Object... id) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param ids
	 * @return
	 * @throws UncheckedException
	 */
	List<Domain<?>> buscarPorIds(Domain<?> domain, List<?> ids) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @return
	 * @throws UncheckedException
	 */
	PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @param method
	 * @return
	 * @throws UncheckedException
	 */
	PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter, String method) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @param typeRepository
	 * @param queryName
	 * @return
	 * @throws UncheckedException
	 */
	PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @return
	 * @throws UncheckedException
	 */
	Integer buscarTotal(Domain<?> domain, Map<String, Object> filter) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @param method
	 * @return
	 * @throws UncheckedException
	 */
	Integer buscarTotal(Domain<?> domain, Map<String, Object> filter, String method) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @param typeRepository
	 * @param queryName
	 * @return
	 * @throws UncheckedException
	 */
	Integer buscarTotal(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;

	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	boolean existe(Domain<?> domain) throws UncheckedException;
	
	/**
	 * @param domain
	 * @throws UncheckedException
	 */
	void excluir(Domain<?> domain) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param ids
	 * @throws UncheckedException
	 */
	void excluir(Domain<?> domain, List<?> ids) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param id
	 * @throws UncheckedException
	 */
	void excluir(Domain<?> domain, Object id) throws UncheckedException;
	
	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	Domain<?> salvar(Domain<?> domain) throws UncheckedException;
	
	/**
	 * @param domain
	 * @param flush
	 * @return
	 * @throws UncheckedException
	 */
	Domain<?> salvar(Domain<?> domain, Boolean flush) throws UncheckedException;
	
	/**
	 * @param entities
	 * @return
	 * @throws UncheckedException
	 */
	List<Domain<?>> salvarLista(List<Domain<?>> entities) throws UncheckedException;
	
	/**
	 * @param entities
	 * @param flush
	 * @return
	 * @throws UncheckedException
	 */
	List<Domain<?>> salvarLista(List<Domain<?>> entities, Boolean flush) throws UncheckedException;
	
	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException	
	 */
	Domain<?> plus(Domain<?> domain) throws UncheckedException;
	
	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	Domain<?> previous(Domain<?> domain) throws UncheckedException;
	
	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	Domain<?> next(Domain<?> domain) throws UncheckedException;
}
