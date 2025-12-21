package br.com.enginer.domain.system.usecase.port.upload;

import java.io.InputStream;

import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.usecase.ActionUseCase;

/**
 * 
 */
public interface UploadFileUseCase extends ActionUseCase<UploadFile> {
	
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
}
