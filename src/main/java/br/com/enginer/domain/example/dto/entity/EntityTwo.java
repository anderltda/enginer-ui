package br.com.enginer.domain.example.dto.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import br.com.enginer.domain.system.usecase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.annotation.field.UIDate;
import br.com.enginer.domain.system.usecase.annotation.field.UIFilter;
import br.com.enginer.domain.system.usecase.annotation.field.UIId;
import br.com.enginer.domain.system.usecase.annotation.field.UIJoin;
import br.com.enginer.domain.system.usecase.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.annotation.field.UISelect;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.annotation.instance.UITitle;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionDomain;
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
import br.com.enginer.domain.system.usecase.annotation.instance.validate.UIValidate;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.custom.UICustom;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.custom.UICustomOn;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.dependency.UIDependency;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.dependency.UIDependencyOn;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.global.UIGlobal;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.global.UIGlobalOn;
import br.com.enginer.domain.system.usecase.constants.Constants;
import br.com.enginer.domain.system.usecase.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.enums.TypeDateFormat;
import br.com.enginer.domain.system.usecase.enums.TypeOperator;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.helper.ComboHelper;
import br.com.enginer.domain.system.usecase.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;

/**
 * 
 */
@UITitle("Segundo")
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
}, 
value = {
	@UIButton(
		label = "Go entityTree", 
		template = TypeTemplate.FORM,
		icon = "google_plus", 
		needsValidation = true,
		confirm = false, 
		action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH, ui = "form", domain = "entityTree", param = "{ disable=false, field=entityTree, value=$object }"))
	),
	@UIButton(
		template = { TypeTemplate.DISABLED }, 
		label = "Filtro EntityOne",  
		icon = "send", 
		needsValidation = false,
		action = @UIAction(
			redirect = @UIActionRedirect(value = Constants.PATH, ui = "filter", domain = "entityOne", param = "{ disabled=false }")
		)
	),
	@UIButton(
	    label = Constants.LABEL_BACK,
	    icon = "undo",
	    needsValidation = false,
	    state = TypeButtonState.BTN_STATE_DEFAULT,
	    template = TypeTemplate.TAB,
		conditional = @UIConditional({
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityOne", "entityAll" }),
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
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityTwo" }),
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
			@UIConditionalOn(field = "mainDomain", operator = TypeOperator.NOT_CONTAINS, matchs = { "entityOne", "entityTwo" }),
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
    config = @UIConfig(expandable = true, multiSelectable = false),
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
			label = "Abrir uma listagem Entity One",
			template = TypeTemplate.PAGINATOR,
			highlight = false,
			dropdown = true,
			action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH, ui = "filter", domain = "entityOne", param = "{ disable=true, field=entityTwo, value=$object }"))
		), 
		@UIButton(
		    label = "Novo Entity One com Entity Two", 
		    icon = "add_circle",
		    needsValidation = false,
    		dropdown = true,
		    template = TypeTemplate.PAGINATOR, 
	   		action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH, ui = "form", domain = "entityOne", param = "{ disable=false, field=entityTwo, value=$object }"))
		),
		@UIButton(
		    label = "Add Entity One com Entity Two", 
		    icon = "add_circle",
		    needsValidation = false,
		    dropdown = true,
		    template = TypeTemplate.PAGINATOR, 
	   		action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityOne", param = "{ disable=true, field=entityTwo, value=$object }"))
		),    		
		@UIButton(
			label = "Visualizar (tab) detalhes do registro", 
			template = TypeTemplate.PAGINATOR, 
			highlight = false,
			dropdown = true,
			action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH_FIND_BY_ID, ui = "tab", param = "{ disableAll=true }"))
		),	
		@UIButton(
			label = "Editar (tab) detalhes do registro", 
			template = TypeTemplate.PAGINATOR, 
			highlight = true,
			dropdown = true,
			action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH_FIND_BY_ID, ui = "tab", param = "{ disableAll=false }"))
		),
        @UIButton(template = TypeTemplate.PAGINATOR, label = "Another Method Action", dropdown = true, action = @UIAction(method = @UIActionMethod(clientMethod = "salvar"))),
        @UIButton(template = TypeTemplate.PAGINATOR, label = "Tree domain link", dropdown = true, action = @UIAction(actionObject = @UIActionDomain(object = "entityTwo.entityTree", param = "$id"))),
        @UIButton(template = TypeTemplate.PAGINATOR, label = "Five domain link", dropdown = true, action = @UIAction(actionObject = @UIActionDomain(object = "entityTwo.entityTree.entityFour.entityFive", param = "$id")))
    })
)
@UIValidate(
	global = @UIGlobal(template = { TypeTemplate.TAB }, value = { 
		@UIGlobalOn(function = "customEntitySumValuesValidatorTab", message = "Encontramos erros, verifique todos os campos do tipo inteiro em seu formulario, a soma desses campos não pode ser maior que 100!!") 
	}), 		
	custom = @UICustom(template = { TypeTemplate.TAB }, value = {
		@UICustomOn(function = "customContainsNumberSpecialValidator", message = "Esse campo tem apenas 1 numero, o correto é ter pelo menos 2 numeros", fields = { "entityTwo.entityTree.amount", "entityTwo.entityTree.entityFour.entityFive.factor" }) 
	}),		
	conditional = @UIConditional(template = { TypeTemplate.TAB }, value = {
		@UIConditionalOn(label = "Hex", field = "entityTwo.hex", operator = TypeOperator.GREATER_THAN_OR_EQUALS, matchs = { "entityTwo.entityTree.indicator" }),
		@UIConditionalOn(label = "Indicator", field = "entityTwo.entityTree.indicator", operator = TypeOperator.NOT_EQUALS, matchs = { "entityTwo.entityTree.entityFour.attribute" }),
		@UIConditionalOn(label = "Attribute", field = "entityTwo.entityTree.entityFour.attribute", operator = TypeOperator.EQUALS, matchs = { "entityTwo.entityTree.entityFour.entityFive.factor" }),
		@UIConditionalOn(label = "Factor", field = "entityTwo.entityTree.entityFour.entityFive.factor", operator = TypeOperator.GREATER_THAN_OR_EQUALS, matchs = { "entityTwo.hex" }),
		@UIConditionalOn(label = "Entity Status do Entity Two", field = "entityTwo.entityStatus", operator = TypeOperator.NOT_EQUALS, matchs = { "entityTwo.entityTree.entityStatus" }) 
	}), 
	dependency = @UIDependency(template = { TypeTemplate.TAB }, value ={
		@UIDependencyOn(template = { TypeTemplate.TAB }, label = "Fruit", field = "entityTwo.entityTree.entityFour.fruit", depends = { "entityTwo.entityTree.entityFour.attribute" }),
		@UIDependencyOn(template = { TypeTemplate.TAB }, label = "Status", field = "entityTwo.entityStatus", depends = { "entityTwo.entityTree.entityStatus" }) 
	})
)
public class EntityTwo extends DomainAbstract<UUID> {

