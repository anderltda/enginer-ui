package br.com.enginer.domain.ui.usercase.schema.validate.conditional;

import br.com.enginer.domain.ui.usercase.enums.TypeOperator;

/**
 * 
 */
public class Conditional {

	private String label;
	private String field;
	private String operator;
	private String[] matchs;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(TypeOperator operator) {
		this.operator = operator.getValue();
	}

	public String[] getMatchs() {
		return matchs;
	}

	public void setMatchs(String[] matchs) {
		this.matchs = matchs;
	}
}
