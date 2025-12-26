package br.com.enginer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.enginer.domain.system.usecase.core.registry.DependencyInjectorRegistry;
import br.com.enginer.domain.system.usecase.port.outbound.DependencyInjectorPort;
import br.com.enginer.infrastructure.injector.DependencyInjector;

/**
 * Teste de integração para validar o registro automático do DependencyInjector.
 *
 * Simula o comportamento real do Spring Boot:
 * → o InjectorInitializer é detectado pelo Spring (@Component)
 * → o método @PostConstruct executa
 * → o DependencyInjector é registrado no registry global
 */
@SpringBootTest
@ActiveProfiles("test")
class InjectorInitializerIntegrationTest {

    @Test
    void deveRegistrarAutomaticamenteDependencyInjectorAoIniciarSpring() {
        // Quando o contexto Spring sobe, o InjectorInitializer deve rodar automaticamente.
        DependencyInjectorPort injector = DependencyInjectorRegistry.get();

        // Validações principais
        assertNotNull(injector, "O DependencyInjector não foi registrado automaticamente.");
        assertTrue(injector instanceof DependencyInjector,
                "O injetor registrado não é a implementação esperada da infraestrutura.");

        System.out.println("\n DependencyInjector foi inicializado automaticamente com sucesso!");
        System.out.printf("   → Classe concreta: %s%n", injector.getClass().getName());
    }

    @Test
    void deveRetornarMesmaInstanciaParaMultiplasChamadas() {
    	
        DependencyInjectorPort injector1 = DependencyInjectorRegistry.get();
        DependencyInjectorPort injector2 = DependencyInjectorRegistry.get();

        assertSame(injector1, injector2,
                "As chamadas subsequentes a DependencyInjectorRegistry.get() devem retornar a mesma instância.");
    }
}