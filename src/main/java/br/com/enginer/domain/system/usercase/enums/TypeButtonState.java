package br.com.enginer.domain.system.usercase.enums;

public enum TypeButtonState {
	 
	BTN_ICON_DANGER("text-danger"), 
	BTN_ICON_DEFAULT("text-default"), 
	BTN_STATE_DEFAULT("btn-default"), 
	BTN_STATE_INFO("btn-info"), 
	BTN_STATE_PRIMARY("btn-primary"), 
	BTN_STATE_COMPLETE("btn-complete"),
	BTN_STATE_SUCCESS("btn-success"), 
	BTN_STATE_DANGER("btn-danger"), 
	BTN_STATE_WARNING("btn-warning");

	private final String value;

	TypeButtonState(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}