	@UIId(label = "Id")
	@UIColumn(label = "EntityTwo Id", initial = false)
	private UUID id;

	@UISelect(label = "Colors", multi = false, provider = ComboHelper.class, method = "colors")
	@UIFieldValidation(required = true)
	@UIColumn(label = "Cor", initial = true,
	conditional = @UIConditional({
		@UIConditionalOn(field = "entityTwo.hex", operator = TypeOperator.GREATER_THAN, matchs = { "entityTwo.entityTree.entityFour.attribute" }, value = "label label-important"),
		@UIConditionalOn(field = "entityTwo.hex", operator = TypeOperator.LESS_THAN, matchs = { "entityTwo.entityTree.entityFour.attribute" }, value = "label label-warning"),
		@UIConditionalOn(field = "entityTwo.hex", operator = TypeOperator.EQUALS, matchs = { "entityTwo.entityTree.entityFour.attribute" }, value = "label label-info"),

	}))
	@UIRow(visible = true)
	private String color;

	@UIFieldValidation(required = true)
	@UIDate(label = "Date Inclusion", showtime = false, format = TypeDateFormat.DATE_FORMAT)
	@UIColumn(label = "Data de Inclusao", initial = false)
	@UIRow(visible = true)
	private LocalDate inclusionDate;

	@UIColumn(label = "Hexagonal", initial = true, style = "badge badge-inverse")
	@UIRow(visible = true)
	private Integer hex;
	
	@UIFieldValidation(required = true)
	@UIColumn(label = "Custo", initial = true)
	@UIRow(visible = true)
	private BigDecimal cost;

	@UIFieldValidation(required = true, template = TypeTemplate.FORM)
	@UIFilter(label = "Entity Status", field = "name", select = false)
	@UIRow(visible = true, fields = { "name", "status" })
	private EntityStatus entityStatus;

	@UIFilter(label = "Entity Tree", field = "animal", template = { TypeTemplate.FILTER, TypeTemplate.MODAL })
	@UIJoin(icon = "save")
	@UIRow(visible = true, fields = { "animal" })
	@UIColumn(label = "Entity Tree", fields = { "animal", "indicator", "amount", "localDate", "localDateTime", "entityFour" }, initial = false)
	private EntityTree entityTree;
	
	public void setIdEntityStatus(Long idEntityStatus) {
		this.entityStatus = new EntityStatus();
		this.entityStatus.setId(idEntityStatus);
	}

	public void setIdEntityTree(UUID idEntityTree) {
		this.entityTree = new EntityTree();
		this.entityTree.setId(idEntityTree);
	}
	
	public EntityTwo() {
		super();
	}

	public EntityTwo(UUID id) {
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}

	public Integer getHex() {
		return hex;
	}

	public void setHex(Integer hex) {
		this.hex = hex;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public LocalDate getInclusionDate() {
		return inclusionDate;
	}

	public void setInclusionDate(LocalDate inclusionDate) {
		this.inclusionDate = inclusionDate;
	}

	public EntityTree getEntityTree() {
		return entityTree;
	}

	public void setEntityTree(EntityTree entityTree) {
		this.entityTree = entityTree;
	}
}
