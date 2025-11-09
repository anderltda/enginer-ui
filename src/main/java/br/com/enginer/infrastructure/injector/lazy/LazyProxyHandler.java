package br.com.enginer.infrastructure.injector.lazy;

import java.lang.reflect.Method;
import java.util.Map;

import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.port.outbound.OutboundPort;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.infrastructure.injector.DependencyInjector;

/**
 * LazyProxyHandler
 *
 * Responsável por criar instâncias de UserCases sob demanda,
 * injetando automaticamente os OutboundPorts registrados e
 * processando as dependências internas via DependencyInjector.
 *
 * Cada UserCase é instanciado uma única vez (lazy singleton).
 */
public final class LazyProxyHandler<T extends AbstractUserCase<? extends Domain<?>>> {

    private final Class<T> targetClass;
    private T instance;
    private boolean initialized = false;

    // Cores ANSI para logs no console
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";

    /**
     * @param targetClass classe concreta do UserCase
     */
    public LazyProxyHandler(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    // ---------------------------------------------------------------------------------------
    // MÉTODOS PÚBLICOS
    // ---------------------------------------------------------------------------------------

    /**
     * Cria ou retorna a instância já inicializada do UserCase.
     * Inclui medição de tempo e logs de performance detalhados.
     */
    public synchronized T get() {
    	
        if (!initialized) {
        	
            try {
            	
                long start = System.nanoTime();

                // --- Instancia o UserCase ---
                instance = targetClass.getDeclaredConstructor().newInstance();

                // --- Injeta Outbounds conhecidos ---
                reinjectOutboundPorts();

                // --- Processa dependências internas (recursivamente) ---
                new DependencyInjector().processDependencies(instance);

                long elapsed = System.nanoTime() - start;
                double ms = elapsed / 1_000_000.0;

                if (DependencyInjector.LOG_VERBOSE) {
                    if (DependencyInjector.LOG_PERFORMANCE_ONLY) {
                        System.out.printf(
                            MAGENTA + " → LazyInit: %s (%.2f ms)%n" + RESET,
                            targetClass.getSimpleName(), ms
                        );
                    } else {
                        System.out.printf(
                            CYAN + "   [LazyProxy] " + RESET +
                            "Instanciado %s em %.2f ms%n",
                            targetClass.getSimpleName(), ms
                        );
                    }
                }

                initialized = true;
                
            } catch (Exception e) {
                throw new RuntimeException(
                    "Erro ao inicializar UserCase: " + targetClass.getSimpleName(), e
                );
            }
        }
        
        return instance;
    }

    /**
     * Reinjeção de todos os OutboundPorts conhecidos no UserCase.
     * Esse método é chamado automaticamente sempre que novos Outbounds são registrados.
     */
    public void reinjectOutboundPorts() {
    	
        if (instance == null) return;

        long start = System.nanoTime();
        int injected = 0;

        for (Map.Entry<String, OutboundPort> entry : DependencyInjector.getOutboundPortCache().entrySet()) {
            boolean ok = invokeSetterFlexible(instance, entry.getKey(), entry.getValue());
            if (ok) injected++;
        }

        long elapsed = System.nanoTime() - start;
        double ms = elapsed / 1_000_000.0;

        if (DependencyInjector.LOG_VERBOSE && injected > 0) {
            if (DependencyInjector.LOG_PERFORMANCE_ONLY) {
                System.out.printf(
                    GREEN + "   ↳ Reinjected %d Outbounds → %s (%.2f ms)%n" + RESET,
                    injected, targetClass.getSimpleName(), ms
                );
            } else {
                System.out.printf(
                    YELLOW + "      [Reinject]" + RESET +
                    " %d OutboundPorts aplicados em %s (%.2f ms)%n",
                    injected, targetClass.getSimpleName(), ms
                );
            }
        }
    }

    // ---------------------------------------------------------------------------------------
    // UTILITÁRIOS INTERNOS
    // ---------------------------------------------------------------------------------------

    /**
     * Invoca dinamicamente um setter compatível com o tipo da porta fornecida.
     * Busca pelo tipo exato, pelas interfaces implementadas e pela superclasse.
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

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}