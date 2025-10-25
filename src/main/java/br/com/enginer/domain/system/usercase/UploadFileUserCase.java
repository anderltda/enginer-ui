package br.com.enginer.domain.system.usercase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.system.dto.entity.UploadFile;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.utils.FileNameUtils;
import br.com.enginer.domain.system.usercase.validator.UploadFileValidator;

/**
 * 
 */
public class UploadFileUserCase extends AbstractUserCase<UploadFile> {
	
	/**
	 * Realiza o upload do arquivo, salvando-o fisicamente e registrando seus
	 * metadados.
	 * 
	 * @param uploadFile
	 * @return UploadFile
	 * @throws UncheckedException
	 */
	public UploadFile uploadFile(UploadFile uploadFile) throws UncheckedException {
		
		UploadFileValidator validator = new UploadFileValidator(loggerOutboundPort, this);

		try {
		
			loggerOutboundPort.info(getClass(), "Iniciando upload: " + uploadFile.getName());

			validator.validateRequiredFields(uploadFile);

			String storageName = FileNameUtils.generateStorageName(uploadFile.getName());
			
			uploadFile.setStorageName(storageName);

			validator.validateDuplicity(uploadFile);

			Path destination = fileStorageOutboundPort.saveFile(storageName, new ByteArrayInputStream(uploadFile.getBytes()));

			String checksum = hashGeneratorOutboundPort.generateSha256(uploadFile.getBytes());

			uploadFile.setPath(destination.toString());
			uploadFile.setChecksumSha256(checksum);
			uploadFile.setCreatedAt(LocalDateTime.now());
			uploadFile.setIsPublic(false);

			validator.validateBeforeSave(uploadFile);

			uploadFile = super.salvar(uploadFile);

			loggerOutboundPort.info(getClass(), String.format("Upload concluído: %s (%s)", uploadFile.getName(), uploadFile.getStorageName()));

			return uploadFile;

		} catch (IOException e) {
			loggerOutboundPort.error(getClass(), "Erro de I/O ao salvar o arquivo: " + e.getMessage(), e);
			throw new UncheckedException("Erro ao fazer upload do arquivo: " + e.getMessage(), e);
		} catch (UncheckedException e) {
			loggerOutboundPort.error(getClass(), "Falha de validação ao processar upload: " + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			loggerOutboundPort.error(getClass(), "Erro inesperado no upload: " + e.getMessage(), e);
			throw new UncheckedException("Falha inesperada ao processar upload", e);
		}
	}

	/**
	 * Exclui o arquivo fisicamente e os metadados.
	 * 
	 * @param uploadFile
	 * @throws UncheckedException
	 */
	@Override
	public void excluir(UploadFile uploadFile) throws UncheckedException {
		
		try {
			
			loggerOutboundPort.info(getClass(), "Exclusão iniciada para arquivo: " + uploadFile.getName());

			// Busca o arquivo para garantir que ele existe
			uploadFile = buscarPorId(uploadFile);

			// Exclui fisicamente via porta de saída
			Path path = Path.of(uploadFile.getPath());
			fileStorageOutboundPort.deleteFile(path);

			// Exclui os metadados do banco
			super.excluir(uploadFile);

			loggerOutboundPort.info(getClass(), "Arquivo excluído com sucesso: " + uploadFile.getPath());

		} catch (IOException e) {
			loggerOutboundPort.error(getClass(), "Erro ao excluir arquivo: " + uploadFile.getName(), e);
			throw new UncheckedException("Erro ao excluir o arquivo: " + e.getMessage(), e);
		}
	}

	/**
	 * @param uploadFile
	 * @param id
	 * @return UploadFile
	 * @throws UncheckedException
	 */
	public UploadFile salvarEntityId(UploadFile uploadFile, Object id) throws UncheckedException {

		UploadFile file = (UploadFile) buscarPorId(uploadFile);

		file.setDomainId(id.toString());

		return super.salvar(file);
	}

	/**
	 * @param domain
	 * @param domainId
	 * @return List<UploadFile>
	 * @throws UncheckedException
	 */
	public List<UploadFile> buscarPorDomainAndDomainId(String domain, Object domainId) throws UncheckedException {

		Map<String, Object> filter = Map.of("domain", domain, "domainId", domainId.toString());

		List<UploadFile> files = super.buscarTodos(new UploadFile(), filter);

		return files;
	}

}
