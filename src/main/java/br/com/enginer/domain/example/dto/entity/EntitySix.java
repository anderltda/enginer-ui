package br.com.enginer.domain.example.dto.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import br.com.enginer.domain.system.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usercase.annotation.field.UIDate;
import br.com.enginer.domain.system.usercase.annotation.field.UIDecimal;
import br.com.enginer.domain.system.usercase.annotation.field.UIId;
import br.com.enginer.domain.system.usercase.annotation.field.UIRow;
import br.com.enginer.domain.system.usercase.annotation.field.UIText;
import br.com.enginer.domain.system.usercase.annotation.field.UITime;
import br.com.enginer.domain.system.usercase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usercase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionResponseSuccess;
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
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.row.UIButtonRowAdd;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.tab.UIButtonTabBack;
import br.com.enginer.domain.system.usercase.annotation.instance.action.button.tab.UIButtonTabFinish;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usercase.annotation.instance.validate.UIValidate;
import br.com.enginer.domain.system.usercase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usercase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usercase.constants.Constants;
import br.com.enginer.domain.system.usercase.enums.TypeButtonState;
import br.com.enginer.domain.system.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.system.usercase.enums.TypeFormat;
import br.com.enginer.domain.system.usercase.enums.TypeOperator;
import br.com.enginer.domain.system.usercase.enums.TypeTemplate;
import br.com.enginer.domain.system.usercase.schema.instance.DomainAbstract;

@UITitle("Sexto")
@UIButtonAction(includes = { 
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
	    label = Constants.LABEL_ADD,
	    icon = "plus",
	    state = TypeButtonState.BTN_STATE_PRIMARY,
	    template = { TypeTemplate.ROW },
	    needsValidation = true,
		action = @UIAction(
			method = @UIActionMethod(serverMethod = "plus"),
			response = @UIActionResponse(
			template = { TypeTemplate.ROW },
			success = @UIActionResponseSuccess(
					method = @UIActionMethod(clientMethod = "setDataSetField")
		)))
	),
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
})
@UIPaginator(
	config = @UIConfig(expandable = true, multiSelectable = false, deletable = true),
	actions = @UIButtonAction(
		includes = { 
			UIButtonPaginatorView.class, 
			UIButtonPaginatorEdit.class, 
			UIButtonPaginatorDelete.class
		}
))
@UIValidate(
	conditional = @UIConditional({
		@UIConditionalOn(label = "Start", field = "entitySix.startDate", operator = TypeOperator.EQUALS, matchs = { "entitySix.stopDate" })
	})
)
public class EntitySix extends DomainAbstract<Long> {

	@UIId(label = "Id")
	@UIRow(visible = true)
	private Long id;

	@UIPosition(x = 1, y = 1)
	@UIFieldValidation(required = true)
	@UIText(label = "Package", max = 100)
	@UIColumn(label = "EntitySix Package", initial = true)
	@UIRow(visible = true)
	private String packageName;
	
	@UIPosition(x = 1, y = 2)
	@UIFieldValidation(required = true)
	@UIDecimal(label = "Valor Monetário", typeFormat = TypeFormat.decimal, precision = 2)
	private BigDecimal valorMonetario;

	@UIPosition(x = 2, y = 2)
	@UIFieldValidation(required = true)
	@UIDecimal(label = "Percentual", typeFormat = TypeFormat.percentage)
	private BigDecimal percentual;

	@UIPosition(x = 3, y = 2)
	@UIFieldValidation(required = true)
	@UIDecimal(label = "Câmbio", typeFormat = TypeFormat.exchange)
	private BigDecimal cambio;

	@UIPosition(x = 1, y = 3)
	@UIFieldValidation(required = true)
	@UIDecimal(label = "Quantidade", typeFormat = TypeFormat.quantity)
	private BigDecimal quantidade;

	@UIPosition(x = 2, y = 3)
	@UIFieldValidation(required = true)
	@UIDecimal(label = "Peso Medida", typeFormat = TypeFormat.weight)
	private BigDecimal pesoMedida;

	@UIPosition(x = 1, y = 4)
	@UIFieldValidation(required = true)
	@UIDecimal(label = "Latitude", min = -90, max = 90, typeFormat = TypeFormat.latitude)
	private BigDecimal latitude;

	@UIPosition(x = 2, y = 4)
	@UIFieldValidation(required = true)
	@UIDecimal(label = "Longitude", min = -180, max = 180, typeFormat = TypeFormat.longitude)
	private BigDecimal longitude;

	@UIPosition(x = 1, y = 5)
	@UIFieldValidation(required = true)
	@UIDecimal(label = "Duração", typeFormat = TypeFormat.duration)
	private BigDecimal duracao;

	@UIPosition(x = 2, y = 5)
	@UIFieldValidation(required = true)
	private Double valorCientifico;

	@UIPosition(x = 2, y = 6)
	@UIFieldValidation(required = true)
	@UITime(label = "Tempo Fixo", format = TypeDateFormat.TIME_HHMMSS_FORMAT)
	private LocalTime tempoFixo;

	@UIPosition(x = 1, y = 6)
	@UIFieldValidation(required = true)
	@UIDate(label = "Start", format = TypeDateFormat.DATE_FORMAT)
	@UIColumn(label = "EntitySix Data Aberta", initial = false)	
	@UIRow(visible = true)
	private LocalDate startDate;

	@UIPosition(x = 3, y = 6)
	@UIFieldValidation(required = true)
	@UIDate(label = "Stop", format = TypeDateFormat.DATE_FORMAT)
	@UIColumn(label = "EntitySix Data Fechada", initial = false)
	@UIRow(visible = true)
	private LocalDate stopDate;

	public EntitySix() {
		super();
	}

	public EntitySix(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public BigDecimal getValorMonetario() {
		return valorMonetario;
	}

	public void setValorMonetario(BigDecimal valorMonetario) {
		this.valorMonetario = valorMonetario;
	}

	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public BigDecimal getPesoMedida() {
		return pesoMedida;
	}

	public void setPesoMedida(BigDecimal pesoMedida) {
		this.pesoMedida = pesoMedida;
	}

	public BigDecimal getCambio() {
		return cambio;
	}

	public void setCambio(BigDecimal cambio) {
		this.cambio = cambio;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getDuracao() {
		return duracao;
	}

	public void setDuracao(BigDecimal duracao) {
		this.duracao = duracao;
	}

	public LocalTime getTempoFixo() {
		return tempoFixo;
	}

	public void setTempoFixo(LocalTime tempoFixo) {
		this.tempoFixo = tempoFixo;
	}

	public Double getValorCientifico() {
		return valorCientifico;
	}

	public void setValorCientifico(Double valorCientifico) {
		this.valorCientifico = valorCientifico;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getStopDate() {
		return stopDate;
	}

	public void setStopDate(LocalDate stopDate) {
		this.stopDate = stopDate;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public String toString() {
		return "EntitySix [id=" + id + ", packageName=" + packageName + ", valorMonetario=" + valorMonetario
				+ ", percentual=" + percentual + ", pesoMedida=" + pesoMedida + ", cambio=" + cambio + ", quantidade="
				+ quantidade + ", latitude=" + latitude + ", longitude=" + longitude + ", duracao=" + duracao
				+ ", tempoFixo=" + tempoFixo + ", valorCientifico=" + valorCientifico + ", startDate=" + startDate
				+ ", stopDate=" + stopDate + "]";
	}
}
