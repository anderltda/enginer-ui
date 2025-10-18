package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDate;
import java.util.UUID;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIDate;
import br.com.enginer.domain.ui.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.ui.usercase.annotation.field.UIId;
import br.com.enginer.domain.ui.usercase.annotation.field.UIJoin;
import br.com.enginer.domain.ui.usercase.annotation.field.UIRow;
import br.com.enginer.domain.ui.usercase.annotation.field.UISelect;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.ui.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionDomain;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponseError;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionTriggerMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonBack;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonBefore;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonDelete;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonEdit;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonNew;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonSave;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonSearch;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonView;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.UIValidate;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.custom.UICustom;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.custom.UICustomOn;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.dependency.UIDependency;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.dependency.UIDependencyOn;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.global.UIGlobal;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.global.UIGlobalOn;
import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.ui.usercase.enums.TypeOperator;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.helper.ComboHelper;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
@UITitle("Segundo")
@UIButtonAction(includes = { 
		UIButtonBack.class, 
		UIButtonBefore.class,
		UIButtonNew.class, 
		UIButtonEdit.class, 
		UIButtonDelete.class, 
		UIButtonSearch.class, 
		UIButtonSave.class 
	}, 
	value = {
		@UIButton(
		    label = Constants.LABEL_NEXT,
		    icon = "chevron_right",
		    state = TypeButtonState.BTN_STATE_PRIMARY,
		    template = { TypeTemplate.TAB },
		    notDomain = { "entityOne" },
		    action = @UIAction(
		        method = @UIActionMethod(clientMethod = "onNext")
		    )
		),
		@UIButton(
			label = Constants.LABEL_FINISH,
		    icon = "save",
		    state = TypeButtonState.BTN_STATE_PRIMARY,
		    template = { TypeTemplate.TAB },
		    notDomain = { "entityAll" },
		    action = @UIAction(
				method = @UIActionMethod(
					clientMethod = "onFinish", 
					trigger = @UIActionTriggerMethod(serverMethod = "salvar")
				),
		        response = @UIActionResponse(
		        	template = { TypeTemplate.TAB },
		    		error = @UIActionResponseError(method = @UIActionMethod(clientMethod = "onAlertTestError")), 
		    		success = @UIActionResponseSuccess(redirect = @UIActionRedirect(ui = "tab", value = Constants.PATH_FIND_BY_ID))
		        )
		    )
		),		
		@UIButton(
			template = { TypeTemplate.FILTER }, 
			label = "Filtro EntityOne",  
			icon = "send", 
			needsValidation = false, 
			action = @UIAction(
				redirect = @UIActionRedirect(
					value = Constants.PATH, ui = "filter", domain = "entityOne", param = "{ disabled=false }")
			)
		)		
    })
@UIPaginator(
    config = @UIConfig(expandable = true, multiSelectable = false),
    actions = @UIButtonAction(includes = { UIButtonView.class, UIButtonEdit.class, UIButtonDelete.class  },
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
        }
    )
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
	@UIColumn(label = "Cor", initial = true)
	@UIRow(visible = true)
	private String color;

	@UIFieldValidation(required = true)
	@UIDate(label = "Date Inclusion", showtime = false, format = TypeDateFormat.DATE_FORMAT)
	@UIColumn(label = "Data de Inclusao", initial = false)
	@UIRow(visible = true)
	private LocalDate inclusionDate;

	@UIColumn(label = "Hexagonal", initial = true)
	@UIRow(visible = true)
	private Integer hex;
	
	@UIFieldValidation(required = true)
	@UIColumn(label = "Custo", initial = true)
	@UIRow(visible = true)
	private Double cost;

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
	
	@Override
	public UUID getId() {
		return id;
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

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
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
