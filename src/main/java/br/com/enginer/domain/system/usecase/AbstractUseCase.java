package br.com.enginer.domain.system.usecase;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import br.com.enginer.domain.system.usecase.port.outbound.repository.UploadFileRepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.storage.FileStorageOutboundPort;
import br.com.enginer.domain.system.usecase.schema.Form;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.template.FormTemplate;
import br.com.enginer.infrastructure.adapter.outbound.repository.TypeRepository;
import br.com.enginer.infrastructure.injector.DependencyInjector;

/**
 * Classe base para todos os casos de uso do domínio.
 * Agora totalmente tipada com <T extends Domain<?>>.
 */
public abstract class AbstractUseCase<T extends Domain<?>> implements TemplateUseCase<T>, ActionUseCase<T> {
	
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
     * Port de acesso a dados para UploadFile.
     */
    protected UploadFileRepositoryOutboundPort uploadFileRepositoryOutboundPort;
    
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
	public UploadFileRepositoryOutboundPort getUploadFileRepositoryOutboundPort() {
		return uploadFileRepositoryOutboundPort;
	}

	@Override
	public void setUploadFileRepositoryOutboundPort(UploadFileRepositoryOutboundPort uploadFileRepositoryOutboundPort) {
		this.uploadFileRepositoryOutboundPort = uploadFileRepositoryOutboundPort;
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
			map.put(TypeTemplate.MODAL, domain.getModal());
			map.put(TypeTemplate.DISABLED, domain.getDisabled());
			map.put(TypeTemplate.MAIN_DOMAIN, domain.getMainDomain());

			domain = repositoryOutboundPort.formId(domain);

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
		return repositoryOutboundPort.findById(domain);
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
	public void excluir(T domain, Object id) throws UncheckedException {
		repositoryOutboundPort.delete(domain, id);
	}

	@Override
	public void excluir(T domain, List<?> ids) throws UncheckedException {
		repositoryOutboundPort.delete(domain, ids);
	}
	
	@Override
	public void excluir(T domain) throws UncheckedException {
		repositoryOutboundPort.delete(domain);
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
	 * Metodos que serao executados antes e depois do metodo real chamado
	 * --------------------------------------------------------------------------------------------
     **/	
	@PreAction
	public void pre(T domain) {
		System.out.println("Pré-execução: validando...");
		tagRepositoryOutboundPort.pull(domain);
		uploadFileRepositoryOutboundPort.pull(domain);
	}

	@PostAction
	public void post(T domain) {
		System.out.println("Pós-execução: auditando...");
		tagRepositoryOutboundPort.push(domain);
		uploadFileRepositoryOutboundPort.push(domain);
	}	

	/** 
	 * --------------------------------------------------------------------------------------------
	 * Metodos utilizados em formulários TemplateUseCase<T>
	 * --------------------------------------------------------------------------------------------
     **/	
	@Override
	public T buscarFormPorId(T domain) throws CheckedException {
		return repositoryOutboundPort.findById(domain);
	}
	
	@Override
	public List<T> buscarFormTodos(T domain, Map<String, Object> filter) throws UncheckedException {
		return repositoryOutboundPort.findAll(domain, filter);
	}

}