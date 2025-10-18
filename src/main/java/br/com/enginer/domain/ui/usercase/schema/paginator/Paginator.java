package br.com.enginer.domain.ui.usercase.schema.paginator;

import java.util.List;

import br.com.enginer.domain.ui.usercase.schema.field.Field;
import br.com.enginer.domain.ui.usercase.schema.paginator.column.Column;
import br.com.enginer.domain.ui.usercase.schema.paginator.config.Config;

/**
 * 
 */
public class Paginator {

	private Column column;
	private Config config;
	private List<Field> actions;

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public List<Field> getActions() {
		return actions;
	}

	public void setActions(List<Field> actions) {
		this.actions = actions;
	}
}
