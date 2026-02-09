package br.com.enginer.domain.system.usecase.upload;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.AbstractUseCase;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;
import net.coobird.thumbnailator.Thumbnails;

/**
 * UseCase responsável pelo ciclo de vida do upload de arquivos no sistema.
 *
 * <p>
 * <b>Fluxo de upload por chunks</b>
 * </p>
 * <ol>
 * <li>{@link #startSession()} cria um UID para identificar a sessão de
 * upload.</li>
 * <li>{@link #uploadChunk(String, int, InputStream)} recebe e armazena cada
 * chunk via adapter (porta).</li>
 * <li>{@link #finalizeUpload(UploadFile)} faz merge dos chunks, calcula
 * hash/tamanho e persiste metadados.</li>
 * </ol>
 *
 * <p>
 * <b>Otimização de avatar</b>
 * </p>
 * <ul>
 * <li>Se o upload representar um avatar (regra em
 * {@link #isAvatar(UploadFile)}), a imagem pode ser otimizada.</li>
 * <li>Suporta JPEG/PNG/WEBP (converte para JPEG).</li>
 * <li>Aplica crop quadrado central + resize (256x256) + compressão JPEG.</li>
 * </ul>
 *
 * <p>
 * <b>Observação importante</b>
 * </p>
 * <ul>
 * <li>A otimização é "best-effort": se falhar, o upload não quebra; persiste o
 * arquivo original.</li>
 * </ul>
 */
public class UploadFileUseCase extends AbstractUseCase<UploadFile> implements br.com.enginer.domain.system.usecase.port.UploadFileUseCase {
	
