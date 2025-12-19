package br.com.enginer.infrastructure.adapter.outbound.repository;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.system.usecase.page.PageResult;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;

/**
 * Adapter base que delega todas as operações de {@link RepositoryOutboundPort}
 * para um repositório "genérico" ({@code RepositoryOutboundPort<Domain<?>>}),
 * concentrando os casts em um único lugar.
 *
 * <p>
 * Uso típico:
 * </p>
 * 
 * <pre>{@code
 * @Component
 * public class TagRepositoryOutboundAdapter extends DelegatingRepositoryOutboundAdapter<Tag>
 * 		implements TagRepositoryOutboundPort {
 *
 * 	public TagRepositoryOutboundAdapter(RepositoryOutboundPort<Domain<?>> delegate) {
 * 		super(delegate);
 * 	}
 * }
 * }</pre>
 *
 * @param <T> domínio concreto
 */
public abstract class DelegatingRepositoryOutboundAdapter<T extends Domain<?>> implements RepositoryOutboundPort<T> {

	/**
	 * 
	 */
	protected final RepositoryOutboundPort<Domain<?>> delegate;

	/**
	 * @param delegate
	 */
	protected DelegatingRepositoryOutboundAdapter(RepositoryOutboundPort<Domain<?>> delegate) {
		this.delegate = delegate;
	}

	// ------------------------------------------------------------
	// Métodos de busca
	// ------------------------------------------------------------
	@Override
	public T findById(T domain, Object id) {
		return cast(delegate.findById(domain, id));
	}

	@Override
	public T findByIdComposite(T domain, Map<String, Object> ids) {
		return cast(delegate.findByIdComposite(domain, ids));
	}

	@Override
	public T findBySingle(T domain, Map<String, Object> filter, String... method) {
		return cast(delegate.findBySingle(domain, filter, method));
	}

	@Override
	public T findBySingle(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) {
		return cast(delegate.findBySingle(domain, filter, typeRepository, queryName));
	}

	@Override
	public List<T> findAll(T domain, Map<String, Object> filter, String... method) {
		return castList(delegate.findAll(domain, filter, method));
	}

	@Override
	public List<T> findAll(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) {
		return castList(delegate.findAll(domain, filter, typeRepository, queryName));
	}

	@Override
	public List<T> findAllById(T domain, Object... id) {
		return castList(delegate.findAllById(domain, id));
	}

	@Override
	public List<T> findAllById(T domain, List<?> ids) {
		return castList(delegate.findAllById(domain, ids));
	}

	// ------------------------------------------------------------
	// Paginação e contagem
	// ------------------------------------------------------------
	@Override
	public PageResult<T> paginator(T domain, Map<String, Object> filter, String... method) {
		return castPage(delegate.paginator(domain, filter, method));
	}

	@Override
	public PageResult<T> paginator(T domain, Map<String, Object> filter, TypeRepository typeRepository,
			String queryName) {
		return castPage(delegate.paginator(domain, filter, typeRepository, queryName));
	}

	@Override
	public Integer count(T domain, Map<String, Object> filter, String... method) {
		return delegate.count(domain, filter, method);
	}

	@Override
	public Integer count(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) {
		return delegate.count(domain, filter, typeRepository, queryName);
	}

	// ------------------------------------------------------------
	// Verificação de existência
	// ------------------------------------------------------------
	@Override
	public boolean existsById(T domain, Object id) {
		return delegate.existsById(domain, id);
	}

	// ------------------------------------------------------------
	// Exclusão
	// ------------------------------------------------------------
	@Override
	public void delete(T domain, Object id) {
		delegate.delete(domain, id);
	}

	@Override
	public void delete(T domain, Map<String, Object> ids) {
		delegate.delete(domain, ids);
	}

	// ------------------------------------------------------------
	// Persistência
	// ------------------------------------------------------------
	@Override
	public T save(T domain, Boolean... flush) {
		return cast(delegate.save(domain, flush));
	}

	@Override
	public List<T> save(List<T> entities, Boolean... flush) {
		// delegate espera List<Domain<?>>, mas o método aceita List<?>,
		// então o cast fica concentrado aqui.
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Domain<?>> raw = (List) entities;
		return castList(delegate.save(raw, flush));
	}

	// ------------------------------------------------------------
	// Helpers de cast centralizados
	// ------------------------------------------------------------
	@SuppressWarnings("unchecked")
	protected T cast(Domain<?> value) {
		return (T) value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<T> castList(List<? extends Domain<?>> value) {
		return (List) value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected PageResult<T> castPage(PageResult<? extends Domain<?>> value) {
		return (PageResult) value;
	}
}