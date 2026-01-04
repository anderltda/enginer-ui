package br.com.enginer.domain.system.usecase.core.template;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.usecase.core.annotation.field.UICheckbox;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIDate;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIDecimal;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIEmail;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIFile;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIFilter;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIHidden;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIId;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIIgnore;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIJoin;
import br.com.enginer.domain.system.usecase.core.annotation.field.UINumber;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIPassword;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIRadio;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.core.annotation.field.UISelect;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIText;
import br.com.enginer.domain.system.usecase.core.annotation.field.UITextArea;
import br.com.enginer.domain.system.usecase.core.annotation.field.UITime;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.UITag;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.autocomplete.UIAutoComplete;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.autocomplete.UIAutoCompleteSuggestion;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.validation.UIAsync;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.validation.UIPattern;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.validation.UISync;
import br.com.enginer.domain.system.usecase.core.annotation.instance.UIHeader;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionDomain;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionResponseError;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.UIValidate;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.custom.UICustom;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.custom.UICustomOn;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.dependency.UIDependency;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.dependency.UIDependencyOn;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.global.UIGlobal;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.global.UIGlobalOn;
import br.com.enginer.domain.system.usecase.core.constants.Constants;
import br.com.enginer.domain.system.usecase.core.enums.TypeButton;
import br.com.enginer.domain.system.usecase.core.enums.TypeFormat;
import br.com.enginer.domain.system.usecase.core.enums.TypeOperator;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.core.helper.ComboHelper;
import br.com.enginer.domain.system.usecase.core.schema.Form;
import br.com.enginer.domain.system.usecase.core.schema.field.Field;
import br.com.enginer.domain.system.usecase.core.schema.field.behavior.Autocomplete;
import br.com.enginer.domain.system.usecase.core.schema.field.behavior.Base;
import br.com.enginer.domain.system.usecase.core.schema.field.behavior.Default;
import br.com.enginer.domain.system.usecase.core.schema.field.behavior.Pattern;
import br.com.enginer.domain.system.usecase.core.schema.field.behavior.Position;
import br.com.enginer.domain.system.usecase.core.schema.field.behavior.validation.Async;
import br.com.enginer.domain.system.usecase.core.schema.field.behavior.validation.Sync;
import br.com.enginer.domain.system.usecase.core.schema.field.behavior.validation.Validation;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Area;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Checkbox;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Date;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Decimal;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Email;
import br.com.enginer.domain.system.usecase.core.schema.field.type.File;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Filter;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Hidden;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Join;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Number;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Password;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Radio;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Select;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Tag;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Text;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Time;
import br.com.enginer.domain.system.usecase.core.schema.header.DropdownItem;
import br.com.enginer.domain.system.usecase.core.schema.header.Header;
import br.com.enginer.domain.system.usecase.core.schema.instance.Action;
import br.com.enginer.domain.system.usecase.core.schema.instance.ActionObject;
import br.com.enginer.domain.system.usecase.core.schema.instance.ActionResponse;
import br.com.enginer.domain.system.usecase.core.schema.instance.ActionResponseError;
import br.com.enginer.domain.system.usecase.core.schema.instance.ActionResponseSuccess;
import br.com.enginer.domain.system.usecase.core.schema.instance.ActionTrigger;
import br.com.enginer.domain.system.usecase.core.schema.instance.Button;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainId;
import br.com.enginer.domain.system.usecase.core.schema.paginator.Paginator;
import br.com.enginer.domain.system.usecase.core.schema.paginator.column.Column;
import br.com.enginer.domain.system.usecase.core.schema.paginator.config.Config;
import br.com.enginer.domain.system.usecase.core.schema.tab.Tab;
import br.com.enginer.domain.system.usecase.core.schema.validate.TypeOperatorEvaluator;
import br.com.enginer.domain.system.usecase.core.schema.validate.Validate;
import br.com.enginer.domain.system.usecase.core.schema.validate.conditional.Conditional;
import br.com.enginer.domain.system.usecase.core.schema.validate.custom.Custom;
import br.com.enginer.domain.system.usecase.core.schema.validate.dependency.Dependency;
import br.com.enginer.domain.system.usecase.core.schema.validate.global.Global;
import br.com.enginer.domain.system.usecase.core.ui.UIUseCase;
import br.com.enginer.domain.system.usecase.core.utils.ParamUtils;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.core.utils.StringsUtils;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;

/**
 * 
 */
public class FormTemplate {

	private static final Map<Class<? extends Annotation>, Class<? extends Annotation>> annotationMap = new HashMap<>();
	
	private TypeTemplate typeTemplate;
	private UIUseCase<?> useCase;
	private Domain<?> domain;

	//private static Boolean modal = Boolean.FALSE;
	//private static Boolean disabled = Boolean.FALSE;
	//private static String mainDomain;

	static {

		annotationMap.put(UIId.class, UIId.class);
		annotationMap.put(UIText.class, UIText.class);
		annotationMap.put(UIEmail.class, UIEmail.class);
		annotationMap.put(UIPassword.class, UIPassword.class);
		annotationMap.put(UINumber.class, UINumber.class);
		annotationMap.put(UIDecimal.class, UIDecimal.class);
		annotationMap.put(UICheckbox.class, UICheckbox.class);
		annotationMap.put(UIDate.class, UIDate.class);
		annotationMap.put(UITime.class, UITime.class);
		annotationMap.put(UIRadio.class, UIRadio.class);
		annotationMap.put(UISelect.class, UISelect.class);
		annotationMap.put(UITextArea.class, UITextArea.class);
		annotationMap.put(UITag.class, UITag.class);
		annotationMap.put(UIFile.class, UIFile.class);
		annotationMap.put(UIFilter.class, UIFilter.class);
		annotationMap.put(UIJoin.class, UIJoin.class);
		annotationMap.put(UIHidden.class, UIHidden.class);
		annotationMap.put(UIIgnore.class, UIIgnore.class);

		annotationMap.put(UIPosition.class, UIPosition.class);
		annotationMap.put(UIAutoComplete.class, UIAutoComplete.class);
		annotationMap.put(UIAutoCompleteSuggestion.class, UIAutoCompleteSuggestion.class);
		annotationMap.put(UIFieldValidation.class, UIFieldValidation.class);
		annotationMap.put(UIDependency.class, UIDependency.class);

	}

