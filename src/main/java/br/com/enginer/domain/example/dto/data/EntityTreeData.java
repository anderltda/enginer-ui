package br.com.enginer.domain.example.dto.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

public class EntityTreeData extends DomainAbstract<String> {

	private String id;
	private String animal;
	private Integer indicator;
	private Double amount;
	private LocalDate localDate;
	private LocalDateTime localDateTime;

	private EntityStatusData entityStatusData;
	private EntityFourData entityFourData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnimal() {
		return animal;
	}

	public void setAnimal(String animal) {
		this.animal = animal;
	}

	public Integer getIndicator() {
		return indicator;
	}

	public void setIndicator(Integer indicator) {
		this.indicator = indicator;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public EntityStatusData getEntityStatusData() {
		return entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	public EntityFourData getEntityFourData() {
		return entityFourData;
	}

	public void setEntityFourData(EntityFourData entityFourData) {
		this.entityFourData = entityFourData;
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
		EntityTreeData other = (EntityTreeData) obj;
		return Objects.equals(id, other.id);
	}
}
