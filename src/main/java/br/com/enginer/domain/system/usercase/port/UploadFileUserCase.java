package br.com.enginer.domain.system.usercase.port;

import java.io.InputStream;
import java.util.List;

import br.com.enginer.domain.system.dto.entity.UploadFile;
import br.com.enginer.domain.system.usercase.ActionUserCase;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;

/**
 * 
 */
public interface UploadFileUserCase extends ActionUserCase<UploadFile> {
	
	/** 
	 * Inicia uma sessão de upload (idempotente). 
	 */
	String startSession();

	/**
	 * Recebe 1 chunk (delegando ao adapter). 
	 */
	void uploadChunk(String uploadId, int chunkIndex, InputStream content);

	/**
	 * Finaliza: faz merge, calcula checksum/size e persiste metadados. 
	 */
	UploadFile finalizeUpload(UploadFile uploadFile);

	/**
	 * Associa um arquivo a uma entidade de domínio existente, movendo-o
	 * para o diretório definitivo e atualizando seu metadado.
	 *
	 * @param uploadFile o arquivo previamente salvo (com domainId nulo)
	 * @param id identificador da entidade à qual o arquivo será associado
	 * @return UploadFile atualizado e persistido
	 * @throws UncheckedException caso ocorra erro de I/O ou persistência
	 */
	public UploadFile salvarEntityId(UploadFile uploadFile, Object id) throws UncheckedException;

	/**
	 * @param domain
	 * @param domainId
	 * @return List<UploadFile>
	 * @throws UncheckedException
	 */
	List<UploadFile> buscarPorDomainAndDomainId(String domain, Object domainId) throws UncheckedException;

}
