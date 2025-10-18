package br.com.enginer.infrastructure.utils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.domain.ui.usercase.utils.ReflectionUtils;
import br.com.enginer.domain.ui.usercase.utils.StringsUtils;

/**
 * Utilitários para normalização de dados e JSON.
 */
public class NormalizeUtils {

	/**
	 * Normaliza o objeto Domain populando seus campos a partir do JsonNode.
	 * 
	 * @param jsonNode O nó JSON contendo os dados.
	 * @param domain   A instância do Domain a ser populada.
	 */
	public static void normalize(JsonNode jsonNode, Domain<?> domain) {
		List<Field> fields = ReflectionUtils.extractFieldsDomain(domain, false);
		for (Field field : fields) {
			if (jsonNode.has(field.getName())) {
				System.out.println(StringsUtils.setMethod(field.getName()) + " = " + jsonNode.get(field.getName()));
				ReflectionUtils.set(domain, StringsUtils.setMethod(field.getName()),
						new Class<?>[] { identifyFieldClass(field.getType().getName()).getClass() },
						new Object[] { extractValueFromJson(field, jsonNode) });
			}
		}
	}

	/**
	 * Identifica a classe do campo e retorna uma instância padrão.
	 * 
	 * @param instance Nome completo da classe.
	 * @return Instância padrão do tipo identificado ou null se não for possível.
	 */
	private static Object identifyFieldClass(String instance) {
		try {
			ClassLoader classLoader = NormalizeUtils.class.getClassLoader();
			Class<?> main = classLoader.loadClass(instance);
			// Tratamento para tipos primitivos e wrappers
			if (main.equals(Integer.class))
				return 0;
			if (main.equals(Long.class))
				return 0L;
			if (main.equals(Double.class))
				return 0.0;
			if (main.equals(Boolean.class))
				return false;
			if (main.equals(String.class))
				return "";
			if (main.equals(LocalDate.class))
				return LocalDate.now();
			if (main.equals(LocalDateTime.class))
				return LocalDateTime.now();
			if (main.equals(Collection.class) || main.equals(List.class) || main.equals(Map.class))
				return new ArrayList<>();

			// Caso seja uma classe com construtor padrão
			return main.getDeclaredConstructor().newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Extrai o valor do JsonNode e converte para o tipo apropriado do campo.
	 * 
	 * @param field    O campo do Domain.
	 * @param jsonNode O nó JSON contendo os dados.
	 * @return O valor convertido ou null se não for possível.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object extractValueFromJson(Field field, JsonNode jsonNode) {
		String fieldName = field.getName();
		Class<?> fieldType = field.getType();

		JsonNode valueNode = jsonNode.get(fieldName);
		if (valueNode == null || valueNode.isNull())
			return null;

		if (fieldType.equals(String.class)) {
			return valueNode.asText();
		} else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
			return (Integer) valueNode.asInt();
		} else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
			return (Long) valueNode.asLong();
		} else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
			return (Double) valueNode.asDouble();
		} else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
			return (Boolean) valueNode.asBoolean();
		} else if (fieldType.equals(LocalDate.class)) {
			return parseToLocalDate(valueNode.asText());
		} else if (fieldType.equals(LocalDateTime.class)) {
			return parseToLocalDateTime(valueNode.asText());
		} else if (fieldType.isEnum()) {
			return Enum.valueOf((Class<Enum>) fieldType, valueNode.asText());
		} else if (Collection.class.isAssignableFrom(fieldType)) {
			Collection<Object> collection = new ArrayList<>();
			if (valueNode.isArray()) {
				for (JsonNode item : valueNode) {
					collection.add(item.asText());
				}
			}
			return collection;
		} else {
			Domain<?> domain = (Domain<?>) identifyFieldClass(field.getType().getName());
			normalize(jsonNode.get(field.getName()), domain);
			return domain;
		}
	}

	/**
	 * Converte uma string ISO 8601 em LocalDate, ignorando hora/fuso se presente.
	 * Aceita formatos como: - 2025-04-16 - 2025-04-16T03:00:00 -
	 * 2025-04-16T03:00:00.000Z
	 * 
	 * @param isoDateTime string em formato ISO
	 * @return LocalDate
	 * @throws IllegalArgumentException se a string não for parseável
	 */
	public static LocalDate parseToLocalDate(String isoDateTime) {
		try {
			// Caso seja apenas a data
			return LocalDate.parse(isoDateTime);
		} catch (DateTimeParseException e1) {
			try {
				// Caso contenha hora, mas sem fuso (ex: 2025-04-16T03:00:00)
				return LocalDateTime.parse(isoDateTime).toLocalDate();
			} catch (DateTimeParseException e2) {
				try {
					// Caso contenha fuso Z ou offset (ex: 2025-04-16T03:00:00.000Z)
					return OffsetDateTime.parse(isoDateTime).toLocalDate();
				} catch (DateTimeParseException e3) {
					throw new IllegalArgumentException("Data inválida: " + isoDateTime);
				}
			}
		}
	}

	/**
	 * Converte uma string ISO 8601 para LocalDateTime, ignorando fuso horário se
	 * presente. Aceita formatos como: - 2025-04-16T03:00:00 -
	 * 2025-04-16T03:00:00.000Z - 2025-04-16T03:00:00.000-03:00
	 *
	 * @param isoString string ISO
	 * @return LocalDateTime correspondente
	 * @throws IllegalArgumentException se a string não puder ser convertida
	 */
	public static LocalDateTime parseToLocalDateTime(String isoString) {
		try {
			// Exato LocalDateTime
			return LocalDateTime.parse(isoString);
		} catch (DateTimeParseException e1) {
			try {
				// OffsetDateTime com fuso -> extrai o LocalDateTime
				return OffsetDateTime.parse(isoString).toLocalDateTime();
			} catch (DateTimeParseException e2) {
				throw new IllegalArgumentException("Data/hora inválida: " + isoString);
			}
		}
	}

	/**
	 * Ajusta o JSON: localiza o nó que termina com "Id", extrai e normaliza os
	 * campos de chave composta e move-os para um novo nó chamado "id",
	 * sobrescrevendo repetições com a última ocorrência.
	 */
	public static JsonNode normalizer(JsonNode jsonNode) {
		if (jsonNode != null && jsonNode.isObject()) {
			adjustRecursively((ObjectNode) jsonNode);
		}
		return jsonNode;
	}

	/**
	 * Ajusta recursivamente o nó JSON, procurando por campos que terminam com "Id"
	 * ou que contenham objetos com campos "Id" para normalização.
	 * 
	 * @param node O nó JSON a ser ajustado.
	 */
	private static void adjustRecursively(ObjectNode node) {
		List<String> keysToReplace = new ArrayList<>();
		Map<String, JsonNode> replacementMap = new LinkedHashMap<>();
		Iterator<Map.Entry<String, JsonNode>> it = node.fields();
		while (it.hasNext()) {
			Map.Entry<String, JsonNode> entry = it.next();
			String key = entry.getKey();
			JsonNode value = entry.getValue();
			// Desce primeiro
			if (value.isObject()) {
				adjustRecursively((ObjectNode) value);
			} else if (value.isArray()) {
				for (JsonNode child : value) {
					if (child.isObject())
						adjustRecursively((ObjectNode) child);
				}
			}
			// Se o campo termina com "Id" OU o objeto contém outro "*Id" por dentro,
			// normaliza
			if (value.isObject() && (key.endsWith("Id") || containsCompositeId(value))) {
				ObjectNode normalizedIdNode = (ObjectNode) normalizeIdFieldNames(key, value);

				Map<String, Object> compositeKey = new LinkedHashMap<>();
				findIdFields(normalizedIdNode, compositeKey); // coleta idEntity*, já limpo

				ObjectNode newIdNode = JsonNodeFactory.instance.objectNode();
				compositeKey.forEach((k, v) -> {
					if (v == null) {
						newIdNode.putNull(k);
					} else if (v instanceof Number) {
						// mantém número
						if (v instanceof Integer)
							newIdNode.put(k, (Integer) v);
						else if (v instanceof Long)
							newIdNode.put(k, (Long) v);
						else if (v instanceof Double)
							newIdNode.put(k, (Double) v);
						else
							newIdNode.putPOJO(k, v);
					} else if (v instanceof Boolean) {
						newIdNode.put(k, (Boolean) v);
					} else {
						newIdNode.put(k, String.valueOf(v));
					}
				});
				keysToReplace.add(key);
				replacementMap.put("id", newIdNode); // sempre substitui pelo nome "id"
			}
		}

		// aplica no final
		for (String k : keysToReplace)
			node.remove(k);
		replacementMap.forEach(node::set);
	}

	/**
	 * Verifica se o nó JSON contém um campo que termina com "Id" e cujo valor é um
	 * objeto (indicando chave composta).
	 * 
	 * @param node O nó JSON a ser verificado.
	 * @return true se contiver tal campo, false caso contrário.
	 */
	private static boolean containsCompositeId(JsonNode node) {
		if (!node.isObject())
			return false;
		Iterator<String> it = node.fieldNames();
		while (it.hasNext()) {
			String f = it.next();
			JsonNode v = node.get(f);
			if (f.endsWith("Id") && v != null && v.isObject())
				return true;
		}
		return false;
	}

	/**
	 * Normaliza os nomes dos campos "id" dentro do nó JSON, renomeando-os para
	 * evitar conflitos.
	 * 
	 * @param parentName   Nome do campo pai (pode ser null).
	 * @param originalNode O nó JSON original a ser normalizado.
	 * @return Um novo nó JSON com os campos "id" renomeados.
	 */
	private static JsonNode normalizeIdFieldNames(String parentName, JsonNode originalNode) {
		ObjectNode result = JsonNodeFactory.instance.objectNode();
		List<Map.Entry<String, JsonNode>> reversedEntries = new ArrayList<>();
		originalNode.fields().forEachRemaining(reversedEntries::add);
		Collections.reverse(reversedEntries);

		for (Map.Entry<String, JsonNode> entry : reversedEntries) {
			String fieldName = entry.getKey();
			JsonNode value = entry.getValue();

			if (value.isObject()) {
				JsonNode child = normalizeIdFieldNames(fieldName, value);

				if (child.has("id") && fieldName != null) {
					JsonNode idValue = child.get("id");
					String renamedKey = "id" + StringsUtils.capitalize(fieldName);
					result.set(renamedKey, idValue);

					child.fields().forEachRemaining(subEntry -> {
						if (!subEntry.getKey().equals("id")) {
							result.set(subEntry.getKey(), subEntry.getValue());
						}
					});
				} else {
					result.set(fieldName, child);
				}

			} else {
				result.set(fieldName, value);
			}
		}

		return result;
	}

	/**
	 * Encontra todos os campos que começam com "id" no nó JSON e os adiciona ao
	 * mapa de resultados.
	 * 
	 * @param node   O nó JSON a ser examinado.
	 * @param result O mapa onde os campos encontrados serão armazenados.
	 */
	private static void findIdFields(JsonNode node, Map<String, Object> result) {
		if (!node.isObject())
			return;

		node.fields().forEachRemaining(entry -> {
			String key = entry.getKey();
			JsonNode value = entry.getValue();
			if (key.startsWith("id") && !value.isObject()) {
				if (value.isNull()) {
					result.put(key, null);
				} else if (value.isNumber()) {
					result.put(key, value.numberValue());
				} else if (value.isTextual()) {
					String text = value.textValue();
					if ("null".equals(text)) {
						result.put(key, null);
					} else {
						result.put(key, text);
					}
				} else {
					result.put(key, value.toString());
				}
			} else if (value.isObject()) {
				findIdFields(value, result);
			}
		});
	}
}
