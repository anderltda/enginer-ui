package br.com.enginer.domain.ui.usercase.enums;

/**
 * Representa categorias lógicas para os operadores de comparação.
 * 
 * Usado para agrupar operadores por tipo de dado ou contexto de uso.
 */
public enum TypeOperatorCategory {

	NUMERIC("Numérico / Lógico"),
	TEXTUAL("Texto"),
	LIST("Lista"),
	RANGE("Intervalo Numérico"),
	DATE("Data / Tempo"),
	LENGTH("Comprimento de Texto"),
	SPECIAL("Especiais");

	private final String description;

	TypeOperatorCategory(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}