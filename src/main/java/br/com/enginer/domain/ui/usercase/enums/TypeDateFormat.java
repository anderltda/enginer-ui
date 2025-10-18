package br.com.enginer.domain.ui.usercase.enums;

public enum TypeDateFormat {

	DATE_TIME_FORMAT("DD/MM/YYYY HH:mm:ss"), DATE_FORMAT("DD/MM/YYYY");

	private final String value;

	TypeDateFormat(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}