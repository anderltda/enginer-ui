package br.com.enginer.domain.system.usecase.port.outbound.repository;

import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;

/**
 * Port outbound responsável por acesso a dados (camada repositório apenas de UploadFile).
 * Tipado com UploadFile para garantir segurança de tipo em toda a camada de casos de uso.
 */
public interface UploadFileRepositoryOutboundPort extends RepositoryOutboundPort<UploadFile> {
	
	/**
	 * @param domain
	 */
	void pull(Domain<?> domain);
	
	/**
	 * @param domain
	 */
	void push(Domain<?> domain);
}
