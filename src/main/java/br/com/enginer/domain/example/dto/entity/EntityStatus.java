package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import br.com.enginer.domain.system.usecase.annotation.field.UICheckbox;
import br.com.enginer.domain.system.usecase.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.annotation.field.UIId;
import br.com.enginer.domain.system.usecase.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.annotation.field.UISelect;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usecase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.annotation.instance.UITitle;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter.UIButtonFilterClear;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter.UIButtonFilterTabNew;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormBack;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormClear;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormDelete;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormEdit;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.form.UIButtonFormSave;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.row.UIButtonRowAdd;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.tab.UIButtonTabBack;
import br.com.enginer.domain.system.usecase.annotation.instance.action.button.tab.UIButtonTabFinish;
import br.com.enginer.domain.system.usecase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usecase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.helper.ComboHelper;
import br.com.enginer.domain.system.usecase.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;

/**
 * 
 */
@UITitle("Status -> Stream")
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
	UIButtonTabFinish.class,  
})
@UIPaginator(
     config = @UIConfig(multiSelectable = false), 
	 actions = @UIButtonAction(
		 includes = { 
			UIButtonPaginatorView.class, 
			UIButtonPaginatorEdit.class, 
			UIButtonPaginatorDelete.class
		 }
	 )
)
public class EntityStatus extends DomainAbstract<Long> {

	@UIId(label = "Id")
	@UIColumn(label = "EntityStatus Id", hidden = true, initial = false)
	private Long id;
	
	@UIPosition(x = 1, y = 1)
	@UIFieldValidation(required = true, template = { TypeTemplate.FORM })
	@UIColumn(label = "EntityStatus Nome do Status", initial = false)
	@UIRow(visible = true)
	private String name;
	
	@UIPosition(x = 1, y = 2)
	@UIFieldValidation(required = true, template = { TypeTemplate.FORM })
	@UISelect(label = "Escolha um Status!", method = "status", provider = ComboHelper.class)
	@UIColumn(label = "EntityStatus Status do Status", initial = false)
	@UIRow(visible = true)
	private Integer status;
	
	@UIPosition(x = 3, y = 2)
	@UICheckbox(label = "Deseja que esse campo esteja ativo?")
	@UIColumn(label = "Está ativo ??", initial = false)
	@UIRow(visible = true)
	private Boolean ativo;
	
	@UIPosition(x = 2, y = 2)
	@UIFieldValidation(required = true, template = { TypeTemplate.FORM })
	@UIColumn(label = "EntityStatus Data da Localizacao", hidden = true, initial = false)
	@UIRow(visible = true)
	private LocalDateTime startDateTime;
	
	public EntityStatus() {
		super();
	}
	
	public EntityStatus(Long id) {
		super();
		this.id = id;
	}

	@Override
	public Long getId() {
		try {
			Boolean existId = (Boolean) ReflectionUtils.isIdNull(this);
			if (!existId) {
				this.id = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.id;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
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
		EntityStatus other = (EntityStatus) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "EntityStatus [id=" + id + ", name=" + name + ", status=" + status + ", ativo=" + ativo
				+ ", startDateTime=" + startDateTime + "]";
	}
}
