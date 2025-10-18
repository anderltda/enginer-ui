package br.com.enginer.domain.ui.usercase.enums;

public enum TypeOperator {

	EQUALS("==="), NOT_EQUALS("!=="), GREATER_THAN(">"), LESS_THAN("<"), GREATER_THAN_OR_EQUALS(">="),
	LESS_THAN_OR_EQUALS("<="), IN("in"), NOT_IN("not in");

	private final String value;

	TypeOperator(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}