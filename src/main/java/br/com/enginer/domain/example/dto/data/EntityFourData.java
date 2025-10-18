package br.com.enginer.domain.example.dto.data;

import java.time.LocalDateTime;
import java.util.Objects;

import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

public class EntityFourData extends DomainAbstract<String> {

	private String id;
	private String fruit;
	private Integer attribute;
	private LocalDateTime inclusionDateTime;

	private EntityStatusData entityStatusData;

	private EntityFiveData entityFiveData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFruit() {
		return fruit;
	}

	public void setFruit(String fruit) {
		this.fruit = fruit;
	}

	public Integer getAttribute() {
		return attribute;
	}

	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}

	public LocalDateTime getInclusionDateTime() {
		return inclusionDateTime;
	}

	public void setInclusionDateTime(LocalDateTime inclusionDateTime) {
		this.inclusionDateTime = inclusionDateTime;
	}

	public EntityStatusData getEntityStatusData() {
		return entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	public EntityFiveData getEntityFiveData() {
		return entityFiveData;
	}

	public void setEntityFiveData(EntityFiveData entityFiveData) {
		this.entityFiveData = entityFiveData;
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
		EntityFourData other = (EntityFourData) obj;
		return Objects.equals(id, other.id);
	}
}
