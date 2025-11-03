package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.enginer.domain.system.usercase.annotation.field.UICheckbox;
import br.com.enginer.domain.system.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usercase.annotation.field.UIDate;
import br.com.enginer.domain.system.usercase.annotation.field.UIDecimal;
import br.com.enginer.domain.system.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.system.usercase.annotation.field.UIHidden;
import br.com.enginer.domain.system.usercase.annotation.field.UINumber;
import br.com.enginer.domain.system.usercase.annotation.field.UIRow;
import br.com.enginer.domain.system.usercase.annotation.field.UIText;
import br.com.enginer.domain.system.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.filter.UIButtonFilterClear;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.filter.UIButtonFilterTabNew;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.form.UIButtonFormBack;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.form.UIButtonFormClear;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.form.UIButtonFormDelete;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.form.UIButtonFormEdit;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.form.UIButtonFormSave;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorAdd;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorBack;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.tab.UIButtonTabBack;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.tab.UIButtonTabFinish;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usercase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usercase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usercase.constants.Constants;
import br.com.enginer.domain.system.usercase.enums.TypeButtonState;
import br.com.enginer.domain.system.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.system.usercase.enums.TypeFormat;
import br.com.enginer.domain.system.usercase.enums.TypeOperator;
import br.com.enginer.domain.system.usercase.enums.TypeTemplate;
import br.com.enginer.domain.system.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
@UITitle("Entity Rows")
@UIButtonAction(
	includes = {
		// FILTER
		UIButtonFilterClear.class,
		UIButtonFilterTabNew.class, 
		UIButtonFilterFormNew.class, 
		UIButtonFilterSearch.class, 
		// FORM
		UIButtonFormClear.class, 
		UIButtonFormBack.class, 
		UIButtonFormDelete.class,
		UIButtonFormEdit.class,
		UIButtonFormSave.class,
		// ROW
		//UIButtonRowClear.class, 
		//UIButtonRowBack.class,
		//UIButtonRowAdd.class,
		// TAB
		UIButtonTabBack.class, 
		UIButtonTabFinish.class
	}
)
@UIPaginator(config = @UIConfig(expandable = true, editableAll = true, multiSelectable = false), 
actions = @UIButtonAction(
	includes = {
		UIButtonPaginatorView.class, 
		UIButtonPaginatorEdit.class, 
		UIButtonPaginatorDelete.class,			
		UIButtonPaginatorBack.class,
		UIButtonPaginatorAdd.class		
	},
	value = { 
		@UIButton(
			label = Constants.LABEL_SAVE, 
			icon = "save",
			needsValidation = true,
			state = TypeButtonState.BTN_STATE_PRIMARY, 
			template = TypeTemplate.ROW, 
			action = @UIAction(
				method = @UIActionMethod(serverMethod = "rowSalvar")
			)
		) 
	}
))
public class EntityRow extends DomainAbstract<Long> {

	@UIHidden()
	private Long id;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIFilter(label = "Ten", field = "name", readonly = false)
	@UIColumn(label = "Ten", template = { TypeTemplate.FILTER }, fields = { "name", "dateCreate", "dateUpdate" }, initial = true)
	@UIRow(visible = false, fields = { "name" })
	private EntityTen entityTen;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIText(label = "String", template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.MODAL })
	@UIColumn(label = "String", initial = false)
	@UIRow(visible = true, editable = true, order = 1)
	private String lineString;

	@UIHidden(template = { TypeTemplate.ROW })
	@UINumber(label = "Integer", min = 1, max = 60, template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIColumn(label = "Integer", initial = true,
	conditional = @UIConditional({
		@UIConditionalOn(field = "lineDouble", operator = TypeOperator.BETWEEN, matchs = { "1","500" }, value = "badge badge-info"),
		@UIConditionalOn(field = "lineDate", operator = TypeOperator.DATE_BEFORE, matchs = { "lineDateTime" }, value = "badge badge-warning"),
	}))		
	@UIRow(visible = true, editable = true, order = 2)
	private Integer lineInteger;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIColumn(label = "Double", initial = true)
	@UIDecimal(label = "Double", typeFormat = TypeFormat.decimal, precision = 2, template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIRow(visible = true, editable = true, order = 3)
	private Double lineDouble;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIColumn(label = "Long", initial = true)
	@UIRow(visible = true, editable = true, order = 4)
	private Long lineLong;

	@UIHidden(template = { TypeTemplate.ROW })
	@UICheckbox(label = "<b>Boolean</b>", enableSwitch = false, template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.MODAL })
	@UIColumn(label = "Boolean", initial = false)
	@UIRow(visible = true, editable = true, order = 5)
	private Boolean lineBoolean;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIDate(label = "Date")
	@UIColumn(label = "Date", initial = false)
	@UIRow(visible = true, editable = true, order = 6)
	private LocalDate lineDate;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIDate(label = "Date Time", format = TypeDateFormat.DATE_TIME_FORMAT, showtime = true)
	@UIColumn(label = "Date Time", initial = false)
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
