package br.com.enginer.domain.system.usecase.enums;

public enum TypeDateFormat {

	DATE_TIME_FORMAT("DD/MM/YYYY HH:mm:ss"), 
	DATE_FORMAT("DD/MM/YYYY"), 
	TIME_HHMM_FORMAT("HH:mm"), 
	TIME_HHMMSS_FORMAT("HH:mm:ss");

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