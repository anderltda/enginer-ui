package br.com.enginer.domain.system.usecase.port.outbound.repository;

import br.com.enginer.domain.system.dto.entity.tag.Tag;

/**
 * Port outbound responsável por acesso a dados (camada repositório apenas de Tag).
 * Tipado com Tag para garantir segurança de tipo em toda a camada de casos de uso.
 */
public interface TagRepositoryOutboundPort extends RepositoryOutboundPort<Tag> {
	
}
