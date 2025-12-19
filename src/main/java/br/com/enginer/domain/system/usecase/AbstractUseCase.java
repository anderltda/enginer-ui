package br.com.enginer.domain.system.usecase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.enginer.domain.system.dto.entity.tag.Tag;
import br.com.enginer.domain.system.dto.entity.tag.TagId;
import br.com.enginer.domain.system.usecase.annotation.PostAction;
import br.com.enginer.domain.system.usecase.annotation.PreAction;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.exception.CheckedException;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.page.PageResult;
import br.com.enginer.domain.system.usecase.port.outbound.OutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.publisher.PublisherOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.TagRepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.storage.FileStorageOutboundPort;
import br.com.enginer.domain.system.usecase.schema.Form;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.schema.instance.DomainId;
import br.com.enginer.domain.system.usecase.template.FormTemplate;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.utils.StringsUtils;
import br.com.enginer.infrastructure.adapter.outbound.repository.TypeRepository;
import br.com.enginer.infrastructure.injector.DependencyInjector;

/**
 * Classe base para todos os casos de uso do domínio.
 * Agora totalmente tipada com <T extends Domain<?>>.
 */
public abstract class AbstractUseCase<T extends Domain<?>> implements TemplateUseCase<T>, ActionUseCase<T> {
	
	/** 
	 * Lista de Tags para o domínio atual.
	 */
	private List<Tag> entities;

	/** 
	 * Port de log.
	 */
	protected LoggerOutboundPort loggerOutboundPort;
	
    /** 
     * Port de acesso a dados tipado com o domínio T.
     */
    protected RepositoryOutboundPort<T> repositoryOutboundPort;

    /**
     * Port de publicação de eventos (também pode ser genérico).
     */
    protected PublisherOutboundPort<T> publisherOutboundPort;
    
    /**
	 * Port de publicação de eventos (também pode ser genérico).
	 */
    protected FileStorageOutboundPort fileStorageOutboundPort;
    
    /** 
	 * Port de acesso a dados para Tags.
	 */
    protected TagRepositoryOutboundPort tagRepositoryOutboundPort;

    /** 
	 * 
	 */    
	@Override
	@SuppressWarnings("unchecked")
	public void addOutboundPort(OutboundPort... outboundPorts) {
		DependencyInjector.addOutboundPort((AbstractUseCase<Domain<?>>) this, outboundPorts);
	}

	/** 
	 * --------------------------------------------------------------------------------------------
	 * Injeção de dependências
	 * --------------------------------------------------------------------------------------------
     **/
	@Override
    public void setRepositoryOutboundPort(RepositoryOutboundPort<T> repositoryOutboundPort) {
        this.repositoryOutboundPort = repositoryOutboundPort;
    }

    @Override
    public RepositoryOutboundPort<T> getRepositoryOutboundPort() {
        return repositoryOutboundPort;
    }

    @Override
    public void setPublisherOutboundPort(PublisherOutboundPort<T> publisherOutboundPort) {
        this.publisherOutboundPort = publisherOutboundPort;
    }

    @Override
    public PublisherOutboundPort<T> getPublisherOutboundPort() {
        return publisherOutboundPort;
    }

	@Override
	public void setFileStorageOutboundPort(FileStorageOutboundPort fileStorageOutboundPort) {
        this.fileStorageOutboundPort = fileStorageOutboundPort;
	}

	@Override
	public FileStorageOutboundPort getFileStorageOutboundPort() {
		return fileStorageOutboundPort;
	}
	
	@Override
	public void setLoggerOutboundPort(LoggerOutboundPort loggerOutboundPort) {
		this.loggerOutboundPort = loggerOutboundPort;
	}

	@Override
	public LoggerOutboundPort getLoggerOutboundPort() {
		return loggerOutboundPort;
	}
	
	@Override
    public void setTagRepositoryOutboundPort(TagRepositoryOutboundPort tagRepositoryOutboundPort) {
        this.tagRepositoryOutboundPort = tagRepositoryOutboundPort;
    }

