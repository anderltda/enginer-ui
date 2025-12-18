package br.com.enginer.domain.system.usecase.enums;

/**
 * Enum que representa todos os operadores suportados nas condições dinâmicas da UI.
 *
 * <p>
 * Compatível com os operadores do frontend Angular (<code>OPERATORS_MAP</code>).
 * </p>
 * 
 * <h3>Exemplo geral de uso:</h3>
 * <pre>
 * {
 *   "field": "age",
 *   "operator": ">=",
 *   "value": "badge badge-success",
 *   "matchs": ["18"]
 * }
 * </pre>
 */
public enum TypeOperator {

	// ============================================================
	// OPERADORES NUMÉRICOS E LÓGICOS
	// ============================================================

	/**
	 * Igualdade exata.
	 * <p>Exemplo lógico: <code>10 === 10 → true</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "age", "operator": "===", "matchs": ["18"] }
	 * </pre>
	 */
	EQUALS("===", TypeOperatorCategory.NUMERIC),

	/**
	 * Diferença exata.
	 * <p>Exemplo lógico: <code>10 !== 5 → true</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "score", "operator": "!==", "matchs": ["100"] }
	 * </pre>
	 */
	NOT_EQUALS("!==", TypeOperatorCategory.NUMERIC),

	/**
	 * Maior que.
	 * <p>Exemplo lógico: <code>idade > 18</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "age", "operator": ">", "matchs": ["18"] }
	 * </pre>
	 */
	GREATER_THAN(">", TypeOperatorCategory.NUMERIC),

	/**
	 * Menor que.
	 * <p>Exemplo lógico: <code>idade < 18</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "age", "operator": "<", "matchs": ["18"] }
	 * </pre>
	 */
	LESS_THAN("<", TypeOperatorCategory.NUMERIC),

	/**
	 * Maior ou igual.
	 * <p>Exemplo lógico: <code>idade >= 18</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "age", "operator": ">=", "matchs": ["18"] }
	 * </pre>
	 */
	GREATER_THAN_OR_EQUALS(">=", TypeOperatorCategory.NUMERIC),

	/**
	 * Menor ou igual.
	 * <p>Exemplo lógico: <code>idade <= 18</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "age", "operator": "<=", "matchs": ["18"] }
	 * </pre>
	 */
	LESS_THAN_OR_EQUALS("<=", TypeOperatorCategory.NUMERIC),

	// ============================================================
	// OPERADORES TEXTUAIS
	// ============================================================

	/**
	 * Contém substring.
	 * <p>Exemplo lógico: <code>"Anderson" CONTAINS "son" → true</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "name", "operator": "CONTAINS", "matchs": ["son"] }
	 * </pre>
	 */
	CONTAINS("CONTAINS", TypeOperatorCategory.TEXTUAL),

	/**
	 * Não contém substring.
	 * <p>Exemplo lógico: <code>"Anderson" NOT CONTAINS "xyz" → true</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "name", "operator": "NOT CONTAINS", "matchs": ["xyz"] }
	 * </pre>
	 */
	NOT_CONTAINS("NOT CONTAINS", TypeOperatorCategory.TEXTUAL),

	/**
	 * Começa com substring.
	 * <p>Exemplo lógico: <code>"Anderson" STARTS_WITH "And" → true</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "name", "operator": "STARTS_WITH", "matchs": ["And"] }
	 * </pre>
	 */
	STARTS_WITH("STARTS_WITH", TypeOperatorCategory.TEXTUAL),

	/**
	 * Termina com substring.
	 * <p>Exemplo lógico: <code>"Anderson" ENDS_WITH "son" → true</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "name", "operator": "ENDS_WITH", "matchs": ["son"] }
	 * </pre>
	 */
	ENDS_WITH("ENDS_WITH", TypeOperatorCategory.TEXTUAL),

	// ============================================================
	// OPERADORES DE LISTA
	// ============================================================

	/**
	 * Valor está dentro da lista.
	 * <p>Exemplo lógico: <code>"ativo" IN ["ativo","pendente"]</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "status", "operator": "IN", "matchs": ["ativo,pendente"] }
	 * </pre>
	 */
	IN("IN", TypeOperatorCategory.LIST),

	/**
	 * Valor não está na lista.
	 * <p>Exemplo lógico: <code>"inativo" NOT IN ["ativo","pendente"]</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "status", "operator": "NOT IN", "matchs": ["ativo,pendente"] }
	 * </pre>
	 */
	NOT_IN("NOT IN", TypeOperatorCategory.LIST),

	// ============================================================
	// INTERVALOS NUMÉRICOS
	// ============================================================

	/**
	 * Valor está entre dois limites.
	 * <p>Exemplo lógico: <code>idade BETWEEN 18 e 60 → true</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "age", "operator": "BETWEEN", "matchs": ["18","60"] }
	 * </pre>
	 */
	BETWEEN("BETWEEN", TypeOperatorCategory.RANGE),

	/**
	 * Valor está fora do intervalo.
	 * <p>Exemplo lógico: <code>idade NOT BETWEEN 18 e 60 → true</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "age", "operator": "NOT BETWEEN", "matchs": ["18","60"] }
	 * </pre>
	 */
	NOT_BETWEEN("NOT BETWEEN", TypeOperatorCategory.RANGE),

