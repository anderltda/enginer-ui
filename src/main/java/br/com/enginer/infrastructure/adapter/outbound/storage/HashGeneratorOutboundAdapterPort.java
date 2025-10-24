package br.com.enginer.infrastructure.adapter.outbound.storage;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.usercase.port.outbound.storage.HashGeneratorOutboundPort;

/**
 * Implementação concreta usando Apache Commons Codec.
 */
@Component
public class HashGeneratorOutboundAdapterPort implements HashGeneratorOutboundPort {

    @Override
    public String generateSha256(byte[] bytes) {
        return DigestUtils.sha256Hex(bytes);
    }
}
