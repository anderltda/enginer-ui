package br.com.enginer.domain.system.usercase.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.exception.CheckedException;
import br.com.enginer.domain.system.usercase.port.outbound.DependencyInjectorPort;
import br.com.enginer.domain.system.usercase.port.outbound.OutboundPort;
import br.com.enginer.domain.system.usercase.registry.DependencyInjectorRegistry;
import br.com.enginer.domain.system.usercase.schema.field.type.Id;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.domain.system.usercase.schema.instance.DomainId;
import br.com.enginer.infrastructure.injector.DependencyInjector;

/**
 * Classe utilitária de reflexão central do projeto.
 * Contém suporte completo a introspecção, injeção dinâmica e manipulação de objetos Domain/UserCase.
 */
public class ReflectionUtils {

    /**
     * Cache global de instâncias de UserCases, por classe de domínio.
     * Evita recriação e reinjeção desnecessária, melhorando performance e consistência.
     */
    private static final Map<Class<?>, Object> USERCASE_CACHE = new ConcurrentHashMap<>();

    /**
     * Limpa o cache de UserCases — útil em ambiente de testes ou recarga de contexto.
     */
    public static void clearUserCaseCache() {
        USERCASE_CACHE.clear();
    }

    /**
     * Cria ou recupera do cache uma instância de {@code UserCase} associada a um domínio,
     * injetando dinamicamente todas as dependências fornecidas via reflexão.
     * <p>
     * Se o domínio for uma implementação de {@code DomainId}, identifica automaticamente
     * o domínio pai (ex: {@code EntityNineId} → {@code EntityNine}) e carrega o
     * {@code UserCase} correspondente ao domínio pai.
     * </p>
     *
     * @param domainClass classe do domínio cujo {@code UserCase} será instanciado/injetado
     * @param outboundPorts lista variável de dependências (ex: RepositoryOutboundPort, PublisherOutboundPort)
     * @return instância do {@code UserCase} com dependências injetadas
     * @throws Exception caso ocorra falha de reflexão, injeção ou criação de instância
     */
    public static Object executeInjectedDependencyUserCaseCached(Class<?> domainClass, OutboundPort... outboundPorts) throws Exception {
        
        // 0 - Se for um DomainId, mapeia para o domínio pai (ex: EntityNineId → EntityNine)
        if (DomainId.class.isAssignableFrom(domainClass)) {
            String className = domainClass.getSimpleName();
            if (className.endsWith("Id")) {
                String parentName = className.substring(0, className.length() - 2); // remove "Id"
                String packageName = domainClass.getPackageName();
                String parentQualifiedName = packageName + "." + parentName;
                try {
                    domainClass = Class.forName(parentQualifiedName);
                } catch (ClassNotFoundException e) {
                    throw new CheckedException("Domínio pai não encontrado para: " + className, e);
                }
            }
        }

        // 1 - Recupera (ou cria) o UserCase no cache
        Object userCaseInstance = USERCASE_CACHE.get(domainClass);
        final boolean fromCache = (userCaseInstance != null);

        if (!fromCache) {
            String userCaseName = findUserCaseQualifiedName(domainClass, domainClass.getSimpleName());
            userCaseInstance = createInstance(userCaseName);
            USERCASE_CACHE.put(domainClass, userCaseInstance);
        }

        // 2 - Obtém instância do injetor (infraestrutura)
        DependencyInjectorPort injector = DependencyInjectorRegistry.get();

        // 3 - Registra os OutboundPorts no cache global (serão aplicados em todos os UserCases)
        injector.registerOutboundPorts(outboundPorts);

        // 4 - Injeta os Outbounds no UserCase raiz e processa dependências internas
        if (userCaseInstance instanceof AbstractUserCase<?>) {
            AbstractUserCase<?> root = (AbstractUserCase<?>) userCaseInstance;
            DependencyInjector.addOutboundPort(root, outboundPorts);
        }

        // 5 - Retorna o UserCase pronto para uso
        return userCaseInstance;
    }

