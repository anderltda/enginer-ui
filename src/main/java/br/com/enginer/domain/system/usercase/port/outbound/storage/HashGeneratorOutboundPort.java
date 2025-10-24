package br.com.enginer.domain.system.usercase.port.outbound.storage;

import br.com.enginer.domain.system.usercase.port.OutboundPort;

/**
 * Porta de saída responsável por gerar checksums ou hashes de arquivos.
 * Permite trocar a implementação sem impactar o domínio.
 */
public interface HashGeneratorOutboundPort extends OutboundPort {

    /**
     * Gera o hash SHA-256 para o conteúdo fornecido.
     *
     * @param bytes conteúdo em bytes
     * @return hash hexadecimal SHA-256
     */
    String generateSha256(byte[] bytes);
}
