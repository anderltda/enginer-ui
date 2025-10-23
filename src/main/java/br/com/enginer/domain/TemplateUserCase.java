package br.com.enginer.domain;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.ui.port.outbound.PublisherOutboundPort;
import br.com.enginer.domain.ui.port.outbound.RepositoryOutboundPort;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.Form;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

/**
 * Interface base para casos de uso com operações de template (form, tab, filter, row).
 * 
 * Agora totalmente tipada com <T extends Domain<?>>, garantindo segurança de tipo
 * e eliminando conflitos com ActionUserCase<T>.
 */
public interface TemplateUserCase<T extends Domain<?>> {

	public static final String buscarFormPorId = "buscarFormPorId";
	public static final String buscarFormTodos = "buscarFormTodos";
	public static final String buscarTodos = "buscarTodos";

    // --------------------------------------------------------------------------------------------
    // Dependências tipadas
    // --------------------------------------------------------------------------------------------
    void setRepositoryOutboundPort(RepositoryOutboundPort<T> repositoryOutboundPort);
    RepositoryOutboundPort<T> getRepositoryOutboundPort();

    void setPublisherOutboundPort(PublisherOutboundPort<T> publisherOutboundPort);
    PublisherOutboundPort<T> getPublisherOutboundPort();

    // --------------------------------------------------------------------------------------------
    // Métodos de template (form, tab, filter, row)
    // --------------------------------------------------------------------------------------------
    Form form(T domain) throws UncheckedException;
    Form tab(T domain) throws UncheckedException;
    Form filter(T domain) throws UncheckedException;
    Form row(T domain) throws UncheckedException;

    // --------------------------------------------------------------------------------------------
    // Métodos de busca com tipo T
    // --------------------------------------------------------------------------------------------
    T buscarFormPorId(T domain) throws CheckedException;

    List<T> buscarFormTodos(T domain, Map<String, Object> filter) throws UncheckedException;
}