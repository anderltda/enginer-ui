package br.com.enginer.domain.system.usecase.schema.instance;

import java.util.List;

import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.usecase.annotation.field.UIFile;
import br.com.enginer.domain.system.usecase.annotation.field.UIIgnore;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.UITag;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.enums.TypeFileUpload;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;
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
	
	@UIFieldValidation(required = false, template = { TypeTemplate.FORM, TypeTemplate.TAB })
	//@UIFile(label = "Imagem", mode = TypeFileUpload.WALL_PICKER, listType = "picture-card", limit = 5)
	@UIFile(label = "Arquivos", mode = TypeFileUpload.LIST, listType = "picture", limit = 8)
	//@UIFile(label = "Arquivos", mode = TypeFileUpload.SIMPLE, listType = "text", limit = 3)
	//@UIFile(label = "Arquivos", mode = TypeFileUpload.DRAG_DROP, listType = "text", limit = 2)
	private transient List<UploadFile> files;
	
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
	public Boolean getModal() {
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
	public Boolean getDisabled() {
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

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	/**
	 * @return the files
	 */
	public List<UploadFile> getFiles() {
		return files;
	}
	
	/**
	 * @param files the files to set
	 */
	public void setFiles(List<UploadFile> files) {
		this.files = files;
	}
}
