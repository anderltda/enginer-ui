package br.com.enginer.domain;

public class Constants {

	/**
	 * Nome do pacote onde estao as classes do dominio
	 */
	public static final String PACKAGE_NAME_DOMAIN = "br.com.enginer.domain";
	
	/**
	 *  Pattern para aplicar no JSON mapper @JsonFormat - Date Time
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	
	/**
	 *  Pattern para aplicar no JSON mapper @JsonFormat - Date
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * Operador Ordenacao
	 */
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	/**
	 * Operadores no formato p/ API
	 */
	public static final String MAIOR_QUE = "gt";
	public static final String MENOR_QUE = "lt";
	public static final String MAIOR_OU_IGUAL = "ge";
	public static final String MENOR_OU_IGUAL = "le";
	public static final String IGUAL = "eq";
	public static final String DIFERENTE = "ne";
	public static final String LIKE = "lk";
	public static final String IN = "in";
	public static final String BETWEEN = "bt";
	
	/**
	 * URL(s) dinamicas
	 */
	public static final String PATH = "/$module/$ui/$domain";
	public static final String PATH_FIND_BY_ID = PATH + "/$id";
	
	/**
	 * Label dos buttons estaticos
	 */
	public static final String LABEL_DELETE = "Excluir";
	public static final String LABEL_EDIT = "Alterar";
	public static final String LABEL_SAVE = "Salvar";
	public static final String LABEL_BACK = "Voltar";
	public static final String LABEL_CLEAR = "Limpar";
	public static final String LABEL_NEW = "Novo";
	public static final String LABEL_SEARCH = "Buscar";
	public static final String LABEL_ADD = "Adicionar";
	
	public static final String LABEL_NEXT = "Proximo";
	public static final String LABEL_BEFORE = "Anterior";
	public static final String LABEL_FINISH = "Finalizar";
	
	public static final String LABEL_ACTION_VIEW = "Exibir detalhes do registro";
	public static final String LABEL_ACTION_EDIT = "Alterar detalhes do registro";
	public static final String LABEL_ACTION_DELETE	 = "Realizar exclusão do registro";
	
	/**
	 * Function, javascript para abrir um modal para criacao um novo dominio
	 */
	public static final String METHOD_OPEN_MODAL_CREATE = "onShowFieldModal";
	public static final String METHOD_CLEAR_FORM = "onClearForm";
	
	/**
	 * Palavras reservadas, utilizadas no DominioResolver, essa palavras serao ignoradas devido a compor a url
	 */
	public static final String[] WORDS_RESERVED = { "form", "filter", "tab", "action", "search", "autocomplete", "validator", "async" };
	
	/**
	 * hash1 = Readonly e hash2 = Editable
	 */
	public static final String HASH1 = "6f6vf44ecb8265a7c47vbfda065c25b79c6592u9c0ed912055e294d21953c695";
	public static final String HASH2 = "6f6ef44ecb8265a7c47ubfda065c25o79c6592u9c0ed912055e294d21953c695";
	
}
