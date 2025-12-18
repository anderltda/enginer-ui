package br.com.enginer.domain.system.usecase.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.usecase.AbstractUseCase;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;

/**
 * 
 */
public class UploadFileUseCase extends AbstractUseCase<UploadFile> implements br.com.enginer.domain.system.usecase.port.upload.UploadFileUseCase {
	
    private static final String PATH_DIR_FINAL = "final";

	/** 
	 * Inicia uma sessão de upload (idempotente). 
	 */
    @Override
	public String startSession() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * Recebe 1 chunk (delegando ao adapter). 
	 */
    @Override    
	public void uploadChunk(String uploadId, int chunkIndex, InputStream content) {
		
		try {
			
			loggerOutboundPort.info(getClass(), "Chunk recebido: " + uploadId + " idx=" + chunkIndex);
			
			fileStorageOutboundPort.saveChunk(uploadId, chunkIndex, content);
			
		} catch (IOException e) {
			throw new UncheckedException("Falha ao salvar chunk: " + e.getMessage(), e);
		}
	}

	/**
	 * Finaliza: faz merge, calcula checksum/size e persiste metadados. 
	 */
    @Override
	public UploadFile finalizeUpload(UploadFile uploadFile) {

		try {

			// valida campos obrigatórios mínimos
			if (uploadFile.getName() == null || uploadFile.getType() == null) {
				throw new UncheckedException("Nome e tipo MIME são obrigatórios.");
			}

			// gera storageName único (nome-base + UUID + extensão)
			String original = uploadFile.getName();
			String ext = "";
			int dot = original.lastIndexOf('.');

			if (dot >= 0) {
				ext = original.substring(dot);
			}
			
			String base = (dot >= 0 ? original.substring(0, dot) : original);
			
			String storageName = base + "-" + UUID.randomUUID().toString().replace("-", "").toUpperCase() + ext;
			uploadFile.setStorageName(storageName);

			// resolve caminho final no storage
			Path finalPath = fileStorageOutboundPort.resolveFinalPath(storageName);

			// merge em streaming
			fileStorageOutboundPort.mergeChunks(uploadFile.getUid(), finalPath);

			// calcula checksum e tamanho em streaming (sem carregar tudo na RAM)
			Map<String, Object> result = calculateChecksumAndSize(finalPath);
			uploadFile.setPath(finalPath.toString());
			uploadFile.setStorageType("LOCAL"); // "LOCAL" ou "S3"
			uploadFile.setChecksumSha256((String) result.get("checksum"));
			uploadFile.setSize((Long) result.get("size"));
			uploadFile.setIsPublic(false);
			uploadFile.setCreatedAt(LocalDateTime.now());

			// flexível: duplicidade/negócio tratado por código, não por constraints
			// (adicione suas validações aqui, se desejar)

			UploadFile saved = super.salvar(uploadFile);

			// limpa temporários da sessão
			fileStorageOutboundPort.cleanupSession(uploadFile.getUid());

			loggerOutboundPort.info(getClass(), "Upload finalizado: " + saved.getStorageName() + " (" + saved.getSize() + " bytes)");

			return saved;

		} catch (UncheckedException e) {
			throw e;
		} catch (Exception e) {
			throw new UncheckedException("Falha ao finalizar upload: " + e.getMessage(), e);
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
	 * Associa um arquivo a uma entidade de domínio existente, movendo-o
	 * para o diretório definitivo e atualizando seu metadado.
	 *
	 * @param uploadFile o arquivo previamente salvo (com domainId nulo)
	 * @param id identificador da entidade à qual o arquivo será associado
	 * @return UploadFile atualizado e persistido
	 * @throws UncheckedException caso ocorra erro de I/O ou persistência
	 */
    @Override
	public UploadFile salvarEntityId(UploadFile uploadFile, Object id) throws UncheckedException {
		
	    try {
	    
	    	// Busca o metadado atual
	        UploadFile file = (UploadFile) buscarPorId(uploadFile);

	        if (file == null) {
	            throw new UncheckedException("Arquivo não encontrado para associação.");
	        }

	        // Define o novo domainId
	        file.setDomainId(id.toString());

	        // Define o diretório final: /uploads/<domain>/<domainId>/
	        //String finalDirectory = String.format("%s/%s/", file.getDomain(), id);
	        String finalDirectory = String.format("%s/", PATH_DIR_FINAL);

	        // Move fisicamente o arquivo via FileStorageOutboundPort
	        Path newPath = fileStorageOutboundPort.moveFile(Path.of(file.getPath()), finalDirectory);

	        // Atualiza o metadado com o novo path
	        file.setPath(newPath.toString());

	        // Persiste a atualização
	        file = super.salvar(file);

	        loggerOutboundPort.info(getClass(), String.format("Arquivo [%s] movido para [%s]", file.getName(), newPath));

	        return file;

	    } catch (IOException e) {
	        throw new UncheckedException("Erro ao mover o arquivo após associação: " + e.getMessage(), e);
	    }
	}

	/**
	 * @param domain
	 * @param domainId
	 * @return List<UploadFile>
	 * @throws UncheckedException
	 */
    @Override
	public List<UploadFile> buscarPorDomainAndDomainId(String domain, Object domainId) throws UncheckedException {

		Map<String, Object> filter = Map.of("domain", domain.toUpperCase(), "domainId", domainId.toString());

		List<UploadFile> files = super.buscarTodos(new UploadFile(), filter);
		
		return files;
	}

	/**
	 * Calcula o checksum SHA-256 e o tamanho de um arquivo sem carregar tudo na
	 * memória. Ideal para grandes arquivos (usa streaming).
	 * @param path caminho do arquivo no sistema de arquivos
	 * @return Map com "checksum" e "size"
	 * @throws IOException caso ocorra falha de leitura
	 */
	private Map<String, Object> calculateChecksumAndSize(Path path) throws IOException {
		
		try {

			MessageDigest md = MessageDigest.getInstance("SHA-256");

			long size;
			
			try (InputStream in = Files.newInputStream(path); DigestInputStream dis = new DigestInputStream(in, md)) {
				size = dis.transferTo(OutputStream.nullOutputStream());
			}

			String sha256 = HexFormat.of().formatHex(md.digest());
			
			return Map.of("checksum", sha256, "size", size);
			
		} catch (Exception e) {
			throw new IOException("Algoritmo SHA-256 não disponível.", e);
		}
	}
}
