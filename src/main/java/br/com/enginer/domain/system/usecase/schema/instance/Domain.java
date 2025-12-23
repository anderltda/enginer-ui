package br.com.enginer.domain.system.usecase.schema.instance;

import br.com.enginer.domain.system.usecase.logger.ActionLogger;

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
	 * Metodo para verificar se o id está nulo, pois alguns ids podem ser compostos.
	 * Entao se o retorno for nulo, quer dizer que o id pode está incomplento, no caso de
	 * ids compostos, ou seja, nao está completo o id, ou está nulo completo.	
	 * @return id está nulo
	 */
	public Boolean isIdNull();
	
    /**
     * @return Indica se o domínio é um modal.
     */
	public Boolean getModal();

    /**
     * @param Define se o domínio é um modal.
     */
	public void setModal(Boolean modal);
	
    /**
     * @return Informa se os campos estao disabled, porém apenas visualmente.
     */
	public Boolean getDisabled();

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
	
	/**
	 * @return
	 */
	public void setActionLogger(ActionLogger actionLogger);

}