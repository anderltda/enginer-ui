package br.com.enginer.domain.ui.usercase.injector;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.ui.usercase.annotation.AutoDependencyInjector;

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
	public static void processDependencies(AbstractUserCase parent) {
		processDependenciesInternal(parent, new HashSet<>(), 0);
	}

	/**
	 * Processamento recursivo com indentação visual.
	 */
	private static void processDependenciesInternal(AbstractUserCase parent, Set<Object> visited, int level) {
		if (parent == null || visited.contains(parent)) return;
		visited.add(parent);

		//String indent = " ".repeat(Math.max(0, level * 2));
		if (level == 0) {
			//System.out.println("\n[DependencyInjector] Injetando dependências para: " + parent.getClass().getSimpleName());
		}

		Class<?> clazz = parent.getClass();
		while (clazz != null && clazz != Object.class) {
			for (Field field : clazz.getDeclaredFields()) {
				if (!field.isAnnotationPresent(AutoDependencyInjector.class))
					continue;

				field.setAccessible(true);
				try {
					Object childInstance = field.get(parent);

					// cria instância se nula
					if (childInstance == null) {
						Class<?> fieldType = field.getType();
						childInstance = fieldType.getDeclaredConstructor().newInstance();
						field.set(parent, childInstance);
						//System.out.printf("%s├── [instanciado] %s%n", indent, fieldType.getSimpleName());
					} else {
						//System.out.printf("%s├── [existente] %s%n", indent, childInstance.getClass().getSimpleName());
					}

					// se for outro AbstractUserCase, propaga dependências e processa recursivamente
					if (childInstance instanceof AbstractUserCase childUserCase) {
						childUserCase.setRepositoryOutboundPort(parent.getRepositoryOutboundPort());
						childUserCase.setPublisherOutboundPort(parent.getPublisherOutboundPort());
						processDependenciesInternal(childUserCase, visited, level + 1);
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