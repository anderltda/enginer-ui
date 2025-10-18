package br.com.enginer.domain.example.dto.view;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
public class EntityOneView extends DomainAbstract<Long> {

	private Long id;

	// EntityOne
	private Long idEntityOne;
	private String name;
	private Integer age;
	private Boolean code;
	private Double height;
	private LocalDate birthDate;
	private LocalDateTime prohibitedDateTime;

	// EntityStatus
	private Long idEntityStatus;
	private String nameStatus;
	private Integer status;
	private Boolean ativo;
	private LocalDateTime startDateTime;

	// EntityTwo
	private String idEntityTwo;
	private String color;
	private Integer hex;
	private Double cost;
	private LocalDate inclusionDate;

	// EntityTree
	private String idEntityTree;
	private String animal;
	private Integer indicator;
	private Double amount;
	private LocalDate localDate;
	private LocalDateTime localDateTime;

	// EntityFour
	private String idEntityFour;
	private String fruit;
	private Integer attribute;
	private LocalDateTime inclusionDateTime;

	// EntityFive
	private String idEntityFive;
	private String reference;
	private Integer factor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdEntityOne() {
		return idEntityOne;
	}

	public void setIdEntityOne(Long idEntityOne) {
		this.idEntityOne = idEntityOne;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getCode() {
		return code;
	}

	public void setCode(Boolean code) {
		this.code = code;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDateTime getProhibitedDateTime() {
		return prohibitedDateTime;
	}

	public void setProhibitedDateTime(LocalDateTime prohibitedDateTime) {
		this.prohibitedDateTime = prohibitedDateTime;
	}

	public Long getIdEntityStatus() {
		return idEntityStatus;
	}

	public void setIdEntityStatus(Long idEntityStatus) {
		this.idEntityStatus = idEntityStatus;
	}

	public String getNameStatus() {
		return nameStatus;
	}

	public void setNameStatus(String nameStatus) {
		this.nameStatus = nameStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getIdEntityTwo() {
		return idEntityTwo;
	}

	public void setIdEntityTwo(String idEntityTwo) {
		this.idEntityTwo = idEntityTwo;
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

	public String getIdEntityTree() {
		return idEntityTree;
	}

	public void setIdEntityTree(String idEntityTree) {
		this.idEntityTree = idEntityTree;
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

	public String getIdEntityFour() {
		return idEntityFour;
	}

	public void setIdEntityFour(String idEntityFour) {
		this.idEntityFour = idEntityFour;
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

	public String getIdEntityFive() {
		return idEntityFive;
	}

	public void setIdEntityFive(String idEntityFive) {
		this.idEntityFive = idEntityFive;
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
}