    /**
     * Descobre o nome totalmente qualificado do UserCase baseado no domínio.
     * Exemplo: br.com.enginer.domain.example.dto.entity.EntityEight -> br.com.enginer.domain.example.usercase.EntityEightUserCase
     */
    private static String findUserCaseQualifiedName(Class<?> domainClass, String domainName) {
        String packageName = domainClass.getPackageName();
        if (packageName.contains(".dto.entity")) {
            packageName = packageName.replace(".dto.entity", ".usercase");
        }
        return packageName + "." + domainName + "UserCase";
    }

    /**
     * Cria uma instância de uma classe pelo nome completo.
     */
    private static Object createInstance(String className) throws Exception {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (ClassNotFoundException ex) {
            throw new CheckedException("Classe UserCase não encontrada: " + className, ex);
        }
    }

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
			// extractFieldsWithClassAbstract(object.getClass(), Object.class, fields, ".*");
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
	 * Verifica se um objeto não é nulo e não está "vazio".
	 * <p>
	 * Este método é genérico e realiza verificações específicas conforme o tipo do
	 * objeto:
	 * <ul>
	 * <li><b>String:</b> Retorna {@code false} se for nula, vazia ou contiver
	 * apenas espaços em branco.</li>
	 * <li><b>Collection:</b> Retorna {@code false} se a coleção estiver vazia.</li>
	 * <li><b>Map:</b> Retorna {@code false} se o mapa estiver vazio.</li>
	 * <li><b>Array:</b> Retorna {@code false} se o array não tiver elementos.</li>
	 * <li><b>Outros tipos:</b> Retorna {@code true} se não for nulo (não há
	 * verificação de conteúdo).</li>
	 * </ul>
	 *
	 * @param value objeto a ser verificado
	 * @return {@code true} se o valor não for nulo nem vazio conforme as regras
	 *         acima; caso contrário, {@code false}
	 */
	public static boolean isNotEmpty(Object value) {
		if (value == null)
			return false;
		if (value instanceof String)
			return !((String) value).trim().isEmpty();
		if (value instanceof Collection<?>)
			return !((Collection<?>) value).isEmpty();
		if (value instanceof Map<?, ?>)
			return !((Map<?, ?>) value).isEmpty();
		if (value.getClass().isArray())
			return java.lang.reflect.Array.getLength(value) > 0;
		return true; // qualquer outro objeto não-nulo é considerado "não vazio"
	}
	
	/**
	 * Retorna o próprio valor se não for nulo nem vazio; caso contrário, retorna {@code null}.
	 * <p>
	 * Este método realiza verificações específicas conforme o tipo do objeto:
	 * <ul>
	 *     <li><b>String:</b> Retorna {@code null} se for nula, vazia ou contiver apenas espaços em branco.</li>
	 *     <li><b>Collection:</b> Retorna {@code null} se estiver vazia.</li>
	 *     <li><b>Map:</b> Retorna {@code null} se estiver vazio.</li>
	 *     <li><b>Array:</b> Retorna {@code null} se não tiver elementos.</li>
	 *     <li><b>Outros tipos:</b> Retorna o próprio valor se não for {@code null}.</li>
	 * </ul>
	 *
	 * @param value objeto a ser avaliado
	 * @param <T> tipo genérico do objeto de entrada
	 * @return o próprio valor se não for nulo/vazio; caso contrário, {@code null}
	 */
	public static <T> T nullIfEmpty(T value) {
	    if (value == null) return null;
	    if (value instanceof String && ((String) value).trim().isEmpty()) return null;
	    if (value instanceof Collection<?> && ((Collection<?>) value).isEmpty()) return null;
	    if (value instanceof Map<?, ?> && ((Map<?, ?>) value).isEmpty()) return null;
	    if (value.getClass().isArray() && java.lang.reflect.Array.getLength(value) == 0) return null;
	    return value;
	}	

	
	/**
	 * Verifica se o objeto informado possui pelo menos um campo não nulo.
	 * <p>
	 * O método utiliza reflexão para inspecionar todos os campos declarados (inclusive privados)
	 * da classe do objeto. Campos estáticos ou transientes são ignorados.
	 * 
	 * @param obj objeto a ser verificado
	 * @return {@code true} se existir ao menos um campo não nulo, {@code false} caso contrário
	 */
	public static boolean hasNonNullField(Object obj) {
	    if (obj == null) return false;

	    for (Class<?> c = obj.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
	        for (Field f : c.getDeclaredFields()) {
	            // Ignora estáticos e transientes (apenas dados de instância)
	            if (Modifier.isStatic(f.getModifiers()) || Modifier.isTransient(f.getModifiers())) {
	                continue;
	            }
	            try {
	                if (!f.canAccess(obj)) {
	                    f.setAccessible(true);
	                }
	                if (f.get(obj) != null) {
	                    return true;
	                }
	            } catch (InaccessibleObjectException e) {
	                // Java 16+ (módulos): não foi possível abrir o pacote/classe; ignora e segue
	                continue;
	            } catch (IllegalAccessException e) {
	                // Campo privado sem acesso; ignora e segue
	                continue;
	            }
	        }
	    }
	    return false;
	}
	


