package br.com.enginer.domain.ui.port.outbound;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.OutboundPort;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.infrastructure.adapter.outbound.repository.TypeRepository;

/**
 * 
 */
public interface RepositoryOutboundPort extends OutboundPort {
	
	public static final String findById = "findById";

	public Domain<?> findById(Domain<?> domain, Object id) throws UncheckedException;
	
	public Domain<?> findByIdComposite(Domain<?> domain, Map<String, Object> ids) throws UncheckedException;
	
	public Domain<?> findBySingle(Domain<?> domain, Map<String, Object> filter, String... method) throws UncheckedException;
	
	public Domain<?> findBySingle(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;
	
	public List<Domain<?>> findAll(Domain<?> domain, Map<String, Object> filter, String... method) throws UncheckedException;
	
	public List<Domain<?>> findAll(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;
	
	public List<Domain<?>> findAllById(Domain<?> domain, Object... id) throws UncheckedException;
	
	public List<Domain<?>> findAllById(Domain<?> domain, List<?> ids) throws UncheckedException;
	
	public PageResult<Domain<?>> paginator(Domain<?> domain, Map<String, Object> filter, String... method) throws UncheckedException;
	
	public PageResult<Domain<?>> paginator(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;
	
	public Integer count(Domain<?> domain, Map<String, Object> filter, String... method) throws UncheckedException;
	
	public Integer count(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException;

	public boolean existsById(Domain<?> domain, Object id) throws UncheckedException;
	
	public void delete(Domain<?> domain, Object id) throws UncheckedException;
	
	public void delete(Domain<?> domain, Map<String, Object> ids) throws UncheckedException;
	
	public Domain<?> save(Domain<?> domain, Boolean... flush) throws UncheckedException;
	
	public List<Domain<?>> save(List<Domain<?>> entities, Boolean... flush) throws UncheckedException;

}



