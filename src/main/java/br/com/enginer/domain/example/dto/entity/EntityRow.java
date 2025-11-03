package br.com.enginer.domain.example.dto.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.enginer.domain.system.usercase.annotation.field.UICheckbox;
import br.com.enginer.domain.system.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usercase.annotation.field.UIDate;
import br.com.enginer.domain.system.usercase.annotation.field.UIDecimal;
import br.com.enginer.domain.system.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.system.usercase.annotation.field.UIHidden;
import br.com.enginer.domain.system.usercase.annotation.field.UINumber;
import br.com.enginer.domain.system.usercase.annotation.field.UIRow;
import br.com.enginer.domain.system.usercase.annotation.field.UIText;
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
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorAdd;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorBack;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.row.UIButtonRowDelete;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.tab.UIButtonTabBack;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.tab.UIButtonTabFinish;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usercase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usercase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usercase.constants.Constants;
import br.com.enginer.domain.system.usercase.enums.TypeButtonState;
import br.com.enginer.domain.system.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.system.usercase.enums.TypeFormat;
import br.com.enginer.domain.system.usercase.enums.TypeOperator;
import br.com.enginer.domain.system.usercase.enums.TypeTemplate;
import br.com.enginer.domain.system.usercase.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usercase.utils.StringsUtils;

/**
 * 
 */
@UITitle("Entity Rows")
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
		//UIButtonRowClear.class, 
		//UIButtonRowBack.class,
		//UIButtonRowAdd.class,
		// TAB
		UIButtonTabBack.class, 
		UIButtonTabFinish.class
	}
)
@UIPaginator(config = @UIConfig(expandable = true, editableAll = true, multiSelectable = false), 
actions = @UIButtonAction(
	includes = {
		UIButtonPaginatorView.class, 
		UIButtonPaginatorEdit.class, 
		UIButtonPaginatorDelete.class,			
		UIButtonPaginatorBack.class,
		UIButtonPaginatorAdd.class,
		UIButtonRowDelete.class
	},
	value = { 
		@UIButton(
			label = Constants.LABEL_SAVE, 
			icon = "save",
			needsValidation = true,
			state = TypeButtonState.BTN_STATE_PRIMARY, 
			template = TypeTemplate.ROW, 
			action = @UIAction(
				method = @UIActionMethod(serverMethod = "rowSalvar")
			)
		) 
	}
))
public class EntityRow extends DomainAbstract<Long> {

	@UIHidden()
	private Long id;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIFilter(label = "Ten", field = "name", readonly = false)
	@UIColumn(label = "Ten", template = { TypeTemplate.FILTER }, fields = { "name", "dateCreate", "dateUpdate" }, initial = true)
	@UIRow(visible = false, fields = { "name" })
	private EntityTen entityTen;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIText(label = "CPF", mask = "000.000.000-00", template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIColumn(label = "CPF", initial = true, type = "cpf",
	conditional = @UIConditional({
		@UIConditionalOn(field = "valueDouble", operator = TypeOperator.BETWEEN, matchs = { "1","500" }, value = "badge badge-info"),
		@UIConditionalOn(field = "lineDate", operator = TypeOperator.DATE_BEFORE, matchs = { "lineDateTime" }, value = "badge badge-warning"),
	}))		
	@UIRow(visible = true, editable = true, order = 2)
	private String cpf;
	
	@UIHidden(template = { TypeTemplate.ROW })
	@UIText(label = "CNPJ", mask = "00.000.000/0000-00" , template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIColumn(label = "CNPJ", initial = true, type = "cnpj")		
	@UIRow(visible = true, editable = true, order = 3)
	private String cnpj;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIText(label = "Phone", mask = "(00)00000-0000" , template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIColumn(label = "Phone", initial = true, type = "phone")		
	@UIRow(visible = true, editable = true, order = 4)
	private String phone;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIText(label = "CEP", mask = "00000-000" , template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIColumn(label = "CEP", initial = true, type = "cep")		
	@UIRow(visible = true, editable = true, order = 5)	
	private String cep;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIDecimal(label = "Custo", typeFormat = TypeFormat.decimal, precision = 2)
	@UIColumn(label = "Custo", initial = true)		
	@UIRow(visible = true, editable = true, order = 6)
	private BigDecimal custo;	

	@UIHidden(template = { TypeTemplate.ROW })
	@UIColumn(label = "Exchange", initial = true)
	@UIDecimal(label = "Exchange", typeFormat = TypeFormat.currency, template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIRow(visible = true, editable = true, order = 7)
	private Double valueDouble;

	@UIHidden(template = { TypeTemplate.ROW })
	@UINumber(label = "Numero Longo", max = 5000, template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIColumn(label = "Numero Longo", initial = true)
	@UIRow(visible = true, editable = true, order = 8)
	private Long valueLong;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIDate(label = "Data Nascimento")
	@UIColumn(label = "Data Nascimento", initial = false)
	@UIRow(visible = true, editable = true, order = 10)
	private LocalDate dataNascimento;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIDate(label = "Data Processamento", format = TypeDateFormat.DATE_TIME_FORMAT, showtime = true)
	@UIColumn(label = "Data Processamento", initial = false)
	@UIRow(visible = true, editable = true, order = 11)
	private LocalDateTime dataProcessamento;

	@UIHidden(template = { TypeTemplate.ROW })
	@UICheckbox(label = "<b>Ativo</b>", enableSwitch = false, template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.MODAL })
	@UIColumn(label = "Boolean", initial = false)
	@UIRow(visible = true, editable = true, order = 9)
	private Boolean ativo;

	public void setIdEntityTen(Long idEntityTen) {
		this.entityTen = new EntityTen();
		this.entityTen.setId(idEntityTen);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public EntityTen getEntityTen() {
		return entityTen;
	}

	public void setEntityTen(EntityTen entityTen) {
		this.entityTen = entityTen;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf != null ? StringsUtils.onlyNumbers(cpf) : cpf;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj != null ? StringsUtils.onlyNumbers(cnpj) : cnpj;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone != null ? StringsUtils.onlyNumbers(phone) : phone;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep != null ? StringsUtils.onlyNumbers(cep) : cep;
	}

	public BigDecimal getCusto() {
		return custo;
	}

	public void setCusto(BigDecimal custo) {
		this.custo = custo;
	}

	public Double getValueDouble() {
		return valueDouble;
	}

	public void setValueDouble(Double valueDouble) {
		this.valueDouble = valueDouble;
	}

	public Long getValueLong() {
		return valueLong;
	}

	public void setValueLong(Long valueLong) {
		this.valueLong = valueLong;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public LocalDateTime getDataProcessamento() {
		return dataProcessamento;
	}

	public void setDataProcessamento(LocalDateTime dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}
}
