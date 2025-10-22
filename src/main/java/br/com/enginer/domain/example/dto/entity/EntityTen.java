package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDateTime;
import java.util.List;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIFile;
import br.com.enginer.domain.ui.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.ui.usercase.annotation.field.UIHidden;
import br.com.enginer.domain.ui.usercase.annotation.field.UIId;
import br.com.enginer.domain.ui.usercase.annotation.field.UIRow;
import br.com.enginer.domain.ui.usercase.annotation.field.UIText;
import br.com.enginer.domain.ui.usercase.annotation.field.UITextArea;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.ui.usercase.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.ui.usercase.annotation.instance.UITitle;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponseError;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.filter.UIButtonFilterClear;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.filter.UIButtonFilterTabNew;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.form.UIButtonFormBack;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.form.UIButtonFormClear;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.form.UIButtonFormDelete;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.form.UIButtonFormEdit;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.row.UIButtonRowAdd;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.tab.UIButtonTabBack;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.button.tab.UIButtonTabFinish;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.ui.usercase.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.ui.usercase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeFileUpload;
import br.com.enginer.domain.ui.usercase.enums.TypeOperator;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
@UITitle("Decimo")
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
	// ROW
	UIButtonRowAdd.class,
	// TAB
	UIButtonTabBack.class, 
	UIButtonTabFinish.class
},
value = { 
	@UIButton(
	    label = Constants.LABEL_SAVE,
	    icon = "save",
	    state = TypeButtonState.BTN_STATE_PRIMARY,
	    template = { TypeTemplate.FORM, TypeTemplate.MODAL },
	    action = @UIAction(
	        method = @UIActionMethod(serverMethod = "salvar"),
	        response = @UIActionResponse(
	        	template = { TypeTemplate.FORM },
	    		error = @UIActionResponseError(method = @UIActionMethod(clientMethod = "onAlertTestError")), 
	    		success = @UIActionResponseSuccess(redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityEleven", param = "{ disable=true, field=entityTen, value=$object }"))
	        )
	    )
	)	
}
)
@UIPaginator(
		config = @UIConfig(expandable = false, multiSelectable = false), 
		actions = @UIButtonAction(
		includes = { 
			UIButtonPaginatorView.class, 
			UIButtonPaginatorEdit.class, 
			UIButtonPaginatorDelete.class  
		},
		value = {
			@UIButton(
				label = "Add 10 --> 11", 
				template = TypeTemplate.PAGINATOR, 
				highlight = false,
				dropdown = true,
				action = @UIAction(
					redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityEleven", param = "{ disable=true, field=entityTen, value=$object }")
				)
			),
			@UIButton(
				label = "Add 10 --> Row", 
				template = TypeTemplate.PAGINATOR, 
				highlight = false,
				dropdown = true,
				action = @UIAction(
					redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityRow", param = "{ disable=true, field=entityTen, value=$object }")
				)
			)			
		}
		)
)
public class EntityTen extends DomainAbstract<Long> {

	@UIId(label = "Id")
	@UIColumn(label = "Id", initial = false)
	private Long id;

	@UIPosition(x = 1, y = 1)
	@UIFieldValidation(required = true, template = { TypeTemplate.FORM, TypeTemplate.TAB })
	@UIText(label = "Nome", min = 2, max = 100)
	@UIColumn(label = "Nome", initial = true, style = "label label-success",
	conditional = @UIConditional({
		@UIConditionalOn(field = "entityTen.name", operator = TypeOperator.CONTAINS, matchs = { "Two" }, value = "label label-success"),
		@UIConditionalOn(field = "entityTen.name", operator = TypeOperator.EQUALS, matchs = { "Agrupamento One" }, value = "badge badge-important"),
		@UIConditionalOn(field = "name", operator = TypeOperator.CONTAINS, matchs = { "Two" }, value = "label label-info"),
		@UIConditionalOn(field = "name", operator = TypeOperator.EQUALS, matchs = { "Agrupamento One" }, value = "label label-important"),
	}))		
	@UIRow(visible = true)
	private String name;

	@UIHidden
	@UIColumn(label = "Quantidade Total", initial = true)
	@UIRow(visible = true)
	private Integer totalAmount;

	@UIHidden
	@UIColumn(label = "Valor Total", initial = true)
	@UIRow(visible = true)
	private Double totalValue;

	@UIPosition(x = 2, y = 1)
	@UIFilter(label = "Status", field = "name", select = true, filter = { "status=0", "status_op=ge" }, template = { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.MODAL })
	@UIRow(visible = true, fields = { "name", "status", "ativo" })
	@UIColumn(label = "", fields = { "name", "status", "startDateTime" }, initial = false)
	private EntityStatus entityStatus;
	
	@UIPosition(x = 1, y = 2)
	@UITextArea(label = "Descrição", editor = false)
	private String description;
	
	@UIPosition(x = 1, y = 3)
	@UIFile(label = "Arquivo", mode = TypeFileUpload.LIST)
	private List<File> imageLists;
	
	@UIPosition(x = 2, y = 3)
	@UIFile(label = "Simple List", mode = TypeFileUpload.SIMPLE)
	private List<File> simpleLists;

	@UIPosition(x = 1, y = 4)
	@UIFile(label = "Wall Picker", mode = TypeFileUpload.WALL_PICKER, limit = 10)
	private List<File> wallPickers;
	
	@UIPosition(x = 1, y = 5)
	@UIFile(label = "Image Drag Drop", mode = TypeFileUpload.DRAG_DROP)
	private List<File> imageDragDrops;

	@UIHidden
	@UIColumn(label = "Data de Criacao", initial = false)
	private LocalDateTime dateCreate;

	@UIHidden
	@UIColumn(label = "Data de Atualizacao", initial = false)
	private LocalDateTime dateUpdate;

	public void setIdEntityStatus(Long idEntityStatus) {
		this.entityStatus = new EntityStatus();
		this.entityStatus.setId(idEntityStatus);
	}

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

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<File> getWallPickers() {
		return wallPickers;
	}

	public void setWallPickers(List<File> wallPickers) {
		this.wallPickers = wallPickers;
	}

	public List<File> getImageLists() {
		return imageLists;
	}

	public void setImageLists(List<File> imageLists) {
		this.imageLists = imageLists;
	}

	public List<File> getSimpleLists() {
		return simpleLists;
	}

	public void setSimpleLists(List<File> simpleLists) {
		this.simpleLists = simpleLists;
	}

	public List<File> getImageDragDrops() {
		return imageDragDrops;
	}

	public void setImageDragDrops(List<File> imageDragDrops) {
		this.imageDragDrops = imageDragDrops;
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

	@Override
	public String toString() {
		return "EntityTen [id=" + id + ", name=" + name + ", totalAmount=" + totalAmount + ", totalValue=" + totalValue
				+ ", entityStatus=" + entityStatus + ", dateCreate=" + dateCreate + ", dateUpdate=" + dateUpdate + "]";
	}

}
