package br.com.enginer.domain.system.usercase.port.outbound;

import br.com.enginer.domain.system.usercase.AbstractUserCase;

/**
 * Porta de saída responsável pela injeção de dependências de UserCases.
 * Mantém o domínio desacoplado da implementação concreta na infraestrutura.
 *
 * Essa interface define as operações que permitem:
 * - Registrar OutboundPorts globalmente.
 * - Processar dependências anotadas com @AutoDependencyInjector recursivamente.
 */
public interface DependencyInjectorPort {

    /**
     * Registra os OutboundPorts disponíveis globalmente.
     *
     * @param outboundPorts lista de portas de saída a serem registradas
     */
    void registerOutboundPorts(OutboundPort... outboundPorts);

    /**
     * Processa e injeta as dependências de um UserCase raiz e seus filhos recursivamente.
     *
     * @param root instância do UserCase principal
     */
    void processDependencies(AbstractUserCase<?> root);
}