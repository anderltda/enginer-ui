package br.com.enginer.domain.system.usecase.schema.instance;

import java.util.List;

import br.com.enginer.domain.system.usecase.annotation.field.UIIgnore;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.UITag;
import br.com.enginer.domain.system.usecase.logger.ActionLogger;

/**
 * 
 */
public abstract class DomainAbstract<I> implements Domain<I> {
	
	@UIIgnore
	private Boolean modal;
	
	@UIIgnore
	private Boolean disabled;
	
	@UIIgnore
	private String mainDomain;
	
	@UIIgnore
	private ActionLogger actionLogger;
	
	@UITag(label = "Tags", disable = false)
	private transient List<String> tags;
	
	/**
	 * @return
	 */
	@Override
	public ActionLogger getActionLogger() {
		return actionLogger;
	}

	/**
	 * @param actionLogger
	 */
	public void setActionLogger(ActionLogger actionLogger) {
		this.actionLogger = actionLogger;
	}

    /**
     * Indica se o domínio é um modal.
     */
	@Override
	public Boolean isModal() {
		return modal;
	}

    /**
     * Define se o domínio é um modal.
     */
	@Override
	public void setModal(Boolean modal) {
		this.modal = modal;
	}

    /**
     * Informa se os campos estao disabled, porém apenas visualmente.
     */
	@Override
	public Boolean isDisabled() {
		return disabled;
	}

    /**
     * Define se o disabled está habilitado.
     */
	@Override
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	
	/**
	 * Define o domain principal.
	 */
	@Override
	public void setMainDomain(String mainDomain) {
		this.mainDomain = mainDomain;
	}

	/**
	 * Informar qual o domain principal.
	 */
	@Override
	public String getMainDomain() {
		return this.mainDomain;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
