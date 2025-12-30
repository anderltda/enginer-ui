package br.com.enginer.infrastructure.utils;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.core.utils.StringsUtils;

/**
 * Utilitários para normalização de dados e JSON.
 *
 * - normalizer(JsonNode): ajusta chaves "*Id" -> "id" e enriquece chaves compostas.
 * - order(JsonNode): ordena o JSON recursivamente para gerar sempre a mesma ordem de campos.
 */
public class NormalizeUtils {

	private static final JsonNodeFactory NODE_FACTORY = JsonNodeFactory.instance;

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
				ReflectionUtils.set(
					domain,
					StringsUtils.setMethod(field.getName()),
					new Class<?>[] { field.getType() },
					new Object[] { extractValueFromJson(field, jsonNode) }
				);
			}
		}
	}

	/**
	 * Extrai o valor do JsonNode e converte para o tipo apropriado do campo.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object extractValueFromJson(Field field, JsonNode jsonNode) {
		String fieldName = field.getName();
		Class<?> fieldType = field.getType();

		JsonNode valueNode = jsonNode.get(fieldName);
		if (valueNode == null || valueNode.isNull()) return null;

		if (fieldType.equals(String.class)) {
			return valueNode.asText();
		} else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
			return valueNode.asInt();
		} else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
			return valueNode.asLong();
		} else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
			return valueNode.asDouble();
		} else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
			return valueNode.asBoolean();
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
					collection.add(item.isValueNode() ? item.asText() : item.toString());
				}
			}
			return collection;
		} else {
			Domain<?> domain = (Domain<?>) identifyFieldInstance(fieldType);
			if (domain != null && valueNode.isObject()) {
				normalize(valueNode, domain);
			}
			return domain;
		}
	}

	/**
	 * Tenta instanciar um tipo de campo (wrappers, datas e classes com construtor default).
	 */
	private static Object identifyFieldInstance(Class<?> type) {
		try {
			if (type.equals(Integer.class) || type.equals(int.class)) return 0;
			if (type.equals(Long.class) || type.equals(long.class)) return 0L;
			if (type.equals(Double.class) || type.equals(double.class)) return 0.0;
			if (type.equals(Boolean.class) || type.equals(boolean.class)) return false;
			if (type.equals(String.class)) return "";
			if (type.equals(LocalDate.class)) return LocalDate.now();
			if (type.equals(LocalDateTime.class)) return LocalDateTime.now();

			return type.getDeclaredConstructor().newInstance();
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Converte uma string ISO 8601 em LocalDate, ignorando hora/fuso se presente.
	 */
	public static LocalDate parseToLocalDate(String isoDateTime) {
		try {
			return LocalDate.parse(isoDateTime);
		} catch (DateTimeParseException e1) {
			try {
				return LocalDateTime.parse(isoDateTime).toLocalDate();
			} catch (DateTimeParseException e2) {
				try {
					return OffsetDateTime.parse(isoDateTime).toLocalDate();
				} catch (DateTimeParseException e3) {
					throw new IllegalArgumentException("Data inválida: " + isoDateTime);
				}
			}
		}
	}

	/**
	 * Converte uma string ISO 8601 para LocalDateTime, ignorando fuso horário.
	 */
	public static LocalDateTime parseToLocalDateTime(String isoString) {
		try {
			return LocalDateTime.parse(isoString);
		} catch (DateTimeParseException e1) {
			try {
				return OffsetDateTime.parse(isoString).toLocalDateTime();
			} catch (DateTimeParseException e2) {
				throw new IllegalArgumentException("Data/hora inválida: " + isoString);
			}
		}
	}

	/**
	 * Ajusta o JSON: localiza nós cujo nome termina com "Id", enriquece a chave composta
	 * e renomeia o nó para "id".
	 *
	 * No final, aplica uma ordenação determinística de campos para o JSON sempre sair
	 * com a mesma ordem.
	 */
	public static JsonNode normalizer(JsonNode jsonNode) {
		if (jsonNode != null && jsonNode.isObject()) {
			adjustRecursively((ObjectNode) jsonNode);
			return order(jsonNode); // <- ordenação final
		}
		return jsonNode;
	}

	/**
	 * Percorre recursivamente o JSON procurando nós "*Id" e normalizando-os.
	 */
	private static void adjustRecursively(ObjectNode node) {
		// 1) desce primeiro
		List<String> fieldNames = new ArrayList<>();
		node.fieldNames().forEachRemaining(fieldNames::add);

		for (String fieldName : fieldNames) {
			JsonNode value = node.get(fieldName);
			if (value == null) continue;

			if (value.isObject()) {
				adjustRecursively((ObjectNode) value);
			} else if (value.isArray()) {
				for (JsonNode child : value) {
					if (child.isObject()) {
						adjustRecursively((ObjectNode) child);
					}
				}
			}
		}

		// 2) trata campos "*Id" neste nível
		List<String> idFields = new ArrayList<>();
		node.fieldNames().forEachRemaining(name -> {
			JsonNode value = node.get(name);
			if (name.endsWith("Id") && value != null && value.isObject()) {
				idFields.add(name);
			}
		});

		for (String idFieldName : idFields) {
			ObjectNode compositeIdNode = ((ObjectNode) node.get(idFieldName)).deepCopy();

			enrichCompositeId(compositeIdNode);

			node.remove(idFieldName);
			node.set("id", compositeIdNode);
		}
	}

	/**
	 * Enriquecer o nó de chave composta:
	 * - Copia id simples de filhos (entityX.id -> idEntityX)
	 * - Se id do filho for objeto: copia somente campos que começam com "id"
	 */
	private static void enrichCompositeId(ObjectNode compositeIdNode) {
		List<String> childNames = new ArrayList<>();
		compositeIdNode.fieldNames().forEachRemaining(childNames::add);

		for (String childName : childNames) {
			JsonNode childNode = compositeIdNode.get(childName);
			if (childNode == null || !childNode.isObject()) continue;

			JsonNode childIdNode = childNode.get("id");
			if (childIdNode == null || childIdNode.isNull()) continue;

			if (childIdNode.isValueNode()) {
				String capitalized = StringsUtils.capitalize(childName);
				String idKey = "id" + capitalized;
				compositeIdNode.set(idKey, childIdNode);

			} else if (childIdNode.isObject()) {
				childIdNode.properties().forEach(entry -> {
				    String key = entry.getKey();
				    if (key.startsWith("id")) {
				        compositeIdNode.set(key, entry.getValue());
				    }
				});
			}
		}
	}

	/**
	 * Ordena recursivamente um JsonNode para sempre produzir a mesma ordem de campos.
	 *
	 * Regra:
	 *  1) "id" primeiro
	 *  2) depois campos que começam com "id"
	 *  3) resto em ordem alfabética (case-insensitive)
	 */
	public static JsonNode order(JsonNode node) {
		if (node == null) return null;

		if (node.isObject()) {
			ObjectNode obj = (ObjectNode) node;

			List<Map.Entry<String, JsonNode>> entries = new ArrayList<>();
			entries.addAll(obj.properties());

			entries.sort(fieldOrderComparator());

			ObjectNode ordered = NODE_FACTORY.objectNode();
			for (Map.Entry<String, JsonNode> e : entries) {
				ordered.set(e.getKey(), order(e.getValue())); // recursivo
			}
			return ordered;
		}

		if (node.isArray()) {
			ArrayNode arr = NODE_FACTORY.arrayNode();
			for (JsonNode item : node) {
				arr.add(order(item)); // recursivo
			}
			return arr;
		}

		return node; // value node
	}

	private static Comparator<Map.Entry<String, JsonNode>> fieldOrderComparator() {
		return (a, b) -> {
			String ka = a.getKey();
			String kb = b.getKey();

			// 1) "id" primeiro
			boolean aIsId = "id".equalsIgnoreCase(ka);
			boolean bIsId = "id".equalsIgnoreCase(kb);
			if (aIsId && !bIsId) return -1;
			if (!aIsId && bIsId) return 1;

			// 2) campos começando com "id" antes do resto
			boolean aStartsId = ka.regionMatches(true, 0, "id", 0, 2);
			boolean bStartsId = kb.regionMatches(true, 0, "id", 0, 2);
			if (aStartsId && !bStartsId) return -1;
			if (!aStartsId && bStartsId) return 1;

			// 3) alfabético (case-insensitive)
			return ka.compareToIgnoreCase(kb);
		};
	}

	/**
	 * Decode apenas se estiver URL-encoded.
	 */
	public static String decodeIfNeeded(String value) {
		if (value == null) return null;
		if (value.indexOf('%') >= 0 || value.indexOf('+') >= 0) {
			return URLDecoder.decode(value, StandardCharsets.UTF_8);
		}
		return value;
	}

	public static String decodeIfEncoded(String value) {
		if (value == null) return null;
		if (value.contains("%") || value.contains("+")) {
			return URLDecoder.decode(value, StandardCharsets.UTF_8);
		}
		return value;
	}
}