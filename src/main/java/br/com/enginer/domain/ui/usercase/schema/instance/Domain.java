package br.com.enginer.domain.ui.usercase.schema.instance;

import br.com.enginer.domain.ui.dto.logger.ActionLogger;

/**
 * @param <I>
 */
public interface Domain<I> {
	
	/**
	 * @return id
	 */
	public I getId();
	
	/**
	 * @param id
	 */
	public void setId(I id);
	
    /**
     * @return Indica se o domínio é um modal.
     */
	public Boolean isModal();

    /**
     * @param Define se o domínio é um modal.
     */
	public void setModal(Boolean modal);
	
    /**
     * @return Informa se os campos estao disabled, porém apenas visualmente.
     */
	public Boolean isDisabled();

    /**
     * @param Define se o disabled está habilitado.
     */
	public void setDisabled(Boolean disabled);
    
    /**
     * @param Define o domain principal.
     */
	public void setMainDomain(String mainDomain);
    
    /**
     * @return Informar qual o domain principal.
     */
	public String getMainDomain();

	/**
	 * @return
	 */
	public ActionLogger getActionLogger();

}