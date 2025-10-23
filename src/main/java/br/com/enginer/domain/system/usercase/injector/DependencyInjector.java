package br.com.enginer.domain.system.usercase.injector;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;

/**
 * Processador reflexivo para 
 * @AutoInjectDependencies com logs visuais hierárquicos.
 * 
 * Exemplo de saída: UserCase ├── FileUserCase │ └── DocumentUserCase
 */
public final class DependencyInjector {

	private DependencyInjector() {
		// construtor privado para evitar instanciação
	}

	/**
	 * Ponto de entrada principal.
	 */
	public static void processDependencies(AbstractUserCase<Domain<?>> domain) {
		processDependenciesInternal(domain, new HashSet<>(), 0);
	}

	/**
	 * Processamento recursivo com indentação visual.
	 */
	@SuppressWarnings("unchecked")
	private static void processDependenciesInternal(AbstractUserCase<Domain<?>> domain, Set<Object> visited, int level) {
		if (domain == null || visited.contains(domain)) return;
		visited.add(domain);

		//String indent = " ".repeat(Math.max(0, level * 2));
		if (level == 0) {
			//System.out.println("\n[DependencyInjector] Injetando dependências para: " + parent.getClass().getSimpleName());
		}

		Class<?> clazz = domain.getClass();
		while (clazz != null && clazz != Object.class) {
			for (Field field : clazz.getDeclaredFields()) {
				if (!field.isAnnotationPresent(AutoDependencyInjector.class))
					continue;

				field.setAccessible(true);
				try {
					Object childInstance = field.get(domain);

					// cria instância se nula
					if (childInstance == null) {
						Class<?> fieldType = field.getType();
						childInstance = fieldType.getDeclaredConstructor().newInstance();
						field.set(domain, childInstance);
						//System.out.printf("%s├── [instanciado] %s%n", indent, fieldType.getSimpleName());
					} else {
						//System.out.printf("%s├── [existente] %s%n", indent, childInstance.getClass().getSimpleName());
					}

					// se for outro AbstractUserCase, propaga dependências e processa recursivamente
					if (childInstance instanceof AbstractUserCase<?> childUserCase) {
						childUserCase.setRepositoryOutboundPort(domain.getRepositoryOutboundPort());
						childUserCase.setPublisherOutboundPort(domain.getPublisherOutboundPort());
						processDependenciesInternal(((AbstractUserCase<Domain<?>>) childUserCase), visited, level + 1);
					}

				} catch (Exception ex) {
					//System.err.printf("[DependencyInjector] Erro ao injetar campo '%s' em '%s': %s%n", field.getName(), clazz.getSimpleName(), e.getMessage());
					ex.printStackTrace();
				}
			}
			clazz = clazz.getSuperclass();
		}

		if (level == 0) {
			//System.out.println("[DependencyInjector] Injeção concluída para " + parent.getClass().getSimpleName() + "\n");
		}
	}
}