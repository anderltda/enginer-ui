package br.com.enginer.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.port.outbound.PublisherOutboundPort;
import br.com.enginer.domain.ui.port.outbound.RepositoryOutboundPort;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.Form;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainId;
import br.com.enginer.domain.ui.usercase.template.FormTemplate;
import br.com.enginer.domain.ui.usercase.utils.ReflectionUtils;
import br.com.enginer.infrastructure.adapter.outbound.repository.TypeRepository;

/**
 * 
 */
public abstract class AbstractUserCase implements TemplateUserCase, ActionUserCase {

	protected RepositoryOutboundPort repositoryOutboundPort;
	protected PublisherOutboundPort publisherOutboundPort;

	/**
	 *
	 */
	@Override
	public void setRepositoryOutboundPort(RepositoryOutboundPort repositoryOutboundPort) {
		this.repositoryOutboundPort = repositoryOutboundPort;
	}
	
	/**
	 * @return repositoryOutboundPort
	 */
	@Override
	public RepositoryOutboundPort getRepositoryOutboundPort() {
		return repositoryOutboundPort;
	}
	
	/**
	 *
	 */
	@Override
	public void setPublisherOutboundPort(PublisherOutboundPort publisherOutboundPort) {
		this.publisherOutboundPort = publisherOutboundPort;
	}
	
	/**
	 * @return publisherOutboundPort
	 */
	public PublisherOutboundPort getPublisherOutboundPort() {
		return publisherOutboundPort;
	}
	
