package br.com.enginer.domain.system.usercase.injector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.port.OutboundPort;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.domain.system.usercase.utils.ReflectionUtils;
import br.com.enginer.domain.system.usercase.utils.StringsUtils;

/**
 * Processador reflexivo para 
 * @AutoInjectDependencies com logs visuais hierárquicos.
 * Exemplo de saída: UserCase ├── FileUserCase │ └── DocumentUserCase
 */
public final class DependencyInjector {

	/**
	 * 
	 */
	private DependencyInjector() {}

	/**
	 * Ponto de entrada principal.
	 */
	public static void processDependencies(AbstractUserCase<Domain<?>> domain) {
		processDependenciesInternal(domain, new HashSet<>(), 0);
	}

	/**
	 * Processamento recursivo com indentação visual.
	 * @param domain
	 * @param visited
	 * @param level
	 */
	@SuppressWarnings({ "unchecked" })
	private static void processDependenciesInternal(AbstractUserCase<Domain<?>> domain, Set<Object> visited, int level) {
		
		if (domain == null || visited.contains(domain)) return;
		
		visited.add(domain);
		
		Class<?> clazz = domain.getClass();
		
		while (clazz != null && clazz != Object.class) {
			
			for (Field field : clazz.getDeclaredFields()) {
				
				if (!field.isAnnotationPresent(AutoDependencyInjector.class)) continue;

				field.setAccessible(true);
				
				try {
				
					Object childInstance = field.get(domain);

					if (childInstance == null) {
						Class<?> fieldType = field.getType();
						childInstance = fieldType.getDeclaredConstructor().newInstance();
						field.set(domain, childInstance);
					} 

					// se for outro AbstractUserCase, propaga dependências e processa recursivamente
					if (childInstance instanceof AbstractUserCase<?> childUserCase) {
						
						List<Field> fields = getAllFields(childInstance.getClass());
						
						for (Field fieldUserCase : fields) {

							String getOutboundPort = StringsUtils.getMethod(fieldUserCase.getName());
							
							OutboundPort outboundPort = (OutboundPort) ReflectionUtils.get(getOutboundPort, domain);

							if (outboundPort != null) {
							
								String setOutboundPort = StringsUtils.setMethod(fieldUserCase.getName());
								
				            	ReflectionUtils.set(childUserCase, setOutboundPort, new Class<?>[] { fieldUserCase.getType() }, new Object[] { outboundPort });
					        
							}
						}
						
						processDependenciesInternal(((AbstractUserCase<Domain<?>>) childUserCase), visited, level + 1);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			clazz = clazz.getSuperclass();
		}
	}
	
	/**
	 * @param type
	 * @return
	 */
	private static List<Field> getAllFields(Class<?> type) {
		
	    List<Field> fields = new ArrayList<>();

	    Class<?> current = type;
	    
	    while (current != null && current != Object.class) {
	    	
	        for (Field f : current.getDeclaredFields()) {
	            fields.add(f);
	        }
	        
	        current = current.getSuperclass();
	    }

	    return fields;
	}
}
