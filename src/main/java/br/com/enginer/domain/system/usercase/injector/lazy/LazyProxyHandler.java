package br.com.enginer.domain.system.usercase.injector.lazy;

import java.lang.reflect.Method;
import java.util.Map;

import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.injector.DependencyInjector;
import br.com.enginer.domain.system.usercase.port.outbound.OutboundPort;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;

public final class LazyProxyHandler<T extends AbstractUserCase<? extends Domain<?>>> {

    private final Class<T> targetClass;
    private T instance;
    private boolean initialized = false;

    // 🎨 Cores
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String MAGENTA = "\u001B[35m";

    public LazyProxyHandler(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    /** Cria ou retorna instância já inicializada */
    public synchronized T get() {
        if (!initialized) {
            try {
                long start = System.nanoTime();
                instance = targetClass.getDeclaredConstructor().newInstance();

                reinjectOutboundPorts();
                DependencyInjector.processDependencies(instance);

                long elapsed = System.nanoTime() - start;

                if (DependencyInjector.LOG_VERBOSE)
                    System.out.printf(MAGENTA + "   [Lazy]" + RESET +
                            " Criado %s (%.2f ms)%n",
                            targetClass.getSimpleName(), elapsed / 1_000_000.0);

                initialized = true;

            } catch (Exception e) {
                throw new RuntimeException("Erro ao inicializar UserCase: " + targetClass.getSimpleName(), e);
            }
        }
        return instance;
    }

    /** Reinjeção de todos os OutboundPorts conhecidos */
    public void reinjectOutboundPorts() {
        if (instance == null) return;
        long start = System.nanoTime();
        int injected = 0;

        for (Map.Entry<String, OutboundPort> entry : DependencyInjector.getOutboundPortCache().entrySet()) {
            boolean ok = invokeSetterFlexible(instance, entry.getKey(), entry.getValue());
            if (ok) injected++;
        }

        long elapsed = System.nanoTime() - start;

        if (DependencyInjector.LOG_VERBOSE && injected > 0)
            System.out.printf(YELLOW + "      [Reinject]" + RESET +
                    " %d OutboundPorts aplicados em %s (%.2f ms)%n",
                    injected, targetClass.getSimpleName(), elapsed / 1_000_000.0);
    }

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
}