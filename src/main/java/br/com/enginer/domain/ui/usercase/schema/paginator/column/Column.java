package br.com.enginer.domain.ui.usercase.schema.paginator.column;

import java.util.List;
import java.util.Map;

/**
 * 
 */
public class Column {

	private Map<String, String> name;
	private Map<String, String> type;
	private Map<String, String> totalizer;
	private List<String> initials;
	private List<String> rows;
	private List<String> hiddens;
	private List<String> editables;
	private List<String> visibles;
	private List<String> calculations;

	public Map<String, String> getName() {
		return name;
	}

	public void setName(Map<String, String> name) {
		this.name = name;
	}

	public Map<String, String> getType() {
		return type;
	}

	public void setType(Map<String, String> type) {
		this.type = type;
	}

	public Map<String, String> getTotalizer() {
		return totalizer;
	}

	public void setTotalizer(Map<String, String> totalizer) {
		this.totalizer = totalizer;
	}

	public List<String> getInitials() {
		return initials;
	}

	public void setInitials(List<String> initials) {
		this.initials = initials;
	}

	public List<String> getRows() {
		return rows;
	}

	public void setRows(List<String> rows) {
		this.rows = rows;
	}

	public List<String> getHiddens() {
		return hiddens;
	}

	public void setHiddens(List<String> hiddens) {
		this.hiddens = hiddens;
	}

	public List<String> getEditables() {
		return editables;
	}

	public void setEditables(List<String> editables) {
		this.editables = editables;
	}

	public List<String> getVisibles() {
		return visibles;
	}

	public void setVisibles(List<String> visibles) {
		this.visibles = visibles;
	}

	public List<String> getCalculations() {
		return calculations;
	}

	public void setCalculations(List<String> calculations) {
		this.calculations = calculations;
	}
}
