package br.com.enginer.domain.system.usecase.core.schema.instance;

import java.time.Instant;

import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

/**
 * @param <I>
 */
public interface Domain<I> {

	/**
	 * @return id
	 */
	I getId();

	/**
	 * @param id
	 */
	void setId(I id);

	/**
	 * Metodo para verificar se o id está nulo, pois alguns ids podem ser compostos.
	 * Entao se o retorno for nulo, quer dizer que o id pode está incomplento, no
	 * caso de ids compostos, ou seja, nao está completo o id, ou está nulo
	 * completo.
	 * 
	 * @return id está nulo
	 */
	Boolean isIdNull();

	/**
	 * @return Indica se o domínio é um modal.
	 */
	Boolean getModal();

	/**
	 * @param Define se o domínio é um modal.
	 */
	void setModal(Boolean modal);

	/**
	 * @return Informa se os campos estao disabled, porém apenas visualmente.
	 */
	Boolean getDisabled();

	/**
	 * @param Define se o disabled está habilitado.
	 */
	void setDisabled(Boolean disabled);

	/**
	 * @param Define o domain principal.
	 */
	void setMainDomain(String mainDomain);

	/**
	 * @return Informar qual o domain principal.
	 */
	String getMainDomain();

	/**
	 * @return
	 */
	ActionLogger getActionLogger();

	/**
	 * @return
	 */
	void setActionLogger(ActionLogger actionLogger);

	/**
	 * @return
	 */
	TypeTemplate getTypeTemplate();

	/**
	 * @return
	 */
	void setTypeTemplate(TypeTemplate typeTemplate);

	/**
	 * @return the createdBy
	 */
	UserAccount getCreatedBy();

	/**
	 * @param createdBy the createdBy to set
	 */
	void setCreatedBy(UserAccount createdBy);

	/**
	 * @return the updatedBy
	 */
	UserAccount getUpdatedBy();

	/**
	 * @param updatedBy the updatedBy to set
	 */
	void setUpdatedBy(UserAccount updatedBy);

	/**
	 * @return the createdById
	 */
	Long getCreatedById();

	/**
	 * @param createdById the createdById to set
	 */
	void setCreatedById(Long createdById);

	/**
	 * @return the updatedById
	 */
	Long getUpdatedById();

	/**
	 * @param updatedById the updatedById to set
	 */
	void setUpdatedById(Long updatedById);

	/**
	 * @return the createdAt
	 */
	Instant getCreatedAt();

	/**
	 * @param createdAt the createdAt to set
	 */
	void setCreatedAt(Instant createdAt);

	/**
	 * @return the updatedAt
	 */
	Instant getUpdatedAt();

	/**
	 * @param updatedAt the updatedAt to set
	 */
	void setUpdatedAt(Instant updatedAt);
}