package br.com.enginer.domain.system.usecase.core.schema.paginator.config;

/**
 * 
 */
public class Config {

	private Boolean expandable;
	private Boolean editableAll;
	private Boolean multiSelectable;

	public Boolean getExpandable() {
		return expandable;
	}

	public void setExpandable(Boolean expandable) {
		this.expandable = expandable;
	}

	public Boolean getMultiSelectable() {
		return multiSelectable;
	}

	public void setMultiSelectable(Boolean multiSelectable) {
		this.multiSelectable = multiSelectable;
	}

	public Boolean getEditableAll() {
		return editableAll;
	}

	public void setEditableAll(Boolean editableAll) {
		this.editableAll = editableAll;
	}
}
