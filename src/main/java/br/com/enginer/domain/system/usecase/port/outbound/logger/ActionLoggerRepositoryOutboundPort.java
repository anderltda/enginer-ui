package br.com.enginer.domain.system.usecase.port.outbound.logger;

import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;

/**
 * Port outbound responsável por acesso a dados (camada repositório apenas de ActionLogger).
 * Tipado com ActionLogger para garantir segurança de tipo em toda a camada de casos de uso.
 */
public interface ActionLoggerRepositoryOutboundPort extends RepositoryOutboundPort<ActionLogger> {

}
