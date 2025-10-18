package br.com.enginer.domain.ui.usercase.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.ui.usercase.exception.CheckedException;

public class StringsUtils {

	/**
	 * @param value
	 * @return
	 */
	public static String setMethod(String value) {
		return "set".concat(firstUpper(value));
	}

	/**
	 * @param value
	 * @return
	 */
	public static String getMethod(String value) {
		return "get".concat(firstUpper(value));
	}
	
	/**
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
		if (str == null || str.isEmpty()) return str;
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static String trim(Object value) {
		if (value != null && value.toString().length() > 0)
			return value.toString().trim();
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	public static String firstUpper(String name) {
		String returnValue = name.substring(0, 1).toUpperCase();
		if (name.length() > 1)
			returnValue += name.substring(1);
		return returnValue;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public static String firstLower(String name) {
		String returnValue = name.substring(0, 1).toLowerCase();
		if (name.length() > 1)
			returnValue += name.substring(1);
		return returnValue;
	}
	
	/**
	 * @param className
	 * @return
	 * @throws CheckedException
	 */
	public static String getNameUserCase(String className) throws CheckedException {
		try {
			return className + "UserCase";
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * @param clazz
	 * @return
	 */
	public static String convertDtoToUsercasePackage(Class<?> clazz) {
	    String fullClassName = clazz.getName();
	    int dtoIndex = fullClassName.indexOf(".dto.");
	    if (dtoIndex == -1) {
	        throw new IllegalArgumentException("Pacote 'dto' não encontrado na classe: " + fullClassName);
	    }
	    String basePackage = fullClassName.substring(0, dtoIndex);
	    return getNameUserCase(basePackage.concat(".usercase.").concat(removeIdSuffix(clazz.getSimpleName())));
	}
	
	/**
	 * @param className
	 * @return
	 */
	public static String removeIdSuffix(String className) {
	    if (className.endsWith("Id")) {
	        return className.substring(0, className.length() - 2);
	    }
	    return className;
	}

	/**
	 * Formatar atributo para uma formacao ex: Prohibited Date Time ou
	 * ProhibitedDateTime para isso prohibitedDateTime
	 * 
	 * @param input - valor a ser formatado
	 * @return
	 */
	public static String normalizeToCamelCaseFromPascalCase(String input) {
	    if (input == null || input.isEmpty()) return input;

	    return input.substring(0, 1).toLowerCase() + input.substring(1);
	}

	/**
	 * Formatar atributo para uma formacao ex: prohibitedDateTime para isso
	 * Prohibited Date Time
	 * 
	 * @param input - valor a ser formatado
	 * @return
	 */
	public static String normalizeLabelToLowercaseCamelization(String input) {
		if (input == null || input.isEmpty())
			return input;

		String spaced = input.replaceAll("([a-z])([A-Z])", "$1 $2");
		String[] words = spaced.split(" ");
		StringBuilder builder = new StringBuilder();
		for (String word : words) {
			if (!word.isEmpty()) {
				builder.append(Character.toUpperCase(word.charAt(0)));
				builder.append(word.substring(1));
				builder.append(" ");
			}
		}
		return builder.toString().trim();
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static List<?> toList(Object value) {
	    if (value == null) return Collections.emptyList();
	    if (value instanceof List<?>) {
	        return (List<?>) value;
	    }
	    if (value.getClass().isArray()) {
	        return Arrays.asList((Object[]) value);
	    }
	    throw new IllegalArgumentException("Não é possível converter para List: " + value.getClass().getName());
	}

	/**
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object value) {
	    if (value instanceof Map<?, ?> map) {
	        return (Map<String, Object>) map;
	    }
	    throw new IllegalArgumentException("Não é possível converter para Map: " + value.getClass().getName());
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static LocalDate toLocalDate(Object value) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; 
	    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	    return LocalDate.parse(value.toString(), value.toString().contains("T") ? dateTimeFormatter : dateFormatter);
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static LocalDateTime toLocalDateTime(Object value) {
		return LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}
