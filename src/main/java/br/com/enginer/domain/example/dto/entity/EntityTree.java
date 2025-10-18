package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIDate;
import br.com.enginer.domain.ui.usercase.annotation.field.UIDecimal;
import br.com.enginer.domain.ui.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.ui.usercase.annotation.field.UIId;
import br.com.enginer.domain.ui.usercase.annotation.field.UIJoin;
import br.com.enginer.domain.ui.usercase.annotation.field.UIRow;
import br.com.enginer.domain.ui.usercase.annotation.field.UISelect;
import br.com.enginer.domain.ui.usercase.annotation.field.UIText;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.autocomplete.UIAutoCompleteSuggestion;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.ui.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonBack;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonBefore;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonDelete;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonEdit;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonNew;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonNext;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonSave;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonSearch;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonView;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.helper.ComboHelper;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
@UITitle("Terceiro")
@UIButtonAction(includes = { 
	UIButtonBefore.class, 
	UIButtonNext.class, 
	UIButtonNew.class, 
	UIButtonBack.class,
	UIButtonDelete.class, 
	UIButtonSearch.class, 
	UIButtonSave.class 
}, 
value = {
	@UIButton(
	    label = Constants.LABEL_BACK,
	    icon = "undo",
	    needsValidation = false,
	    state = TypeButtonState.BTN_STATE_DEFAULT,
	    template = { TypeTemplate.DISABLED },
	    action = @UIAction(
	        method = @UIActionMethod(clientMethod = "onBack")
	    )
	),
	@UIButton(
	    label = Constants.LABEL_CLEAR,
	    icon = "bin_alt",
	    needsValidation = false,
	    template = { TypeTemplate.DISABLED },
	    state = TypeButtonState.BTN_STATE_DEFAULT,
	    action = @UIAction(
	        method = @UIActionMethod(clientMethod = Constants.METHOD_CLEAR_FORM)
	    )
	)		
}
)
@UIPaginator(config = @UIConfig(expandable = true, multiSelectable = false), actions = @UIButtonAction(includes = { UIButtonView.class, UIButtonEdit.class, UIButtonDelete.class }))
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
	private Double amount;

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

	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}
}
