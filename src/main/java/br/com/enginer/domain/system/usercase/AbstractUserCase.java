package br.com.enginer.domain.system.usercase;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.system.usercase.enums.TypeTemplate;
import br.com.enginer.domain.system.usercase.exception.CheckedException;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.injector.DependencyInjector;
import br.com.enginer.domain.system.usercase.page.PageResult;
import br.com.enginer.domain.system.usercase.port.outbound.PublisherOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.RepositoryOutboundPort;
import br.com.enginer.domain.system.usercase.schema.Form;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.domain.system.usercase.schema.instance.DomainId;
import br.com.enginer.domain.system.usercase.template.FormTemplate;
import br.com.enginer.domain.system.usercase.utils.ReflectionUtils;
import br.com.enginer.infrastructure.adapter.outbound.repository.TypeRepository;

/**
 * Classe base para todos os casos de uso do domínio.
 * Agora totalmente tipada com <T extends Domain<?>>.
 */
public abstract class AbstractUserCase<T extends Domain<?>> implements TemplateUserCase<T>, ActionUserCase<T> {

    /** 
     * Port de acesso a dados tipado com o domínio T.
     */
    protected RepositoryOutboundPort<T> repositoryOutboundPort;

    /**
     * Port de publicação de eventos (também pode ser genérico).
     */
    protected PublisherOutboundPort<T> publisherOutboundPort;

	/** 
	 * --------------------------------------------------------------------------------------------
	 * Injeção de dependências
	 * --------------------------------------------------------------------------------------------
     **/
	@Override
	@SuppressWarnings("unchecked")
    public void setRepositoryOutboundPort(RepositoryOutboundPort<T> repositoryOutboundPort) {
        this.repositoryOutboundPort = repositoryOutboundPort;
        DependencyInjector.processDependencies((AbstractUserCase<Domain<?>>) this);
    }

    @Override
    public RepositoryOutboundPort<T> getRepositoryOutboundPort() {
        return repositoryOutboundPort;
    }

    @Override
	@SuppressWarnings("unchecked")
    public void setPublisherOutboundPort(PublisherOutboundPort<T> publisherOutboundPort) {
        this.publisherOutboundPort = publisherOutboundPort;
        DependencyInjector.processDependencies((AbstractUserCase<Domain<?>>) this);
    }

    @Override
    public PublisherOutboundPort<T> getPublisherOutboundPort() {
        return publisherOutboundPort;
    }

	/** 
	 * --------------------------------------------------------------------------------------------
	 * Construção de templates dinâmicos (form, tab, filter, row)
	 * --------------------------------------------------------------------------------------------
     **/    
	@Override
	public Form form(T domain) throws UncheckedException {
		return createTemplate(domain, TypeTemplate.FORM);
	}

	@Override
	public Form tab(T domain) throws UncheckedException {
		return createTemplate(domain, TypeTemplate.TAB);
	}

	@Override
	public Form filter(T domain) throws UncheckedException {
		return createTemplate(domain, TypeTemplate.FILTER);
	}

	@Override
	public Form row(T domain) throws UncheckedException {
		return createTemplate(domain, TypeTemplate.ROW);
	}

	private Form createTemplate(T domain, TypeTemplate templateType) {
		try {
			Map<TypeTemplate, Object> map = new LinkedHashMap<>();
			map.put(TypeTemplate.TYPE_TEMPLATE, templateType);
			map.put(TypeTemplate.MODAL, domain.isModal());
			map.put(TypeTemplate.DISABLED, domain.isDisabled());
			map.put(TypeTemplate.MAIN_DOMAIN, domain.getMainDomain());

			domain = formId(domain);
			return FormTemplate.create(domain, this, map);

		} catch (Exception ex) {
			throw new UncheckedException("Erro ao montar o template " + templateType + " para " + domain.getClass().getSimpleName(), ex);
		}
	}

	/** 
	 * --------------------------------------------------------------------------------------------
	 * Ações CRUD genéricas
	 * --------------------------------------------------------------------------------------------
     **/    
	@Override
	public T buscarPorId(T domain) throws UncheckedException {
		return findById(domain);
	}

	@Override
	public T buscarPorRegistroUnico(T domain, Map<String, Object> filter) throws UncheckedException {
		return repositoryOutboundPort.findBySingle(domain, filter);
	}

	@Override
	public T buscarPorRegistroUnico(T domain, Map<String, Object> filter, String method) throws UncheckedException {
		return repositoryOutboundPort.findBySingle(domain, filter, method);
	}

	@Override
	public T buscarPorRegistroUnico(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		return repositoryOutboundPort.findBySingle(domain, filter, typeRepository, queryName);
	}

	@Override
	public List<T> buscarTodos(T domain, Map<String, Object> filter) throws UncheckedException {
		return repositoryOutboundPort.findAll(domain, filter);
	}

	@Override
	public List<T> buscarTodos(T domain, Map<String, Object> filter, String method) throws UncheckedException {
		return repositoryOutboundPort.findAll(domain, filter, method);
	}

