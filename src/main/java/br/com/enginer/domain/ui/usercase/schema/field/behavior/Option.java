package br.com.enginer.domain.ui.usercase.schema.field.behavior;

/**
 * 
 */
public class Option {

	private String label;
	private Object value;

	public Option(String label, Object value) {
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