	/**
	 *
	 */
	@Override
	public Form form(Domain<?> domain) throws UncheckedException {
		
		try {
			
			Map<TypeTemplate, Object> map = new LinkedHashMap<TypeTemplate, Object>();
			map.put(TypeTemplate.FORM, true);
			map.put(TypeTemplate.FILTER, false);
			map.put(TypeTemplate.TAB, false);
			map.put(TypeTemplate.MODAL, domain.isModal());
			map.put(TypeTemplate.DISABLED, domain.isDisabled());
			map.put(TypeTemplate.MAIN_DOMAIN, domain.getMainDomain());
			
			domain = formId(domain);
			
			return FormTemplate.create(domain, this, map);
			
		} catch (Exception ex) {
			throw new UncheckedException("Erro ao montar o formulário com o Domain ----->>>> (" + domain.getClass().getSimpleName() + ")" + ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	public Form tab(Domain<?> domain) throws UncheckedException {
		
		try {
			
			Map<TypeTemplate, Object> map = new LinkedHashMap<TypeTemplate, Object>();
			map.put(TypeTemplate.TAB, true);
			map.put(TypeTemplate.FORM, false);
			map.put(TypeTemplate.FILTER, false);
			map.put(TypeTemplate.MODAL, domain.isModal());
			map.put(TypeTemplate.DISABLED, domain.isDisabled());
			map.put(TypeTemplate.MAIN_DOMAIN, domain.getMainDomain());
			
			domain = formId(domain);
			
			return FormTemplate.create(domain, this, map);
			
		} catch (Exception ex) {
			throw new UncheckedException("Erro ao montar o tab com o Domain ----->>>> (" + domain.getClass().getSimpleName() + ") - message erro: " + ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	public Form filter(Domain<?> domain) throws UncheckedException {
		
		try {
			
			Map<TypeTemplate, Object> map = new LinkedHashMap<TypeTemplate, Object>();
			map.put(TypeTemplate.FILTER, true);
			map.put(TypeTemplate.FORM, false);
			map.put(TypeTemplate.TAB, false);
			map.put(TypeTemplate.MODAL, domain.isModal());
			map.put(TypeTemplate.DISABLED, domain.isDisabled());
			map.put(TypeTemplate.MAIN_DOMAIN, domain.getMainDomain());
			
			return FormTemplate.create(domain, this, map);
			
		} catch (Exception ex) {
			throw new UncheckedException("Erro ao montar o filter com o Domain ----->>>> (" + domain.getClass().getSimpleName() + ") - message erro: " + ex.getMessage(), ex);
		}
	}
	
	/**
	 *
	 */
	@Override
	public Form row(Domain<?> domain) throws UncheckedException {
		
		try {
			
			Map<TypeTemplate, Object> map = new LinkedHashMap<TypeTemplate, Object>();
			map.put(TypeTemplate.ROW, true);
			map.put(TypeTemplate.FILTER, false);
			map.put(TypeTemplate.FORM, false);
			map.put(TypeTemplate.TAB, false);
			map.put(TypeTemplate.MODAL, domain.isModal());
			map.put(TypeTemplate.DISABLED, domain.isDisabled());
			map.put(TypeTemplate.MAIN_DOMAIN, domain.getMainDomain());
			
			return FormTemplate.create(domain, this, map);
			
		} catch (Exception ex) {
			throw new UncheckedException("Erro ao montar o row com o Domain ----->>>> (" + domain.getClass().getSimpleName() + ") - message erro: " + ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	public Domain<?> buscarPorId(Domain<?> domain) throws UncheckedException {
		return this.findById(domain);
	}

	/**
	 *
	 */
	@Override
	public Domain<?> buscarFormPorId(Domain<?> domain) throws CheckedException {
		return this.findById(domain);
	}
	
	/**
	 *
	 */
	@Override
	public Domain<?> buscarPorRegistroUnico(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		return repositoryOutboundPort.findBySingle(domain, filter);
	}

	/**
	 *
	 */
	@Override
	public Domain<?> buscarPorRegistroUnico(Domain<?> domain, Map<String, Object> filter, String method) throws UncheckedException {
		return repositoryOutboundPort.findBySingle(domain, filter, method);
	}

	/**
	 *
	 */
	@Override
	public Domain<?> buscarPorRegistroUnico(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		return repositoryOutboundPort.findBySingle(domain, filter, typeRepository, queryName);
	}

	/**
	 *
	 */
	@Override
	public List<Domain<?>> buscarTodos(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		return repositoryOutboundPort.findAll(domain, filter);
	}

	/**
	 *
	 */
	@Override
	public List<Domain<?>> buscarFormTodos(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		return repositoryOutboundPort.findAll(domain, filter);
	}

	/**
	 *
	 */
	@Override
	public List<Domain<?>> buscarTodos(Domain<?> domain, Map<String, Object> filter, String method) throws UncheckedException {
		return repositoryOutboundPort.findAll(domain, filter, method);
	}

	/**
	 *
	 */
	@Override
	public List<Domain<?>> buscarTodos(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		return repositoryOutboundPort.findAll(domain, filter, typeRepository, queryName);
	}

	/**
	 *
	 */
	@Override
	public List<Domain<?>> buscarPorIds(Domain<?> domain, Object... id) throws UncheckedException {
		return repositoryOutboundPort.findAllById(domain, id);
	}

	/**
	 *
	 */
	@Override
	public List<Domain<?>> buscarPorIds(Domain<?> domain, List<?> ids) throws UncheckedException {
		return repositoryOutboundPort.findAllById(domain, ids);
	}

	/**
	 *
	 */
	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		return repositoryOutboundPort.paginator(domain, filter);
	}

	/**
	 *
	 */
	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter, String method) throws UncheckedException {
		return repositoryOutboundPort.paginator(domain, filter, method);
	}

	/**
	 *
	 */
	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		return repositoryOutboundPort.paginator(domain, filter, typeRepository, queryName);
	}

	/**
	 *
	 */
	@Override
	public Integer buscarTotal(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		return repositoryOutboundPort.count(domain, filter);
	}

	/**
	 *
	 */
	@Override
	public Integer buscarTotal(Domain<?> domain, Map<String, Object> filter, String method) throws UncheckedException {
		return repositoryOutboundPort.count(domain, filter, method);
	}

	/**
	 *
	 */
	@Override
	public Integer buscarTotal(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		return repositoryOutboundPort.count(domain, filter, typeRepository, queryName);
	}

	/**
	 *
	 */
	@Override
	public boolean existe(Domain<?> domain) throws UncheckedException {
		
		if(domain.getId() != null) {
			return repositoryOutboundPort.existsById(domain, domain.getId());
		}
		
		return false;
	}

	/**
	 *
	 */
	@Override
	public void excluir(Domain<?> domain, List<?> ids) throws UncheckedException {
		repositoryOutboundPort.delete(domain, ids);
	}

	/**
	 *
	 */
	@Override
	public void excluir(Domain<?> domain, Object ids) throws UncheckedException {
		repositoryOutboundPort.delete(domain, ids);
	}

	/**
	 *
	 */
	@Override
	public void excluir(Domain<?> domain) throws UncheckedException {

		if (domain.getId() != null) {
			
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
	}

	/**
	 *
	 */
	@Override
	public Domain<?> salvar(Domain<?> domain) throws UncheckedException {
		return repositoryOutboundPort.save(domain);
	}

	/**
	 *
	 */
	@Override
	public List<Domain<?>> salvarLista(List<Domain<?>> entities) throws UncheckedException {
		return repositoryOutboundPort.save(entities);
	}

	/**
	 *
	 */
	@Override
	public Domain<?> salvar(Domain<?> domain, Boolean flush) throws UncheckedException {
		return repositoryOutboundPort.save(domain, flush);
	}

	/**
	 *
	 */
	@Override
	public List<Domain<?>> salvarLista(List<Domain<?>> entities, Boolean flush) throws UncheckedException {
		return repositoryOutboundPort.save(entities, flush);
	}

	/**
	 *
	 */	
	@Override
	public Domain<?> plus(Domain<?> domain) throws UncheckedException {
		return domain;
	}

	/**
	 *
	 */		
	@Override
	public Domain<?> previous(Domain<?> domain) throws UncheckedException {
		return domain;
	}

	/**
	 *
	 */		
	@Override
	public Domain<?> next(Domain<?> domain) throws UncheckedException {
		return domain;
	}

	/**
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	private Domain<?> formId(Domain<?> domain) throws Exception {
		if (!(DomainId.class.isAssignableFrom(domain.getClass()))) {
			Domain<?> loadedDomain = (ReflectionUtils.isTypeMatching(domain.getClass(), "id", domain.getId())) ? buscarPorId(domain) : null;
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
	private Domain<?> findById(Domain<?> domain) throws UncheckedException {
		
		if (domain.getId() != null) {
			
			try {
				
				if (ReflectionUtils.isTypeId(domain.getId().getClass())) {
					return repositoryOutboundPort.findById(domain, domain.getId());
				}
				
				Boolean idNull = ReflectionUtils.isIdNullKeyCompositedByDomain(domain.getId().getClass(), domain);
				
				if(idNull) {
					return null;
				}
					
				Map<String, Object> ids = ReflectionUtils.getCompositedKeyFields(domain);
				return repositoryOutboundPort.findByIdComposite(domain, ids);
				
			} catch (Exception ex) {
				throw new UncheckedException(ex.getMessage(), ex);
			}
		}
		
		return null;
	}
}
