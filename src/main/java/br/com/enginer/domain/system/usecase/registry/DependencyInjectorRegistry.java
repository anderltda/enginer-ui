package br.com.enginer.domain.system.usecase.registry;

import java.util.Optional;

import br.com.enginer.domain.system.usecase.port.outbound.DependencyInjectorPort;

/**
 * Registro global do {@link DependencyInjectorPort}.
 * 
 * Atua como um Service Locator controlado, permitindo que o domínio
 * recupere a implementação concreta (geralmente da camada de infraestrutura)
 * sem conhecer a classe real.
 *
 * Segue o princípio de inversão de dependência: o domínio depende apenas da
 * abstração (porta), não da implementação.
 */
public final class DependencyInjectorRegistry {

    private static volatile DependencyInjectorPort injector;

    /**
     * Construtor privado para nao ser instanciado
     */
    private DependencyInjectorRegistry() {}

    /**
     * Registra a implementação concreta do {@link DependencyInjectorPort}.
     * 
     * @param impl implementação concreta do injetor
     */
    public static synchronized void register(DependencyInjectorPort impl) {
    	
        if (injector != null) {
        
        	System.err.println("[WARN] DependencyInjector já foi registrado: " +
                    injector.getClass().getName());
            
        	return;
        }
        
        injector = impl;
    }

    /**
     * Obtém o injetor registrado (lança erro se não houver registro).
     * 
     * @return instância do injetor registrada
     */
    public static DependencyInjectorPort get() {
    	
        if (injector == null) {
            throw new IllegalStateException("Nenhum DependencyInjector foi registrado ainda.");
        }
        
        return injector;
    }

    /**
     * Retorna o injetor registrado (se houver), sem lançar exceção.
     * 
     * @return Optional contendo o injetor (ou vazio)
     */
    public static Optional<DependencyInjectorPort> getOptional() {
        return Optional.ofNullable(injector);
    }

    /**
     * Limpa o registro global (usado em testes).
     */
    public static synchronized void clear() {
        injector = null;
    }
}