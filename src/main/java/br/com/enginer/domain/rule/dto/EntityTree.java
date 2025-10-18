package br.com.enginer.domain.rule.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTree implements Model<String> {

	private String id;

	private String animal;

	private Integer indicator;

	private Double amount;

	private LocalDate localDate;

	private LocalDateTime localDateTime;

	private EntityFour entityFour;

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

	public EntityFour getEntityFour() {
		return entityFour;
	}

	public void setEntityFour(EntityFour entityFour) {
		this.entityFour = entityFour;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(animal, localDate, localDateTime, entityFour, id, indicator, amount);
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
		EntityTree other = (EntityTree) obj;
		return Objects.equals(animal, other.animal) && Objects.equals(localDate, other.localDate)
				&& Objects.equals(localDateTime, other.localDateTime) && Objects.equals(entityFour, other.entityFour)
				&& Objects.equals(id, other.id) && Objects.equals(indicator, other.indicator)
				&& Objects.equals(amount, other.amount);
	}
}
