package br.com.enginer.domain.system.usecase.core.schema;

import java.util.List;

import br.com.enginer.domain.system.usecase.core.schema.field.Field;
import br.com.enginer.domain.system.usecase.core.schema.header.Header;
import br.com.enginer.domain.system.usecase.core.schema.paginator.Paginator;
import br.com.enginer.domain.system.usecase.core.schema.tab.Tab;
import br.com.enginer.domain.system.usecase.core.schema.validate.Validate;

/**
 * 
 */
public class Form {

	private String id;
	private Header header;
	private Tab tab;
	private Validate validate;
	private Paginator paginator;
	private List<Field> fields;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the header
	 */
	public Header getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(Header header) {
		this.header = header;
	}

	/**
	 * @return the tab
	 */
	public Tab getTab() {
		return tab;
	}

	/**
	 * @param tab the tab to set
	 */
	public void setTab(Tab tab) {
		this.tab = tab;
	}

	/**
	 * @return the validate
	 */
	public Validate getValidate() {
		return validate;
	}

	/**
	 * @param validate the validate to set
	 */
	public void setValidate(Validate validate) {
		this.validate = validate;
	}

	/**
	 * @return the paginator
	 */
	public Paginator getPaginator() {
		return paginator;
	}

	/**
	 * @param paginator the paginator to set
	 */
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	/**
	 * @return the fields
	 */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

}
