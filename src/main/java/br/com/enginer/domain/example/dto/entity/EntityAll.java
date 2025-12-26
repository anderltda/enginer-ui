package br.com.enginer.domain.example.dto.entity;

import br.com.enginer.domain.system.usecase.core.annotation.field.UIFilter;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIId;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIJoin;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usecase.core.annotation.instance.UITitle;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionMethod;
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
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.tab.UIButtonTabNext;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usecase.core.constants.Constants;
import br.com.enginer.domain.system.usecase.core.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.core.enums.TypeLayoutTarget;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;

/**
 * 
 */
@UITitle("Todos")
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
	// TAB
	UIButtonTabNext.class
}, 
value = {
	@UIButton(
	    label = Constants.LABEL_BACK,
	    icon = "undo",
	    needsValidation = false,
	    template = TypeTemplate.TAB,
	    state = TypeButtonState.BTN_STATE_DEFAULT,
	    action = @UIAction(
	        method = @UIActionMethod(clientMethod = "onBack")
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
public class EntityAll extends DomainAbstract<Long> {

	@UIId(label = "Id")
	private Long id;
	
	@UIPosition(x = 1, y = 1)
	@UIFilter(label = "Entity Status", field = "name", readonly = false)
	@UIRow(visible = true, fields = { "name" })
	private EntityStatus entityStatus;
	
	@UIPosition(x = 2, y = 1)
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "cloud", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Two", field = "color", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.ROW })
	@UIRow(visible = true, fields = { "color" })
	private EntityTwo entityTwo;
	
	@UIPosition(x = 1, y = 2)
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "cloud_success", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Tree", field = "animal", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.ROW })
	@UIRow(visible = true, fields = { "animal" })
	private EntityTree entityTree;
	
	@UIPosition(x = 2, y = 2)
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "cloud_off", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Four", field = "fruit", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.ROW })
	@UIRow(visible = true, fields = { "fruit" })
	private EntityFour entityFour;
	
	@UIPosition(x = 1, y = 3)
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "database", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Five", field = "reference", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.ROW })
	@UIRow(visible = true, fields = { "reference" })
	private EntityFive entityFive;

	@UIPosition(x = 2, y = 3)
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "keyboard", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Six", field = "packageName", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.ROW })
	@UIRow(visible = true, fields = { "packageName" })
	private EntitySix entitySix;

	@UIPosition(x = 1, y = 4)
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "alert_warning", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Seven", field = "dado", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.ROW })
	@UIRow(visible = true, fields = { "dado" })
	private EntitySeven entitySeven;
	
	@UIPosition(x = 2, y = 4)
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "calendar", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Eight", field = "position", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.ROW })
	@UIRow(visible = true, fields = { "position" })
	private EntityEight entityEight;
	
	@UIPosition(x = 1, y = 5)
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "color_palette", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Nine", field = "keyNine", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.ROW })
	@UIRow(visible = true, fields = { "keyNine" })
	private EntityNine entityNine;

	@UIPosition(x = 2, y = 5)
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "send", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Ten", field = "name", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.ROW })
	@UIRow(visible = true, fields = { "name" })
	private EntityTen entityTen;

	@UIPosition(x = 1, y = 6)
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "printer", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Eleven", field = "amount", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.ROW })
	@UIRow(visible = true, fields = { "amount" })
	private EntityEleven entityEleven;
	
	@UIPosition(x = 1, y = 6)
	@UIFilter(label = "Entity One", field = "name", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.ROW })
	@UIRow(visible = true, fields = { "name" })
	private EntityOne entityOne;
	
	public EntityAll() {
		super();
	}

	public EntityAll(Long id) {
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
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}

	public EntityTwo getEntityTwo() {
		return entityTwo;
	}

	public void setEntityTwo(EntityTwo entityTwo) {
		this.entityTwo = entityTwo;
	}

	public EntityTree getEntityTree() {
		return entityTree;
	}

	public void setEntityTree(EntityTree entityTree) {
		this.entityTree = entityTree;
	}

	public EntityFour getEntityFour() {
		return entityFour;
	}

	public void setEntityFour(EntityFour entityFour) {
		this.entityFour = entityFour;
	}

	public EntityFive getEntityFive() {
		return entityFive;
	}

	public void setEntityFive(EntityFive entityFive) {
		this.entityFive = entityFive;
	}

	public EntitySix getEntitySix() {
		return entitySix;
	}

	public void setEntitySix(EntitySix entitySix) {
		this.entitySix = entitySix;
	}

	public EntitySeven getEntitySeven() {
		return entitySeven;
	}

	public void setEntitySeven(EntitySeven entitySeven) {
		this.entitySeven = entitySeven;
	}

	public EntityEight getEntityEight() {
		return entityEight;
	}

	public void setEntityEight(EntityEight entityEight) {
		this.entityEight = entityEight;
	}

	public EntityNine getEntityNine() {
		return entityNine;
	}

	public void setEntityNine(EntityNine entityNine) {
		this.entityNine = entityNine;
	}

	public EntityTen getEntityTen() {
		return entityTen;
	}

	public void setEntityTen(EntityTen entityTen) {
		this.entityTen = entityTen;
	}

	public EntityEleven getEntityEleven() {
		return entityEleven;
	}

	public void setEntityEleven(EntityEleven entityEleven) {
		this.entityEleven = entityEleven;
	}

	public EntityOne getEntityOne() {
		return entityOne;
	}

	public void setEntityOne(EntityOne entityOne) {
		this.entityOne = entityOne;
	}
}
