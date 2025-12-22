package br.com.enginer.domain.example.dto.entity;

import br.com.enginer.domain.system.usecase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.annotation.field.UIJoin;
import br.com.enginer.domain.system.usecase.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.annotation.field.UIText;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.UIPosition;
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
@UITitle("Nono")
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
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityNine" }),
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
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityNine" }),
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
config = @UIConfig(expandable = true), 
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
    label = "Clean",
    icon = "trash",
    state = TypeButtonState.BTN_STATE_PRIMARY,
    template = TypeTemplate.ROW,
    needsValidation = false,
	action = @UIAction( 
		method = @UIActionMethod(clientMethod = "triggerMethod", 
		trigger = @UIActionTriggerMethod(clientMethod = Constants.METHOD_CLEAR_FORM)) 
	)
),
@UIButton(
		label = "Add Nine code", 
		needsValidation = false, 
		dropdown = true, 
		template = TypeTemplate.PAGINATOR, 
		action = @UIAction(
			redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityNine", param = "{ disable=true, field=code, value=$entityNine.code  }")
		)
),
@UIButton(
		label = "Add Nine", 
		needsValidation = false, 
		dropdown = true, 
		template = TypeTemplate.PAGINATOR, 
		action = @UIAction(
			redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityNine", param = "{ disable=true, field=id, value=$object }")
		)
),
@UIButton(
	label = "Add EntitySeven in Nine", 
	needsValidation = false, 
	dropdown = true, 
	template = TypeTemplate.PAGINATOR, 
	action = @UIAction(
		redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityNine", param = "{ disable=true, field=id.entitySeven, value=$object }")
	)
),
@UIButton(
	label = "Add EntityEight in Nine", 
	needsValidation = false, 
	dropdown = true, 
	template = TypeTemplate.PAGINATOR, 
	action = @UIAction(
		redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityNine", param = "{ disable=true, field=id.entityEight, value=$object }")
	)
)}))
public class EntityNine extends DomainAbstract<EntityNineId> {

	@UIJoin(template = { TypeTemplate.MODAL, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.FILTER })
	@UIRow(visible = true, fields = { "entitySeven", "entityEight" })
	@UIColumn(label = "Id", fields = { "entitySeven", "entityEight" }, initial = false)
	private EntityNineId id;

	@UIPosition(x = 1, y = 1)
	@UIText(label = "Key")
	@UIColumn(label = "EntityNine Key", initial = true)
	@UIRow(visible = true)
	private String keyNine;

	@UIPosition(x = 2, y = 1)
	@UIText(label = "Code")
	@UIColumn(label = "EntityNine Code", initial = false)
	@UIRow(visible = true)
	private String code;

	@UIPosition(x = 3, y = 1)
	@UIText(label = "Variable")
	@UIColumn(label = "EntityNine Variable", initial = true)
	@UIRow(visible = true)
	private String variable;
	
	public EntityNine() {
		super();
	}
	
	public EntityNine(EntityNineId id) {
		super();
		this.id = id;
	}

	@Override
	public EntityNineId getId() {
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

	public void setId(EntityNineId id) {
		this.id = id;
	}

	public String getKeyNine() {
		return this.keyNine;
	}

	public void setKeyNine(String keyNine) {
		this.keyNine = keyNine;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVariable() {
		return this.variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String toString() {
		return "EntityNine{" + "id=" + (id != null ? id.toString() : "null") + ", " + "keyNine="
				+ (keyNine != null ? keyNine.toString() : "null") + ", " + "code="
				+ (code != null ? code.toString() : "null") + ", " + "variable="
				+ (variable != null ? variable.toString() : "null") + '}';
	}
}
