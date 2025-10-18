package br.com.enginer.domain.example.usercase;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.port.outbound.PublisherOutboundPort;
import br.com.enginer.domain.ui.port.outbound.RepositoryOutboundPort;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.Form;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.infrastructure.adapter.outbound.repository.TypeRepository;

public class EntityAllUserCase extends AbstractUserCase {

	@Override
	public void setRepositoryOutboundPort(RepositoryOutboundPort repositoryOutboundPort) {
		// TODO Auto-generated method stub
		super.setRepositoryOutboundPort(repositoryOutboundPort);
	}

	@Override
	public RepositoryOutboundPort getRepositoryOutboundPort() {
		// TODO Auto-generated method stub
		return super.getRepositoryOutboundPort();
	}

	@Override
	public void setPublisherOutboundPort(PublisherOutboundPort publisherOutboundPort) {
		// TODO Auto-generated method stub
		super.setPublisherOutboundPort(publisherOutboundPort);
	}

	@Override
	public PublisherOutboundPort getPublisherOutboundPort() {
		// TODO Auto-generated method stub
		return super.getPublisherOutboundPort();
	}

	@Override
	public Form form(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
		return super.form(domain);
	}

	@Override
	public Form tab(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
		return super.tab(domain);
	}

	@Override
	public Form filter(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
		return super.filter(domain);
	}

	@Override
	public Form row(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
		return super.row(domain);
	}

	@Override
	public Domain<?> buscarPorId(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
		return domain;
	}

	@Override
	public Domain<?> buscarFormPorId(Domain<?> domain) throws CheckedException {
		// TODO Auto-generated method stub
		return domain;
	}

	@Override
	public Domain<?> buscarPorRegistroUnico(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		// TODO Auto-generated method stub
		return domain;
	}

	@Override
	public Domain<?> buscarPorRegistroUnico(Domain<?> domain, Map<String, Object> filter, String method) throws UncheckedException {
		// TODO Auto-generated method stub
		return domain;
	}

	@Override
	public Domain<?> buscarPorRegistroUnico(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		// TODO Auto-generated method stub
		return domain;
	}

	@Override
	public List<Domain<?>> buscarTodos(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Domain<?>> buscarFormTodos(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Domain<?>> buscarTodos(Domain<?> domain, Map<String, Object> filter, String method) throws UncheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Domain<?>> buscarTodos(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Domain<?>> buscarPorIds(Domain<?> domain, Object... id) throws UncheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Domain<?>> buscarPorIds(Domain<?> domain, List<?> ids) throws UncheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter, String method)
			throws UncheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer buscarTotal(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer buscarTotal(Domain<?> domain, Map<String, Object> filter, String method) throws UncheckedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer buscarTotal(Domain<?> domain, Map<String, Object> filter, TypeRepository typeRepository, String queryName) throws UncheckedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean existe(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void excluir(Domain<?> domain, List<?> ids) throws UncheckedException {
		// TODO Auto-generated method stub
	}

	@Override
	public void excluir(Domain<?> domain, Object ids) throws UncheckedException {
		// TODO Auto-generated method stub
	}

	@Override
	public void excluir(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
	}

	@Override
	public Domain<?> salvar(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
		return domain;
	}

	@Override
	public List<Domain<?>> salvarLista(List<Domain<?>> entities) throws UncheckedException {
		// TODO Auto-generated method stub
		return super.salvarLista(entities);
	}

	@Override
	public Domain<?> salvar(Domain<?> domain, Boolean flush) throws UncheckedException {
		// TODO Auto-generated method stub
		return domain;
	}

	@Override
	public List<Domain<?>> salvarLista(List<Domain<?>> entities, Boolean flush) throws UncheckedException {
		// TODO Auto-generated method stub
		return entities;
	}

	@Override
	public Domain<?> plus(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
		return domain;
	}

	@Override
	public Domain<?> previous(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
		return domain;
	}

	@Override
	public Domain<?> next(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
		return domain;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
	}
}
