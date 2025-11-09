package br.com.enginer.infrastructure.injector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.port.outbound.DependencyInjectorPort;
import br.com.enginer.domain.system.usercase.port.outbound.OutboundPort;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.infrastructure.injector.lazy.LazyProxyHandler;
import br.com.enginer.infrastructure.injector.metrics.InjectionMetrics;
import br.com.enginer.infrastructure.injector.metrics.InjectionMetricsHistory;

/**
 * Gerencia o registro e injeção de dependências entre UserCases e OutboundPorts.
 * 
 * Localizado na camada de INFRAESTRUTURA, este componente:
 *  - mantém caches globais de UserCases e OutboundPorts,
 *  - executa injeção reflexiva hierárquica (@AutoDependencyInjector),
 *  - coleta métricas de performance,
 *  - registra histórico cumulativo de injeções.
 */
public final class DependencyInjector implements DependencyInjectorPort {

    /** Configuração de log */
    public static boolean LOG_VERBOSE = true;             // logs detalhados (modo DEV)
    public static boolean LOG_PERFORMANCE_ONLY = true;     // logs resumidos (modo PROD)

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    /** 
     * Caches globais 
     */
    private static final Map<String, OutboundPort> OUTBOUND_PORT_CACHE = new ConcurrentHashMap<>();
    
    private static final Map<Class<?>, LazyProxyHandler<?>> LAZY_CACHE = new ConcurrentHashMap<>();

    /** 
     * Acesso ao cache global de OutboundPorts 
     */
    public static Map<String, OutboundPort> getOutboundPortCache() {
        return OUTBOUND_PORT_CACHE;
    }

    /** 
     * Retorna (ou cria) um LazyProxyHandler para o UserCase informado 
     */
    @SuppressWarnings("unchecked")
    public static <T extends AbstractUserCase<? extends Domain<?>>> LazyProxyHandler<T> getLazyHandler(Class<T> clazz) {
        return (LazyProxyHandler<T>) LAZY_CACHE.computeIfAbsent(clazz, c -> new LazyProxyHandler<>(clazz));
    }

    // ---------------------------------------------------------------------------------------
    // REGISTRO DE OUTBOUND PORTS
    // ---------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * 
     * Registra OutboundPorts no cache global e reinjeta em todos os UserCases ativos,
     * medindo o tempo total da operação e atualizando métricas globais.
     */
    @Override
    public void registerOutboundPorts(OutboundPort... outboundPorts) {
    	
        if (outboundPorts == null) return;

        long start = System.nanoTime();
        int count = 0;

        // --- Registro no cache global ---
        for (OutboundPort port : outboundPorts) {
        	
            if (port == null) continue;

            Class<?>[] interfaces = port.getClass().getInterfaces();
            
            Class<?> iface = interfaces.length > 0 ? interfaces[0] : port.getClass();
            
            String ifaceName = iface.getSimpleName();
            
            String setMethod = "set" + ifaceName.substring(0, 1).toUpperCase() + ifaceName.substring(1);

            OUTBOUND_PORT_CACHE.put(setMethod, port);
            
            count++;

            if (LOG_VERBOSE && !LOG_PERFORMANCE_ONLY) {
                System.out.printf(GREEN + "   [Cache] " + RESET + "%s → %s%n", ifaceName, port.getClass().getSimpleName());
            }
        }

        int totalUserCases = LAZY_CACHE.size();

        // --- Reinjeção de dependências em UserCases já carregados ---
        if (totalUserCases > 0) {
        	
            if (LOG_VERBOSE && !LOG_PERFORMANCE_ONLY) {
                System.out.printf(BLUE + "   [Injector] " + RESET + "Reaplicando dependências em %d UserCases...%n", totalUserCases);
            }

            long reinjectStart = System.nanoTime();

            for (LazyProxyHandler<?> handler : LAZY_CACHE.values()) {
                handler.reinjectOutboundPorts();
            }

            long reinjectEnd = System.nanoTime();
            
            double reinjectMs = (reinjectEnd - reinjectStart) / 1_000_000.0;

            if (LOG_VERBOSE && !LOG_PERFORMANCE_ONLY) {
                System.out.printf(CYAN + "   [Injector] Reinjeção concluída em %.2f ms%n" + RESET, reinjectMs);
            }
        }

        // --- Métricas ---
        long end = System.nanoTime();
        
        double totalMs = (end - start) / 1_000_000.0;

        InjectionMetrics.update(totalUserCases, count, totalMs);
        
        printSummary(totalMs, count, totalUserCases);
        
    }