	@Override
    public TagRepositoryOutboundPort getTagRepositoryOutboundPort() {
        return tagRepositoryOutboundPort;
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
			
			FormTemplate form = new FormTemplate();
			Map<TypeTemplate, Object> map = new LinkedHashMap<>();
			map.put(TypeTemplate.TYPE_TEMPLATE, templateType);
			map.put(TypeTemplate.MODAL, domain.isModal());
			map.put(TypeTemplate.DISABLED, domain.isDisabled());
			map.put(TypeTemplate.MAIN_DOMAIN, domain.getMainDomain());

			domain = formId(domain);

			return form.create(domain, this, map);

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
	 * Metodos utilizados em formulários TemplateUseCase<T>
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
	 * Metodos que serao executados antes e depois do metodo real chamado
	 * --------------------------------------------------------------------------------------------
     **/	
	@PreAction
	public void pre(T domain) {
		System.out.println("Pré-execução: validando...");
		pull(domain);
	}

	@PostAction
	public void post(T domain) {
		System.out.println("Pós-execução: auditando...");
		push(domain);
	}
	
	/** 
	 * --------------------------------------------------------------------------------------------
	 * Metodos para manipulação de Tags
	 * --------------------------------------------------------------------------------------------
     **/
	@SuppressWarnings("unchecked")
	public void pull(T t) {
		
		try {
			
			if(t == null) return;
		
			List<String> tags = (List<String>) ReflectionUtils.executeMethod(t, StringsUtils.getMethod("tags"));
			String domain = t.getClass().getSimpleName();
			Object domainId = ReflectionUtils.createUriIdComposedType(t);

			entities = new ArrayList<Tag>();

			Optional.ofNullable(tags)
					.orElse(List.of())
					.stream()
					.filter(Objects::nonNull)
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.forEach(name -> {
						TagId tagId = new TagId();
						tagId.setNormalizedName(name.toLowerCase());
						tagId.setDomain(null);
						tagId.setDomainId(null);

						Tag tag = new Tag();
						tag.setId(tagId);
						tag.setName(name);
						tag.setCreatedAt(LocalDateTime.now());

						entities.add(tag);
					});

			if (domainId != null) {
				
				Map<String, Object> filter = new HashMap<String, Object>();
				filter.put("id.domain", domain);
				filter.put("id.domainId", domainId);
				
				List<Tag> tagzz = tagRepositoryOutboundPort.findAll(new Tag(), filter);
				
				tagzz.forEach(tag -> {
					filter.clear();
					filter.put("normalizedName", tag.getId().getNormalizedName());
					filter.put("domain", tag.getId().getDomain());
					filter.put("domainId", tag.getId().getDomainId());
					tagRepositoryOutboundPort.delete(new Tag(), filter);
				});
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void push(T t) {

		try {

			if (t == null) return;
			
			String domain = t.getClass().getSimpleName();
			
			Object domainId = ReflectionUtils.createUriIdComposedType(t);
			
			entities.forEach(tag -> {
				tag.getId().setDomain(domain);
				tag.getId().setDomainId(domainId.toString());
				tagRepositoryOutboundPort.save(tag);
			});

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** 
	 * --------------------------------------------------------------------------------------------
	 * Utilitários privados
	 * --------------------------------------------------------------------------------------------
     **/	
	private T formId(T domain) throws Exception {

		if (!(DomainId.class.isAssignableFrom(domain.getClass()))) {
			
			Boolean hasValueId = ReflectionUtils.hasIdValue(domain);
			
			T loadedDomain = hasValueId ? buscarPorId(domain) : null;
			
			if(hasValueId && loadedDomain == null) {
				throw new CheckedException("Nenhum registro encontrado");
			}
			
			if (loadedDomain != null) {
				domain = loadedDomain;
			}
		}
		
		return domain;
	}

	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
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