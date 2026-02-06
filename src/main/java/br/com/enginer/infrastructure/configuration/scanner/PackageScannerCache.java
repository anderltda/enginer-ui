package br.com.enginer.infrastructure.configuration.scanner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.usecase.core.exception.CheckedException;
import br.com.enginer.domain.system.usecase.core.utils.StringsUtils;
import jakarta.annotation.PostConstruct;

/**
 * Singleton cache de classes escaneadas por package.
 */
@Component
public class PackageScannerCache {

	/**
	 * No YAML você pode passar separado por vírgula:
	 * enginer.scanner.base-packages=br.com.enginer.domain,br.com.enginer.infrastructure
	 */
	@Value("#{'${enginer.scanner.base-packages:}'.split(',')}")
	private String[] basePackages;
	
	private final Map<String, Class<?>> cache = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() throws Exception {

		if (basePackages == null)
			return;

		for (String pkg : basePackages) {
			if (pkg == null)
				continue;
			String p = pkg.trim();
			if (p.isBlank())
				continue;

			PackageScannerLoader.loadByClass(p, cache);
		}
		
		System.out.println(cache.size() + " classes loaded in PackageScannerCache.");
	}
	
	/**
	 * @param simpleName
	 * @return
	 * @throws CheckedException
	 */
	public Object newInstance(String simpleName) throws CheckedException {
		
		Class<?> clazz = cache.get(StringsUtils.firstUpper(simpleName));
		
		Object instance = null;
	
		if (clazz != null) {
			
			try {
			
				instance = clazz.getDeclaredConstructor().newInstance();
			
			} catch (Exception ex) {
				throw new CheckedException(String.format("Failed to create instance of class %s: %s", simpleName, ex.getMessage()), ex);
			}
		}
		
		return instance;
	}

	public Class<?> get(String simpleName) {
		return cache.get(simpleName);
	}

	public Map<String, Class<?>> all() {
		return cache;
	}
}
