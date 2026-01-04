package br.com.enginer.domain.example.dto.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import br.com.enginer.domain.system.usecase.core.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIFilter;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIHidden;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIId;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIText;
import br.com.enginer.domain.system.usecase.core.annotation.field.UITextArea;
import br.com.enginer.domain.system.usecase.core.annotation.field.UITime;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.core.annotation.instance.UIHeader;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionResponseError;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionTriggerMethod;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.UIButtonClear;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterTabNew;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormEdit;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorSave;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.row.UIButtonRowAdd;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.row.UIButtonRowClear;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.row.UIButtonRowDelete;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.row.UIButtonRowEdit;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usecase.core.constants.Constants;
import br.com.enginer.domain.system.usecase.core.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.core.enums.TypeDateFormat;
import br.com.enginer.domain.system.usecase.core.enums.TypeOperator;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;

/**
 * 
 */
@UIHeader(title = "Decimo")
@UIButtonAction(includes = { 
	UIButtonClear.class,
	// FILTER
	UIButtonFilterTabNew.class, 
	UIButtonFilterFormNew.class, 
	UIButtonFilterSearch.class, 
	// FORM
	UIButtonFormEdit.class,
	// ROW
	UIButtonRowClear.class, 
	UIButtonRowAdd.class
},
value = { 
	@UIButton(
		label = Constants.LABEL_BACK,
		icon = "undo",
		needsValidation = false,
		state = TypeButtonState.BTN_STATE_DEFAULT,
		template = { TypeTemplate.FORM, TypeTemplate.TAB },
		conditional = @UIConditional({
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityAll" }),
		}),
		action = @UIAction(
			method = @UIActionMethod(clientMethod = "onBack")
		)
	),
	@UIButton(
		label = Constants.LABEL_DELETE,
		icon = "close_ circle",
		state = TypeButtonState.BTN_STATE_DANGER,
		confirm = true,
		needsValidation = false,
		highlight = true,
		dropdown = true,
		template = { TypeTemplate.FORM },
		conditional = @UIConditional({
			@UIConditionalOn(field = "disabled", operator = TypeOperator.EQUALS, matchs = { "false" }),
			@UIConditionalOn(field = "id", operator = TypeOperator.IS_NOT_NULL),
		}),	
		action = @UIAction(
		method = @UIActionMethod(serverMethod = "excluir"),
		response = @UIActionResponse(
			template = { TypeTemplate.FORM },
			error = @UIActionResponseError(method = @UIActionMethod(clientMethod = "onAlertTestError")), 
			success = @UIActionResponseSuccess(redirect = @UIActionRedirect(value = Constants.PATH, ui = "filter")))
		)
	),	
	@UIButton(
	    label = Constants.LABEL_SAVE,
	    icon = "save",
	    state = TypeButtonState.BTN_STATE_PRIMARY,
	    template = TypeTemplate.FORM,
		conditional = @UIConditional({
			@UIConditionalOn(field = "disabled", operator = TypeOperator.EQUALS, matchs = { "false" }),
		}),		    
	    action = @UIAction(
	        method = @UIActionMethod(serverMethod = "salvar"),
	        response = @UIActionResponse(
	        	template = { TypeTemplate.FORM },
	    		error = @UIActionResponseError(method = @UIActionMethod(clientMethod = "onAlertTestError")), 
	    		success = @UIActionResponseSuccess(redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityEleven", param = "{ disable=true, field=entityTen, value=$object }"))
	        )
	    )
	),
	@UIButton(
	    label = Constants.LABEL_BEFORE,
	    icon = "chevron_left",
	    state = TypeButtonState.BTN_STATE_PRIMARY,
	    needsValidation = false,
	    template = { TypeTemplate.TAB },
		conditional = @UIConditional({
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityTen" }),
		}),
	    action = @UIAction(
	        method = @UIActionMethod(clientMethod = "onPrevious")
	    )
	),
	@UIButton(
	    label = Constants.LABEL_NEXT,
	    icon = "chevron_right",
	    state = TypeButtonState.BTN_STATE_PRIMARY,
	    template = { TypeTemplate.TAB, TypeTemplate.MODAL },
		conditional = @UIConditional({
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityTen" }),
		}),	    
	    action = @UIAction(
	        method = @UIActionMethod(clientMethod = "onNext")
	    )
	),	
	@UIButton(
		label = Constants.LABEL_FINISH,
	    icon = "save",
	    state = TypeButtonState.BTN_STATE_PRIMARY,
	    template = TypeTemplate.TAB,
		conditional = @UIConditional({
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityAll" }),
		}),
	    action = @UIAction(
			method = @UIActionMethod(
				clientMethod = "onFinish", 
				trigger = @UIActionTriggerMethod(serverMethod = "salvar")
			),
	        response = @UIActionResponse(
	        	template = TypeTemplate.TAB,
	    		error = @UIActionResponseError(method = @UIActionMethod(clientMethod = "onAlertTestError")), 
	    		success = @UIActionResponseSuccess(redirect = @UIActionRedirect(ui = "tab", value = Constants.PATH_FIND_BY_ID))
	        )
	    )
	)	
})
@UIPaginator(
	config = @UIConfig(expandable = false, multiSelectable = false), 
	actions = @UIButtonAction(
	includes = { 
		UIButtonPaginatorView.class, 
		UIButtonPaginatorEdit.class, 
		UIButtonPaginatorDelete.class,
		UIButtonPaginatorSave.class,
		UIButtonRowEdit.class,
		UIButtonRowDelete.class 
	},
	value = {
		@UIButton(
			label = "Add 10 --> 11", 
			template = TypeTemplate.PAGINATOR, 
			highlight = false,
			dropdown = true,
			action = @UIAction(
				redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityEleven", param = "{ disable=true, field=entityTen, value=$object }")
			)
		),
		@UIButton(
			label = "Add 10 --> Row", 
			template = TypeTemplate.PAGINATOR, 
			highlight = false,
			dropdown = true,
			action = @UIAction(
				redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityRow", param = "{ disable=true, field=entityTen, value=$object }")
			)
		)			
	}
))
public class EntityTen extends DomainAbstract<Long> {

	@UIId(label = "Id")
	@UIColumn(label = "Id", initial = false)
	private Long id;

	@UIPosition(x = 1, y = 1)
	@UIFieldValidation(required = true, template = { TypeTemplate.FORM, TypeTemplate.TAB })
	@UIText(label = "Nome", min = 2, max = 100)
	@UIColumn(label = "Nome", initial = true, style = "label label-success",
	conditional = @UIConditional({
		@UIConditionalOn(field = "entityTen.name", operator = TypeOperator.CONTAINS, matchs = { "Two" }, value = "label label-success"),
		@UIConditionalOn(field = "entityTen.name", operator = TypeOperator.EQUALS, matchs = { "Agrupamento One" }, value = "badge badge-important"),
		@UIConditionalOn(field = "name", operator = TypeOperator.CONTAINS, matchs = { "Two" }, value = "label label-info"),
		@UIConditionalOn(field = "name", operator = TypeOperator.EQUALS, matchs = { "Agrupamento One" }, value = "label label-important"),
	}))		
	@UIRow(visible = true)
	private String name;

	@UIHidden
	@UIColumn(label = "Quantidade Total", initial = true)
	@UIRow(visible = true)
	private Integer totalAmount;

	@UIHidden
	@UIColumn(label = "Valor Total", initial = true)
	@UIRow(visible = true)
	private BigDecimal totalValue;
	
	@UITime(label = "Tempo Fixo", format = TypeDateFormat.TIME_HHMM_FORMAT)
	private LocalTime timeProcess;

	@UIPosition(x = 2, y = 1)
	@UIFilter(label = "Status", field = "name", select = true, filter = { "status=0", "status_op=ge" }, template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.MODAL })
	@UIRow(visible = true, fields = { "name", "status", "ativo" })
	@UIColumn(label = "", fields = { "name", "status", "startDateTime" }, initial = false)
	private EntityStatus entityStatus;
	
	@UIPosition(x = 1, y = 2)
	@UIFieldValidation(required = false, template = { TypeTemplate.FORM, TypeTemplate.TAB })
	@UITextArea(label = "Descrição", editor = false)
	private String description;
	
	@UIHidden
	@UIColumn(label = "Data de Criacao", initial = false)
	private LocalDateTime dateCreate;

	@UIHidden
	@UIColumn(label = "Data de Atualizacao", initial = false)
	private LocalDateTime dateUpdate;

	public EntityTen() {
		super();
	}

	public EntityTen(Long id) {
		super();
		this.id = id;
	}

	@Override
	public Boolean isIdNull() {
		Boolean isIdNull = false;
		try {
			isIdNull = (Boolean) ReflectionUtils.isIdNull(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return isIdNull;
	}	
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public LocalTime getTimeProcess() {
		return timeProcess;
	}

	public void setTimeProcess(LocalTime timeProcess) {
		this.timeProcess = timeProcess;
	}

	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(LocalDateTime dateCreate) {
		this.dateCreate = dateCreate;
	}

	public LocalDateTime getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(LocalDateTime dateUpdate) {
		this.dateUpdate = dateUpdate;
	}
	
	public void setIdEntityStatus(Long idEntityStatus) {
		this.entityStatus = new EntityStatus();
		this.entityStatus.setId(idEntityStatus);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityTen other = (EntityTen) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "EntityTen [id=" + id + ", name=" + name + ", totalAmount=" + totalAmount + ", totalValue=" + totalValue
				+ ", timeProcess=" + timeProcess + ", entityStatus=" + entityStatus + ", description=" + description
				+ ", dateCreate=" + dateCreate + ", dateUpdate=" + dateUpdate + "]";
	}
}
