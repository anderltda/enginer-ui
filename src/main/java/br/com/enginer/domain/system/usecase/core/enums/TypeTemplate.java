package br.com.enginer.domain.system.usecase.core.enums;

/**
 * Tipos de template suportados pela UI. Usado pelo endpoint /module/{template}.
 * Apenas para TAB, ROW, FORM e FILTER;
 */
public enum TypeTemplate {

	TYPE_TEMPLATE, FILTER, FORM, ROW, TAB, PAGINATOR, MODAL, DISABLED, MAIN_DOMAIN;

	public static TypeTemplate from(String value) {
		if (value == null || value.isBlank())
			return null;
		return TypeTemplate.valueOf(value.trim().toUpperCase());
	}
}