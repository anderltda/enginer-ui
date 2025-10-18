package br.com.enginer.domain.ui.usercase.enums;

public enum TypeButton {

	SUBMIT("submit"), BUTTON("button");

	private final String value;

	TypeButton(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}