	/**
	 * @param domain
	 * @param uIUseCase
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Form create(Domain<?> domain_, UIUseCase<?> uIUseCase, Map<TypeTemplate, Object> map) throws Exception {
		
		List<Field> fields = new ArrayList<>();
		Field field = null;
		Form form = null;

		try {

			domain = domain_;
			useCase = uIUseCase;
			typeTemplate = (TypeTemplate) map.get(TypeTemplate.TYPE_TEMPLATE);
			
			Boolean modal = (Boolean) map.get(TypeTemplate.MODAL);
			Boolean disabled = (Boolean) map.get(TypeTemplate.DISABLED);
			String mainDomain = (String) map.get(TypeTemplate.MAIN_DOMAIN);
			
			domain.setModal(modal);
			domain.setDisabled(disabled);
			domain.setMainDomain(mainDomain);
			domain.setTypeTemplate(typeTemplate);

			Header header = getHeader();
			Tab tab = getTab();
			Paginator paginator = getPaginator();
			Validate validate = getValidate();

			form = new Form();
			form.setId(StringsUtils.firstLower(domain.getClass().getSimpleName()));
			form.setHeader(header);
			form.setValidate(validate);
			form.setFields(fields);

			form.setTab(tab);
			form.setPaginator(paginator);

			List<java.lang.reflect.Field> fs = ReflectionUtils.extractFieldsDomain(domain, false);

			int count = 1;

			for (java.lang.reflect.Field f : fs) {

				int x = count;
				int y = count % 2 == 0 ? count - 1 : count;

				Default default_ = new Default(x, y, f.getName(), domain);

				field = new Field();
				fields.add(field);

				Annotation[] annotations = f.getAnnotations();

				boolean identity = false;

				for (Annotation annotation : annotations) {

					Class<? extends Annotation> annotationType = annotation.annotationType();

					if (annotationMap.containsKey(annotationType)) {

						if (annotation instanceof UIId) {

							Hidden hidden = getHidden(f, default_, annotations);
							field.setHidden(hidden);
							hidden.setValue(domain.getId());

							identity = true;

						} else if (annotation instanceof UIHidden uiHidden) {

							boolean containsTemplate = checkTemplate(uiHidden);

							if (containsTemplate) {
								field.setHidden(getHidden(f, default_, annotations));
							}

							identity = true;

						} else if (annotation instanceof UIJoin uiJoin) {

							boolean containsTemplate = checkTemplate(uiJoin);

							if (containsTemplate) {
								field.setJoin(getJoin(domain, f, default_, annotations));
							}

							identity = true;

						} else if (annotation instanceof UIText uiText) {

							boolean containsTemplate = checkTemplate(uiText);

							if (containsTemplate) {
								field.setText(getText(f, default_, annotations));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UIEmail uiEmail) {

							boolean containsTemplate = checkTemplate(uiEmail);

							if (containsTemplate) {
								field.setEmail(getEmail(f, default_, annotations));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UIPassword uiPassword) {

							boolean containsTemplate = checkTemplate(uiPassword);

							if (containsTemplate) {
								field.setPassword(getPassword(f, default_, annotations));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UINumber uiNumber) {

							boolean containsTemplate = checkTemplate(uiNumber);

							if (containsTemplate) {
								field.setNumber(getNumber(f, default_, annotations));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UIDecimal uiDecimal) {

							boolean containsTemplate = checkTemplate(uiDecimal);

							if (containsTemplate) {
								Decimal decimal = getDecimal(f, default_, annotations);
								field.setDecimal(decimal);
								count++;
							}

							identity = true;

						} else if (annotation instanceof UICheckbox uiCheckbox) {

							boolean containsTemplate = checkTemplate(uiCheckbox);

							if (containsTemplate) {
								field.setCheckbox(getCheckbox(f, default_, annotations));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UIDate uiDate) {

							boolean containsTemplate = checkTemplate(uiDate);

							if (containsTemplate) {
								field.setDate(getDate(f, default_, annotations, uiDate.showtime()));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UITime uiTime) {

							boolean containsTemplate = checkTemplate(uiTime);

							if (containsTemplate) {
								field.setTime(getTime(f, default_, annotations));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UIRadio uiRadio) {

							boolean containsTemplate = checkTemplate(uiRadio);

							if (containsTemplate) {
								field.setRadio(getRadio(f, default_, annotations, uiRadio));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UISelect uiSelect) {

							boolean containsTemplate = checkTemplate(uiSelect);

							if (containsTemplate) {
								field.setSelect(getSelect(f, default_, annotations, uiSelect));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UITag uiTag) {

							boolean containsTemplate = checkTemplate(uiTag);

							if (containsTemplate) {
								field.setTag(getTag(f, default_, annotations));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UIFile uiFile) {

							boolean containsTemplate = checkTemplate(uiFile);

							if (containsTemplate) {
								field.setFile(getFiles(f, default_, annotations));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UITextArea uiTextArea) {

							boolean containsTemplate = checkTemplate(uiTextArea);

							if (containsTemplate) {
								field.setTextarea(getTextArea(f, default_, annotations));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UIFilter uiFilter) {

							boolean containsTemplate = checkTemplate(uiFilter);

							if (containsTemplate) {
								field.setFilter(getFilter(f, default_, annotations, uiFilter));
								count++;
							}

							identity = true;

						} else if (annotation instanceof UIIgnore) {
							identity = true;
						}
					}
				}

				if (identity)
					continue;

				if (!ReflectionUtils.isClassTypeCustom(f.getType())) {

					field.setJoin(getJoin(domain, f, default_, annotations));

					count--;

				} else if (f.getType().equals(Integer.class) || f.getType().equals(Short.class)
						|| f.getType().equals(Long.class) || f.getType().equals(Byte.class)
						|| f.getType().equals(BigInteger.class)) {

					field.setNumber(getNumber(f, default_, annotations));

				} else if (f.getType().equals(Float.class) || f.getType().equals(Double.class)
						|| f.getType().equals(BigDecimal.class)) {

					field.setDecimal(getDecimal(f, default_, annotations));

				} else if (f.getType().equals(String.class)) {

					field.setText(getText(f, default_, annotations));

				}  else if (f.getType().equals(UUID.class)) {

					field.setText(getText(f, default_, annotations));

				} else if (f.getType().equals(StringBuilder.class) || f.getType().equals(StringBuffer.class)) {

					field.setTextarea(getTextArea(f, default_, annotations));

				} else if (f.getType().equals(LocalDate.class)) {

					field.setDate(getDate(f, default_, annotations, false));

				} else if (f.getType().equals(LocalDateTime.class)) {

					field.setDate(getDate(f, default_, annotations, true));

				} else if (f.getType().equals(Boolean.class)) {

					field.setCheckbox(getCheckbox(f, default_, annotations));

				} else if (Collection.class.isAssignableFrom(f.getType())) {

					Type genericType = f.getGenericType();

					if (genericType instanceof ParameterizedType parameterizedType) {
						Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
						if (actualTypeArguments.length == 1) {
							field.setFile(getFiles(f, default_, annotations));
						}
						continue;
					}

					field.setSelect(getSelect(f, default_, annotations, null));
				}

				count++;
			}

			List<Button> buttons = getButton(domain);

			if (buttons != null) {
				buttons.forEach(button -> {
					Field fieldButton = new Field();
					fieldButton.setButton(button);
					fields.add(fieldButton);
				});
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

		return form;
	}

	/**
	 * @return
	 */
	private Tab getTab() {
		if(!typeTemplate.equals(TypeTemplate.TAB))return null;
		Tab tab = new Tab(false);  // habilitado para cada acao chama o backend
		tab.getConfig().setTabEnabled(true); // habilitado para todas as abas está abertas ao clique (tester)
		//Tab tab = new Tab(true);     // nao habilitado para cada acao chama o backend
		//tab.getConfig().setTabEnabled(false); // funcionamento normal
		return tab;
	}