	@Override
	public List<T> buscarTodos(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		return repositoryOutboundPort.findAll(domain, filter, typeRepository, queryName);
	}

	@Override
	public List<T> buscarPorIds(T domain, Object... id) throws UncheckedException {
		return repositoryOutboundPort.findAllById(domain, id);
	}

	@Override
	public List<T> buscarPorIds(T domain, List<?> ids) throws UncheckedException {
		return repositoryOutboundPort.findAllById(domain, ids);
	}

	@Override
	public PageResult<T> buscarTodosPaginado(T domain, Map<String, Object> filter) throws UncheckedException {
		return repositoryOutboundPort.paginator(domain, filter);
	}

	@Override
	public PageResult<T> buscarTodosPaginado(T domain, Map<String, Object> filter, String method) throws UncheckedException {
		return repositoryOutboundPort.paginator(domain, filter, method);
	}

	@Override
	public PageResult<T> buscarTodosPaginado(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		return repositoryOutboundPort.paginator(domain, filter, typeRepository, queryName);
	}

	@Override
	public Integer buscarTotal(T domain, Map<String, Object> filter) throws UncheckedException {
		return repositoryOutboundPort.count(domain, filter);
	}

	@Override
	public Integer buscarTotal(T domain, Map<String, Object> filter, String method) throws UncheckedException {
		return repositoryOutboundPort.count(domain, filter, method);
	}

	@Override
	public Integer buscarTotal(T domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		return repositoryOutboundPort.count(domain, filter, typeRepository, queryName);
	}

	@Override
	public boolean existe(T domain) throws UncheckedException {
		if (domain.getId() != null) {
			return repositoryOutboundPort.existsById(domain, domain.getId());
		}
		return false;
	}

	@Override
	public void excluir(T domain) throws UncheckedException {
		if (domain.getId() == null) return;

		try {
			if (ReflectionUtils.isTypeId(domain.getId().getClass())) {
				repositoryOutboundPort.delete(domain, domain.getId());
			} else {
				Map<String, Object> ids = ReflectionUtils.getIdDomainId((DomainId) domain.getId());
				repositoryOutboundPort.delete(domain, ids);
			}
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	@Override
	public void excluir(T domain, Object id) throws UncheckedException {
		repositoryOutboundPort.delete(domain, id);
	}

	@Override
	public void excluir(T domain, List<?> ids) throws UncheckedException {
		repositoryOutboundPort.delete(domain, ids);
	}

	@Override
	public void excluirLista(List<T> entities) throws UncheckedException {
		for (T entity : entities) {
			repositoryOutboundPort.delete(entity, entity.getId());
		}
	}

	@Override
	public T salvar(T domain) throws UncheckedException {
		return repositoryOutboundPort.save(domain);
	}

	@Override
	public List<T> salvarLista(List<T> entities) throws UncheckedException {
		return repositoryOutboundPort.save(entities);
	}

	@Override
	public T salvar(T domain, Boolean flush) throws UncheckedException {
		return repositoryOutboundPort.save(domain, flush);
	}

	@Override
	public List<T> salvarLista(List<T> entities, Boolean flush) throws UncheckedException {
		return repositoryOutboundPort.save(entities, flush);
	}

	@Override
	public T plus(T domain) throws UncheckedException {
		return domain;
	}

	@Override
	public T previous(T domain) throws UncheckedException {
		return domain;
	}

	@Override
	public T next(T domain) throws UncheckedException {
		return domain;
	}

	/** 
	 * --------------------------------------------------------------------------------------------
	 * Metodos utilizados em formulários TemplateUserCase<T>
	 * --------------------------------------------------------------------------------------------
     **/	
	@Override
	public T buscarFormPorId(T domain) throws CheckedException {
		return this.findById(domain);
	}
	
	@Override
	public List<T> buscarFormTodos(T domain, Map<String, Object> filter) throws UncheckedException {
		return repositoryOutboundPort.findAll(domain, filter);
	}	

	/** 
	 * --------------------------------------------------------------------------------------------
	 * Utilitários privados
	 * --------------------------------------------------------------------------------------------
     **/	
	private T formId(T domain) throws Exception {
		if (!(DomainId.class.isAssignableFrom(domain.getClass()))) {
			T loadedDomain = (ReflectionUtils.isTypeMatching(domain.getClass(), "id", domain.getId()))
					? buscarPorId(domain)
					: null;
			if (loadedDomain != null) {
				domain = loadedDomain;
			}
		}
		return domain;
	}

	private T findById(T domain) throws UncheckedException {
		if (domain.getId() == null) return null;

		try {
			if (ReflectionUtils.isTypeId(domain.getId().getClass())) {
				return repositoryOutboundPort.findById(domain, domain.getId());
			}
			if (ReflectionUtils.isIdNullKeyCompositedByDomain(domain.getId().getClass(), domain)) {
				return null;
			}
			Map<String, Object> ids = ReflectionUtils.getCompositedKeyFields(domain);
			return repositoryOutboundPort.findByIdComposite(domain, ids);
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
}