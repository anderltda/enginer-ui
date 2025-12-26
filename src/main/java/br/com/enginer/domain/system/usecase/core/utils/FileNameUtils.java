package br.com.enginer.domain.system.usecase.core.utils;

import java.util.UUID;

/**
 * Utilitário responsável pela geração e normalização de nomes físicos de
 * arquivos para armazenamento no sistema.
 *
 * <p>
 * Essa classe garante que os nomes sejam únicos, seguros e padronizados,
 * evitando caracteres inválidos e colapsos de duplicidade no storage.
 * </p>
 */
public final class FileNameUtils {

	private FileNameUtils() {
		// Classe utilitária — não instanciável
	}

	/**
	 * Gera um nome físico único para o arquivo a ser armazenado. Exemplo:
	 * {@code relatorio-financeiro-2024-ABC123.pdf}
	 *
	 * @param originalName nome original do arquivo enviado
	 * @return nome físico seguro e único para armazenamento
	 */
	public static String generateStorageName(String originalName) {
		String uniqueId = UUID.randomUUID().toString().toUpperCase().replace("-", "");
		String extension = getFileExtension(originalName);
		String baseName = normalizeBaseName(originalName, extension);
		return baseName + "-" + uniqueId + extension;
	}

	/**
	 * Extrai a extensão do arquivo, preservando o ponto. Exemplo: {@code .pdf}
	 *
	 * @param fileName nome completo do arquivo
	 * @return extensão do arquivo (ou string vazia se não existir)
	 */
	public static String getFileExtension(String fileName) {
		if (fileName == null || !fileName.contains("."))
			return "";
		return fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
	}

	/**
	 * Normaliza o nome base do arquivo, substituindo caracteres inválidos. Mantém
	 * apenas letras, números, hífens e underlines.
	 *
	 * @param originalName nome original
	 * @param extension    extensão (será removida do nome base)
	 * @return nome normalizado e seguro
	 */
	public static String normalizeBaseName(String originalName, String extension) {
		if (originalName == null || originalName.isBlank())
			return "file";
		String base = originalName.replace(extension, "");
		return base.replaceAll("[^a-zA-Z0-9_-]", "_");
	}
}