	/**
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	private Paginator getPaginator() throws Exception {
		
		if(typeTemplate.equals(TypeTemplate.FORM) || typeTemplate.equals(TypeTemplate.TAB)) return null;
		
		Paginator paginator = new Paginator();
		Config config = new Config();
		Column column = new Column();
		List<Field> actions = new ArrayList<>();

		paginator.setConfig(config);
		paginator.setColumn(column);
		paginator.setActions(actions);

		if (domain.getClass().isAnnotationPresent(UIPaginator.class)) {

			TypeTemplate copyTypeTemplate = typeTemplate;

			if (typeTemplate.equals(TypeTemplate.FILTER)) {
				typeTemplate = TypeTemplate.PAGINATOR;
			}

			UIPaginator uiPaginator = domain.getClass().getAnnotation(UIPaginator.class);

			UIConfig uiConfig = uiPaginator.config();
			config.setEditableAll(uiConfig.editableAll());
			config.setExpandable(uiConfig.expandable());
			config.setMultiSelectable(domain.getModal() ? false: uiConfig.multiSelectable());

			UIButtonAction uiButtonAction = uiPaginator.actions();
			UIButton[] uiButtons = uiButtonAction.value();

			List<UIButton> uiListButtons = new ArrayList<>();

			for (Class<? extends Annotation> custom : uiButtonAction.includes()) {
				uiListButtons.add(custom.getAnnotation(UIButton.class));
			}

			uiListButtons.addAll(Arrays.asList(uiButtons));

			if (uiListButtons.size() > 0) {
				List<Button> buttons = new ArrayList<>();
				for (UIButton uiButton : uiListButtons) {
					boolean containsTemplate = checkTemplate(uiButton);
					if (containsTemplate) {
						Method[] methods = uiButton.annotationType().getDeclaredMethods();
						Button button = new Button(TypeButton.BUTTON);
						for (Method method : methods) {
							Object buttonObject = ReflectionUtils.get(method.getName(), uiButton);
							if (buttonObject instanceof UIAction) {
								button.setAction(getButtonAction(uiButton));
								continue;
							}
							if (method.getName().equals("template")) {
								continue;
							}

							if (method.getName().equalsIgnoreCase("label") && uiButton.label().equals(Constants.LABEL_DELETE)) {
								ReflectionUtils.set(button, StringsUtils.setMethod(method.getName()), new Class<?>[] { buttonObject.getClass() }, new Object[] { Constants.LABEL_ACTION_DELETE });
								continue;
							}

							ReflectionUtils.set(button, StringsUtils.setMethod(method.getName()), new Class<?>[] { buttonObject.getClass() }, new Object[] { buttonObject });
						}
						buttons.add(button);
					}
				}

				if (buttons != null) {
					buttons.forEach(button -> {
						br.com.enginer.domain.system.usecase.core.schema.field.Field fieldButton = new br.com.enginer.domain.system.usecase.core.schema.field.Field();
						fieldButton.setButton(button);
						actions.add(fieldButton);
					});
				}

			}

			typeTemplate = copyTypeTemplate;
		}

		configPaginator(paginator);

		return paginator;
	}

	/**
	 * @param domain
	 * @param paginator
	 * @throws Exception
	 */
	private void configPaginator(Paginator paginator) throws Exception {

		ParamUtils paramUtils = new ParamUtils();
		paramUtils.setTypeTemplate(typeTemplate);

		String domainName = StringsUtils.firstLower(domain.getClass().getSimpleName());

		for (java.lang.reflect.Field field_ : domain.getClass().getDeclaredFields()) {
			String name = domainName.concat(".").concat(field_.getName());
			// UIFilter
			UIFilter uiFilter = field_.getAnnotation(UIFilter.class);
			if (uiFilter != null) {
				extractFieldPaginator(paramUtils, null, field_);
				continue;
			}
			// UIJoin
			UIJoin uiJoin = field_.getAnnotation(UIJoin.class);
			if (uiJoin != null) {
				extractFieldPaginator(paramUtils, null, field_);
				continue;
			}
			// UIColumn
			UIColumn uiColumn = field_.getAnnotation(UIColumn.class);
			if (uiColumn != null) {
				if (!uiColumn.hidden()) {
					if (uiColumn.initial()) {
						paramUtils.addInitials(name);
					}
					paramUtils.addColumnNames(field_.getName(), uiColumn.label());
					paramUtils.addColumnTypes(field_.getName(), uiColumn.type().equals(TypeFormat.none) ? field_.getType().getSimpleName() : uiColumn.type().name());
					paramUtils.addColumnStyles(field_.getName(), uiColumn.style());
					paramUtils.addVisibles(name);

					UIConditional uiConditional = uiColumn.conditional();
					if (uiConditional.value().length > 0) {
						List<Conditional> conditionals = getConditionalColumn(uiConditional);
						paramUtils.addColumnConditionals(field_.getName(), conditionals);
					}
				}
			}
			// UIRow
			UIRow uiRow = field_.getAnnotation(UIRow.class);
			if (uiRow != null) {
				paramUtils.addRowsMap(new AbstractMap.SimpleEntry<>(field_.getName(), uiRow.order()));
				if (uiRow.editable()) {
					paramUtils.addEditables(field_.getName());
				}
				if (!uiRow.visible()) {
					paramUtils.addHiddens(field_.getName());
				}
				if (!uiRow.calculation().isEmpty()) {
					paramUtils.addCalculations(field_.getName().concat(" = ").concat(uiRow.calculation()));
				}
				if (uiRow.totalizer()) {
					paramUtils.addTotalizers(field_.getName(), (!uiRow.label().isEmpty() ? uiRow.label() : uiColumn.label()) + ":");
				}
			}
		}
		
		paginator.getColumn().setConditional(paramUtils.getColumnConditionals());

		paginator.getColumn().setName(paramUtils.getColumnNames());
		paginator.getColumn().setType(paramUtils.getColumnTypes());
		paginator.getColumn().setStyle(paramUtils.getColumnStyles());

		if (paramUtils.getTypeTemplate().equals(TypeTemplate.ROW)) {
			paginator.getColumn().setRows(paramUtils.getRows());
			paginator.getColumn().setEditables(paramUtils.getEditables());
			paginator.getColumn().setHiddens(paramUtils.getHiddens());
			paginator.getColumn().setCalculations(paramUtils.getCalculations());
			paginator.getColumn().setTotalizer(paramUtils.getTotalizers());
		}

		if (paramUtils.getTypeTemplate().equals(TypeTemplate.FILTER)) {
			paginator.getColumn().setInitials(paramUtils.getInitials());
			paginator.getColumn().setVisibles(paramUtils.getVisibles());
		}
	}
	
