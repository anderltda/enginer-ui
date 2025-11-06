package br.com.enginer.domain.system.usercase.injector.lazy;

import java.lang.reflect.Method;
import java.util.Map;

import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.injector.DependencyInjector;
import br.com.enginer.domain.system.usercase.port.OutboundPort;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;

/**
 * Lazy loader para UserCases concretos.
 * Injeta OutboundPorts automaticamente e mede tempo de criação.
 */
public final class LazyProxyHandler<T extends AbstractUserCase<? extends Domain<?>>> {

    private final Class<T> targetClass;
    private T instance;
    private boolean initialized = false;

    /**
     * @param targetClass
     */
    public LazyProxyHandler(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * @return
     */
    public synchronized T get() {
    	
        if (!initialized) {
        	
            try {
            	
                long start = System.nanoTime();
                
                instance = targetClass.getDeclaredConstructor().newInstance();

				// Injeta todos os OutboundPorts conhecidos
				for (Map.Entry<String, OutboundPort> entry : DependencyInjector.getOutboundPortCache().entrySet()) {

					String methodName = entry.getKey();
					
					OutboundPort outboundPort = entry.getValue();
					
					invokeSetterFlexible(instance, methodName, outboundPort);
				}

                // Injeta dependências internas recursivamente
                DependencyInjector.processDependencies(instance);

                long elapsed = System.nanoTime() - start;
                
                logCreation(elapsed);
                
                initialized = true;

            } catch (Exception e) {
                throw new RuntimeException("Erro ao inicializar UserCase: " + targetClass.getSimpleName(), e);
            }
        }

        return instance;
    }

    /**
     * @param target
     * @param methodName
     * @param arg
     * @return
     */
	private boolean invokeSetterFlexible(Object target, String methodName, Object arg) {
		
        try {
        
        	Class<?>[] interfaces = arg.getClass().getInterfaces();
            
        	Class<?> expectedType = interfaces.length > 0 ? interfaces[0] : arg.getClass();

            Method m = target.getClass().getMethod(methodName, expectedType);
            m.invoke(target, arg);
            return true;

        } catch (NoSuchMethodException e1) {
            
        	for (Class<?> iface : arg.getClass().getInterfaces()) {
        		try {
                    Method m2 = target.getClass().getMethod(methodName, iface);
                    m2.invoke(target, arg);
                    return true;
                } catch (Exception ignore) {}
            }

        	try {
                Method m3 = target.getClass().getMethod(methodName, arg.getClass().getSuperclass());
                m3.invoke(target, arg);
                return true;
            } catch (Exception ignore) {}
            return false;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param nanos
     */
    private void logCreation(long nanos) {
        final String YELLOW = "\u001B[33m";
        final String RESET = "\u001B[0m";
        System.out.printf(YELLOW + "Lazy-loaded: %s (%.2f ms)" + RESET + "%n", targetClass.getSimpleName(), nanos / 1_000_000.0);
    }
}