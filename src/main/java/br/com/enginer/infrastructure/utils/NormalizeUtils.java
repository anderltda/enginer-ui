package br.com.enginer.infrastructure.utils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.domain.system.usercase.utils.ReflectionUtils;
import br.com.enginer.domain.system.usercase.utils.StringsUtils;

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
				ReflectionUtils.set(
						domain,
						StringsUtils.setMethod(field.getName()),
						new Class<?>[] { identifyFieldClass(field.getType().getName()).getClass() },
						new Object[] { extractValueFromJson(field, jsonNode) }
				);
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
			if (main.equals(Integer.class)) return 0;
			if (main.equals(Long.class))    return 0L;
			if (main.equals(Double.class))  return 0.0;
			if (main.equals(Boolean.class)) return false;
			if (main.equals(String.class))  return "";
			if (main.equals(LocalDate.class))     return LocalDate.now();
			if (main.equals(LocalDateTime.class)) return LocalDateTime.now();
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
		if (valueNode == null || valueNode.isNull()) return null;

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
	 * Ajusta o JSON: localiza nós cujo nome termina com "Id", extrai/atualiza os
	 * campos de chave composta e renomeia o nó para "id", mantendo os dados
	 * internos (entitySeven, entityEight, etc.).
	 *
	 * Exemplo:
	 *  "entityNineId": { ... }  →  "id": { idEntityEight, idEntitySeven, idEntitySix, entitySeven, entityEight, ... }
	 */
	public static JsonNode normalizer(JsonNode jsonNode) {
		if (jsonNode != null && jsonNode.isObject()) {
			adjustRecursively((ObjectNode) jsonNode);
		}
		return jsonNode;
	}

	/**
	 * Percorre recursivamente o JSON procurando nós "*Id" e normalizando-os.
	 */
	private static void adjustRecursively(ObjectNode node) {
		// 1) Desce recursivamente primeiro (para filhos)
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

		// 2) Agora trata os campos que terminam com "Id" neste nível
		List<String> idFields = new ArrayList<>();
		node.fieldNames().forEachRemaining(name -> {
			JsonNode value = node.get(name);
			if (name.endsWith("Id") && value != null && value.isObject()) {
				idFields.add(name);
			}
		});

		for (String idFieldName : idFields) {
			ObjectNode compositeIdNode = ((ObjectNode) node.get(idFieldName)).deepCopy();

			// Preenche/atualiza idEntity* a partir dos filhos que possuem "id"
			enrichCompositeId(compositeIdNode);

			// Remove o campo original "*Id" e adiciona como "id"
			node.remove(idFieldName);
			node.set("id", compositeIdNode);
		}
	}

	/**
	 * Enriquecer o nó de chave composta:
	 *
	 * - Se houver filhos como "entitySeven", "entityEight" que tenham um campo "id":
	 *   - Se o "id" for valor simples, copia para "id<EntityNameCamelCase>".
	 *   - Se o "id" for objeto, copia apenas campos que começam com "id" (idEntitySeven, idEntitySix, etc.)
	 *
	 * Mantém os objetos originais (entitySeven, entityEight, etc.) intactos.
	 */
	private static void enrichCompositeId(ObjectNode compositeIdNode) {
		List<String> childNames = new ArrayList<>();
		compositeIdNode.fieldNames().forEachRemaining(childNames::add);

		for (String childName : childNames) {
			JsonNode childNode = compositeIdNode.get(childName);
			if (childNode == null || !childNode.isObject()) continue;

			JsonNode childIdNode = childNode.get("id");
			if (childIdNode == null || childIdNode.isNull()) continue;

			// Caso 1: id simples (ex: entityEight.id = 2)
			if (childIdNode.isValueNode()) {
				String capitalized = StringsUtils.capitalize(childName); // entityEight -> EntityEight
				String idKey = "id" + capitalized;                        // idEntityEight
				compositeIdNode.set(idKey, childIdNode);

			// Caso 2: id composto (ex: entitySeven.id = { idEntitySeven, idEntitySix, entitySix{...} })
			} else if (childIdNode.isObject()) {
				childIdNode.properties().forEach(entry -> {
					String key = entry.getKey();
					JsonNode value = entry.getValue();

					// Só copia campos que começam com "id" (evita pegar "entitySix", etc.)
					if (key.startsWith("id")) {
						compositeIdNode.set(key, value);
					}
				});
			}
		}
	}
}