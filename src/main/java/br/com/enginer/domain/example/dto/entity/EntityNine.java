package br.com.enginer.domain.example.dto.entity;

import br.com.enginer.domain.system.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usercase.annotation.field.UIJoin;
import br.com.enginer.domain.system.usercase.annotation.field.UIRow;
import br.com.enginer.domain.system.usercase.annotation.field.UIText;
import br.com.enginer.domain.system.usercase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionRedirect;
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
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.tab.UIButtonTabBack;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.tab.UIButtonTabFinish;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usercase.constants.Constants;
import br.com.enginer.domain.system.usercase.enums.TypeButtonState;
import br.com.enginer.domain.system.usercase.enums.TypeTemplate;
import br.com.enginer.domain.system.usercase.schema.instance.DomainAbstract;

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
	// TAB
	UIButtonTabBack.class, 
	UIButtonTabFinish.class
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
	action = @UIAction( 
		method = @UIActionMethod(clientMethod = "triggerMethod", 
		trigger = @UIActionTriggerMethod(clientMethod = Constants.METHOD_CLEAR_FORM)) 
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

	@Override
	public EntityNineId getId() {
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
