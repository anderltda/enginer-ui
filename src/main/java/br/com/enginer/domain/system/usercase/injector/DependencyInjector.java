package br.com.enginer.domain.system.usercase.injector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.injector.lazy.LazyProxyHandler;
import br.com.enginer.domain.system.usercase.port.OutboundPort;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;

/**
 * Responsável por:
 * Registrar e injetar todos os OutboundPorts no UserCase raiz
 * Resolver dependências @AutoDependencyInjector recursivamente
 * Suportar proxies e beans Spring via fallback por interface
 */
public final class DependencyInjector {


	/** 
     * Cache global de OutboundPorts injetados 
     */
    private static final Map<String, OutboundPort> OUTBOUND_PORT_CACHE = new ConcurrentHashMap<>();

    /** 
     * Cache de LazyProxyHandlers para UserCases 
     */
    private static final Map<Class<?>, LazyProxyHandler<?>> LAZY_CACHE = new ConcurrentHashMap<>();

    /**
     * 
     */
    private DependencyInjector() {}

    /** 
     * Acesso público ao cache global 
     */
    public static Map<String, OutboundPort> getOutboundPortCache() {
        return OUTBOUND_PORT_CACHE;
    }

    /** 
     * Recupera (ou cria) um LazyProxyHandler 
     */
    @SuppressWarnings("unchecked")
    public static <T extends AbstractUserCase<? extends Domain<?>>> LazyProxyHandler<T> getLazyHandler(Class<T> clazz) {
        return (LazyProxyHandler<T>) LAZY_CACHE.computeIfAbsent(clazz, c -> new LazyProxyHandler<>(clazz));
    } 

    /** 
     * Registra OutboundPorts no cache global 
     */
    public static void registerOutboundPorts(OutboundPort... outboundPorts) {
        if (outboundPorts == null) return;
        for (OutboundPort port : outboundPorts) {
            if (port == null) continue;
            Class<?>[] interfaces = port.getClass().getInterfaces();
            Class<?> iface = interfaces.length > 0 ? interfaces[0] : port.getClass();
            String ifaceName = iface.getSimpleName();
            String setMethod = "set" + ifaceName.substring(0, 1).toUpperCase() + ifaceName.substring(1);
            OUTBOUND_PORT_CACHE.put(setMethod, port);
        }
    }

    /** 
     * Registra e injeta os OutboundPorts + resolve dependências internas 
     */
	public static void addOutboundPort(AbstractUserCase<?> domain, OutboundPort... outboundPorts) {

		if (domain == null || outboundPorts == null) return;

		registerOutboundPorts(outboundPorts);

		for (OutboundPort outboundPort : outboundPorts) {

			if (outboundPort == null) continue;

			Class<?>[] interfaces = outboundPort.getClass().getInterfaces();
			Class<?> portInterface = interfaces.length > 0 ? interfaces[0] : outboundPort.getClass();

			String interfaceName = portInterface.getSimpleName();
			String setMethod = "set" + interfaceName.substring(0, 1).toUpperCase() + interfaceName.substring(1);

			invokeSetterFlexible(domain, setMethod, outboundPort, portInterface);

		}

		processDependencies(domain);
	}

    /** 
     * Tenta invocar o setter de forma flexível (proxy-safe) 
     */
    private static boolean invokeSetterFlexible(Object domain, String methodName, Object arg, Class<?> expectedType) {
    	
        try {
        
        	Method m = domain.getClass().getMethod(methodName, expectedType);
            m.invoke(domain, arg);
            return true;
        
        } catch (NoSuchMethodException e1) {
        	
            for (Class<?> iface : arg.getClass().getInterfaces()) {
                try {
                    Method m2 = domain.getClass().getMethod(methodName, iface);
                    m2.invoke(domain, arg);
                    return true;
                } catch (Exception ignore) {}
            }
            
            try {
            	
                Method m3 = domain.getClass().getMethod(methodName, arg.getClass().getSuperclass());
                m3.invoke(domain, arg);
                return true;
                
            } catch (Exception ignore) {}
            
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 
     * Ponto de entrada principal da injeção hierárquica 
     */
    public static void processDependencies(AbstractUserCase<?> root) {
        if (root == null) return;
        processDependenciesInternal(root, new HashSet<>(), 0);
    }

    /** 
     * Injeção recursiva de UserCases dependentes 
     */
    private static void processDependenciesInternal(AbstractUserCase<?> root, Set<Class<?>> visited, int depth) {
    	
        if (!visited.add(root.getClass())) return;

        for (Field field : root.getClass().getDeclaredFields()) {
        	
            if (!field.isAnnotationPresent(AutoDependencyInjector.class)) continue;

            Class<?> type = field.getType();
            
            if (!AbstractUserCase.class.isAssignableFrom(type)) continue;

            @SuppressWarnings("unchecked")
            Class<? extends AbstractUserCase<?>> userCaseClass = (Class<? extends AbstractUserCase<?>>) type;

            AbstractUserCase<?> dependency = getLazyHandler(userCaseClass).get();

            field.setAccessible(true);
            
            try {
            	
                field.set(root, dependency);
                logLink(depth, root.getClass(), userCaseClass);
                
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao injetar " + userCaseClass.getSimpleName() + " em " + root.getClass().getSimpleName(), e);
            }

            processDependenciesInternal(dependency, visited, depth + 1);
        }
    }

    /**
     * @param depth
     * @param owner
     * @param dep
     */
    private static void logLink(int depth, Class<?> owner, Class<?> dep) {
        //String prefix = "  ".repeat(depth);
        //System.out.printf("%s├── %s (injetado em %s)%n", prefix, dep.getSimpleName(), owner.getSimpleName());
    }
}