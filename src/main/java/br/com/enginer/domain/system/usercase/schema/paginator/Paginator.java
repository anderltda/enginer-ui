package br.com.enginer.domain.system.usercase.schema.paginator;

import java.util.List;

import br.com.enginer.domain.system.usercase.schema.field.Field;
import br.com.enginer.domain.system.usercase.schema.paginator.column.Column;
import br.com.enginer.domain.system.usercase.schema.paginator.config.Config;
import br.com.enginer.domain.system.usercase.schema.validate.conditional.Conditional;

/**
 * 
 */
public class Paginator {

	private Column column;
	private Config config;
	private List<Field> actions;
	private List<Conditional> conditional;

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

	public List<Conditional> getConditional() {
		return conditional;
	}

	public void setConditional(List<Conditional> conditional) {
		this.conditional = conditional;
	}
}