	/**
	 * Exclui o arquivo fisicamente e remove o registro no banco.
	 *
	 * <p>
	 * O fluxo garante que o uploadFile exista via {@code buscarPorId} antes de
	 * apagar.
	 * </p>
	 *
	 * @param uploadFile referência do arquivo (contendo id)
	 * @throws UncheckedException em caso de erro de IO ou exclusão
	 */
	@Override
	public void excluir(UploadFile uploadFile) throws UncheckedException {

		try {

			loggerOutboundPort.info(getClass(), "Exclusão iniciada para arquivo: " + uploadFile.getName());

			// Garante os dados reais (principalmente path)
			uploadFile = buscarPorId(uploadFile);

			// Apaga fisicamente via porta de storage
			Path path = Path.of(uploadFile.getPath());
			fileStorageOutboundPort.deleteFile(path);

			// Remove metadados no banco
			super.excluir(uploadFile);

			loggerOutboundPort.info(getClass(), "Arquivo excluído com sucesso: " + uploadFile.getPath());

		} catch (IOException ex) {
			loggerOutboundPort.error(getClass(), "Erro ao excluir arquivo: " + uploadFile.getName(), ex);
			throw new UncheckedException("Erro ao excluir o arquivo: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Inicia uma sessão de upload (idempotente).
	 *
	 * <p>
	 * O retorno é um UID sem hífen, usado para identificar a sessão e armazenar
	 * chunks temporários.
	 * </p>
	 *
	 * @return uid da sessão de upload
	 */
	@Override
	public String startSession() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * Recebe um chunk e delega o armazenamento ao adapter/porta de storage.
	 *
	 * <p>
	 * O storage pode ser filesystem, S3, etc. A UseCase não sabe onde está
	 * persistindo.
	 * </p>
	 *
	 * @param uploadId   uid da sessão
	 * @param chunkIndex índice/ordem do chunk
	 * @param content    bytes do chunk em stream
	 * @throws UncheckedException caso falhe ao salvar chunk
	 */
	@Override
	public void uploadChunk(String uploadId, int chunkIndex, InputStream content) {

		try {
			
			loggerOutboundPort.info(getClass(), "Chunk recebido: " + uploadId + " idx=" + chunkIndex);

			// Salva o chunk (implementação concreta fica no adapter)
			fileStorageOutboundPort.saveChunk(uploadId, chunkIndex, content);

		} catch (IOException ex) {
			throw new UncheckedException("Falha ao salvar chunk: " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * Finaliza o upload:
	 * <ul>
	 * <li>Valida metadados básicos (name + type).</li>
	 * <li>Gera um storageName único (base + UUID + extensão).</li>
	 * <li>Faz merge dos chunks em um arquivo final (streaming).</li>
	 * <li>Se for avatar, tenta otimizar a imagem (crop/resize/compress).</li>
	 * <li>Calcula checksum SHA-256 e tamanho do arquivo que será persistido.</li>
	 * <li>Persiste metadados no banco via {@code salvar}.</li>
	 * <li>Limpa temporários da sessão.</li>
	 * </ul>
	 *
	 * @param uploadFile metadados do upload (nome, mime, domínio, uid, etc.)
	 * @return registro persistido
	 */
	@Override
	public UploadFile finalizeUpload(UploadFile uploadFile) {

		try {

			// ----------------------------------------------------
			// 1) Valida campos mínimos necessários para persistir
			// ----------------------------------------------------
			if (uploadFile.getName() == null || uploadFile.getType() == null) {
				throw new UncheckedException("Nome e tipo MIME são obrigatórios.");
			}

			// ----------------------------------------------------
			// 2) Gera storageName único para evitar colisões
			// ----------------------------------------------------
			String original = uploadFile.getName();
			String ext = "";
			int dot = original.lastIndexOf('.');

			if (dot >= 0) {
				ext = original.substring(dot);
			}

			String base = (dot >= 0 ? original.substring(0, dot) : original);

			String storageName = base + "-" + UUID.randomUUID().toString().replace("-", "").toUpperCase() + ext;
			uploadFile.setStorageName(storageName);

			// ----------------------------------------------------
			// 3) Resolve path final e faz o merge dos chunks
			// ----------------------------------------------------
			Path finalPath = fileStorageOutboundPort.resolveFinalPath(storageName);
			fileStorageOutboundPort.mergeChunks(uploadFile.getUid(), finalPath);

			// ----------------------------------------------------
			// 4) Tenta otimizar avatar (se aplicável)
			// - se retornar null => usa arquivo original
			// ----------------------------------------------------
			Path optimized = optimizeAvatarIfNeeded(uploadFile, finalPath);
			Path pathToPersist = (optimized != null ? optimized : finalPath);

			// ----------------------------------------------------
			// 5) Checksum + size do arquivo final que será persistido
			// ----------------------------------------------------
			Map<String, Object> result = calculateChecksumAndSize(pathToPersist);

			// Persistimos o path do arquivo final (otimizado ou original)
			uploadFile.setPath(pathToPersist.toString());
			uploadFile.setStorageType("LOCAL"); // no futuro pode virar S3 etc.
			uploadFile.setChecksumSha256((String) result.get("checksum"));
			uploadFile.setSize((Long) result.get("size"));
			uploadFile.setIsPublic(false);
			uploadFile.setCreatedAt(Instant.now());

			// ----------------------------------------------------
			// 6) Persiste metadados no banco
			// ----------------------------------------------------
			UploadFile saved = super.salvar(uploadFile);

			// ----------------------------------------------------
			// 7) Limpa temporários da sessão (chunks)
			// ----------------------------------------------------
			fileStorageOutboundPort.cleanupSession(uploadFile.getUid());

			loggerOutboundPort.info(getClass(), "Upload finalizado: " + saved.getStorageName() + " (" + saved.getSize() + " bytes)");

			return saved;

		} catch (UncheckedException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new UncheckedException("Falha ao finalizar upload: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Otimiza um arquivo de avatar (se aplicável) e retorna o path do arquivo
	 * otimizado.
	 *
	 * <p>
	 * Regras:
	 * </p>
	 * <ul>
	 * <li>Somente se {@link #isAvatar(UploadFile)} for true.</li>
	 * <li>MIME suportados: jpeg/png/webp.</li>
	 * <li>Converte para JPEG (remove alpha quando existir).</li>
	 * <li>Crop quadrado central + resize para 256x256.</li>
	 * </ul>
	 *
	 * <p>
	 * Falha na otimização não deve quebrar o upload: retorna null e persiste
	 * original.
	 * </p>
	 *
	 * @param uploadFile   metadados do upload
	 * @param originalPath path do arquivo final (merged)
	 * @return path otimizado ou null (quando não aplica/erro)
	 */
	private Path optimizeAvatarIfNeeded(UploadFile uploadFile, Path originalPath) {

		try {
			
			// Não é avatar => não otimiza
			if (!isAvatar(uploadFile))
				return null;

			// Valida MIME permitido
			String mime = (uploadFile.getType() == null ? "" : uploadFile.getType().toLowerCase());
			if (!(mime.equals("image/jpeg") || mime.equals("image/png") || mime.equals("image/webp"))) {
				return null;
			}

			// destino: mesmo diretório, mas com nome gerado + .jpg
			Path dir = originalPath.getParent();
			if (dir == null) {
				dir = originalPath.toAbsolutePath().getParent();
			}

			String optimizedName = "avatar_" + UUID.randomUUID().toString().replace("-", "").toUpperCase() + ".jpg";
			
			Path optimizedPath = fileStorageOutboundPort.resolveFinalPath("avatar", optimizedName);
			fileStorageOutboundPort.mergeChunks(uploadFile.getUid(), optimizedPath);

			// Lê imagem para BufferedImage
			BufferedImage src = ImageIO.read(originalPath.toFile());
			if (src == null)
				return null;

			// Remove canal alpha (JPEG não suporta transparência)
			BufferedImage noAlpha = removeAlphaIfNeeded(src);

			// Crop quadrado central
			int w = noAlpha.getWidth();
			int h = noAlpha.getHeight();
			int size = Math.min(w, h);
			int x = (w - size) / 2;
			int y = (h - size) / 2;

			BufferedImage cropped = noAlpha.getSubimage(x, y, size, size);

			// Resize final (padrão 256) + compressão JPEG
			int target = 256;
			Thumbnails.of(cropped).size(target, target).outputFormat("jpg").outputQuality(0.82f) // ajuste fino:
																									// qualidade x
																									// tamanho
					.toFile(optimizedPath.toFile());

			// Atualiza metadados do upload para refletir o arquivo final persistido
			uploadFile.setType("image/jpeg");
			uploadFile.setName(uploadFile.getName()); // mantém nome original (display)
			uploadFile.setStorageName(optimizedName);

			// Opcional: remover original para não acumular lixo
			fileStorageOutboundPort.deleteFile(originalPath);

			return optimizedPath;

		} catch (Exception ex) {
			// best-effort: falhou otimização => loga e segue com original
			loggerOutboundPort.warn(getClass(), "Falha ao otimizar avatar. Usando original. Motivo: " + ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Calcula checksum SHA-256 e tamanho do arquivo via streaming (sem carregar
	 * tudo na memória).
	 *
	 * <p>
	 * Bom para arquivos grandes, pois evita {@code byte[]} gigante.
	 * </p>
	 *
	 * @param path caminho do arquivo no filesystem
	 * @return map contendo: "checksum" (String), "size" (Long)
	 * @throws IOException se falhar leitura ou algoritmo indisponível
	 */
	private Map<String, Object> calculateChecksumAndSize(Path path) throws IOException {

		try {

			MessageDigest md = MessageDigest.getInstance("SHA-256");

			long size;

			// DigestInputStream acumula hash enquanto lê a stream
			try (InputStream in = Files.newInputStream(path); DigestInputStream dis = new DigestInputStream(in, md)) {

				// Conta bytes e lê tudo sem armazenar
				size = dis.transferTo(OutputStream.nullOutputStream());
			}

			String sha256 = HexFormat.of().formatHex(md.digest());

			return Map.of("checksum", sha256, "size", size);

		} catch (Exception ex) {
			throw new IOException("Algoritmo SHA-256 não disponível.", ex);
		}
	}

	/**
	 * Regra de negócio: identifica se o upload representa um avatar.
	 *
	 * <p>
	 * No seu modelo atual, você usa o campo {@code domain} para decidir. Ex: domain
	 * = "UserAccount" => avatar do usuário.
	 * </p>
	 *
	 * @param uploadFile upload em processamento
	 * @return true se for avatar
	 */
	private boolean isAvatar(UploadFile uploadFile) {
		return uploadFile != null && uploadFile.getDomain() != null
				&& uploadFile.getDomain().equalsIgnoreCase(UserAccount.class.getSimpleName());
	}

	/**
	 * Remove transparência (alpha) caso a imagem tenha canal alpha (ex.: PNG).
	 *
	 * <p>
	 * JPEG não suporta alpha, então desenhamos em um fundo branco e convertemos
	 * para {@link BufferedImage#TYPE_INT_RGB}.
	 * </p>
	 *
	 * @param src imagem original
	 * @return imagem RGB sem alpha
	 */
	private BufferedImage removeAlphaIfNeeded(BufferedImage src) {
		
		if (!src.getColorModel().hasAlpha()) {
			return src;
		}

		BufferedImage rgb = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = rgb.createGraphics();

		// fundo branco
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, src.getWidth(), src.getHeight());

		// desenha a imagem original em cima
		g.drawImage(src, 0, 0, null);

		g.dispose();
		return rgb;
	}
}