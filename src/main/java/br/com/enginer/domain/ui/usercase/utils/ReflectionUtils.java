package br.com.enginer.domain.ui.usercase.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.enginer.domain.OutboundPort;
import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.ui.usercase.annotation.field.UIJoin;
import br.com.enginer.domain.ui.usercase.annotation.field.UIRow;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;
import br.com.enginer.domain.ui.usercase.schema.field.type.Id;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainId;

public class ReflectionUtils {

	/**
	 * @param object
	 * @param recursive
	 * @return
	 */
	public static List<Field> extractFieldsDomain(Object object, boolean recursive) {
		List<Field> fields = new ArrayList<>();
		if (recursive) {
			Set<Class<?>> visited = new HashSet<>();
			extractFieldsRecursively(object.getClass(), Object.class, visited, fields);
		} else {
			extractFields(object.getClass(), Object.class, fields, ".*");
			// extractFieldsWithClassAbstract(object.getClass(), Object.class, fields,
			// ".*");
		}
		return fields;
	}

	/**
	 * Retorna uma lista de Fields de uma classe com base nos nomes informados.
	 *
	 * @param clazz      Classe a ser inspecionada
	 * @param fieldNames Array com os nomes dos campos
	 * @return Lista de campos refletidos
	 * @throws NoSuchFieldException se algum campo não existir na classe
	 */
	public static Field[] getFieldsByName(Class<?> clazz, String[] fieldNames) throws Exception {
		List<Field> fields = new ArrayList<>();
		for (String name : fieldNames) {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			fields.add(field);
		}
		return fields.toArray(new Field[0]);
	}

