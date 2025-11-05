package br.com.enginer.domain.system.usercase.enums;

public enum TypeLocale {

	PT_BR("pt-BR"), 
	EN_US("en-US");

	private final String value;

	TypeLocale(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
