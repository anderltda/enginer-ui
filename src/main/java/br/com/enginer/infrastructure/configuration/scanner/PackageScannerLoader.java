package br.com.enginer.infrastructure.configuration.scanner;

import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

/**
 * PACKAGE SCANNER - Carrega classes do(s) package(s) e adiciona no map
 * (targetCache) informado.
 */
public class PackageScannerLoader {

	/**
	 * Token obrigatório para considerar a classe no escaneamento.
	 */
	private static String REQUIRED_PACKAGE_TOKEN = ".dto.";

	/**
	 * Carrega classes do package e adiciona no map informado.
	 */
	public static void loadByClass(String basePackage, Map<String, Class<?>> targetCache) throws Exception {

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

		// inclui tudo (sem depender de @Component etc.)
		scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);

		for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
			
			String className = bd.getBeanClassName();
			if (className == null || className.isBlank())
				continue;

			// garante match do token como parte do package
			if (!className.contains(REQUIRED_PACKAGE_TOKEN)) continue;

			Class<?> clazz = Class.forName(className);
			targetCache.put(clazz.getSimpleName(), clazz);
		}
	}
}