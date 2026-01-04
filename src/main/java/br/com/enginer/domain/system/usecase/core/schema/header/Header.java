package br.com.enginer.domain.system.usecase.core.schema.header;

import java.util.List;

/**
 * 
 */
public class Header {

	private String title;
	private List<DropdownItem> dropdownItens;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the dropdownItens
	 */
	public List<DropdownItem> getDropdownItens() {
		return dropdownItens;
	}

	/**
	 * @param dropdownItens the dropdownItens to set
	 */
	public void setDropdownItens(List<DropdownItem> dropdownItens) {
		this.dropdownItens = dropdownItens;
	}

}
