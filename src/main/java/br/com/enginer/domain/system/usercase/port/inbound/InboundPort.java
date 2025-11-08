package br.com.enginer.domain.system.usercase.port.inbound;

/**
 * Porta de entrada base (InboundPort) da arquitetura hexagonal.
 * 
 * <p>Define um contrato genérico para todos os casos de uso (UserCases)
 * que representam interações que entram no domínio, como controllers,
 * REST endpoints, CLI handlers, etc.</p>
 *
 * <p>Usada como tipo raiz para padronizar dependências e permitir
 * extensões genéricas em interfaces como {@link br.com.enginer.domain.system.usercase.port.inbound.api.ActionInboundPort}.</p>
 */
public interface InboundPort {
    // Interface marcadora (sem métodos)
    // → usada apenas para categorização de "Portas de Entrada"
}