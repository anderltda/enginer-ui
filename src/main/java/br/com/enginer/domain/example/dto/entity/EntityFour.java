package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import br.com.enginer.domain.system.usecase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.annotation.field.UIDate;
import br.com.enginer.domain.system.usecase.annotation.field.UIFilter;
import br.com.enginer.domain.system.usecase.annotation.field.UIId;
import br.com.enginer.domain.system.usecase.annotation.field.UIJoin;
import br.com.enginer.domain.system.usecase.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.annotation.field.UIText;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.validation.UIAsync;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.validation.UIPattern;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.validation.UISync;
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
import br.com.enginer.domain.system.usecase.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;

/**
 * 
 */
@UITitle("Quarto")
@UIButtonAction(
includes = { 
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
			label = "Go entityFive", 
			template = TypeTemplate.FORM,
			icon = "google_plus", 
			needsValidation = true,
			confirm = false, 
			action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH, ui = "form", domain = "entityFive", param = "{ disable=false, field=entityFive, value=$object }"))
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
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityFour" }),
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
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityFour" }),
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
	)
)
public class EntityFour extends DomainAbstract<UUID> {

	@UIId(label = "Id")
	@UIColumn(label = "EntityFour Id", initial = false)
	private UUID id;

	@UIPosition(x = 1, y = 1)
	@UIText(label = "Fruit Name", min = 4, max = 100)
	@UIFieldValidation(required = true, pattern = @UIPattern(pattern = "^[^wW]*$", patternError = "*** PATTERN ***, nao pode adiciona a letra 'W'"), async = @UIAsync(method = "metodoJavaDominioEntityOne", asyncError = "Validação direto no field 'ASYNC'"), sync = @UISync(syncFunc = {
			"dogMel", "dogMagrela" }, syncError = { "message1",
					"Validação direto no field 'SYNC' - O campo está randomico, acabou caindo no erro." }))
	@UIColumn(label = "EntityFour Nome da Fruta", initial = false)
	@UIRow(visible = true)
	private String fruit;

	@UIPosition(x = 1, y = 2)
	@UIFilter(label = "Entity Status", field = "name")
	private EntityStatus entityStatus;

	@UIPosition(x = 2, y = 2)
	@UIColumn(label = "EntityFour Atributo", initial = false)
	@UIRow(visible = true)
	private Integer attribute;

	@UIPosition(x = 3, y = 2)
	@UIFieldValidation(required = true)
	@UIDate(label = "Date Time Inclusion", format = TypeDateFormat.DATE_TIME_FORMAT, showtime = true)
	@UIColumn(label = "EntityFour Data e hora da inclusao", initial = false)
	@UIRow(visible = true)
	private LocalDateTime inclusionDateTime;

	@UIFilter(label = "Entity Five", field = "reference")
	@UIJoin(icon = "cloud")
	@UIRow(visible = true, fields = { "reference" })
	@UIColumn(label = "Entity Five", fields = { "reference", "factor", "entityStatus" }, initial = false)
	private EntityFive entityFive;
	
	public EntityFour() {
		super();
	}

	public EntityFour(UUID id) {
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

	public EntityFive getEntityFive() {
		return entityFive;
	}

	public void setEntityFive(EntityFive entityFive) {
		this.entityFive = entityFive;
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

	public void setIdEntityFive(UUID idEntityFive) {
		this.entityFive = new EntityFive();
		this.entityFive.setId(idEntityFive);
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
		EntityFour other = (EntityFour) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "EntityFour [id=" + id + ", fruit=" + fruit + ", entityStatus=" + entityStatus + ", attribute="
				+ attribute + ", inclusionDateTime=" + inclusionDateTime + ", entityFive=" + entityFive + "]";
	}
}
