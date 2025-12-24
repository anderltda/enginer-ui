package br.com.enginer.domain.example.dto.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import br.com.enginer.domain.system.usecase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.annotation.field.UIDate;
import br.com.enginer.domain.system.usecase.annotation.field.UIDecimal;
import br.com.enginer.domain.system.usecase.annotation.field.UIFilter;
import br.com.enginer.domain.system.usecase.annotation.field.UIId;
import br.com.enginer.domain.system.usecase.annotation.field.UIJoin;
import br.com.enginer.domain.system.usecase.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.annotation.field.UISelect;
import br.com.enginer.domain.system.usecase.annotation.field.UIText;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.autocomplete.UIAutoCompleteSuggestion;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.annotation.instance.UITitle;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionResponseError;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionTriggerMethod;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.UIButtonBack;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.UIButtonClear;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter.UIButtonFilterTabNew;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormDelete;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormEdit;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormSave;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorSave;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.row.UIButtonRowAdd;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.row.UIButtonRowClear;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.row.UIButtonRowDelete;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.row.UIButtonRowEdit;
import br.com.enginer.domain.system.usecase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usecase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usecase.constants.Constants;
import br.com.enginer.domain.system.usecase.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.enums.TypeDateFormat;
import br.com.enginer.domain.system.usecase.enums.TypeOperator;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.helper.ComboHelper;
import br.com.enginer.domain.system.usecase.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;

/**
 * 
 */
@UITitle("Terceiro")
@UIButtonAction(includes = {
	UIButtonBack.class,
	UIButtonClear.class, 
	// FILTER
	UIButtonFilterTabNew.class, 
	UIButtonFilterFormNew.class, 
	UIButtonFilterSearch.class, 
	// FORM
	UIButtonFormDelete.class,
	UIButtonFormEdit.class, 
	UIButtonFormSave.class,
	// ROW
	UIButtonRowClear.class, 
	UIButtonRowAdd.class,
}, 
value = {
	@UIButton(
		label = "Go entityFour", 
		template = TypeTemplate.FORM,
		icon = "google_plus", 
		needsValidation = true,
		confirm = false, 
		action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH, ui = "form", domain = "entityFour", param = "{ disable=false, field=entityFour, value=$object }"))
	),		
	@UIButton(
	    label = Constants.LABEL_BACK,
	    icon = "undo",
	    needsValidation = false,
	    state = TypeButtonState.BTN_STATE_DEFAULT,
	    template = TypeTemplate.TAB,
		conditional = @UIConditional({
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityAll" }),
		}),	    
	    action = @UIAction(
	        method = @UIActionMethod(clientMethod = "onBack")
	    )
	),
	@UIButton(
	    label = Constants.LABEL_BEFORE,
	    icon = "chevron_left",
	    state = TypeButtonState.BTN_STATE_PRIMARY,
	    needsValidation = false,
	    template = { TypeTemplate.TAB },
		conditional = @UIConditional({
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityTree" }),
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
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityTree" }),
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
@UIPaginator(config = @UIConfig(expandable = true), 
actions = @UIButtonAction(
	includes = { 
		UIButtonPaginatorView.class, 
		UIButtonPaginatorEdit.class, 
		UIButtonPaginatorDelete.class,
		UIButtonPaginatorSave.class,
		UIButtonRowEdit.class,
		UIButtonRowDelete.class
	}
))
public class EntityTree extends DomainAbstract<UUID> {

	@UIId(label = "Id")
	@UIColumn(label = "EntityTree Id", initial = false)
	private UUID id;

	@UIPosition(x = 1, y = 1)
	@UIFieldValidation(required = true)
	@UIText(label = "Animal Name", min = 4, max = 50)
	@UIAutoCompleteSuggestion(suggestions = { "Cavalo", "Cachorro", "Gato", "Elefante", "Macaco", "Camelo" })
	@UIColumn(label = "EntityTree Nome do Animal", initial = true)
	@UIRow(visible = true)
	private String animal;

	@UIPosition(x = 2, y = 1)
	@UIFilter(label = "Entity Status", field = "name", select = false)
	private EntityStatus entityStatus;

	@UIPosition(x = 1, y = 2)
	@UISelect(label = "Inidicador", provider = ComboHelper.class, method = "indicadores")
	@UIColumn(label = "EntityTree Indicador", initial = false)
	@UIRow(visible = true)
	private Integer indicator;

	@UIPosition(x = 2, y = 2)
	@UIFieldValidation(required = true)
	@UIColumn(label = "EntityTree Montante", initial = false)
	@UIDecimal(label = "Montante")
	@UIRow(visible = true)
	private BigDecimal amount;

	@UIPosition(x = 4, y = 2)
	@UIFieldValidation(required = true)
	@UIDate(label = "Date local", format = TypeDateFormat.DATE_FORMAT, showtime = false)
	@UIColumn(label = "EntityTree Local da Data", initial = false)
	@UIRow(visible = true)
	private LocalDate localDate;

	@UIPosition(x = 3, y = 2)
	@UIFieldValidation(required = true)
	@UIDate(label = "Date local time", format = TypeDateFormat.DATE_TIME_FORMAT, showtime = true)
	@UIColumn(label = "EntityTree Data Local", initial = false)
	@UIRow(visible = true)
	private LocalDateTime localDateTime;

	@UIJoin(icon = "edit")
	@UIFilter(label = "Entity Four", field = "fruit", template = { TypeTemplate.FILTER, TypeTemplate.MODAL })
	@UIRow(visible = true, fields = { "fruit" })
	@UIColumn(label = "Entity Four", fields = { "fruit", "attribute", "inclusionDateTime", "entityFive" }, initial = false)
	private EntityFour entityFour;

	public EntityTree() {
		super();
	}

	public EntityTree(UUID id) {
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
	public UUID getId() {
		return this.id;
	}

	@Override
	public void setId(UUID id) {
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
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

	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}
	
	public void setIdEntityStatus(Long idEntityStatus) {
		this.entityStatus = new EntityStatus();
		this.entityStatus.setId(idEntityStatus);
	}

	public void setIdEntityFour(UUID idEntityFour) {
		this.entityFour = new EntityFour();
		this.entityFour.setId(idEntityFour);
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
		EntityTree other = (EntityTree) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "EntityTree [id=" + id + ", animal=" + animal + ", entityStatus=" + entityStatus + ", indicator="
				+ indicator + ", amount=" + amount + ", localDate=" + localDate + ", localDateTime=" + localDateTime
				+ ", entityFour=" + entityFour + "]";
	}
}
