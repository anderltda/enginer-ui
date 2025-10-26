package br.com.enginer.domain.system.usercase;

import java.io.ByteArrayInputStream;
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

import br.com.enginer.domain.system.dto.entity.UploadFile;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.utils.FileNameUtils;
import br.com.enginer.domain.system.usercase.validator.UploadFileValidator;

/**
 * 
 */
public class UploadFileUserCase extends AbstractUserCase<UploadFile> {

	/** 
	 * 
	 * Inicia uma sessão de upload (idempotente). 
	 * 
	 */
	public String startSession() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 *  
	 * Recebe 1 chunk (delegando ao adapter). 
	 * 
	 */
	public void uploadChunk(String uploadId, int chunkIndex, InputStream content) {
		
		try {
			
			loggerOutboundPort.info(getClass(), "Chunk recebido: " + uploadId + " idx=" + chunkIndex);
			
			fileChunkStorageOutboundPort.saveChunk(uploadId, chunkIndex, content);
			
		} catch (IOException e) {
			throw new UncheckedException("Falha ao salvar chunk: " + e.getMessage(), e);
		}
	}

	/**
	 *  
	 * Finaliza: faz merge, calcula checksum/size e persiste metadados. 
	 *
	 */
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
			if (dot >= 0)
				ext = original.substring(dot);
			String base = (dot >= 0 ? original.substring(0, dot) : original);
			String storageName = base + "-" + UUID.randomUUID().toString().replace("-", "").toUpperCase() + ext;
			uploadFile.setStorageName(storageName);

			// resolve caminho final no storage
			Path finalPath = fileStorageOutboundPort.resolveFinalPath(storageName);

			// merge em streaming
			fileChunkStorageOutboundPort.mergeChunks(uploadFile.getUid(), finalPath);

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
			fileChunkStorageOutboundPort.cleanupSession(uploadFile.getUid());

			loggerOutboundPort.info(getClass(), "Upload finalizado: " + saved.getStorageName() + " (" + saved.getSize() + " bytes)");

			return saved;

		} catch (UncheckedException e) {
			throw e;
		} catch (Exception e) {
			throw new UncheckedException("Falha ao finalizar upload: " + e.getMessage(), e);
		}
	}

	/**
	 * Calcula o checksum SHA-256 e o tamanho de um arquivo sem carregar tudo na
	 * memória. Ideal para grandes arquivos (usa streaming).
	 *
	 * @param path caminho do arquivo no sistema de arquivos
	 * @return Map com "checksum" e "size"
	 * @throws IOException caso ocorra falha de leitura
	 */
	private Map<String, Object> calculateChecksumAndSize(Path path) throws IOException {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (Exception e) {
			throw new IOException("Algoritmo SHA-256 não disponível.", e);
		}

		long size;
		try (InputStream in = Files.newInputStream(path); DigestInputStream dis = new DigestInputStream(in, md)) {
			size = dis.transferTo(OutputStream.nullOutputStream()); // eficiente, sem buffer extra
		}

		String sha256 = HexFormat.of().formatHex(md.digest());
		return Map.of("checksum", sha256, "size", size);
	}

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

			Path destination = fileStorageOutboundPort.saveFile(storageName,
					new ByteArrayInputStream(uploadFile.getBytes()));

			String checksum = hashGeneratorOutboundPort.generateSha256(uploadFile.getBytes());

			uploadFile.setPath(destination.toString());
			uploadFile.setChecksumSha256(checksum);
			uploadFile.setCreatedAt(LocalDateTime.now());
			uploadFile.setIsPublic(false);

			validator.validateBeforeSave(uploadFile);

			uploadFile = super.salvar(uploadFile);

			loggerOutboundPort.info(getClass(),
					String.format("Upload concluído: %s (%s)", uploadFile.getName(), uploadFile.getStorageName()));

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

		Map<String, Object> filter = Map.of("domain", domain.toUpperCase(), "domainId", domainId.toString());

		List<UploadFile> files = super.buscarTodos(new UploadFile(), filter);
		
		System.out.println("Files found: " + files.size());

		return files;
	}

}
