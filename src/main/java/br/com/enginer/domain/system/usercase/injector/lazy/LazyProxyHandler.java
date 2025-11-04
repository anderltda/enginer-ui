package br.com.enginer.domain.system.usercase.injector.lazy;

import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.injector.DependencyInjector;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;

/**
 * Lazy loader simples para UserCases concretos.
 * Cria instâncias apenas quando necessário.
 */
public final class LazyProxyHandler<T extends AbstractUserCase<? extends Domain<?>>> {

    private final Class<T> targetClass;
    private T instance;
    private boolean initialized = false;

    public LazyProxyHandler(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public synchronized T get() {
        if (!initialized) {
            try {
                instance = targetClass.getDeclaredConstructor().newInstance();
                DependencyInjector.processDependencies(instance);
                logCreation();
                initialized = true;
            } catch (Exception e) {
                throw new RuntimeException("Erro ao inicializar UserCase: " + targetClass.getSimpleName(), e);
            }
        }
        return instance;
    }

    private void logCreation() {
        final String GREEN = "\u001B[32m";
        final String RESET = "\u001B[0m";
        System.out.println(GREEN + "Lazy-loaded: " + targetClass.getSimpleName() + RESET);
    }
}