	/**
	 * @param paramUtils
	 * @param simpleName
	 * @param fieldClass
	 * @throws Exception
	 */
	private void extractFieldPaginator(ParamUtils paramUtils, String simpleName, java.lang.reflect.Field fieldClass) throws Exception {

		java.lang.reflect.Field[] declaredFields = new java.lang.reflect.Field[] {};

		Boolean initial = false;
		
		Boolean visible = false;

		String attribute = (ReflectionUtils.classIsIdType(fieldClass.getType()) && simpleName != null ? "id" : StringsUtils.firstLower(fieldClass.getType().getSimpleName()));

		simpleName = ((simpleName != null) ? simpleName.concat(".").concat(attribute) : attribute);

		if (paramUtils.getTypeTemplate().equals(TypeTemplate.ROW)) {

			UIRow uiRowFieldClass = fieldClass.getAnnotation(UIRow.class);
			if (uiRowFieldClass == null) return;
			visible = uiRowFieldClass.visible();
			declaredFields = ReflectionUtils.getFieldsByName(fieldClass.getType(), uiRowFieldClass.fields());

		} else {

			UIColumn uiColumnFieldClass = fieldClass.getAnnotation(UIColumn.class);
			if (uiColumnFieldClass == null) return;
			initial = uiColumnFieldClass.initial();
			declaredFields = ReflectionUtils.getFieldsByName(fieldClass.getType(), uiColumnFieldClass.fields());
		}

		for (java.lang.reflect.Field field : declaredFields) {
			String name = simpleName.concat(".").concat(field.getName());
			// UIFilter
			UIFilter uiFilter = field.getAnnotation(UIFilter.class);
			if (uiFilter != null) {
				extractFieldPaginator(paramUtils, simpleName, field);
				continue;
			}
			// UIJoin
			UIJoin uiJoin = field.getAnnotation(UIJoin.class);
			if (uiJoin != null) {
				extractFieldPaginator(paramUtils, simpleName, field);
				continue;
			}
			// UIColumn
			UIColumn uiColumn = field.getAnnotation(UIColumn.class);
			if (uiColumn != null) {
				if (!uiColumn.hidden()) {

					if (initial && uiColumn.initial()) {
						paramUtils.addInitials(name);
					}
					
					paramUtils.addColumnNames(name, uiColumn.label());
					paramUtils.addColumnTypes(name, uiColumn.type().equals(TypeFormat.none) ? field.getType().getSimpleName() : uiColumn.type().name());
					paramUtils.addColumnStyles(name, uiColumn.style());
					paramUtils.addVisibles(name);
					
					UIConditional uiConditional = uiColumn.conditional();
					if (uiConditional.value().length > 0) {
						List<Conditional> conditionals = getConditionalColumn(uiConditional);
						paramUtils.addColumnConditionals(name, conditionals);
					}					
				}
			}
			// UIRow
			UIRow uiRow = field.getAnnotation(UIRow.class);
			if (uiRow != null) {
				paramUtils.addRowsMap(new AbstractMap.SimpleEntry<>(name, uiRow.order()));
				if (uiRow.editable()) {
					paramUtils.addEditables(name);
				}
				if (!visible || !uiRow.visible()) {
					paramUtils.addHiddens(name);
				}
				if (!uiRow.calculation().isEmpty()) {
					paramUtils.addCalculations(name.concat(" = ").concat(uiRow.calculation()));
				}
			}
		}
	}	

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @param uiFilter
	 * @return
	 * @throws Exception
	 */
	private Filter getFilter(java.lang.reflect.Field f, Default default_, Annotation[] annotations, UIFilter uiFilter) throws Exception {

		Class<?> typeClass = f.getType();

		Filter filter = default_.getFilter(typeClass);

		Class<?> typeId = ReflectionUtils.getTypeFieldClass(typeClass, "id");

		Domain<?> attribute = (Domain<?>) ReflectionUtils.newInstance(typeClass);

		Domain<?> value = null;

		UIUseCase<?> templateUseCase = null;

		if (domain instanceof DomainId) {
			/** Essa condicao is true quando o domain é um id de uma entidade */

			if (ReflectionUtils.isIdComposedType(typeId)) {

				Domain<?> compositeKey = ReflectionUtils.setCompositeKeyByDomainId(typeClass, ((DomainId) domain));

				if (compositeKey != null) {
					templateUseCase = (UIUseCase<?>) ReflectionUtils.executeInjectedDependencyUseCaseCached(compositeKey.getClass(), useCase.getRepositoryOutboundPort());
					value = (Domain<?>) ReflectionUtils.execute(templateUseCase, UIUseCase.buscarFormPorId, compositeKey);
				}

			} else {

				Domain<?> key = (Domain<?>) ReflectionUtils.newInstance(typeClass);

				Object keyValue = ReflectionUtils.execute(domain,
						StringsUtils.getMethod("id" + typeClass.getSimpleName()));

				if (keyValue != null) {
					value = (Domain<?>) ReflectionUtils.execute(useCase.getRepositoryOutboundPort(), RepositoryOutboundPort.FIND_BY_ID, key, keyValue);
				}
			}

			filter.setValue(value);

		} else if (attribute instanceof Domain) {
			/** Essa condicao is true quando o domain nao é id de uma entidade */

			if (ReflectionUtils.isIdComposedType(typeId)) {

				Domain<?> compositeKey = (Domain<?>) ReflectionUtils.execute(domain,
						StringsUtils.getMethod(typeClass.getSimpleName()));

				if (compositeKey != null) {
					templateUseCase = (UIUseCase<?>) ReflectionUtils.executeInjectedDependencyUseCaseCached(compositeKey.getClass(), useCase.getRepositoryOutboundPort());
					value = (Domain<?>) ReflectionUtils.execute(templateUseCase,
							UIUseCase.buscarFormPorId, compositeKey);
				}

			} else {

				Domain<?> key = (Domain<?>) ReflectionUtils.execute(domain,
						StringsUtils.getMethod(typeClass.getSimpleName()));

				if (key != null) {
					templateUseCase = (UIUseCase<?>) ReflectionUtils.executeInjectedDependencyUseCaseCached(key.getClass(), useCase.getRepositoryOutboundPort());
					value = (Domain<?>) ReflectionUtils.execute(templateUseCase,
							UIUseCase.buscarFormPorId, key);
				}

			}

			filter.setValue(value);
		}

		if (uiFilter.select()) { /** Essa condicao is true é criado um combo de uma entidade */

			Map<String, Object> filters = ReflectionUtils.parseFilter(uiFilter.filter());

			Object provider = ReflectionUtils.newInstance(f.getType());

			List<?> options = (List<?>) ReflectionUtils.execute(useCase, UIUseCase.buscarFormTodos, provider, filters);

			filter.setOptions(options);

		}

		addBehaviorAnnotation(filter, f, annotations);

		return filter;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 */
	private Area getTextArea(java.lang.reflect.Field f, Default default_, Annotation[] annotations) {
		Area textarea = default_.getTextarea();
		addBehaviorAnnotation(textarea, f, annotations);
		return textarea;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private File getFiles(java.lang.reflect.Field f, Default default_, Annotation[] annotations) throws Exception {

		List<UploadFile> files = new ArrayList<>();

		if (domain.getId() != null) {
			
			Map<String, Object> filter = Map.of("domain", domain.getClass().getSimpleName(), "domainId", domain.getId().toString());
		
			files = (List<UploadFile>) ReflectionUtils.execute(this.useCase, UIUseCase.buscarTodos, new UploadFile(), filter);
		}
		
		File file = default_.getFile(files);
		addBehaviorAnnotation(file, f, annotations);
		return file;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Tag getTag(java.lang.reflect.Field f, Default default_, Annotation[] annotations) throws Exception {
		
		List<String> tags = new ArrayList<>();
		
		if (domain.getId() != null) {
			
			Object domainId = ReflectionUtils.createUriIdComposedType(domain);
			
			Map<String, Object> filter = Map.of("id.domain", domain.getClass().getSimpleName(), "id.domainId", domainId);
		
			Object object = ReflectionUtils.execute(this.useCase, UIUseCase.buscarTodos, new br.com.enginer.domain.system.dto.entity.tag.Tag(), filter);
			
			List<br.com.enginer.domain.system.dto.entity.tag.Tag> entities = (List<br.com.enginer.domain.system.dto.entity.tag.Tag>) object;
			
			for (br.com.enginer.domain.system.dto.entity.tag.Tag tag : entities) {
				tags.add(tag.getName());
			}
		}		
		
		Tag tag = default_.getTag(tags);
		addBehaviorAnnotation(tag, f, annotations);
		return tag;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @param uiSelect
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Select getSelect(java.lang.reflect.Field f, Default default_, Annotation[] annotations, UISelect uiSelect) throws Exception {

		List<Object> options = null;

		if (uiSelect != null) {
			Object provider = uiSelect.provider().getDeclaredConstructor().newInstance();
			options = (List<Object>) ReflectionUtils.execute(provider, uiSelect.method());
		} else {
			options = new ComboHelper().values();
		}

		Select select = default_.getSelect(options);

		addBehaviorAnnotation(select, f, annotations);

		return select;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @param uiRadio
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Radio getRadio(java.lang.reflect.Field f, Default default_, Annotation[] annotations, UIRadio uiRadio) throws Exception {

		Object provider = uiRadio.provider().getDeclaredConstructor().newInstance();

		List<Object> options = (List<Object>) ReflectionUtils.execute(provider, uiRadio.method());

		Radio radio = default_.getRadio(options);

		addBehaviorAnnotation(radio, f, annotations);

		return radio;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 */
	private Time getTime(java.lang.reflect.Field f, Default default_, Annotation[] annotations) {
		Time time = default_.getTime();
		addBehaviorAnnotation(time, f, annotations);
		return time;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @param showtime
	 * @return
	 */
	private Date getDate(java.lang.reflect.Field f, Default default_, Annotation[] annotations, Boolean showtime) {
		Date date = default_.getDate(showtime);
		addBehaviorAnnotation(date, f, annotations);
		return date;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 */
	private Checkbox getCheckbox(java.lang.reflect.Field f, Default default_, Annotation[] annotations) {
		Checkbox checkbox = default_.getCheckbox();
		addBehaviorAnnotation(checkbox, f, annotations);
		return checkbox;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 */
	private Number getNumber(java.lang.reflect.Field f, Default default_, Annotation[] annotations) {
		Number number = default_.getNumber();
		addBehaviorAnnotation(number, f, annotations);
		return number;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 */
	private Decimal getDecimal(java.lang.reflect.Field f, Default default_, Annotation[] annotations) {
		Decimal decimal = default_.getDecimal();
		decimal.setTypeFormat(TypeFormat.decimal);
		decimal.setPrecision(2);
		addBehaviorAnnotation(decimal, f, annotations);
		return decimal;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 */
	private Password getPassword(java.lang.reflect.Field f, Default default_, Annotation[] annotations) {
		Password password = default_.getPassword();
		addBehaviorAnnotation(password, f, annotations);
		return password;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 */
	private Email getEmail(java.lang.reflect.Field f, Default default_, Annotation[] annotations) {
		Email email = default_.getEmail();
		addBehaviorAnnotation(email, f, annotations);
		return email;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 */
	private Text getText(java.lang.reflect.Field f, Default default_, Annotation[] annotations) {
		Text text = default_.getText();
		addBehaviorAnnotation(text, f, annotations);
		return text;
	}

	/**
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 */
	private Hidden getHidden(java.lang.reflect.Field f, Default default_, Annotation[] annotations) {
		Hidden hidden = default_.getHidden();
		addBehaviorAnnotation(hidden, f, annotations);
		return hidden;
	}

	/**
	 * @param domain
	 * @param f
	 * @param default_
	 * @param annotations
	 * @return
	 */
	private Join getJoin(Domain<?> domain, java.lang.reflect.Field f, Default default_, Annotation[] annotations) {

		Object id = null;

		Join join = default_.getJoin(f.getType());

		Object object = ReflectionUtils.get(StringsUtils.getMethod(f.getType().getSimpleName()), domain);

		if (object != null) {
			id = ReflectionUtils.get(StringsUtils.getMethod("id"), object);
		}

		join.setValue(id);

		addBehaviorAnnotation(join, f, annotations);

		return join;
	}

	/**
	 * @param base
	 * @param field
	 * @param annotations
	 */
	private void addBehaviorAnnotation(Base base, java.lang.reflect.Field field, Annotation[] annotations) {

		for (Annotation annotation : annotations) {

			Class<? extends Annotation> annotationType = annotation.annotationType();

			if (annotationMap.containsKey(annotationType)) {

				if (annotation instanceof UIAutoComplete uiAutoComplete) {

					Autocomplete autocomplete = new Autocomplete();
					autocomplete.setDomain(uiAutoComplete.domain());
					autocomplete.setAttribute(uiAutoComplete.attribute());
					base.setAutocomplete(autocomplete);

				} else if (annotation instanceof UIAutoCompleteSuggestion uiAutoCompleteSuggestion) {

					Autocomplete autocomplete = new Autocomplete();
					autocomplete.setSuggestions(uiAutoCompleteSuggestion.suggestions());
					base.setAutocomplete(autocomplete);

				} else if (annotation instanceof UIPosition uiPosition) {

					Position position = new Position(uiPosition.x(), uiPosition.y());
					base.setPosition(position);

				} else if (annotation instanceof UIFieldValidation uiValidation) {

					UIPattern uiPattern = uiValidation.pattern();
					UIAsync uiAsync = uiValidation.async();
					UISync uiSync = uiValidation.sync();

					Pattern pattern = new Pattern(uiPattern.pattern(), uiPattern.patternError());
					Async async = new Async(uiAsync.asyncFunc(), uiAsync.asyncError(), uiAsync.method());
					Sync sync = new Sync(uiSync.syncFunc(), uiSync.syncError());

					Validation validation = createValidationIfNotNull(pattern, async, sync);

					boolean containsTemplate = checkTemplate(uiValidation);

					if (containsTemplate) {
						base.setRequired(uiValidation.required());
					} else {
						base.setRequired(!uiValidation.required());
					}

					base.setValidation(validation);

				} else {

					Method[] methods = annotation.annotationType().getDeclaredMethods();
					for (Method method : methods) {
						if (method.getName().equals("template") || method.getName().equals("disabled")) {
							boolean containsTemplate = checkTemplate(annotation);
							if (containsTemplate) {
								continue;
							}
						}

						Object object = ReflectionUtils.get(method.getName(), annotation);

						ReflectionUtils.set(base, StringsUtils.setMethod(method.getName()), new Class<?>[] { object.getClass() }, new Object[] { object });
					}

				}
			}
		}

	}

	/**
	 * @param pattern
	 * @param async
	 * @param sync
	 * @return
	 */
	private Validation createValidationIfNotNull(Pattern pattern, Async async, Sync sync) {
		if (pattern.getRegex() != null || async.getFunction() != null || sync.getFunctions() != null) {
			Validation validation = new Validation();
			validation.setPattern(pattern.getRegex() != null ? pattern : null);
			validation.setAsync(async.getFunction() != null ? async : null);
			validation.setSync(sync.getFunctions() != null ? sync : null);
			return validation;
		}
		return null;
	}

	/**
	 * @param annotation
	 * @return
	 */
	private boolean checkTemplate(Annotation annotation) {
		TypeTemplate[] type = (TypeTemplate[]) ReflectionUtils.get("template", annotation);
		boolean containsFilter = Arrays.stream(type).anyMatch(t -> t == typeTemplate);
		return containsFilter;
	}

	/**
	 * @param domain
	 * @return
	 */
	private Header getHeader() throws Exception {
		
		Header header = null;
		
		String title = StringsUtils.normalizeLabelToLowercaseCamelization(domain.getClass().getSimpleName().toString());
		
		if (domain.getClass().isAnnotationPresent(UIHeader.class)) {
		
			UIHeader uiHeader = domain.getClass().getAnnotation(UIHeader.class);
			
			boolean containsTemplate = checkTemplate(uiHeader);

			if (containsTemplate) {
				
				header = new Header();

				title = uiHeader.title();

				header.setTitle(title);

				UIConditional conditional = uiHeader.conditional();
				UIConditionalOn[] uiConditionalOns = conditional.value();

				for (UIConditionalOn uiConditionalOn : uiConditionalOns) {

					Object object = ReflectionUtils.execute(domain, StringsUtils.getMethod(uiConditionalOn.field()));

					TypeOperator operator = uiConditionalOn.operator();

					List<String> matchs = List.of(uiConditionalOn.matchs());

					boolean ok = TypeOperatorEvaluator.test(object, operator, matchs);

				}

				List<DropdownItem> dropdownItens = List.of(new DropdownItem("api", "code", "API", null),
						new DropdownItem("files", "upload", "Anexo", "files"),
						new DropdownItem("tags", "tag", "Tag", "tags"),
						new DropdownItem("timeline", "history", "Histórico", null),
						new DropdownItem("calendar", "calendar", "Calendar", null),
						new DropdownItem("help", "help", "Ajuda", null));

				header.setDropdownItens(dropdownItens);

			}
		}
				
		return header;
	}

	/**
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	private List<Button> getButton(Domain<?> domain) throws Exception {

		List<Button> buttons = null;

		List<UIButton> uiListButtons = new ArrayList<>();

		if (domain.getClass().isAnnotationPresent(UIButtonAction.class)) {

			UIButtonAction uiButtonAction = domain.getClass().getAnnotation(UIButtonAction.class);
			UIButton[] uiButtons = domain.getClass().getAnnotationsByType(UIButton.class);

			uiListButtons.addAll(Arrays.asList(uiButtons));

			for (Class<? extends Annotation> custom : uiButtonAction.includes()) {
				uiListButtons.add(custom.getAnnotation(UIButton.class));
			}


			if (uiListButtons.size() > 0) {

				buttons = new ArrayList<>();

				outer:
				for (UIButton uiButton : uiListButtons) {
					
					UIConditional conditional = uiButton.conditional();
					UIConditionalOn[] uiConditionalOns = conditional.value();

					for (UIConditionalOn uiConditionalOn : uiConditionalOns) {
						
						Object object = ReflectionUtils.execute(domain, StringsUtils.getMethod(uiConditionalOn.field()));
						
						TypeOperator operator = uiConditionalOn.operator();
				        
						List<String> matchs = List.of(uiConditionalOn.matchs());
						
						boolean ok = TypeOperatorEvaluator.test(object, operator, matchs);
						
						if (ok) continue;
						
						continue outer;
					}

					boolean containsTemplate = checkTemplate(uiButton);

					if (containsTemplate) {

						Method[] methods = uiButton.annotationType().getDeclaredMethods();

						Button button = new Button(TypeButton.BUTTON);

						for (Method method : methods) {

							Object buttonObject = ReflectionUtils.get(method.getName(), uiButton);

							if (buttonObject instanceof UIAction) {
								if (uiButton.label().equals(Constants.LABEL_NEW) && domain.getModal()) {
									Action action = getButtonAction(uiButton);
									action.setClientMethod(Constants.METHOD_OPEN_MODAL_CREATE);
									action.setRedirect(null);
									button.setAction(action);
									continue;
								}
								button.setAction(getButtonAction(uiButton));
								continue;
							}

							if (method.getName().equals("template"))
								continue;

							ReflectionUtils.set(button, StringsUtils.setMethod(method.getName()), new Class<?>[] { buttonObject.getClass() }, new Object[] { buttonObject });
						}
						buttons.add(button);
					}
				}

			}
		}
		return buttons;
	}

	/**
	 * @param uiButton
	 * @return
	 */
	private Action getButtonAction(UIButton uiButton) {
		boolean containsTemplate = false;
		UIAction uiAction = uiButton.action();
		Action action = new Action();
		ActionTrigger actionTrigger = new ActionTrigger();
		if (uiAction.method() instanceof UIActionMethod uiActionMethod) {
			containsTemplate = checkTemplate(uiActionMethod);
			if (containsTemplate) {
				action.setClientMethod(ReflectionUtils.nullIfEmpty(uiActionMethod.clientMethod()));
				action.setServerMethod(ReflectionUtils.nullIfEmpty(uiActionMethod.serverMethod()));
				action.setNeedsValidation(ReflectionUtils.nullIfEmpty(uiButton.needsValidation()));
				actionTrigger.setClientMethod(ReflectionUtils.nullIfEmpty(uiActionMethod.trigger().clientMethod()));
				actionTrigger.setServerMethod(ReflectionUtils.nullIfEmpty(uiActionMethod.trigger().serverMethod()));
				actionTrigger = ReflectionUtils.hasNonNullField(actionTrigger) ? actionTrigger : null;
				action.setTriggerMethod(actionTrigger);
			}
		}

		if (uiAction.redirect() instanceof UIActionRedirect uiActionRedirect) {
			containsTemplate = checkTemplate(uiActionRedirect);
			if (containsTemplate) {
				action.setUi(ReflectionUtils.nullIfEmpty(uiActionRedirect.ui()));
				action.setDomain(ReflectionUtils.nullIfEmpty(uiActionRedirect.domain()));
				action.setParam(ReflectionUtils.nullIfEmpty(uiActionRedirect.param()));
				action.setRedirect(ReflectionUtils.nullIfEmpty(uiActionRedirect.value()));
				action.setNeedsValidation(ReflectionUtils.nullIfEmpty(uiButton.needsValidation()));
			}
		}

		if (uiAction.actionObject() instanceof UIActionDomain uiActionDomain) {
			containsTemplate = checkTemplate(uiActionDomain);
			if (containsTemplate) {
				ActionObject actionObject = new ActionObject();
				actionObject.setObject(ReflectionUtils.nullIfEmpty(uiActionDomain.object()));
				actionObject.setParam(ReflectionUtils.nullIfEmpty(uiActionDomain.param()));
				actionObject = ReflectionUtils.hasNonNullField(actionObject) ? actionObject : null;
				action.setActionObject(actionObject);
			}
		}

		if (uiAction.response() instanceof UIActionResponse uiActionResponse) {
			containsTemplate = checkTemplate(uiActionResponse);
			if (containsTemplate) {
				UIActionResponseSuccess uiActionResponseSuccess = uiActionResponse.success();
				UIActionResponseError uiActionResponseError = uiActionResponse.error();

				ActionResponse response = new ActionResponse();
				ActionResponseSuccess success = new ActionResponseSuccess();
				ActionResponseError error = new ActionResponseError();

				if (uiActionResponseSuccess.method() instanceof UIActionMethod uiActionMethod) {
					containsTemplate = checkTemplate(uiActionResponseSuccess);
					if (containsTemplate) {
						success.setClientMethod(ReflectionUtils.nullIfEmpty(uiActionMethod.clientMethod()));
						success.setServerMethod(ReflectionUtils.nullIfEmpty(uiActionMethod.serverMethod()));
					}
				}

				if (uiActionResponseSuccess.redirect() instanceof UIActionRedirect uiActionRedirect) {
					containsTemplate = checkTemplate(uiActionResponseSuccess);
					if (containsTemplate) {
						success.setUi(ReflectionUtils.nullIfEmpty(uiActionResponseSuccess.redirect().ui()));
						success.setDomain(ReflectionUtils.nullIfEmpty(uiActionResponseSuccess.redirect().domain()));
						success.setParam(ReflectionUtils.nullIfEmpty(uiActionResponseSuccess.redirect().param()));
						success.setRedirect(ReflectionUtils.nullIfEmpty(uiActionRedirect.value()));
					}
				}

				
				if (uiActionResponseError.method() instanceof UIActionMethod uiActionMethod) {
					containsTemplate = checkTemplate(uiActionResponseError);
					if (containsTemplate) {
						error.setClientMethod(ReflectionUtils.nullIfEmpty(uiActionMethod.clientMethod()));
						error.setServerMethod(ReflectionUtils.nullIfEmpty(uiActionMethod.serverMethod()));
					}
				}

				if (uiActionResponseError.redirect() instanceof UIActionRedirect uiActionRedirect) {
					containsTemplate = checkTemplate(uiActionResponseError);
					if (containsTemplate) {
						error.setRedirect(ReflectionUtils.nullIfEmpty(uiActionRedirect.value()));
						error.setUi(ReflectionUtils.nullIfEmpty(uiActionResponseError.redirect().ui()));
						error.setParam(ReflectionUtils.nullIfEmpty(uiActionResponseError.redirect().param()));
					}
				}
				
				success = ReflectionUtils.hasNonNullField(success, "ui") ? success : null;
				error = ReflectionUtils.hasNonNullField(error, "ui") ? error : null;

				response.setSuccess(success);
				response.setError(error);
				
				response = ReflectionUtils.hasNonNullField(response) ? response : null;

				action.setResponse(response);

			}
		}

		return action;
	}

	/**
	 * @return
	 */
	private Validate getValidate() {
		Validate validate = null;
		if (domain.getClass().isAnnotationPresent(UIValidate.class)) {
			validate = new Validate();
			UIValidate uiValidate = domain.getClass().getAnnotation(UIValidate.class);
			boolean containsTemplate = checkTemplate(uiValidate);
			if (containsTemplate) {
				validate.setGlobal(getGlobal(domain, uiValidate.global()));
				validate.setCustom(getCustom(domain, uiValidate.custom()));
				validate.setConditional(getConditional(domain, uiValidate.conditional()));
				validate.setDependency(getDependecy(domain, uiValidate.dependency()));
				if (validate.getGlobal().size() == 0 && validate.getCustom().size() == 0
						&& validate.getConditional().size() == 0 && validate.getDependency().size() == 0) {
					validate = null;
				}
			}
		}
		return validate;
	}
	
	/**
	 * @param uiConditional
	 * @return
	 */
	private List<Conditional> getConditionalColumn(UIConditional uiConditional) {
		List<Conditional> conditionals = new ArrayList<>();
		boolean containsTemplate = checkTemplate(uiConditional);
		if(containsTemplate) {
			UIConditionalOn[] uiConditionalOns = uiConditional.value();
			Conditional conditional = null;
			for (UIConditionalOn uiConditionalOn : uiConditionalOns) {
				containsTemplate = checkTemplate(uiConditionalOn);
				if (containsTemplate) {
					conditional = new Conditional();
					conditional.setLabel(uiConditionalOn.label());
					conditional.setField(uiConditionalOn.field());
					conditional.setOperator(uiConditionalOn.operator());
					conditional.setMatchs(uiConditionalOn.matchs());
					conditional.setValue(uiConditionalOn.value());
					conditionals.add(conditional);
				}
			}
		}
		return conditionals;
	}

	/**
	 * @param domain
	 * @param uiDependency
	 * @return
	 */
	private List<Dependency> getDependecy(Domain<?> domain, UIDependency uiDependency) {
		List<Dependency> dependencys = new ArrayList<>();
		boolean containsTemplate = checkTemplate(uiDependency);
		if (containsTemplate) {
			UIDependencyOn[] uiDependencyOns = uiDependency.value();
			Dependency dependency = null;
			for (UIDependencyOn dependencyOn : uiDependencyOns) {
				containsTemplate = checkTemplate(dependencyOn);
				if (containsTemplate) {
					dependency = new Dependency();
					dependency.setLabel(dependencyOn.label());
					dependency.setField(dependencyOn.field());
					dependency.setDepends(dependencyOn.depends());
					dependencys.add(dependency);
				}
			}
		}
		return dependencys;
	}

	/**
	 * @param domain
	 * @param uiConditional
	 * @return
	 */
	private List<Conditional> getConditional(Domain<?> domain, UIConditional uiConditional) {
		List<Conditional> conditionals = new ArrayList<>();
		boolean containsTemplate = checkTemplate(uiConditional);
		if(containsTemplate) {
			UIConditionalOn[] uiConditionalOns = uiConditional.value();
			Conditional conditional = null;
			for (UIConditionalOn uiConditionalOn : uiConditionalOns) {
				containsTemplate = checkTemplate(uiConditionalOn);
				if (containsTemplate) {
					conditional = new Conditional();
					conditional.setLabel(uiConditionalOn.label());
					conditional.setField(uiConditionalOn.field());
					conditional.setOperator(uiConditionalOn.operator());
					conditional.setMatchs(uiConditionalOn.matchs());
					conditional.setValue(uiConditionalOn.value());
					conditionals.add(conditional);
				}
			}
		}
		return conditionals;
	}

	/**
	 * @param domain
	 * @param uiCustom
	 * @return
	 */
	private List<Custom> getCustom(Domain<?> domain, UICustom uiCustom) {
		List<Custom> custons = new ArrayList<>();
		boolean containsTemplate = checkTemplate(uiCustom);
		if(containsTemplate) {
			UICustomOn[] uiCustomOns = uiCustom.value();
			Custom custom = null;
			for (UICustomOn uiCustomOn : uiCustomOns) {
				containsTemplate = checkTemplate(uiCustomOn);
				if (containsTemplate) {
					custom = new Custom();
					custom.setFunction(uiCustomOn.function());
					custom.setMessage(uiCustomOn.message());
					custom.setFields(uiCustomOn.fields());
					custons.add(custom);
				}
			}
		}
		return custons;
	}

	/**
	 * @param domain
	 * @param uiGlobal
	 * @return
	 */
	private List<Global> getGlobal(Domain<?> domain, UIGlobal uiGlobal) {
		List<Global> globals = new ArrayList<>();
		boolean containsTemplate = checkTemplate(uiGlobal);
		if (containsTemplate) {
			UIGlobalOn[] uiGlobalOns = uiGlobal.value();
			Global global = null;
			for (UIGlobalOn uiGlobalOn : uiGlobalOns) {
				containsTemplate = checkTemplate(uiGlobalOn);
				if (containsTemplate) {
					global = new Global();
					global.setFunction(uiGlobalOn.function());
					global.setMessage(uiGlobalOn.message());
					globals.add(global);
				}
			}
		}
		return globals;
	}
}
