package br.com.enginer.domain.example.dto.entity;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIJoin;
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

@UITitle("Setimo")
@UIButtonAction(includes = { 
	UIButtonBefore.class, 
	UIButtonNext.class, 
	UIButtonNew.class, 
	UIButtonAdd.class,
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
@UIPaginator(
		config = @UIConfig(expandable = true, multiSelectable = false, deletable = true),
		actions = @UIButtonAction(includes = { UIButtonView.class, UIButtonEdit.class, UIButtonPaginatorSave.class },
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
		}))
public class EntitySeven extends DomainAbstract<EntitySevenId> {
	
	@UIJoin
	@UIColumn(label = "Id", fields = { "idEntitySeven", "entitySix" }, initial = false)
	@UIRow(visible = true, fields = { "entitySix" })
	private EntitySevenId id;

	@UIPosition(x = 2, y = 3)
	@UIText(label = "Dado")
	@UIColumn(label = "EntitySeven Dado", initial = true)
	@UIRow(visible = true, editable = true)
	private String dado;

	public EntitySeven() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EntitySeven(EntitySevenId id) {
		super();
		this.id = id;
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
