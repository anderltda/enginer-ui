package br.com.enginer.domain.ui.usercase.schema.paginator.config;

/**
 * 
 */
public class Config {

	private Boolean expandable;
	private Boolean editable;
	private Boolean editableAll;
	private Boolean multiSelectable;
	private Boolean deletable;

	public Boolean getExpandable() {
		return expandable;
	}

	public void setExpandable(Boolean expandable) {
		this.expandable = expandable;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public Boolean getMultiSelectable() {
		return multiSelectable;
	}

	public void setMultiSelectable(Boolean multiSelectable) {
		this.multiSelectable = multiSelectable;
	}

	public Boolean getDeletable() {
		return deletable;
	}

	public void setDeletable(Boolean deletable) {
		this.deletable = deletable;
	}

	public Boolean getEditableAll() {
		return editableAll;
	}

	public void setEditableAll(Boolean editableAll) {
		this.editableAll = editableAll;
	}
}
