package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.enginer.domain.system.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usercase.annotation.field.UIDate;
import br.com.enginer.domain.system.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.system.usercase.annotation.field.UIId;
import br.com.enginer.domain.system.usercase.annotation.field.UIJoin;
import br.com.enginer.domain.system.usercase.annotation.field.UIRow;
import br.com.enginer.domain.system.usercase.annotation.field.UIText;
import br.com.enginer.domain.system.usercase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usercase.annotation.field.behavior.validation.UIAsync;
import br.com.enginer.domain.system.usercase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usercase.annotation.field.behavior.validation.UIPattern;
import br.com.enginer.domain.system.usercase.annotation.field.behavior.validation.UISync;
import br.com.enginer.domain.system.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionMethod;
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
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.row.UIButtonRowAdd;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.tab.UIButtonTabBack;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.tab.UIButtonTabFinish;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usercase.constants.Constants;
import br.com.enginer.domain.system.usercase.enums.TypeButtonState;
import br.com.enginer.domain.system.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.system.usercase.enums.TypeTemplate;
import br.com.enginer.domain.system.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
@UITitle("Quarto")
@UIButtonAction(
   includes = { 
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
	UIButtonRowAdd.class,
	// TAB
	UIButtonTabBack.class, 
	UIButtonTabFinish.class
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
@UIPaginator(config = @UIConfig(deletable = true), actions = @UIButtonAction(includes = { UIButtonPaginatorView.class, UIButtonPaginatorEdit.class }))
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

	public void setIdEntityStatus(Long idEntityStatus) {
		this.entityStatus = new EntityStatus();
		this.entityStatus.setId(idEntityStatus);
	}

	public void setIdEntityFive(UUID idEntityFive) {
		this.entityFive = new EntityFive();
		this.entityFive.setId(idEntityFive);
	}

	@Override
	public UUID getId() {
		return id;
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
}
