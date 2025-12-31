package br.com.enginer.domain.system.dto.entity.tag;

import java.time.Instant;
import java.util.Objects;

import br.com.enginer.domain.system.usecase.core.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIHidden;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIJoin;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.core.annotation.field.UISelect;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIText;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.core.annotation.instance.UITitle;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.UIButtonBack;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.UIButtonClear;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterTabNew;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormDelete;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormEdit;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormSave;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorSave;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.core.helper.ComboHelper;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;

@UITitle("Tag")
@UIButtonAction(includes = {
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
})
@UIPaginator(
	config = @UIConfig(expandable = false, multiSelectable = false),
	actions = @UIButtonAction(
	includes = { 
		UIButtonPaginatorView.class, 
		UIButtonPaginatorEdit.class, 
		UIButtonPaginatorDelete.class,
		UIButtonPaginatorSave.class,
	}
))
public class Tag extends DomainAbstract<TagId> {
	
	@UIJoin(template = { TypeTemplate.MODAL, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.FILTER })
	@UIColumn(label = "Id", fields = { "normalizedName", "domain", "domainId" }, initial = false)
	@UIRow(visible = true, fields = { "normalizedName", "domain", "domainId" })
	private TagId id;
	
	@UIPosition(x = 2, y = 1)
	@UIFieldValidation(required = true)
	@UIText(label = "Name", max = 100)
	@UIColumn(label = "Name", initial = true)
	private String name;
	
	@UIPosition(x = 3, y = 1)
	@UIFieldValidation(required = true)
	@UIColumn(label = "Type", initial = true)
	@UISelect(label = "Type", provider = ComboHelper.class, method = "typesTags")
	private TagType type;
	
	@UIHidden
	@UIColumn(label = "Date Create", initial = true)
	private Instant createdAt;
	
	public Tag() {
		super();
	}

	public Tag(TagId id) {
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
	public TagId getId() {
		return this.id;
	}

	public void setId(TagId id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
	public TagType getType() {
		return type;
	}
	
	public void setType(TagType type) {
		this.type = type;
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
		Tag other = (Tag) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Tag{" + "id=" + (id != null ? id.toString() : "null") + ", " + "name="
				+ (name != null ? name.toString() : "null") + ", " + "createdAt="
				+ (createdAt != null ? createdAt.toString() : "null") + '}';
	}
}
