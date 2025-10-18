package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.field.UICheckbox;
import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIDate;
import br.com.enginer.domain.ui.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.ui.usercase.annotation.field.UIHidden;
import br.com.enginer.domain.ui.usercase.annotation.field.UIId;
import br.com.enginer.domain.ui.usercase.annotation.field.UIJoin;
import br.com.enginer.domain.ui.usercase.annotation.field.UINumber;
import br.com.enginer.domain.ui.usercase.annotation.field.UIRow;
import br.com.enginer.domain.ui.usercase.annotation.field.UIText;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.validation.UIAsync;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.validation.UIPattern;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.validation.UISync;
import br.com.enginer.domain.ui.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionDomain;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionTriggerMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonAdd;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonBack;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonClear;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonDelete;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonEdit;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonNew;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonNext;
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
import br.com.enginer.domain.ui.usercase.enums.TypeLayoutTarget;
import br.com.enginer.domain.ui.usercase.enums.TypeOperator;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
@UITitle("Primeiro")
@UIButtonAction(includes = { 
		UIButtonBack.class, 
		UIButtonClear.class, 
		UIButtonNext.class, 
		UIButtonNew.class, 
		UIButtonEdit.class, 
		UIButtonDelete.class, 
		UIButtonSearch.class, 
		UIButtonAdd.class,
		UIButtonSave.class 
}, 
value = {	
	@UIButton(
		label = Constants.LABEL_NEW + " - Tab",	
		template = { TypeTemplate.FILTER }, 
		icon = "send", 
		needsValidation = false, 
		action = @UIAction(
			redirect = @UIActionRedirect(value = Constants.PATH, ui = "tab", domain = "entityOne", param = "{ disabled=false }")
		)
	),
	@UIButton(
		label = "Custom", 
		template = { TypeTemplate.DISABLED }, 
		icon = "google_plus", 
		confirm = false, 
		needsValidation = false, 
		action = @UIAction(
			method = @UIActionMethod(clientMethod = "custom")
		)
	),
})
@UIPaginator(
    config = @UIConfig(expandable = true, multiSelectable = false, deletable = true, editable = true),
    actions = @UIButtonAction(includes = { UIButtonView.class, UIButtonEdit.class, UIButtonDelete.class  },
    value = {
		@UIButton(
			label = "Abrir uma listagem Entity Two", 
			template = TypeTemplate.PAGINATOR, 
			highlight = false,
			dropdown = true,
			action = @UIAction(
				redirect = @UIActionRedirect(value = Constants.PATH, ui = "filter", domain = "entityTwo", param = "{ disable=true, field=color, value=$entityTwo.color }")
			)
		), 
		@UIButton(
			label = "Novo form Entity One com Entity Two", 
			template = TypeTemplate.PAGINATOR, 
			highlight = false,
			dropdown = true,
			action = @UIAction(
				redirect = @UIActionRedirect(value = Constants.PATH, ui = "form", domain = "entityOne", param = "{ disable=false, field=entityTwo, value=$object }")
			)
		),
		@UIButton(
			label = "Add Entity Two in Entity One", 
			template = TypeTemplate.PAGINATOR, 
			highlight = false,
			dropdown = true,
			action = @UIAction(
				redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityOne", param = "{ disable=true, field=entityTwo, value=$object }")
			)
		), 		
		@UIButton(
			label = "Visualizar (tab) detalhes do registro", 
			template = TypeTemplate.PAGINATOR, 
			highlight = false, 
			dropdown = true,
			action = @UIAction(
				redirect = @UIActionRedirect(value = Constants.PATH_FIND_BY_ID, ui = "tab", param = "{ disableAll=true }")
			)
		),	
		@UIButton(
			label = "Editar (tab) detalhes do registro", 
			template = { TypeTemplate.PAGINATOR }, 
			highlight = true,
			dropdown = true,
			action = @UIAction(
				redirect = @UIActionRedirect(value = Constants.PATH_FIND_BY_ID, ui = "tab",  param = "{ disableAll=false }")
			)
		),
		@UIButton(
		    label = Constants.LABEL_SAVE,
		    icon = "save",
		    state = TypeButtonState.BTN_STATE_PRIMARY,
		    template = { TypeTemplate.ROW },
		    action = @UIAction(
		        method = @UIActionMethod(serverMethod = "salvarLista")
		    )
		), 
		@UIButton(
			label = Constants.LABEL_CLEAR, 
			icon = "bin_alt", 
			needsValidation = false,
			state = TypeButtonState.BTN_STATE_PRIMARY, 
			template = { TypeTemplate.PAGINATOR }, 
			action = @UIAction( 
				method = @UIActionMethod(clientMethod = "triggerMethod", 
				trigger = @UIActionTriggerMethod(clientMethod = Constants.METHOD_CLEAR_FORM)) 
			)
		),
		@UIButton(
		    label = Constants.LABEL_SEARCH,
		    icon = "search",
		    state = TypeButtonState.BTN_STATE_PRIMARY,
		    template = TypeTemplate.PAGINATOR,
			action = @UIAction( 
				method = @UIActionMethod(clientMethod = "triggerMethod", 
				trigger = @UIActionTriggerMethod(serverMethod = "search")) 
			)
		),
        @UIButton(template = TypeTemplate.PAGINATOR, label = "Another Method Action", dropdown = true, action = @UIAction(method = @UIActionMethod(clientMethod = "salvar"))),
        @UIButton(template = TypeTemplate.PAGINATOR, label = "Two domain link", dropdown = true, action = @UIAction(actionObject = @UIActionDomain(object = "entityTwo", param = "$id"))),
        @UIButton(template = TypeTemplate.PAGINATOR, label = "Tree domain link", dropdown = true, action = @UIAction(actionObject = @UIActionDomain(object = "entityTwo.entityTree", param = "$id"))),
        @UIButton(template = TypeTemplate.PAGINATOR, label = "Four domain link", dropdown = true, action = @UIAction(actionObject = @UIActionDomain(object = "entityTwo.entityTree.entityFour", param = "$id"))),            
        @UIButton(template = TypeTemplate.PAGINATOR, label = "Five domain link", dropdown = true, action = @UIAction(actionObject = @UIActionDomain(object = "entityTwo.entityTree.entityFour.entityFive", param = "$id")))
    }
  )
)
@UIValidate(
	global = @UIGlobal({ 
		@UIGlobalOn(function = "customEntitySumValuesValidator", message = "Encontramos erros, verifique todos os campos do tipo inteiro em seu formulario, a soma desses campos não pode ser maior que 100!!") 
	}), 
	custom = @UICustom({
		@UICustomOn(function = "customContainsNumberSpecialValidator", message = "Esse campo tem apenas 1 numero, o correto é ter pelo menos 2 numeros", fields = { "entityOne.entityTwo.cost", "entityOne.entityTwo.hex", "entityOne.entityTwo.entityTree.amount", "entityOne.entityTwo.entityTree.entityFour.attribute", "entityOne.entityTwo.entityTree.entityFour.entityFive.factor" }) 
	}), 
	conditional = @UIConditional({
		@UIConditionalOn(label = "Age", field = "entityOne.age", operator = TypeOperator.LESS_THAN, matchs = { "entityOne.entityTwo.hex" }),
		@UIConditionalOn(label = "Hex", field = "entityOne.entityTwo.hex", operator = TypeOperator.GREATER_THAN_OR_EQUALS, matchs = { "entityOne.entityTwo.entityTree.indicator" }),
		@UIConditionalOn(label = "Indicator", field = "entityOne.entityTwo.entityTree.indicator", operator = TypeOperator.NOT_EQUALS, matchs = { "entityOne.entityTwo.entityTree.entityFour.attribute" }),
		@UIConditionalOn(label = "Attribute", field = "entityOne.entityTwo.entityTree.entityFour.attribute", operator = TypeOperator.EQUALS, matchs = { "entityOne.entityTwo.entityTree.entityFour.entityFive.factor" }),
		@UIConditionalOn(label = "Factor", field = "entityOne.entityTwo.entityTree.entityFour.entityFive.factor", operator = TypeOperator.GREATER_THAN_OR_EQUALS, matchs = { "entityOne.age" }),
		@UIConditionalOn(label = "Entity Status do Entity One", field = "entityOne.entityStatus", operator = TypeOperator.NOT_EQUALS, matchs = { "entityOne.entityTwo.entityStatus", "entityOne.entityTwo.entityTree.entityStatus" }) 
	}), 
	dependency = @UIDependency({
		@UIDependencyOn(template = { TypeTemplate.FORM }, label = "Age", field = "entityOne.age", depends = { "entityOne.entityTwo.hex", "entityOne.entityTwo.entityTree.indicator", "entityOne.entityTwo.entityTree.entityFour.entityFive.factor" }),
		@UIDependencyOn(template = { TypeTemplate.FORM }, label = "Fruit", field = "entityOne.entityTwo.entityTree.entityFour.fruit", depends = { "entityOne.entityTwo.entityTree.entityFour.attribute" }),
		@UIDependencyOn(template = { TypeTemplate.FORM }, label = "Status", field = "entityOne.entityStatus", depends = { "entityOne.entityTwo.entityStatus" }) 
	})
)
public class EntityOne extends DomainAbstract<Long> {

