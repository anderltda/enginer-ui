package br.com.enginer.domain.example.dto.entity;

import java.util.Objects;
import java.util.UUID;

import br.com.enginer.domain.system.usecase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.annotation.field.UIFilter;
import br.com.enginer.domain.system.usecase.annotation.field.UIId;
import br.com.enginer.domain.system.usecase.annotation.field.UINumber;
import br.com.enginer.domain.system.usecase.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.annotation.field.UIText;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.UIPosition;
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
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter.UIButtonFilterClear;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter.UIButtonFilterTabNew;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormBack;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormClear;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormDelete;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormEdit;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormSave;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorSave;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.row.UIButtonRowAdd;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.row.UIButtonRowBack;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.row.UIButtonRowClear;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.row.UIButtonRowDelete;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.row.UIButtonRowEdit;
import br.com.enginer.domain.system.usecase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usecase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usecase.constants.Constants;
import br.com.enginer.domain.system.usecase.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.enums.TypeOperator;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;

/**
 * 
 */
@UITitle("Quinto")
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
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityFive" }),
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
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityFive" }),
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
    actions = @UIButtonAction(includes = { 
			UIButtonPaginatorView.class, 
			UIButtonPaginatorEdit.class, 
			UIButtonPaginatorDelete.class,
			UIButtonPaginatorSave.class,
			UIButtonRowEdit.class,
			UIButtonRowDelete.class
    },
	value = {
			@UIButton(
				label = "Add EntityStatus in Five", 
			needsValidation = false, 
			dropdown = true, 
			template = TypeTemplate.PAGINATOR, 
			action = @UIAction(
				redirect = @UIActionRedirect(
					value = Constants.PATH, 
					ui = "row", 
					domain = "entityFive", 
					param = "{ disable=true, field=entityStatus, value=$object }")
				)
			)
	}
))
public class EntityFive extends DomainAbstract<UUID> {

	@UIId(label = "Id")
	@UIColumn(label = "EntityFive Id", initial = false)
	private UUID id;
	
	@UIPosition(x = 1, y = 2)
	@UIFieldValidation(required = true)
	@UIText(label = "Reference", min = 1, max = 100, template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.MODAL })
	@UIColumn(label = "EntityFive Referencia", initial = true)
	@UIRow(visible = true, editable = true)
	private String reference;
	
	@UIPosition(x = 2, y = 2)
	@UIFieldValidation(required = true)
	@UINumber(label = "Factor", min = 1, max = 60, template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.MODAL })
	@UIColumn(label = "EntityFive Fator", initial = true)
	@UIRow(visible = true, editable = true)
	private Integer factor;

	@UIPosition(x = 1, y = 3)
	@UIFieldValidation(required = true)
	@UIFilter(label = "Entity Status", field = "name", select = true, filter = { "status=0", "status_op=ge" }, template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.MODAL })
	@UIRow(visible = true, fields = { "name", "status", "ativo" })
	@UIColumn(label = "Entity Status", fields = { "name", "status" }, initial = false)
	private EntityStatus entityStatus;
	
	public EntityFive() {
		super();
	}

	public EntityFive(UUID id) {
		super();
		this.id = id;
	}

	@Override
	public UUID getId() {
		try {
			Boolean existId = (Boolean) ReflectionUtils.isIdNull(this);
			if (!existId) {
				this.id = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.id;
	}
	
	@Override
	public void setId(UUID id) {
		this.id = id;
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
		EntityFive other = (EntityFive) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "EntityFive [id=" + id + ", reference=" + reference + ", factor=" + factor + ", entityStatus="
				+ entityStatus + "]";
	}
	
	
}
