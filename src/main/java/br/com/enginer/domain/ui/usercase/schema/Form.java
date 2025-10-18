package br.com.enginer.domain.ui.usercase.schema;

import java.util.List;

import br.com.enginer.domain.ui.usercase.schema.field.Field;
import br.com.enginer.domain.ui.usercase.schema.paginator.Paginator;
import br.com.enginer.domain.ui.usercase.schema.tab.Tab;
import br.com.enginer.domain.ui.usercase.schema.validate.Validate;

/**
 * 
 */
public class Form {

	private String id;
	private String title;
	private Tab tab;
	private Validate validate;
	private Paginator paginator;
	private List<Field> fields;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public Validate getValidate() {
		return validate;
	}

	public void setValidate(Validate validate) {
		this.validate = validate;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

}
