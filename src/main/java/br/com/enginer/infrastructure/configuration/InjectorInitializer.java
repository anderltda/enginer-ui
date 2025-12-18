package br.com.enginer.infrastructure.configuration;

import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.usecase.port.outbound.DependencyInjectorPort;
import br.com.enginer.domain.system.usecase.registry.DependencyInjectorRegistry;
import br.com.enginer.infrastructure.injector.DependencyInjector;
import jakarta.annotation.PostConstruct;

/**
 * Inicializa e registra o DependencyInjector (camada de infraestrutura)
 * como a implementação oficial da porta {@link DependencyInjectorPort}.
 *
 * Essa classe é carregada automaticamente pelo Spring Boot no startup
 * e garante que o domínio possa resolver dependências via porta abstrata,
 * mantendo a arquitetura hexagonal limpa.
 */
@Component
public class InjectorInitializer {

    // --- Cores ANSI para console ---
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String BOLD = "\u001B[1m";

    /**
     * Registra a implementação concreta {@link DependencyInjector}
     * no {@link DependencyInjectorRegistry}.
     */
    @PostConstruct
    public void registerInjector() {
        
    	long start = System.nanoTime();

        DependencyInjectorPort existing = DependencyInjectorRegistry.getOptional().orElse(null);
        
        if (existing != null) {
            System.out.printf(MAGENTA + "[InjectorInitializer]" + RESET +
                    " Um DependencyInjector já está registrado: %s%n", existing.getClass().getSimpleName());
            return;
        }

        DependencyInjectorPort injector = new DependencyInjector();
        DependencyInjectorRegistry.register(injector);

        long elapsed = System.nanoTime() - start;

        System.out.printf("%n" + BOLD + CYAN + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━%n" + RESET);
        System.out.printf(GREEN + " → DependencyInjector inicializado e registrado com sucesso%n" + RESET);
        System.out.printf(CYAN + " → Classe concreta:   " + RESET + "%s%n", injector.getClass().getName());
        System.out.printf(CYAN + " → Local de registro: " + RESET + "%s%n",
                DependencyInjectorRegistry.class.getName());
        System.out.printf(CYAN + " → Tempo total:       " + RESET + "%.2f ms%n", elapsed / 1_000_000.0);
        System.out.printf(BOLD + CYAN + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━%n%n" + RESET);
    }
}