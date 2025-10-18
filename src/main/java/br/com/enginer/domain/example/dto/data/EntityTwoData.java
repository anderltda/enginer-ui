package br.com.enginer.domain.example.dto.data;

import java.time.LocalDate;
import java.util.Objects;

import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

public class EntityTwoData extends DomainAbstract<String> {

	private String id;
	private String color;
	private Integer hex;
	private Double cost;
	private LocalDate inclusionDate;

	private EntityStatusData entityStatusData;
	private EntityTreeData entityTreeData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getHex() {
		return hex;
	}

	public void setHex(Integer hex) {
		this.hex = hex;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public LocalDate getInclusionDate() {
		return inclusionDate;
	}

	public void setInclusionDate(LocalDate inclusionDate) {
		this.inclusionDate = inclusionDate;
	}

	public EntityStatusData getEntityStatusData() {
		return entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	public EntityTreeData getEntityTreeData() {
		return entityTreeData;
	}

	public void setEntityTreeData(EntityTreeData entityTreeData) {
		this.entityTreeData = entityTreeData;
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
		EntityTwoData other = (EntityTwoData) obj;
		return Objects.equals(id, other.id);
	}
}
