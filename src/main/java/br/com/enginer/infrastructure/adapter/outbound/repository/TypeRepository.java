package br.com.enginer.infrastructure.adapter.outbound.repository;

public enum TypeRepository {

	QUERY("query"), MAPPER("mapper");

	private final String value;

	TypeRepository(String value) {
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
