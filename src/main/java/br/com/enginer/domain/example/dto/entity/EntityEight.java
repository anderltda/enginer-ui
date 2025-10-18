package br.com.enginer.domain.example.dto.entity;

import java.util.UUID;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.ui.usercase.annotation.field.UIId;
import br.com.enginer.domain.ui.usercase.annotation.field.UIRow;
import br.com.enginer.domain.ui.usercase.annotation.field.UIText;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.ui.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonAdd;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonBack;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonBefore;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonDelete;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonEdit;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonNew;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonNext;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonPaginatorSave;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonSave;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonSearch;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonView;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

@UITitle("Oitavo")
@UIButtonAction(includes = { 
		UIButtonBefore.class, 
		UIButtonNext.class, 
		UIButtonNew.class, 
		UIButtonBack.class, 
		UIButtonAdd.class, 
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
@UIPaginator(
	config = @UIConfig(deletable = true), actions = @UIButtonAction(includes = { UIButtonView.class, UIButtonEdit.class, UIButtonPaginatorSave.class }, 
    value = { @UIButton(label = "Add EntitySeven", icon = "add_circle", needsValidation = false, dropdown = true, template = TypeTemplate.PAGINATOR, 
    action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityEight", param = "{ disable=true, field=entitySeven.id, value=$object }"))) })
)
public class EntityEight extends DomainAbstract<Long> {

	@UIId(label = "Id")
	private Long id;

	@UIPosition(x = 1, y = 1)
	@UIText(label = "Position", max = 100)
	@UIColumn(label = "EntityEight Position", initial = true)
	@UIRow(visible = true)
	private String position;

	@UIPosition(x = 1, y = 1)
	@UIText(label = "Properties")
	@UIColumn(label = "EntityEight Properties", initial = true)
	@UIRow(visible = true)
	private String properties;

	@UIFilter(label = "Entity Seven", field = "dado", readonly = false)
	@UIColumn(label = "Seven", fields = { "dado", "id" }, initial = false)
	@UIRow(visible = true, fields = { "dado" })
	private EntitySeven entitySeven;

	public EntityEight() {
		super();
	}

	public EntityEight(Long id) {
		super();
		this.id = id;
	}

	public void setIdEntitySeven(UUID idEntitySeven) {
		if (this.entitySeven == null) {
			this.entitySeven = new EntitySeven();
			this.entitySeven.setId(new EntitySevenId());
			this.entitySeven.getId().setIdEntitySeven(idEntitySeven);
		} else {
			this.entitySeven.getId().setIdEntitySeven(idEntitySeven);
		}
	}

	public void setIdEntitySix(Long idEntitySix) {
		if (this.entitySeven == null) {
			this.entitySeven = new EntitySeven();
			this.entitySeven.setId(new EntitySevenId());
			this.entitySeven.getId().setIdEntitySix(idEntitySix);
		} else {
			this.entitySeven.getId().setIdEntitySix(idEntitySix);
		}
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getProperties() {
		return this.properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public EntitySeven getEntitySeven() {
		return this.entitySeven;
	}

	public void setEntitySeven(EntitySeven entitySeven) {
		this.entitySeven = entitySeven;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityEight that = (EntityEight) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityEight{" + "id=" + (id != null ? id.toString() : "null") + ", " + "position="
				+ (position != null ? position.toString() : "null") + ", " + "properties="
				+ (properties != null ? properties.toString() : "null") + ", " + "entitySeven="
				+ (entitySeven != null ? entitySeven.toString() : "null") + "" + '}';
	}
}
