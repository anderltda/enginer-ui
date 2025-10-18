package br.com.enginer.domain.ui.usercase.schema.field.behavior;

/**
 * 
 */
public class Autocomplete {

	private String domain;
	private String attribute;
	private String[] suggestions;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String[] getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(String[] suggestions) {
		this.suggestions = suggestions;
	}

}
