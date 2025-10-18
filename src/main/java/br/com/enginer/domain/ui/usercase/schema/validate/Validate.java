package br.com.enginer.domain.ui.usercase.schema.validate;

import java.util.List;

import br.com.enginer.domain.ui.usercase.schema.validate.conditional.Conditional;
import br.com.enginer.domain.ui.usercase.schema.validate.custom.Custom;
import br.com.enginer.domain.ui.usercase.schema.validate.dependency.Dependency;
import br.com.enginer.domain.ui.usercase.schema.validate.global.Global;

/**
 * 
 */
public class Validate {

	private List<Global> global;
	private List<Custom> custom;
	private List<Conditional> conditional;
	private List<Dependency> dependency;

	public List<Global> getGlobal() {
		return global;
	}

	public void setGlobal(List<Global> global) {
		this.global = global;
	}

	public List<Custom> getCustom() {
		return custom;
	}

	public void setCustom(List<Custom> custom) {
		this.custom = custom;
	}

	public List<Conditional> getConditional() {
		return conditional;
	}

	public void setConditional(List<Conditional> conditional) {
		this.conditional = conditional;
	}

	public List<Dependency> getDependency() {
		return dependency;
	}

	public void setDependency(List<Dependency> dependency) {
		this.dependency = dependency;
	}

}
