package br.com.enginer.domain.ui.usercase.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

/**
 * 
 */
public class ParamUtils {

	private TypeTemplate typeTemplate;
	private List<String> visibles;
	private List<String> initials;
	private List<String> rows;
	private List<String> editables;
	private List<String> hiddens;
	private List<String> calculations;
	private List<Map.Entry<String, Integer>> rowsMap;
	private Map<String, String> columnNames;
	private Map<String, String> columnTypes;
	private Map<String, String> totalizers;

	public TypeTemplate getTypeTemplate() {
		return typeTemplate;
	}

	public void setTypeTemplate(TypeTemplate typeTemplate) {
		this.typeTemplate = typeTemplate;
	}

	public List<String> getVisibles() {
		return visibles;
	}

	public void addVisibles(String row) {

		if (this.visibles == null) {
			visibles = new ArrayList<String>();
		}

		this.visibles.add(row);
	}

	public List<String> getInitials() {
		return initials;
	}

	public void addInitials(String row) {
		if (this.initials == null) {
			initials = new ArrayList<String>();
		}
		this.initials.add(row);
	}

	public List<Map.Entry<String, Integer>> getRowsMap() {
		return rowsMap;
	}

	public void addRowsMap(Map.Entry<String, Integer> row) {

		if (this.rowsMap == null) {
			this.rowsMap = new ArrayList<Map.Entry<String, Integer>>();
		}

		this.rowsMap.add(row);
	}

	public Map<String, String> getColumnTypes() {
		return columnTypes;
	}

	public void addColumnTypes(String name, String type) {
		if (this.columnTypes == null) {
			this.columnTypes = new HashMap<String, String>();
		}

		this.columnTypes.put(name, type.toLowerCase().replace("local", ""));
	}
	
	public Map<String, String> getColumnNames() {
		return columnNames;
	}

	public void addColumnNames(String name, String label) {
		if (this.columnNames == null) {
			this.columnNames = new HashMap<String, String>();
		}

		this.columnNames.put(name, label);
	}

	public List<String> getRows() {
		// Ordena pela parte Integer
		this.rowsMap.sort(Comparator.comparingInt(Map.Entry::getValue));
		this.rowsMap.forEach(entry -> this.addRows(entry.getKey()));
		return this.rows;
	}

	public void addRows(String row) {

		if (this.rows == null) {
			this.rows = new ArrayList<String>();
		}

		this.rows.add(row);
	}

	public List<String> getEditables() {
		return editables;
	}

	public void addEditables(String row) {

		if (this.editables == null) {
			this.editables = new ArrayList<String>();
		}

		this.editables.add(row);
	}

	public List<String> getHiddens() {
		return hiddens;
	}

	public void addHiddens(String row) {

		if (this.hiddens == null) {
			this.hiddens = new ArrayList<String>();
		}

		this.hiddens.add(row);
	}

	public List<String> getCalculations() {
		return calculations;
	}

	public void addCalculations(String row) {

		if (this.calculations == null) {
			this.calculations = new ArrayList<String>();
		}

		this.calculations.add(row);
	}

	public Map<String, String> getTotalizers() {
		return totalizers;
	}

	public void addTotalizers(String name, String label) {
		
		if (this.totalizers == null) {
			this.totalizers = new HashMap<String, String>();
		}
		
		this.totalizers.put(name, label);
	}
	
	
	
}