    /** 
     * Exibe um resumo da injeção 
     */
    private static void printSummary(double totalMs, int outbounds, int userCases) {
    	
        int totalExecutions = InjectionMetricsHistory.getTotalExecutions();
        
        double avgTime = InjectionMetricsHistory.getAverageTimeMs();

        if (LOG_PERFORMANCE_ONLY) {
        	
            System.out.printf(MAGENTA + " → Injeção concluída | UserCases: %d | Outbounds: %d | Tempo: %.2f ms | Execuções: %d | Média: %.2f ms%n" + RESET, userCases, outbounds, totalMs, totalExecutions, avgTime);
            
            return;
        }

        // Modo detalhado (DEV)
        System.out.printf("%n" + BOLD + MAGENTA + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━%n" + "   → Injeção concluída com sucesso%n" + RESET);
        System.out.printf(BOLD + CYAN + "   → UserCases ativos:  %-4d%n", userCases);
        System.out.printf(BOLD + CYAN + "   → OutboundPorts:     %-4d%n", outbounds);
        System.out.printf(BOLD + CYAN + "   → Tempo total:       %.2f ms%n", totalMs);
        System.out.printf(MAGENTA + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━%n" + RESET);
        System.out.printf(GREEN + "→ Histórico atualizado → " + RESET + "%s%n", InjectionMetricsHistory.getHistory());
        System.out.printf(YELLOW + "→ Estatísticas cumulativas → " + RESET + "Injeções: %d | Média: %.2f ms%n%n", totalExecutions, avgTime);
    }

    // ---------------------------------------------------------------------------------------
    // PROCESSAMENTO HIERÁRQUICO DE DEPENDÊNCIAS
    // ---------------------------------------------------------------------------------------

    /** 
     * {@inheritDoc} 
     */
    @Override
    public void processDependencies(AbstractUserCase<?> root) {
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
            Class<? extends AbstractUserCase<?>> userCaseImplClass = (Class<? extends AbstractUserCase<?>>) resolveImplementationClass(type);

            AbstractUserCase<?> dependency = getLazyHandler(userCaseImplClass).get();
            
            field.setAccessible(true);
            
            try {
            	
                field.set(root, dependency);
                
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Erro ao injetar " + userCaseImplClass.getSimpleName() + " em " + root.getClass().getSimpleName(), ex);
            }

            processDependenciesInternal(dependency, visited, depth + 1);
        }
    }

    // ---------------------------------------------------------------------------------------
    // UTILITÁRIOS
    // ---------------------------------------------------------------------------------------

    /** 
     * Resolve automaticamente a classe concreta de uma interface de UserCase. 
     */
    private static Class<?> resolveImplementationClass(Class<?> iface) {
    	
        if (!iface.isInterface()) return iface;
        
        String ifaceName = iface.getName();
        
        String implName = ifaceName.replace(".port", "");
        
        try {
            return Class.forName(implName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Implementação concreta não encontrada para interface: " + ifaceName);
        }
    }

    /** 
     * Invoca um setter de forma flexível e proxy-safe 
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
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /** 
     * Método público para injetar Outbounds e processar dependências em um domínio raiz 
     */
    public static void addOutboundPort(AbstractUserCase<?> domain, OutboundPort... outboundPorts) {
    	
        if (domain == null || outboundPorts == null) return;
        
        DependencyInjector dependencyInjector = new DependencyInjector();
        
        dependencyInjector.registerOutboundPorts(outboundPorts);

        for (OutboundPort outboundPort : outboundPorts) {
        	
            if (outboundPort == null) continue;
            
            Class<?>[] interfaces = outboundPort.getClass().getInterfaces();
            
            Class<?> portInterface = interfaces.length > 0 ? interfaces[0] : outboundPort.getClass();
            
            String interfaceName = portInterface.getSimpleName();
            
            String setMethod = "set" + interfaceName.substring(0, 1).toUpperCase() + interfaceName.substring(1);
            
            invokeSetterFlexible(domain, setMethod, outboundPort, portInterface);
        }

        dependencyInjector.processDependencies(domain);
    }
}