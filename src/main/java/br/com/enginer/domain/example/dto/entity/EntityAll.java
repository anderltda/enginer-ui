package br.com.enginer.domain.example.dto.entity;

import br.com.enginer.domain.ui.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.ui.usercase.annotation.field.UIId;
import br.com.enginer.domain.ui.usercase.annotation.field.UIJoin;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonDelete;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonNext;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonSave;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonSearch;
import br.com.enginer.domain.ui.usercase.enums.TypeLayoutTarget;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
@UIButtonAction(includes = { 
	UIButtonNext.class, 
	UIButtonDelete.class, 
	UIButtonSearch.class, 
	UIButtonSave.class  
}
)
public class EntityAll extends DomainAbstract<Long> {

	@UIId(label = "Id")
	private Long id;
	
	@UIFilter(label = "Entity Status", field = "name", readonly = false)
	private EntityStatus entityStatus;
	
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "cloud", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Two", field = "color", readonly = false, template = { TypeTemplate.FILTER })
	private EntityTwo entityTwo;
	
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "cloud_success", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Tree", field = "animal", readonly = false, template = { TypeTemplate.FILTER })
	private EntityTree entityTree;
	
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "cloud_off", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Four", field = "fruit", readonly = false, template = { TypeTemplate.FILTER })
	private EntityFour entityFour;
	
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "database", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Five", field = "reference", readonly = false, template = { TypeTemplate.FILTER })
	private EntityFive entityFive;

	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "keyboard", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Six", field = "packageName", readonly = false, template = { TypeTemplate.FILTER })
	private EntitySix entitySix;

	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "alert_warning", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Seven", field = "dado", readonly = false, template = { TypeTemplate.FILTER })
	private EntitySeven entitySeven;
	
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "calendar", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Eight", field = "position", readonly = false, template = { TypeTemplate.FILTER })
	private EntityEight entityEight;
	
	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "color_palette", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Nine", field = "keyNine", readonly = false, template = { TypeTemplate.FILTER })
	private EntityNine entityNine;

	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "send", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Ten", field = "name", readonly = false, template = { TypeTemplate.FILTER })
	private EntityTen entityTen;

	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "printer", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Eleven", field = "amount", readonly = false, template = { TypeTemplate.FILTER })
	private EntityEleven entityEleven;
	
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
}
