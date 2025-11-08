package br.com.enginer.domain.system.usercase.port.outbound;

/**
 * Porta de saída base (OutboundPort) da arquitetura hexagonal.
 * 
 * <p>Define o contrato genérico para todos os adaptadores de saída
 * do domínio, como repositórios, mensageria (publishers/subscribers),
 * integrações externas e serviços de infraestrutura.</p>
 * 
 * <p>Esta interface é <b>marcadora</b>, ou seja, não define métodos —
 * seu propósito é identificar claramente todos os componentes que
 * realizam comunicação de saída do domínio.</p>
 * 
 * <h4>Exemplos de implementações:</h4>
 * <ul>
 *   <li>{@code RepositoryOutboundPort} – acesso a banco de dados</li>
 *   <li>{@code PublisherOutboundPort} – publicação de eventos em mensageria</li>
 *   <li>{@code LoggerOutboundPort} – persistência de logs de domínio</li>
 * </ul>
 */
public interface OutboundPort {
    // Interface marcadora para portas de saída (ex: RepositoryOutboundPort, PublisherOutboundPort)
}