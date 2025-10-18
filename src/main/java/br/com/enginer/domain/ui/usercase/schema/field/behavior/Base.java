package br.com.enginer.domain.ui.usercase.schema.field.behavior;

import java.util.List;

import br.com.enginer.domain.ui.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.ui.usercase.enums.TypeFileUpload;
import br.com.enginer.domain.ui.usercase.enums.TypeLayoutTarget;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.schema.field.behavior.validation.Validation;

/**
 * 
 */
public abstract class Base {

	private String type;
	private String label;
	private String field;
	private String mask;
	private String icon;
	private String placeholder;
	private String title;
	private String action;
	private String domain;
	private Class<?> domainClass;
	private String method;
	private Integer min;
	private Integer max;
	private Integer limit;
	private Boolean required;
	private Boolean disable;
	private Boolean editor;
	private Boolean showtime;
	private Boolean multi;
	private Boolean enableSwitch;
	private Boolean readonly;
	private Boolean select;
	private Object value;

	private String format;
	private TypeFileUpload mode;
	private TypeTemplate[] template;
	private TypeLayoutTarget layoutTarget;

	private Position position;
	private Validation validation;
	private Autocomplete autocomplete;

	private Class<?> provider;

	private List<UploadFile> files;
	private List<?> options;
	
	private String[] filter;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Class<?> getDomainClass() {
		return domainClass;
	}

	public void setDomainClass(Class<?> domainClass) {
		this.domainClass = domainClass;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public TypeLayoutTarget getLayoutTarget() {
		return layoutTarget;
	}

	public void setLayoutTarget(TypeLayoutTarget layoutTarget) {
		this.layoutTarget = layoutTarget;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public Boolean getEditor() {
		return editor;
	}

	public void setEditor(Boolean editor) {
		this.editor = editor;
	}

	public Boolean getShowtime() {
		return showtime;
	}

	public void setShowtime(Boolean showtime) {
		this.showtime = showtime;
	}

	public Boolean getMulti() {
		return multi;
	}

	public void setMulti(Boolean multi) {
		this.multi = multi;
	}

	public Boolean getEnableSwitch() {
		return enableSwitch;
	}

	public void setEnableSwitch(Boolean enableSwitch) {
		this.enableSwitch = enableSwitch;
	}

	public Boolean getReadonly() {
		return readonly;
	}

	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	public Boolean getSelect() {
		return select;
	}

	public void setSelect(Boolean select) {
		this.select = select;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(TypeDateFormat format) {
		this.format = format.getValue();
	}

	public TypeFileUpload getMode() {
		return mode;
	}

	public void setMode(TypeFileUpload mode) {
		this.mode = mode;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Validation getValidation() {
		return validation;
	}

	public void setValidation(Validation validation) {
		this.validation = validation;
	}

	public Autocomplete getAutocomplete() {
		return autocomplete;
	}

	public void setAutocomplete(Autocomplete autocomplete) {
		this.autocomplete = autocomplete;
	}

	public Class<?> getProvider() {
		return provider;
	}

	public void setProvider(Class<?> provider) {
		this.provider = provider;
	}

	public List<UploadFile> getFiles() {
		return files;
	}

	public void setFiles(List<UploadFile> files) {
		this.files = files;
	}

	public List<?> getOptions() {
		return options;
	}

	public void setOptions(List<?> options) {
		this.options = options;
	}

	public String[] getFilter() {
		return filter;
	}

	public void setFilter(String[] filter) {
		this.filter = filter;
	}

	public TypeTemplate[] getTemplate() {
		return template;
	}

	public void setTemplate(TypeTemplate[] template) {
		this.template = template;
	}
	
}
