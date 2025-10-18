package br.com.enginer.domain;

public class Constants {
	
	// Inicio do nome do diretorio
	public static final String DIRECTORY_APPLICATION = "src/main/java/";

	// Nome do pacote onde estao as classes do dominio
	public static final String PACKAGE_NAME_DOMAIN = "br.com.enginer.domain.rule.dto";
	
	// Types para @Button
	public static final String TYPE_BUTTON_SUBMIT = "submit";
	public static final String TYPE_BUTTON_RESET = "reset";
	public static final String TYPE_BUTTON = "button";
	public static final String STYLE_BUTTON = "button-width";

	// ControlType para o angular identificar o component
	public static final String CONTROL_TYPE_HIDDEN = "hidden";
	public static final String CONTROL_TYPE_JOIN_CLASS = "join";
	public static final String CONTROL_TYPE_INPUT_TEXT = "text";
	public static final String CONTROL_TYPE_INPUT_COLOR = "color";
	public static final String CONTROL_TYPE_INPUT_DATE = "date";
	public static final String CONTROL_TYPE_INPUT_TIME = "time";
	public static final String CONTROL_TYPE_TEXTAREA = "textarea";
	public static final String CONTROL_TYPE_CODEMIRROR = "codemirror";
	public static final String CONTROL_TYPE_CHECKBOX = "checkbox";
	public static final String CONTROL_TYPE_MULTIPLE = "multiple";
	public static final String CONTROL_TYPE_DROPDOWN = "dropdown";
	public static final String CONTROL_TYPE_RADIO_BUTTON = "radio";
	public static final String CONTROL_TYPE_BUTTON = TYPE_BUTTON;

	// Formato da data que recebido pelo componente @InputDate
	public static final String DATE_PATTERN_FORMAT = "yyyy-MM-dd";
	
	
	// Types para @InputText
	public static final String TYPE_INPUT_TEXT = "text";
	public static final String TYPE_INPUT_NUMBER = "number";
	public static final String TYPE_INPUT_EMAIL = "email";
	public static final String TYPE_INPUT_HIDDEN = "hidden";
	
	
	// Validator
	public static final String VALIDATOR_DATE = "date";
	public static final String VALIDATOR_EMAIL = "email";
	public static final String VALIDATOR_CHECKBOX = "checkbox";
	public static final String VALIDATOR_CUSTOM = "custom";
	public static final String VALIDATOR_JOIN_CLASS = "join";
	
	
	// Tipos de maskaras para @InputText 
	public static final String MASK_CPF = "000.000.000-99";
	public static final String MASK_CNPJ = "00.000.000/0000-99";
	public static final String MASK_TELEFONE = "(00)0000-0000";
	public static final String MASK_CELULAR = "(00)90000-0000";
	public static final String MASK_CNH = "000000000";
	public static final String MASK_RENAVAM = "000000000";
	public static final String MASK_CEP = "00000-999";
	public static final String MASK_RG = "00.000.000-9";
	public static final String MASK_DATE = "d0/M0/0000";
	public static final String MASK_HOUR_MIN_SEG = "Hh:m0:s0";
	public static final String MASK_HOUR_MIN = "Hh:m0";
	
	
	// Class Manager
	public static final String MANAGER = "Manager";
	
	// Tab Manager
	public static final String TAB_STATUS_ACTIVE = "active";
	public static final String TAB_STATUS_DISABLED = "disabled";
	public static final String TAB_STATUS_SHOW = "show";
	
	// CrudManager methods
	public static final String METHOD_SAVE = "save";
	public static final String METHOD_DELETE = "delete";
	public static final String METHOD_FIND_ID = "findId";
	public static final String METHOD_FIND_LIST_ALL = "findListAll";
	public static final String METHOD_FIND_PAGINATOR_ALL = "findPaginatorAll";
	
	// Dynamic-List front methods
	public static final String METHOD_CREATE = "create";
	public static final String METHOD_EDIT = "edit";
	
	
	// CodeMirror
	public static final String CODE_EXAMPLE_GROOVY = "import javax.swing.*\n"
			+ "";
}
