package br.com.enginer.domain.ui.usercase.schema.validate.dependency;

/**
 * 
 */
public class Dependency {

	private String label;
	private String field;
	private String[] depends;

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

	public String[] getDepends() {
		return depends;
	}

	public void setDepends(String[] depends) {
		this.depends = depends;
	}

}
