package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIHidden;
import br.com.enginer.domain.ui.usercase.annotation.field.UIRow;
import br.com.enginer.domain.ui.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionTriggerMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
@UITitle("Entity Rows")
@UIPaginator(config = @UIConfig(expandable = true, editableAll = true, multiSelectable = false, deletable = true), 
actions = @UIButtonAction(
value = { 
	@UIButton(
		label = Constants.LABEL_BACK, 
		icon = "undo",
		needsValidation = false,
		state = TypeButtonState.BTN_STATE_PRIMARY, 
		template = { TypeTemplate.ROW },
		action = @UIAction( 
			method = @UIActionMethod(
				clientMethod = "triggerMethod", 
				trigger = @UIActionTriggerMethod(clientMethod = "onBack")
			) 
		)
	),
	@UIButton(
		label = Constants.LABEL_ADD, 
		icon = "plus", 
		needsValidation = true,
		state = TypeButtonState.BTN_STATE_PRIMARY, 
		template = { TypeTemplate.ROW },
		action = @UIAction( 
			 method = @UIActionMethod(
				 clientMethod = "triggerMethod", 
				 trigger = @UIActionTriggerMethod(clientMethod = "setDataSetField")
			 ) 
	    )
	),	
	@UIButton(
		label = Constants.LABEL_SAVE, 
		icon = "save",
		needsValidation = true,
		state = TypeButtonState.BTN_STATE_PRIMARY, 
		template = { TypeTemplate.ROW }, 
		action = @UIAction(
			method = @UIActionMethod(serverMethod = "rowSalvar")
		)
	) 
}
))
public class EntityRow extends DomainAbstract<Long> {

	@UIHidden()
	private Long id;

	@UIHidden()
	@UIRow(visible = false, fields = { "name" })
	private EntityTen entityTen;

	@UIHidden()
	@UIColumn(label = "String", initial = true)
	@UIRow(visible = true, editable = true, order = 1)
	private String lineString;

	@UIHidden()
	@UIColumn(label = "Integer", initial = true)
	@UIRow(visible = true, editable = true, order = 2)
	private Integer lineInteger;

	@UIHidden()
	@UIColumn(label = "Double", initial = true)
	@UIRow(visible = true, editable = true, order = 3)
	private Double lineDouble;

	@UIHidden()
	@UIColumn(label = "Long", initial = true)
	@UIRow(visible = true, editable = true, order = 4)
	private Long lineLong;

	@UIHidden()
	@UIColumn(label = "Boolean", initial = true)
	@UIRow(visible = true, editable = true, order = 5)
	private Boolean lineBoolean;

	@UIHidden()
	@UIColumn(label = "Date", initial = true)
	@UIRow(visible = true, editable = true, order = 6)
	private LocalDate lineDate;

	@UIHidden()
	@UIColumn(label = "Date Time", initial = true)
	@UIRow(visible = true, editable = true, order = 7)
	private LocalDateTime lineDateTime;

	public void setIdEntityTen(Long idEntityTen) {
		this.entityTen = new EntityTen();
		this.entityTen.setId(idEntityTen);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public EntityTen getEntityTen() {
		return entityTen;
	}

	public void setEntityTen(EntityTen entityTen) {
		this.entityTen = entityTen;
	}

	public String getLineString() {
		return lineString;
	}

	public void setLineString(String lineString) {
		this.lineString = lineString;
	}

	public Integer getLineInteger() {
		return lineInteger;
	}

	public void setLineInteger(Integer lineInteger) {
		this.lineInteger = lineInteger;
	}

	public Double getLineDouble() {
		return lineDouble;
	}

	public void setLineDouble(Double lineDouble) {
		this.lineDouble = lineDouble;
	}

	public Long getLineLong() {
		return lineLong;
	}

	public void setLineLong(Long lineLong) {
		this.lineLong = lineLong;
	}

	public Boolean getLineBoolean() {
		return lineBoolean;
	}

	public void setLineBoolean(Boolean lineBoolean) {
		this.lineBoolean = lineBoolean;
	}

	public LocalDate getLineDate() {
		return lineDate;
	}

	public void setLineDate(LocalDate lineDate) {
		this.lineDate = lineDate;
	}

	public LocalDateTime getLineDateTime() {
		return lineDateTime;
	}

	public void setLineDateTime(LocalDateTime lineDateTime) {
		this.lineDateTime = lineDateTime;
	}
}