	@UIId(label = "Id")
	@UIColumn(label = "EntityOne ID", initial = false)
	private Long id;

	@UIText(label = "Name", template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.MODAL })
	//@UIAutoCompleteSuggestion(suggestions = { "Anderson", "Pedro", "Marcelo", "Michael", "Ramiro", "José" })
	//@UIAutoComplete(domain = "entityOne", attribute = "name")
	@UIPosition(x = 1, y = 1)
	@UIFieldValidation(
		required = false,
		template = { TypeTemplate.FILTER, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL },
	    pattern  = @UIPattern(pattern = "^[^wW]*$",                   patternError = "*** PATTERN ***, nao pode adiciona a letra 'W'"),
	    async    = @UIAsync(   method = "metodoJavaDominioEntityOne", asyncError = "Validação direto no field 'ASYNC'"),
	    sync     = @UISync(  syncFunc = { "dogMel", "dogMagrela" },   syncError = { "message1", "Validação direto no field 'SYNC' - O campo está randomico, acabou caindo no erro." })
	)
	@UIColumn(label = "Nome", initial = true)
	private String name;

	@UIPosition(x = 2, y = 1)
	@UIFieldValidation(required = true)
	@UIFilter(label = "Entity Status", field = "name", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.MODAL })
	@UIColumn(label = "Entity Status", fields = { "name", "status" }, initial = false)
	private EntityStatus entityStatus;
	
	@UIPosition(x = 1, y = 2)
	@UIFieldValidation(required = true)
	//@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "send", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	//@UIFilter(label = "Entity Nine", field = "keyNine", readonly = false, template = { TypeTemplate.FILTER, TypeTemplate.MODAL })
	@UIFilter(label = "Entity Nine", field = "keyNine", readonly = false)
	@UIColumn(label = "Entity Nine", fields = { "keyNine", "id" }, initial = true)
	private EntityNine entityNine;
	
	@UIPosition(x = 2, y = 2)
	@UIFieldValidation(required = true)
	@UICheckbox(label = "<b>Code</b>: I hereby certify that the information above is true and accurate", enableSwitch = false, template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.MODAL })
	@UIColumn(label = "Codigo", initial = true)
	private Boolean code;

	@UIPosition(x = 1, y = 3)
	@UINumber(label = "Age", min = 1, max = 60, template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIColumn(label = "EntityOne Idade", initial = false)
	@UIRow(visible = true, editable = true, totalizer = true, order = 3)
	private Integer age;

	@UIPosition(x = 2, y = 3)
	@UIFieldValidation(required = true)
	@UIText(label = "Height", mask = "0.00", template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIColumn(label = "EntityOne Altura", initial = false)
	@UIRow(visible = true, editable = true, order = 4)
	private Double height;

	@UIPosition(x = 3, y = 3)
	@UIFieldValidation(required = true)
	@UIDate(label = "Birth Date")
	@UIColumn(label = "Aniversario", initial = true)
	@UIRow(visible = false)
	private LocalDate birthDate;

	@UIPosition(x = 4, y = 3)
	@UIFieldValidation(required = true)
	@UIDate(label = "Prohibited Date Time", format = TypeDateFormat.DATE_TIME_FORMAT, showtime = true)
	@UIColumn(label = "Data da Proibicao", initial = true)
	@UIRow(visible = true, order = 2)
	private LocalDateTime prohibitedDateTime;

	@UIJoin(layoutTarget = TypeLayoutTarget.tab, icon = "code", template = { TypeTemplate.TAB, TypeTemplate.FORM })
	@UIFilter(label = "Entity Two", field = "color", template = { TypeTemplate.FILTER, TypeTemplate.ROW })
	@UIColumn(label = "Entity Two", fields = { "color", "inclusionDate", "cost", "entityTree" }, initial = true)
	@UIRow(visible = true, fields = { "color" }, order = 1)
	private EntityTwo entityTwo;
	
	@UIHidden(label = "Multiplicacao Total", template = { TypeTemplate.ROW })
	@UIColumn(label = "Multiplicacao Total", initial = false)
	@UIRow(calculation = "age * height", totalizer = true, visible = true)
	private Double valorMultiplicacao;

	@UIHidden(label = "Soma Total", template = { TypeTemplate.ROW })
	@UIColumn(label = "Soma Total", initial = false)
	@UIRow(calculation = "age + height", totalizer = true, visible = true)
	private Double valorSoma;
	
	@UIHidden(label = "Subtracao Total", template = { TypeTemplate.ROW })
	@UIColumn(label = "Subtracao Total", initial = false)
	@UIRow(calculation = "age - height + valorUm", totalizer = true, visible = true)
	private Double valorSubtracao;	
	
	@UIHidden(label = "Divisao Total", template = { TypeTemplate.ROW })
	@UIColumn(label = "Divisao Total", initial = false)
	@UIRow(calculation = "age / height", totalizer = true, visible = true)
	private Double valorDivisao;	
	
	@UIHidden(label = "Divisao Total", template = { TypeTemplate.ROW })
	@UIRow(visible = false)
	private Integer valorUm = 1;
	
	public Integer getValorUm() {
		return valorUm;
	}

	public void setValorUm(Integer valorUm) {
		this.valorUm = valorUm;
	}

	public Double getValorDivisao() {
		return valorDivisao;
	}

	public void setValorDivisao(Double valorDivisao) {
		this.valorDivisao = valorDivisao;
	}

	public Double getValorSubtracao() {
		return valorSubtracao;
	}

	public void setValorSubtracao(Double valorSubtracao) {
		this.valorSubtracao = valorSubtracao;
	}

	public Double getValorSoma() {
		return valorSoma;
	}

	public void setValorSoma(Double valorSoma) {
		this.valorSoma = valorSoma;
	}

	public Double getValorMultiplicacao() {
		return valorMultiplicacao;
	}

	public void setValorMultiplicacao(Double valorMultiplicacao) {
		this.valorMultiplicacao = valorMultiplicacao;
	}

	/**
	 * BLOCO START
	 */
	public void setIdEntityStatus(Long idEntityStatus) {
		this.entityStatus = new EntityStatus();
		this.entityStatus.setId(idEntityStatus);
	}

	public void setIdEntityTwo(UUID idEntityTwo) {
		this.entityTwo = new EntityTwo();
		this.entityTwo.setId(idEntityTwo);
	}
	
	public void setIdEntityNine(EntityNineId idEntityNine) {
		this.entityNine = new EntityNine();
		this.entityNine.setId(idEntityNine);
	}
	
	public void setIdEntityEight(Long idEntityEight) {
		if(this.entityNine == null) {
			this.entityNine = new EntityNine();
			this.entityNine.setId(new EntityNineId());
			this.entityNine.getId().setIdEntityEight(idEntityEight);
		} else {
			this.entityNine.getId().setIdEntityEight(idEntityEight);
		}
	}

	public void setIdEntitySeven(UUID idEntitySeven) {
		if(this.entityNine == null) {
			this.entityNine = new EntityNine();
			this.entityNine.setId(new EntityNineId());
			this.entityNine.getId().setIdEntitySeven(idEntitySeven);
		} else {
			this.entityNine.getId().setIdEntitySeven(idEntitySeven);
		}
	}

	public void setIdEntitySix(Long idEntitySix) {
		if(this.entityNine == null) {
			this.entityNine = new EntityNine();
			this.entityNine.setId(new EntityNineId());
			this.entityNine.getId().setIdEntitySix(idEntitySix);
		} else {
			this.entityNine.getId().setIdEntitySix(idEntitySix);
		}
	}
	
	/**
	 * BLOCO END
	 */

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDateTime getProhibitedDateTime() {
		return prohibitedDateTime;
	}

	public void setProhibitedDateTime(LocalDateTime prohibitedDateTime) {
		this.prohibitedDateTime = prohibitedDateTime;
	}

	public Boolean getCode() {
		return code;
	}

	public void setCode(Boolean code) {
		this.code = code;
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
	
	public EntityNine getEntityNine() {
		return entityNine;
	}

	public void setEntityNine(EntityNine entityNine) {
		this.entityNine = entityNine;
	}

	@Override
	public String toString() {
		return "EntityOne [id=" + id + ", name=" + name + ", entityStatus=" + entityStatus + ", code=" + code + ", age="
				+ age + ", height=" + height + ", birthDate=" + birthDate + ", prohibitedDateTime=" + prohibitedDateTime
				+ ", entityTwo=" + entityTwo + "]";
	}
}
