package br.com.enginer.domain.ui.usercase.schema.instance;

import br.com.enginer.domain.ui.dto.logger.ActionLogger;

/**
 * 
 */
public abstract class DomainAbstract<I> implements Domain<I> {
	
	private Boolean modal;
	private Boolean disabled;
	private String mainDomain;
	private ActionLogger actionLogger;
	
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
}