	/**
	 * @param paramUtils
	 * @param simpleName
	 * @param fieldClass
	 * @throws Exception
	 */
	public static void extractFieldPaginator(ParamUtils paramUtils, String simpleName, Field fieldClass) throws Exception {

		Field[] declaredFields = new Field[] {};

		Boolean initial = false;
		
		Boolean visible = false;

		String attribute = (classIsIdType(fieldClass.getType()) && simpleName != null ? "id" : StringsUtils.firstLower(fieldClass.getType().getSimpleName()));

		simpleName = ((simpleName != null) ? simpleName.concat(".").concat(attribute) : attribute);

		if (paramUtils.getTypeTemplate().equals(TypeTemplate.ROW)) {

			UIRow uiRowFieldClass = fieldClass.getAnnotation(UIRow.class);
			if (uiRowFieldClass == null) return;
			visible = uiRowFieldClass.visible();
			declaredFields = getFieldsByName(fieldClass.getType(), uiRowFieldClass.fields());

		} else {

			UIColumn uiColumnFieldClass = fieldClass.getAnnotation(UIColumn.class);
			if (uiColumnFieldClass == null) return;
			initial = uiColumnFieldClass.initial();
			declaredFields = getFieldsByName(fieldClass.getType(), uiColumnFieldClass.fields());
		}

		for (Field field : declaredFields) {
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
					paramUtils.addColumnTypes(name, field.getType().getSimpleName());
					paramUtils.addVisibles(name);
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
	 * @param clazz
	 * @param classLimit
	 * @param visited
	 * @param pattern
	 */
	private static void extractFields(Class<?> clazz, Class<?> classLimit, List<Field> visited, String pattern) {
		if (clazz != null && !clazz.equals(classLimit)) {
			for (Field field : clazz.getDeclaredFields()) {
				if (!visited.contains(field) && field.getName().matches(pattern)) {
					visited.add(field);
				}
			}
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Modifier.isAbstract(superClass.getModifiers())) {
				extractFields(superClass, classLimit, visited, pattern);
			}
		}
	}

	/**
	 * @param clazz
	 * @param classLimit
	 * @param visited
	 * @param pattern
	 */
	@SuppressWarnings("unused")
	private static void extractFieldsWithClassAbstract(Class<?> clazz, Class<?> classLimit, List<Field> visited,
			String pattern) {
		if (clazz != null && !clazz.equals(classLimit)) {
			for (Field field : clazz.getDeclaredFields()) {
				if (!visited.contains(field) && field.getName().matches(pattern)) {
					visited.add(field);
				}
			}
			Class<?> superClass = clazz.getSuperclass();
			extractFields(superClass, classLimit, visited, pattern);
		}
	}

	/**
	 * @param clazz
	 * @param classLimit
	 * @param visited
	 * @param result
	 */
	private static void extractFieldsRecursively(Class<?> clazz, Class<?> classLimit, Set<Class<?>> visited,
			List<Field> result) {

		if (clazz == null || clazz.equals(classLimit) || visited.contains(clazz))
			return;

		visited.add(clazz);

		for (Field field : clazz.getDeclaredFields()) {
			Class<?> fieldType = field.getType();
			if (isTypeId(fieldType)) {
				result.add(field);
			} else {
				result.add(field);
				extractFieldsRecursively(fieldType, classLimit, visited, result);
			}
		}
		extractFieldsRecursively(clazz.getSuperclass(), classLimit, visited, result);

	}

	/**
	 * @param clazz
	 * @return
	 */
	public static boolean isTypeId(Class<?> clazz) {
		return clazz.isPrimitive() || clazz.getName().startsWith("java.lang") || clazz.equals(UUID.class);
	}

	/**
	 * @param clazz
	 * @return
	 */
	public static boolean isClassTypeCustom(Class<?> clazz) {
		return (!clazz.equals(LocalDate.class) || !clazz.equals(LocalDateTime.class)) || !clazz.equals(Id.class)
				|| !classIsIdType(clazz);
	}

	/**
	 * @param clazz
	 * @return
	 */
	public static boolean isIdComposedType(Class<?> clazz) {
		return (!clazz.isPrimitive() && !clazz.getName().startsWith("java.lang") && !clazz.equals(LocalDate.class)
				&& !clazz.equals(LocalDateTime.class)) && classIsIdType(clazz);
	}

	/**
	 * @param clazz
	 * @return
	 */
	public static Boolean classIsIdType(Class<?> clazz) {
		return clazz.getSimpleName().endsWith("Id");
	}

	/**
	 * @param className
	 * @return
	 */
	public static Boolean classIsIdType(String className) {
		return className.endsWith("Id");
	}

	/**
	 * @param input
	 * @return
	 */
	public static Map<String, Object> normalizeIdFieldNames(Map<String, Object> input) {

		Map<String, Object> result = new LinkedHashMap<>();

		for (Map.Entry<String, Object> entry : input.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			// Renomeia campo terminado com "Id" para "id"
			String newKey = key.endsWith("Id") && key.length() > 2 ? "id" : key;

			if (value instanceof Map) {
				// Recurse para valores aninhados
				@SuppressWarnings("unchecked")
				Map<String, Object> nested = (Map<String, Object>) value;
				result.put(newKey, normalizeIdFieldNames(nested));
			} else {
				result.put(newKey, value);
			}
		}

		return result;
	}

	/**
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getCompositedKeyFields(Object entity) throws Exception {

		Map<String, Object> keyFields = new LinkedHashMap<>();

		Object embeddedId = get(StringsUtils.getMethod("id"), entity);

		if (embeddedId == null) {
			return keyFields;
		}

		Class<?> embeddedIdClass = embeddedId.getClass();
		for (Field field : embeddedIdClass.getDeclaredFields()) {
			field.setAccessible(true);
			Object value = field.get(embeddedId);
			keyFields.put(field.getName(), value);
		}

		return keyFields;
	}

	/**
	 * CONVERTE array[] em HashMap
	 */

	/**
	 * @param filterArray
	 * @return
	 */
	public static Map<String, Object> parseFilter(String[] filterArray) {
		Map<String, Object> map = new HashMap<>();
		for (String entry : filterArray) {
			String[] parts = entry.split("=", 2);
			if (parts.length == 2) {
				String key = parts[0].trim();
				String rawValue = parts[1].trim();
				// Conversão simples de tipo
				Object value;
				if ("true".equalsIgnoreCase(rawValue) || "false".equalsIgnoreCase(rawValue)) {
					value = Boolean.valueOf(rawValue);
				} else {
					try {
						value = Integer.valueOf(rawValue);
					} catch (NumberFormatException e) {
						value = rawValue;
					}
				}
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * CONVERTE ID<T>
	 */

	/**
	 * @param <T>
	 * @param inputId
	 * @param expectedType
	 * @return
	 */
	public static <T> Id<T> convertIdToExpectedType(Id<?> inputId, Class<T> expectedType) {

		Object rawValue = inputId.getValue();

		if (rawValue == null) {
			return Id.of(null);
		}

		if (expectedType.isInstance(rawValue)) {
			return Id.of(expectedType.cast(rawValue));
		}

		// Conversão comum
		try {

			if (expectedType.equals(Long.class)) {
				return Id.of(expectedType.cast(Long.valueOf(rawValue.toString())));
			} else if (expectedType.equals(Integer.class)) {
				return Id.of(expectedType.cast(Integer.valueOf(rawValue.toString())));
			} else if (expectedType.equals(UUID.class)) {
				return Id.of(expectedType.cast(UUID.fromString(rawValue.toString())));
			} else if (expectedType.equals(String.class)) {
				return Id.of(expectedType.cast(rawValue.toString()));
			}

		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Falha ao converter Id para tipo esperado: " + expectedType.getSimpleName(), e);
		}

		return Id.of(expectedType.cast(rawValue));
	}

	/**
	 * SET REFLECTION
	 */
	public static void set(Object object, String methodName, Class<?>[] paramClass, Object[] paramValue) {
		try {
			Method method = object.getClass().getMethod(methodName, paramClass);
			if (method != null) {
				method.invoke(object, paramValue);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param newClassName
	 * @return
	 */
	public static Object newInstance(String newClassName) {
		Object instance = null;
		try {
			Class<?> instanceClass = Class.forName(newClassName);
			instance = instanceClass.getDeclaredConstructor().newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Erro ao instanciar classe: " + newClassName, ex);
		}

		return instance;
	}

	/**
	 * @param newClassName
	 * @return
	 */
	public static Object newInstance(Class<?> instanceClass) {
		try {
			Constructor<?> constructor = instanceClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException("Erro ao instanciar classe: " + instanceClass.getSimpleName(), ex);
		}
	}

	/**
	 * EXECUTE METHOD REFLECTION USERCASE
	 */

	/**
	 * Metodo responsavel por encontrar a classe UserCase do domain e injetar as
	 * dependencias necessarias
	 * 
	 * @param clazz                  - Classe domain
	 * @param methodName             - Metodo a ser executado
	 * @param repositoryOutboundPort - Repository: acesso a banco de dados
	 * @param publisherOutboundPort  - Publisher: producer de uma mensageria
	 * @param subscriberInboundPort  - Subscriber: consumer de uma mensageria
	 * @return - Retorna a classe instaciada do UserCase
	 * @throws Exception
	 */
	public static Object executeInjectedDependencyUserCase(Class<?> clazz, OutboundPort... outboundPorts)
			throws Exception {

		Object newInstanceUserCase = createUserCase(clazz);

		for (OutboundPort outboundPort : outboundPorts) {
			Class<?>[] interfaces = outboundPort.getClass().getInterfaces();
			executeMethod(newInstanceUserCase, StringsUtils.setMethod(interfaces[0].getSimpleName()), outboundPort);
		}

		return newInstanceUserCase;
	}

	/**
	 * Metodo responsavel por criar uma classe UserCase
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static Object createUserCase(Class<?> clazz) throws Exception {
		return newInstance(StringsUtils.convertDtoToUsercasePackage(clazz));
	}

	/**
	 * GET REFLECTION
	 */
	public static Object get(String methodName, Object object) {
		Method method = getMethod(object.getClass(), methodName);
		if (method != null) {
			try {
				return method.invoke(object);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * EXECUTE METHOD REFLECTION
	 */

	/**
	 * @param object
	 * @param methodName
	 * @param paramValue
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Object executeMethod(Object object, String methodName, Object... paramValue) throws Exception {
		Class[] paramTypes = transformParametersTypes(paramValue);
		Method method = getMethod(object.getClass(), methodName, paramTypes);
		if (method != null) {
			return method.invoke(object, paramValue);
		}
		return null;
	}

	/**
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private static Class<?>[] transformParametersTypes(Object... params) throws Exception {

		Class<?>[] paramTypes = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			paramTypes[i] = getParameterType(params[i]);
		}

		return paramTypes;
	}

	/**
	 * @param param
	 * @return
	 */
	private static Class<?> getParameterType(Object param) {
		if (param instanceof Map) {
			return Map.class;
		}
		if (param instanceof List) {
			return List.class;
		}
		if (param instanceof String[]) {
			return String[].class;
		}
		if (param != null && param.getClass().isArray()) {
			return param.getClass();
		}
		return param.getClass();
	}

	/**
	 * @param <T>
	 * @param clazz
	 * @param methodName
	 * @param paramClass
	 * @return
	 */
	@SafeVarargs
	private static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
		Method m = null;
		try {
			m = clazz.getDeclaredMethod(methodName, paramTypes);
		} catch (Exception e) {
			try {
				m = clazz.getMethod(methodName, paramTypes);
			} catch (Exception e1) {
				for (Method mtmp : clazz.getMethods()) {
					Class<?>[] methodParams = mtmp.getParameterTypes();
					if (mtmp.getName().equals(methodName) && methodParams.length == paramTypes.length) {
						m = mtmp;
						break;
					}
				}
			}
		}
		return m;
	}

	/**
	 * @param query
	 * @return
	 */
	public static Map<String, Object> parseQueryParams(String query) {
		Map<String, Object> result = new LinkedHashMap<>();

		String[] pairs = query.split("&");
		for (String pair : pairs) {
			String[] keyValue = pair.split("=", 2);
			if (keyValue.length == 2) {
				result.put(keyValue[0], keyValue[1]);
			}
		}

		return result;
	}

	/**
	 * @param type
	 * @param value
	 * @return
	 */
	public static Object extractedTypeValue(Class<?> type, Object value) {

		if (type.equals(Integer.class)) {
			return Integer.parseInt(StringsUtils.trim(value));
		} else if (type.equals(Short.class)) {
			return Short.parseShort(StringsUtils.trim(value));
		} else if (type.equals(Long.class)) {
			return Long.parseLong(StringsUtils.trim(value));
		} else if (type.equals(Double.class)) {
			return Double.parseDouble(StringsUtils.trim(value));
		} else if (type.equals(Float.class)) {
			return Float.parseFloat(StringsUtils.trim(value));
		} else if (type.equals(BigDecimal.class)) {
			return new BigDecimal(StringsUtils.trim(value));
		} else if (type.equals(List.class)) {
			return StringsUtils.toList(value);
		} else if (type.equals(Map.class)) {
			return StringsUtils.toMap(value);
		} else if (type.equals(LocalDate.class)) {
			return StringsUtils.toLocalDate(value);
		} else if (type.equals(LocalDateTime.class)) {
			return StringsUtils.toLocalDateTime(value);
		} else if (type.equals(Byte.class)) {
			return Byte.parseByte(StringsUtils.trim(value));
		} else if (type.equals(BigInteger.class)) {
			return new BigInteger(StringsUtils.trim(value));
		} else if (type.equals(UUID.class)) {
			return UUID.fromString(StringsUtils.trim(value));
		} else if (type.equals(String.class)) {
			return StringsUtils.trim(value);
		} else if (type.equals(value.getClass())) {
			if (type.isInstance(value)) {
				return value;
			}
		}
		throw new IllegalArgumentException("Tipo de ID não suportado: " + type);
	}

	/**
	 * @param clazz
	 * @param field
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static Boolean isTypeMatching(Class<?> clazz, String field, Object value) throws Exception {

		Boolean isMatch = Boolean.TRUE;
		Field idField = clazz.getDeclaredField(field);
		Class<?> type = idField.getType();

		try {

			if (value == null) {
				return Boolean.FALSE;
			}

			extractedTypeValue(type, value);

		} catch (IllegalArgumentException ex) {
			isMatch = Boolean.FALSE;
		}

		return isMatch;
	}

	/**
	 * Metodo responsavel por trazer a TYPE('Class') do FIELD informado da clazz
	 * 
	 * @param clazz - Classe que deseja sabe o type do field
	 * @param field - Campo da classe que deseja saber o seu TYPE
	 * @return - CLASS<?>(TYPE)
	 * @throws Exception
	 */
	public static Class<?> getTypeFieldClass(Class<?> clazz, String field) throws Exception {
		Field idField = clazz.getDeclaredField(field);
		Class<?> type = idField.getType();
		return type;
	}

	/**
	 * @param query
	 * @param domain
	 */
	public static void extractKeyCompositedByQueryParameter(String query, Domain<?> domain) {

		Map<String, Object> ids = ReflectionUtils.parseQueryParams(query);

		ids.forEach((k, v) -> {

			try {

				String key = k.substring(k.lastIndexOf(".") + 1);

				Field field = domain.getClass().getDeclaredField(key);

				Class<?> type = field.getType();

				Object value = ReflectionUtils.extractedTypeValue(type, v);

				ReflectionUtils.set(domain, StringsUtils.setMethod(key), new Class<?>[] { type },
						new Object[] { value });

			} catch (NoSuchFieldException | SecurityException ex) {
				ex.printStackTrace();
			}
		});
	}

	/**
	 * @param rawId
	 * @param clazz
	 * @param domain
	 * @throws Exception
	 */
	public static void extractKeyByQueryParameter(String rawId, Class<?> clazz, Domain<?> domain) throws Exception {

		String fieldName = "id";

		Field field = clazz.getDeclaredField(fieldName);

		Class<?> type = field.getType();

		if (DomainId.class.isAssignableFrom(type)) {

			Domain<?> domainId = (Domain<?>) type.getDeclaredConstructor().newInstance();

			ReflectionUtils.extractKeyCompositedByQueryParameter(rawId, domainId);

			ReflectionUtils.executeMethod(domain, StringsUtils.setMethod(fieldName), domainId);

		} else if (ReflectionUtils.isTypeMatching(domain.getClass(), fieldName, rawId)) {

			Object id = ReflectionUtils.extractedTypeValue(type, rawId);

			ReflectionUtils.executeMethod(domain, StringsUtils.setMethod(fieldName), id);

		}
	}

	/**
	 * @param clazzDomain
	 * @param domainId
	 * @return
	 * @throws Exception
	 */
	public static Domain<?> setCompositeKeyByDomainId(Class<?> clazzDomain, DomainId domainId) throws Exception {

		Domain<?> domain = (Domain<?>) ReflectionUtils.newInstance(clazzDomain);

		Class<?> typeId = ReflectionUtils.getTypeFieldClass(domain.getClass(), "id");

		Domain<?> domainIdEmbeddedId = (Domain<?>) ReflectionUtils.newInstance(typeId);

		for (Field field : domainIdEmbeddedId.getClass().getDeclaredFields()) {

			field.setAccessible(true);

			Object object = ReflectionUtils.executeMethod(domainId, StringsUtils.getMethod(field.getName()));

			if (object != null) {
				executeMethod(domainIdEmbeddedId, StringsUtils.setMethod(field.getName()), object);
			}
		}

		executeMethod(domain, StringsUtils.setMethod("id"), domainIdEmbeddedId);

		return domain;
	}

	/**
	 * @param domainId
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getIdDomainId(DomainId domainId) throws Exception {

		Map<String, Object> ids = new HashMap<>();

		for (Field field : domainId.getClass().getDeclaredFields()) {

			if (field.getName().startsWith("id")) {

				Object object = executeMethod(domainId, StringsUtils.getMethod(field.getName()));

				if (object != null) {
					ids.put(field.getName(), object);
				}
			}
		}
		return ids;
	}

	/**
	 * @param idMap
	 * @return
	 */
	public static String encodeId(Map<String, Object> idMap) {
		return idMap.entrySet().stream().filter(entry -> entry.getValue() != null)
				.map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("&"));
	}

	/**
	 * @param clazzDomain
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	public static Boolean isIdNullKeyCompositedByDomain(Class<?> clazzDomain, Domain<?> domain) throws Exception {

		if (ReflectionUtils.isIdComposedType(domain.getId().getClass())) {

			Domain<?> domainId = (Domain<?>) newInstance(clazzDomain);

			for (Field field : domainId.getClass().getDeclaredFields()) {

				if (field.getName().startsWith("id")) {

					Object object = ReflectionUtils.executeMethod(domain.getId(),
							StringsUtils.getMethod(field.getName()));

					if (object == null) {
						return true;
					}
				}
			}
		}

		return false;
	}
}