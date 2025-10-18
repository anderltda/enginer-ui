package br.com.enginer.domain.example.dto.data;

import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

public class EntityNineData extends DomainAbstract<EntityNineDataId>  {

	private EntityNineDataId id;

	private String keyNine;

	private String code;

	private String variable;

	public EntityNineDataId getId() {
		return this.id;
	}

	public void setId(EntityNineDataId id) {
		this.id = id;
	}

	public String getKeyNine() {
		return this.keyNine;
	}

	public void setKeyNine(String keyNine) {
		this.keyNine = keyNine;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVariable() {
		return this.variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String toString() {
		return "EntityNineData [id=" + id + ", keyNine=" + keyNine + ", code=" + code + ", variable=" + variable + "]";
	}
}
