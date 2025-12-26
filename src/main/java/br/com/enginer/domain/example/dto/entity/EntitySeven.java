package br.com.enginer.domain.example.dto.entity;

import br.com.enginer.domain.system.usecase.core.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIJoin;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIText;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.core.annotation.instance.UITitle;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionResponseError;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionTriggerMethod;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.UIButtonBack;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.UIButtonClear;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterTabNew;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormDelete;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormEdit;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormSave;
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
import br.com.enginer.domain.system.usecase.core.enums.TypeOperator;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;

@UITitle("Setimo")
@UIButtonAction(
includes = {
	UIButtonClear.class, 
	UIButtonBack.class, 
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
	UIButtonRowAdd.class
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
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entitySeven" }),
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
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entitySeven" }),
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
	config = @UIConfig(expandable = true, multiSelectable = true),
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
			label = "Add EntitySix in Seven", 
			needsValidation = false, 
			dropdown = true, 
			template = TypeTemplate.PAGINATOR, 
			action = @UIAction(
				redirect = @UIActionRedirect(
					value = Constants.PATH, 
					ui = "row", 
					domain = "entitySeven", 
					param = "{ disable=true, field=id.entitySix, value=$object }")
				)
		)
	}
))
public class EntitySeven extends DomainAbstract<EntitySevenId> {
	
	@UIJoin(template = { TypeTemplate.MODAL, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.FILTER })
	@UIColumn(label = "Id", fields = { "idEntitySeven", "entitySix" }, initial = false)
	@UIRow(visible = true, fields = { "entitySix" })
	private EntitySevenId id;

	@UIPosition(x = 2, y = 3)
	@UIFieldValidation(required = true)
	@UIText(label = "Dado")
	@UIColumn(label = "EntitySeven Dado", initial = true)
	@UIRow(visible = true, editable = true)
	private String dado;

	public EntitySeven() {
		super();
	}

	public EntitySeven(EntitySevenId id) {
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
	public EntitySevenId getId() {
		return this.id;
	}

	public void setId(EntitySevenId id) {
		this.id = id;
	}

	public String getDado() {
		return this.dado;
	}

	public void setDado(String dado) {
		this.dado = dado;
	}

	@Override
	public String toString() {
		return "EntitySeven{" + "id=" + (id != null ? id.toString() : "null") + ", " + "dado="
				+ (dado != null ? dado.toString() : "null") + '}';
	}
}