	/**
	 * Verifica se o objeto informado possui algum campo {@code null}, vazio ou em branco,
	 * ignorando os campos cujo nome foi informado no parâmetro {@code ignoredFields}.
	 * <p>
	 * O método usa reflexão para inspecionar os campos (inclusive privados) da classe
	 * e de suas superclasses. Ignora campos estáticos e transientes.
	 *
	 * @param obj objeto a ser verificado
	 * @param ignoredFields nomes dos campos a serem ignorados durante a checagem
	 * @return {@code true} se existir pelo menos um campo nulo ou vazio (exceto os ignorados),
	 *         caso contrário {@code false}
	 */
	public static boolean hasNonNullField(Object obj, String... ignoredFields) {

		Set<String> ignored = new HashSet<>(Arrays.asList(ignoredFields));

	    if (obj == null) return false;

	    for (Class<?> c = obj.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
	        for (Field f : c.getDeclaredFields()) {
	        	 if (ignored.contains(f.getName())) continue;
	        	 
	            // Ignora estáticos e transientes (apenas dados de instância)
	            if (Modifier.isStatic(f.getModifiers()) || Modifier.isTransient(f.getModifiers())) continue;
	            
	            try {
	            	if (!f.canAccess(obj)) {
	                    f.setAccessible(true);
	                }
	                if (f.get(obj) != null) {
	                    return true;
	                }
	            } catch (InaccessibleObjectException e) {
	                continue;
	            } catch (IllegalAccessException e) {
	                continue;
	            }
	        }
	    }
	    return false;
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
        } catch (Exception e) {
            System.err.printf("Setter '%s(%s)' não encontrado em %s.%n", methodName, paramClass.getClass().getSimpleName(), paramValue.getClass().getSimpleName());
        }
	}
	
    /**
     * Executa um setter de forma segura, com fallback em caso de tipo incompatível.
     */
    public static void setFallback(Object target, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        Class<?> clazz = target.getClass();
        Method method = null;
        try {
            method = clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            // Fallback: tenta com superclasse do parâmetro
            if (paramTypes.length > 0 && paramTypes[0].getSuperclass() != null) {
                try {
                    method = clazz.getMethod(methodName, new Class<?>[]{ paramTypes[0].getSuperclass() });
                } catch (NoSuchMethodException ignored) {}
            }
            if (method == null) {
                throw e;
            }
        }
        method.setAccessible(true);
        method.invoke(target, args);
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
	public static Object executeInjectedDependencyUserCase_(Class<?> clazz, OutboundPort... outboundPorts) throws Exception {

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
			method.setAccessible(true);
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
	
	/**
	 * @param target
	 * @param methodName
	 * @param value
	 * @return
	 */
	public static boolean invokeSetterFlexible(Object target, String methodName, Object value) {
	    try {
	        for (java.lang.reflect.Method m : target.getClass().getMethods()) {
	            if (m.getName().equalsIgnoreCase(methodName) && m.getParameterCount() == 1) {
	                m.invoke(target, value);
	                return true;
	            }
	        }
	    } catch (Exception ignored) {}
	    return false;
	}
}