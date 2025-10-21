package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDateTime;

import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.ui.usercase.annotation.field.UIHidden;
import br.com.enginer.domain.ui.usercase.annotation.field.UIId;
import br.com.enginer.domain.ui.usercase.annotation.field.UINumber;
import br.com.enginer.domain.ui.usercase.annotation.field.UIRow;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.ui.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.filter.UIButtonFilterClear;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.filter.UIButtonFilterTabNew;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.form.UIButtonFormBack;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.form.UIButtonFormClear;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.form.UIButtonFormDelete;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.form.UIButtonFormEdit;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.form.UIButtonFormSave;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorSave;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.row.UIButtonRowAdd;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.row.UIButtonRowBack;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.row.UIButtonRowClear;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.tab.UIButtonTabBack;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.tab.UIButtonTabFinish;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
@UITitle("Decimo Primeiro")
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
		UIButtonRowClear.class, 
		UIButtonRowBack.class,
		UIButtonRowAdd.class,
		// TAB
		UIButtonTabBack.class, 
		UIButtonTabFinish.class
	}
)
@UIPaginator(
config = @UIConfig(expandable = false, editable = true, deletable = true, multiSelectable = false), 
actions = @UIButtonAction(
	includes = { 
		UIButtonPaginatorView.class, 
		UIButtonPaginatorEdit.class, 
		UIButtonPaginatorDelete.class,
		UIButtonPaginatorSave.class 
	})
)
public class EntityEleven extends DomainAbstract<Long> {

	@UIId(label = "Id")
	@UIColumn(label = "Id", initial = false)
	@UIRow(visible = false)
	private Long id;

	@UIPosition(x = 1, y = 1)
	@UIFilter(label = "Ten", field = "name", readonly = false)
	@UIColumn(label = "Ten", fields = { "id", "name", "totalAmount", "totalValue" }, initial = false)
	@UIRow(visible = false, fields = { "name" })
	private EntityTen entityTen;

	@UIPosition(x = 1, y = 2)
	@UIFilter(label = "Six", field = "packageName", readonly = false)
	@UIColumn(label = "Six", fields = { "id", "packageName", "startDate", "stopDate" }, initial = false)
	@UIRow(visible = true, fields = { "packageName" })
	private EntitySix entitySix;

	@UIPosition(x = 2, y = 2)
	@UINumber(label = "Quantidade", min = 1, max = 100, template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.MODAL })
	@UIColumn(label = "Quantidade", initial = false)
	@UIRow(visible = true)
	private Integer amount;

	@UIHidden
	@UIColumn(label = "Valor Unitario", initial = false)
	@UIRow(visible = true, editable = true)
	private Double value;

	@UIHidden
	@UIColumn(label = "Data de Criacao", initial = false)
	private LocalDateTime dateCreate;

	@UIHidden
	@UIColumn(label = "Data de Atualizacao", initial = false)
	private LocalDateTime dateUpdate;
	
	@UIHidden(label = "Valor Total", template = { TypeTemplate.ROW })
	@UIColumn(label = "Valor Total", initial = false)
	@UIRow(calculation = "amount * value", totalizer = true, label = "Custo total" , visible = true)
	private Double amountTotal;

	public void setIdEntityTen(Long idEntityTen) {
		this.entityTen = new EntityTen();
		this.entityTen.setId(idEntityTen);
	}

	public void setIdEntitySix(Long idEntitySix) {
		this.entitySix = new EntitySix();
		this.entitySix.setId(idEntitySix);
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

	public EntitySix getEntitySix() {
		return entitySix;
	}

	public void setEntitySix(EntitySix entitySix) {
		this.entitySix = entitySix;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public LocalDateTime getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(LocalDateTime dateCreate) {
		this.dateCreate = dateCreate;
	}

	public LocalDateTime getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(LocalDateTime dateUpdate) {
		this.dateUpdate = dateUpdate;
	}
	
	public Double getAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(Double amountTotal) {
		this.amountTotal = amountTotal;
	}

	@Override
	public String toString() {
		return "EntityEleven [id=" + id + ", entityTen=" + entityTen + ", entitySix=" + entitySix + ", amount=" + amount
				+ ", value=" + value + ", dateCreate=" + dateCreate + ", dateUpdate=" + dateUpdate + "]";
	}

}
