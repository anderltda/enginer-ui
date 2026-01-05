package br.com.enginer.domain.system.usecase.core.schema.instance;

import java.util.List;

import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIAttachment;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIIgnore;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.UITag;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.core.enums.TypeFileUpload;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

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
	
	@UIIgnore
	private TypeTemplate typeTemplate;
	
	@UITag(label = "Tags", disable = false)
	private transient List<String> tags;
	
	//@UIAttachment(label = "", mode = TypeFileUpload.WALL_PICKER, listType = "picture-card", limit = 5)
	//@UIAttachment(label = "Arquivos", mode = TypeFileUpload.SIMPLE, listType = "text", limit = 3)
	//@UIAttachment(label = "Arquivos", mode = TypeFileUpload.DRAG_DROP, listType = "text", limit = 2)
	@UIFieldValidation(required = false)
	@UIAttachment(label = "", mode = TypeFileUpload.LIST, listType = "picture", limit = 8)
	private transient List<UploadFile> attachments;
	
	/**
	 * @return id
	 */
	@Override
	public I getId() {
		return null;
	}

	/**
	 * Define o domain principal.
	 */
	@Override
	public void setMainDomain(String mainDomain) {
		this.mainDomain = mainDomain;
	}

	/**
	 * @param actionLogger
	 */
	@Override
	public void setActionLogger(ActionLogger actionLogger) {
		this.actionLogger = actionLogger;
	}	

    /**
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

    @Override
	public Boolean isIdNull() {
		return true;
	}

    /**
	 * @return
	 */
	@Override
	public ActionLogger getActionLogger() {
		return actionLogger;
	}

    /**
	 * @return the typeTemplate
	 */
	@Override
	public TypeTemplate getTypeTemplate() {
		return typeTemplate;
	}
	
	/**
     * Define se o domínio é um modal.
     */
	@Override
	public void setModal(Boolean modal) {
		this.modal = modal;
	}

	/**
	 * Informar qual o domain principal.
	 */
	@Override
	public String getMainDomain() {
		return this.mainDomain;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
     * Define se o disabled está habilitado.
     */
	@Override
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	
	/**
	 * Metodo para definir o id, porem em dominios abstratos nao faz nada.
	 */
	@Override
	public void setId(I id) {
		// Do nothing
	}
	
	/**
     * Informa se os campos estao disabled, porém apenas visualmente.
     */
	@Override
	public Boolean getDisabled() {
		return disabled;
	}
	
	
	/**
	 * @param typeTemplate the typeTemplate to set
	 */
	@Override
	public void setTypeTemplate(TypeTemplate typeTemplate) {
		this.typeTemplate = typeTemplate;
	}
	
	/**
	 * @return the attachments
	 */
	public List<UploadFile> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(List<UploadFile> attachments) {
		this.attachments = attachments;
	}

	/**
     * Indica se o domínio é um modal.
     */
	@Override
	public Boolean getModal() {
		return modal;
	}
}
