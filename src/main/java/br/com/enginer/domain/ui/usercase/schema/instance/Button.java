package br.com.enginer.domain.ui.usercase.schema.instance;

import br.com.enginer.domain.ui.usercase.enums.TypeButton;
import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

/**
 * 
 */
public class Button {

	private String type;
	private String label;
	private String icon;
	private Boolean disabled;
	private Boolean highlight;
	private Boolean dropdown;
	private Boolean confirm;
	private Boolean needsValidation;
	private String[] notDomain;
	private String state;
	private TypeTemplate[] template;
	private Action action;

	public Button() {
		super();
	}

	public Button(TypeButton type) {
		super();
		this.type = type.getValue();
	}

	public String getType() {
		return type;
	}

	public void setType(TypeButton type) {
		this.type = type.getValue();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getHighlight() {
		return highlight;
	}

	public void setHighlight(Boolean highlight) {
		this.highlight = highlight;
	}

	public Boolean getDropdown() {
		return dropdown;
	}

	public void setDropdown(Boolean dropdown) {
		this.dropdown = dropdown;
	}

	public Boolean getConfirm() {
		return confirm;
	}

	public void setConfirm(Boolean confirm) {
		this.confirm = confirm;
	}

	public Boolean getNeedsValidation() {
		return needsValidation;
	}

	public void setNeedsValidation(Boolean needsValidation) {
		this.needsValidation = needsValidation;
	}

	public String[] getNotDomain() {
		return notDomain;
	}

	public void setNotDomain(String[] notDomain) {
		this.notDomain = notDomain;
	}

	public String getState() {
		return state;
	}

	public void setState(TypeButtonState state) {
		this.state = state.getValue();
	}

	public TypeTemplate[] getTemplate() {
		return template;
	}

	public void setTemplate(TypeTemplate[] template) {
		this.template = template;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
}
