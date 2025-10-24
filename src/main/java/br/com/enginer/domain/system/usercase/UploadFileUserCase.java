package br.com.enginer.domain.system.usercase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.system.dto.entity.UploadFile;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;

/**
 * 
 */
public class UploadFileUserCase extends AbstractUserCase<UploadFile> {
	
    /**
     * Faz o upload físico e salva os metadados.
     */
    public UploadFile uploadFile(UploadFile uploadFile) {
    	
        try {
            
            Path destination = fileStorageOutboundPort.saveFile(uploadFile.getName(), new ByteArrayInputStream(uploadFile.getBytes()));
            
            String checksum = hashGeneratorOutboundPort.generateSha256(uploadFile.getBytes());

            uploadFile.setPath(destination.toString());
            uploadFile.setChecksumSha256(checksum);
            uploadFile.setCreatedAt(LocalDateTime.now());
            uploadFile.setIsPublic(false);

            return super.salvar(uploadFile);

        } catch (IOException e) {
            throw new UncheckedException("Erro ao fazer upload do arquivo: " + e.getMessage(), e);
        }
    }    
    
	/**
	 * @param uploadFile
	 * @throws UncheckedException
	 */
	@Override
	public void excluir(UploadFile uploadFile) throws UncheckedException {

		try {
			uploadFile = buscarPorId(uploadFile);
			// Exclui o arquivo fisicamente
			Path path = Path.of(uploadFile.getPath());
			Files.deleteIfExists(path);
			// Exclui os metadados
			super.excluir(uploadFile);
			
		} catch (IOException e) {
			throw new UncheckedException("Erro ao excluir o arquivo: " + e.getMessage(), e);
		}
	}

	/**
	 * @param uploadFile
	 * @param id
	 * @return
	 * @throws UncheckedException
	 */
	public UploadFile salvarEntityId(UploadFile uploadFile, Object id) throws UncheckedException {

		UploadFile file = (UploadFile) buscarPorId(uploadFile);

		file.setDomainId(id.toString());

		return super.salvar(file);
	}

	/**
	 * @param entityId
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	public List<UploadFile> buscarPorEntityIdAndDomain(Object entityId, String domain) throws UncheckedException {

		Map<String, Object> filter = Map.of("domain", domain, "domainId", entityId.toString());

		List<UploadFile> files = super.buscarTodos(new UploadFile(), filter);

		return files;
	}

}
