package br.com.enginer.domain.example.dto.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import br.com.enginer.domain.system.usecase.annotation.field.UICheckbox;
import br.com.enginer.domain.system.usecase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.annotation.field.UIDate;
import br.com.enginer.domain.system.usecase.annotation.field.UIDecimal;
import br.com.enginer.domain.system.usecase.annotation.field.UIFilter;
import br.com.enginer.domain.system.usecase.annotation.field.UIHidden;
import br.com.enginer.domain.system.usecase.annotation.field.UINumber;
import br.com.enginer.domain.system.usecase.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.annotation.field.UIText;
import br.com.enginer.domain.system.usecase.annotation.instance.UITitle;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionMethod;
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
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorAdd;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.row.UIButtonRowDelete;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.tab.UIButtonTabFinish;
import br.com.enginer.domain.system.usecase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usecase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usecase.constants.Constants;
import br.com.enginer.domain.system.usecase.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.enums.TypeDateFormat;
import br.com.enginer.domain.system.usecase.enums.TypeFormat;
import br.com.enginer.domain.system.usecase.enums.TypeOperator;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.utils.StringsUtils;

/**
 * 
 */
@UITitle("Entity Rows")
@UIButtonAction(
	includes = {
		UIButtonClear.class, 
		UIButtonBack.class, 
		// FILTER
		UIButtonFilterTabNew.class, 
		UIButtonFilterFormNew.class, 
		UIButtonFilterSearch.class, 
		// FORM
		UIButtonFormDelete.class,
		UIButtonFormEdit.class,
		UIButtonFormSave.class,
		// TAB
		UIButtonTabFinish.class
	}
)
@UIPaginator(config = @UIConfig(editableAll = true), 
actions = @UIButtonAction(
	includes = {
		UIButtonPaginatorView.class, 
		UIButtonPaginatorEdit.class, 
		UIButtonPaginatorDelete.class,			
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
	@UIColumn(label = "CPF", initial = true, type = TypeFormat.cpf,
	conditional = @UIConditional({
		@UIConditionalOn(field = "valueDouble", operator = TypeOperator.BETWEEN, matchs = { "1","500" }, value = "badge badge-info"),
		@UIConditionalOn(field = "lineDate", operator = TypeOperator.DATE_BEFORE, matchs = { "lineDateTime" }, value = "badge badge-warning"),
	}))		
	@UIRow(visible = true, editable = true, order = 2)
	private String cpf;
	
	@UIHidden(template = { TypeTemplate.ROW })
	@UIText(label = "CNPJ", mask = "00.000.000/0000-00" , template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIColumn(label = "CNPJ", initial = true, type = TypeFormat.cnpj)		
	@UIRow(visible = true, editable = true, order = 3)
	private String cnpj;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIText(label = "Phone", mask = "(00) 00000-0000" , template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIColumn(label = "Phone", initial = true, type = TypeFormat.phone)		
	@UIRow(visible = true, editable = true, order = 4)
	private String phone;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIText(label = "CEP", mask = "00000-000" , template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
	@UIColumn(label = "CEP", initial = true, type = TypeFormat.cep)		
	@UIRow(visible = true, editable = true, order = 5)	
	private String cep;

	@UIHidden(template = { TypeTemplate.ROW })
	@UIDecimal(label = "Custo", typeFormat = TypeFormat.decimal, precision = 2)
	@UIColumn(label = "Custo", initial = true)		
	@UIRow(visible = true, editable = true, order = 6)
	private BigDecimal custo;	

	@UIHidden(template = { TypeTemplate.ROW })
	@UIColumn(label = "Cambio", type = TypeFormat.cambio, initial = true)
	@UIDecimal(label = "Exchange", typeFormat = TypeFormat.cambio, template = { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL })
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

	public EntityRow() {
		super();
	}

	public EntityRow(Long id) {
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
		return this.id;
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
	
	public void setIdEntityTen(Long idEntityTen) {
		this.entityTen = new EntityTen();
		this.entityTen.setId(idEntityTen);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityRow other = (EntityRow) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "EntityRow [id=" + id + ", entityTen=" + entityTen + ", cpf=" + cpf + ", cnpj=" + cnpj + ", phone="
				+ phone + ", cep=" + cep + ", custo=" + custo + ", valueDouble=" + valueDouble + ", valueLong="
				+ valueLong + ", dataNascimento=" + dataNascimento + ", dataProcessamento=" + dataProcessamento
				+ ", ativo=" + ativo + "]";
	}
}
