package br.com.enginer.domain.example.dto.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.enginer.domain.system.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usercase.annotation.field.UIDate;
import br.com.enginer.domain.system.usercase.annotation.field.UIDecimal;
import br.com.enginer.domain.system.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.system.usercase.annotation.field.UIId;
import br.com.enginer.domain.system.usercase.annotation.field.UIJoin;
import br.com.enginer.domain.system.usercase.annotation.field.UIRow;
import br.com.enginer.domain.system.usercase.annotation.field.UISelect;
import br.com.enginer.domain.system.usercase.annotation.field.UIText;
import br.com.enginer.domain.system.usercase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usercase.annotation.field.behavior.autocomplete.UIAutoCompleteSuggestion;
import br.com.enginer.domain.system.usercase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionResponseError;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionTriggerMethod;
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
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorSave;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.row.UIButtonRowAdd;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.row.UIButtonRowBack;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.row.UIButtonRowClear;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.row.UIButtonRowDelete;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.row.UIButtonRowEdit;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usercase.constants.Constants;
import br.com.enginer.domain.system.usercase.enums.TypeButtonState;
import br.com.enginer.domain.system.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.system.usercase.enums.TypeTemplate;
import br.com.enginer.domain.system.usercase.helper.ComboHelper;
import br.com.enginer.domain.system.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
@UITitle("Terceiro")
@UIButtonAction(includes = { 
	// FILTER
	UIButtonFilterClear.class,
	UIButtonFilterTabNew.class, 
	UIButtonFilterFormNew.class, 
	UIButtonFilterSearch.class, 
	// FORM
	UIButtonFormBack.class, 
	UIButtonFormClear.class, 
	UIButtonFormDelete.class,
	UIButtonFormEdit.class, 
	UIButtonFormSave.class,
	// ROW
	UIButtonRowClear.class, 
	UIButtonRowBack.class,
	UIButtonRowAdd.class,
}, 
value = {
	@UIButton(
	    label = Constants.LABEL_BACK,
	    icon = "undo",
	    needsValidation = false,
	    state = TypeButtonState.BTN_STATE_DEFAULT,
	    template = TypeTemplate.TAB,
	    notDomain = { "entityAll" },
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
	    notDomain = { "entityTree" },
	    action = @UIAction(
	        method = @UIActionMethod(clientMethod = "onPrevious")
	    )
	),
	@UIButton(
	    label = Constants.LABEL_NEXT,
	    icon = "chevron_right",
	    state = TypeButtonState.BTN_STATE_PRIMARY,
	    template = { TypeTemplate.TAB, TypeTemplate.MODAL },
	    notDomain = { "entityTree" },
	    action = @UIAction(
	        method = @UIActionMethod(clientMethod = "onNext")
	    )
	),	
	@UIButton(
		label = Constants.LABEL_FINISH,
	    icon = "save",
	    state = TypeButtonState.BTN_STATE_PRIMARY,
	    template = TypeTemplate.TAB,
	    notDomain = { "entityAll" },
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

	public void setIdEntityStatus(Long idEntityStatus) {
		this.entityStatus = new EntityStatus();
		this.entityStatus.setId(idEntityStatus);
	}

	public void setIdEntityFour(UUID idEntityFour) {
		this.entityFour = new EntityFour();
		this.entityFour.setId(idEntityFour);
	}

	@Override
	public UUID getId() {
		return id;
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
}
