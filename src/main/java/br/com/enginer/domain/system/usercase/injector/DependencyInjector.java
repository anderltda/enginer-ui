package br.com.enginer.domain.system.usercase.injector;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.injector.lazy.LazyProxyHandler;
import br.com.enginer.domain.system.usercase.port.OutboundPort;
import br.com.enginer.domain.system.usercase.utils.ReflectionUtils;
import br.com.enginer.domain.system.usercase.utils.StringsUtils;

/**
 * Injetor reflexivo com suporte a Lazy Loading.
 * Cria e injeta automaticamente os UserCases filhos anotados com @AutoDependencyInjector.
 * Mantém cache de OutboundPorts e propaga hierarquicamente.
 */
public final class DependencyInjector {

    private static final Map<String, OutboundPort> OUTBOUND_PORT_CACHE = new ConcurrentHashMap<>();

    private DependencyInjector() {}

    /**
     * Inicializa e injeta dependências em um UserCase raiz.
     * Esse método é chamado pelo LazyProxyHandler.
     */
    public static void processDependencies(AbstractUserCase<?> domain) {
        processDependenciesInternal(domain, new HashSet<>(), 0);
    }

    /**
     * Adiciona portas outbound ao UserCase principal e propaga hierarquicamente.
     */
    public static void addOutboundPort(AbstractUserCase<?> domain, OutboundPort... outboundPorts) {

        for (OutboundPort outboundPort : outboundPorts) {
            if (outboundPort != null) {

                Class<?>[] interfaces = outboundPort.getClass().getInterfaces();
                String interfaceName = interfaces.length > 0
                        ? interfaces[0].getSimpleName()
                        : outboundPort.getClass().getSimpleName();

                String setOutboundPort = StringsUtils.setMethod(interfaceName);

                OUTBOUND_PORT_CACHE.put(setOutboundPort, outboundPort);

                ReflectionUtils.set(domain, setOutboundPort,
                        new Class<?>[] { interfaces.length > 0 ? interfaces[0] : outboundPort.getClass() },
                        new Object[] { outboundPort });
            }
        }

        processDependenciesInternal(domain, new HashSet<>(), 0);
    }

    /**
     * Processa recursivamente dependências com logs hierárquicos e lazy loading.
     */
    @SuppressWarnings({ "unchecked" })
    private static void processDependenciesInternal(AbstractUserCase<?> domain, Set<Object> visited, int level) {

        if (domain == null || visited.contains(domain)) return;
        
        visited.add(domain);

        Class<?> clazz = domain.getClass();

        while (clazz != null && clazz != Object.class) {
        	
            for (Field field : clazz.getDeclaredFields()) {

                if (!field.isAnnotationPresent(AutoDependencyInjector.class)) continue;

                field.setAccessible(true);

                try {
                    Object existingValue = field.get(domain);

                    // cria lazy handler se ainda não tiver instância
                    if (existingValue == null) {

                        Class<?> fieldType = field.getType();
                        
                        LazyProxyHandler<?> lazy = new LazyProxyHandler<>((Class<? extends AbstractUserCase<?>>) fieldType);

                        Object instance = lazy.get(); // instancia real sob demanda
                        
                        field.set(domain, instance);

                        logDependency(domain, instance, level);

                        // Propaga outbound ports para o novo usercase
                        OUTBOUND_PORT_CACHE.forEach((key, outboundPort) -> {
                        	
                            try {
                            
                            	Class<?>[] interfaces = outboundPort.getClass().getInterfaces();
                                
                            	Class<?> paramType = interfaces.length > 0 ? interfaces[0] : outboundPort.getClass();

                                ReflectionUtils.set(instance, key, new Class<?>[]{paramType}, new Object[]{outboundPort});

                            } catch (Exception e) {
                                System.err.println("Erro ao injetar port '" + key + "': " + e.getMessage());
                            }
                        });

                        // Recursão — processa dependências do filho
                        if (instance instanceof AbstractUserCase<?> childCase) {
                            processDependenciesInternal(childCase, visited, level + 1);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Log visual hierárquico das dependências.
     */
    private static void logDependency(Object parent, Object child, int level) {
    	
        final String BLUE = "\u001B[34m";
        final String CYAN = "\u001B[36m";
        final String RESET = "\u001B[0m";

        String indent = " ".repeat(level * 2);
        System.out.println(BLUE + indent + "├── " + CYAN + child.getClass().getSimpleName() + RESET + " (inject into " + parent.getClass().getSimpleName() + ")");
    }
}