	/**
	 * Alias para BETWEEN.
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "salary", "operator": "RANGE", "matchs": ["3000","7000"] }
	 * </pre>
	 */
	RANGE("RANGE", TypeOperatorCategory.RANGE),

	// ============================================================
	// OPERADORES DE DATA
	// ============================================================

	/**
	 * Data anterior.
	 * <p>Exemplo lógico: <code>2025-01-01 DATE_BEFORE 2025-10-20</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "startDate", "operator": "DATE_BEFORE", "matchs": ["2025-10-20"] }
	 * </pre>
	 */
	DATE_BEFORE("DATE_BEFORE", TypeOperatorCategory.DATE),

	/**
	 * Data posterior.
	 * <p>Exemplo lógico: <code>2025-11-01 DATE_AFTER 2025-10-20</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "startDate", "operator": "DATE_AFTER", "matchs": ["2025-10-20"] }
	 * </pre>
	 */
	DATE_AFTER("DATE_AFTER", TypeOperatorCategory.DATE),

	/**
	 * Datas equivalentes.
	 * <p>Exemplo lógico: <code>2025-10-20 DATE_EQUALS 2025-10-20</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "date", "operator": "DATE_EQUALS", "matchs": ["2025-10-20"] }
	 * </pre>
	 */
	DATE_EQUALS("DATE_EQUALS", TypeOperatorCategory.DATE),

	/**
	 * Data dentro de intervalo.
	 * <p>Exemplo lógico: <code>2025-10-15 DATE_BETWEEN 2025-10-01 e 2025-10-31</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "date", "operator": "DATE_BETWEEN", "matchs": ["2025-10-01","2025-10-31"] }
	 * </pre>
	 */
	DATE_BETWEEN("DATE_BETWEEN", TypeOperatorCategory.DATE),

	/**
	 * Data fora do intervalo.
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "date", "operator": "DATE_NOT_BETWEEN", "matchs": ["2025-10-01","2025-10-31"] }
	 * </pre>
	 */
	DATE_NOT_BETWEEN("DATE_NOT_BETWEEN", TypeOperatorCategory.DATE),

	// ============================================================
	// OPERADORES DE COMPRIMENTO DE TEXTO
	// ============================================================

	/**
	 * Comprimento maior que.
	 * <p>Exemplo lógico: <code>"Anderson" LENGTH > 5 → true</code></p>
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "name", "operator": "LENGTH >", "matchs": ["5"] }
	 * </pre>
	 */
	LENGTH_GREATER_THAN("LENGTH >", TypeOperatorCategory.LENGTH),

	/**
	 * Comprimento menor que.
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "name", "operator": "LENGTH <", "matchs": ["5"] }
	 * </pre>
	 */
	LENGTH_LESS_THAN("LENGTH <", TypeOperatorCategory.LENGTH),

	/**
	 * Comprimento entre dois valores.
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "name", "operator": "LENGTH BETWEEN", "matchs": ["3","10"] }
	 * </pre>
	 */
	LENGTH_BETWEEN("LENGTH BETWEEN", TypeOperatorCategory.LENGTH),

	/**
	 * Comprimento fora do intervalo.
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "name", "operator": "LENGTH NOT BETWEEN", "matchs": ["3","5"] }
	 * </pre>
	 */
	LENGTH_NOT_BETWEEN("LENGTH NOT BETWEEN", TypeOperatorCategory.LENGTH),

	// ============================================================
	// OPERADORES ESPECIAIS
	// ============================================================

	/**
	 * Valor nulo.
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "email", "operator": "IS NULL" }
	 * </pre>
	 */
	IS_NULL("IS NULL", TypeOperatorCategory.SPECIAL),

	/**
	 * Valor não nulo.
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "email", "operator": "IS NOT NULL" }
	 * </pre>
	 */
	IS_NOT_NULL("IS NOT NULL", TypeOperatorCategory.SPECIAL),

	/**
	 * Valor vazio.
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "description", "operator": "EMPTY" }
	 * </pre>
	 */
	EMPTY("EMPTY", TypeOperatorCategory.SPECIAL),

	/**
	 * Valor não vazio.
	 * <p>Exemplo JSON:</p>
	 * <pre>
	 * { "field": "description", "operator": "NOT EMPTY" }
	 * </pre>
	 */
	NOT_EMPTY("NOT EMPTY", TypeOperatorCategory.SPECIAL);

	// ============================================================
	// ATRIBUTOS E MÉTODOS
	// ============================================================

	private final String value;
	private final TypeOperatorCategory category;

	TypeOperator(String value, TypeOperatorCategory category) {
		this.value = value;
		this.category = category;
	}

	public String getValue() {
		return value;
	}

	public TypeOperatorCategory getCategory() {
		return category;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static TypeOperator fromValue(String value) {
		for (TypeOperator op : values()) {
			if (op.getValue().equalsIgnoreCase(value)) {
				return op;
			}
		}
		throw new IllegalArgumentException("Operador inválido: " + value);
	}

	public static TypeOperator[] valuesByCategory(TypeOperatorCategory category) {
		return java.util.Arrays.stream(values())
			.filter(op -> op.getCategory() == category)
			.toArray(TypeOperator[]::new);
	}
}