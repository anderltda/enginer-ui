package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDate;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIDate;
import br.com.enginer.domain.ui.usercase.annotation.field.UIId;
import br.com.enginer.domain.ui.usercase.annotation.field.UIRow;
import br.com.enginer.domain.ui.usercase.annotation.field.UIText;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.ui.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonBack;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonBefore;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonDelete;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonEdit;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonNew;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonNext;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonPaginatorSave;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonSave;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonSearch;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization.UIButtonView;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.UIValidate;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.ui.usercase.enums.TypeOperator;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

@UITitle("Sexto")
@UIButtonAction(includes = { 
	UIButtonBefore.class, 
	UIButtonNext.class,
	UIButtonNew.class, 
	UIButtonBack.class, 
	UIButtonDelete.class, 
	UIButtonSearch.class, 
	UIButtonSave.class 
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
		actions = @UIButtonAction(includes = { UIButtonView.class, UIButtonEdit.class, UIButtonPaginatorSave.class }))
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
	@UIText(label = "Package", max = 100)
	@UIColumn(label = "EntitySix Package", initial = true)
	@UIRow(visible = true)
	private String packageName;

	@UIPosition(x = 1, y = 2)
	@UIDate(label = "Start", format = TypeDateFormat.DATE_FORMAT)
	@UIColumn(label = "EntitySix Data Aberta", initial = false)	
	@UIRow(visible = true)
	private LocalDate startDate;

	@UIPosition(x = 2, y = 2)
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

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getStopDate() {
		return this.stopDate;
	}

	public void setStopDate(LocalDate stopDate) {
		this.stopDate = stopDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntitySix that = (EntitySix) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntitySix{" + "id=" + (id != null ? id.toString() : "null") + ", " + "packageName="
				+ (packageName != null ? packageName.toString() : "null") + ", " + "startDate="
				+ (startDate != null ? startDate.toString() : "null") + ", " + "stopDate="
				+ (stopDate != null ? stopDate.toString() : "null") + '}';
	}
}
