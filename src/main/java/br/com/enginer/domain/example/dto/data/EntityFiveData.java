package br.com.enginer.domain.example.dto.data;

import java.util.Objects;

import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

public class EntityFiveData extends DomainAbstract<String> {

	private String id;
	private String reference;
	private Integer factor;

	private EntityStatusData entityStatusData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getFactor() {
		return factor;
	}

	public void setFactor(Integer factor) {
		this.factor = factor;
	}

	public EntityStatusData getEntityStatusData() {
		return entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityFiveData other = (EntityFiveData) obj;
		return Objects.equals(id, other.id);
	}
}
