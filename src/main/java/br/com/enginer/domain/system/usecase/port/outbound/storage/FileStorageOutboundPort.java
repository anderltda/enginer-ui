package br.com.enginer.domain.system.usecase.port.outbound.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import br.com.enginer.domain.system.usecase.port.outbound.OutboundPort;

/**
 * Porta de saída genérica para armazenamento de arquivos.
 * Implementações podem ser locais, em nuvem (S3), etc.
 */
public interface FileStorageOutboundPort extends OutboundPort {
	
    /** 
     * Garante a pasta temporária da sessão (idempotente).
     */
    Path ensureSessionFolder(String uploadId) throws IOException;

    /** 
     * Persiste um chunk da sessão (ex.: 00001.part). 
     */
    Path saveChunk(String uploadId, int chunkIndex, InputStream content) throws IOException;

    /** 
     * Lista todos os chunks armazenados (ordenados por nome). 
     */
    List<Path> listChunks(String uploadId) throws IOException;

    /** 
     * Junta os chunks em um arquivo final (streaming), retornando o Path final. 
     * 
     */
    Path mergeChunks(String uploadId, Path finalPath) throws IOException;

    /** 
     * Limpa os temporários da sessão. 
     */
    void cleanupSession(String uploadId) throws IOException;
    
    /**
     * Gera o hash SHA-256 para o conteúdo fornecido.
     *
     * @param bytes conteúdo em bytes
     * @return hash hexadecimal SHA-256
     */
    String generateSha256(byte[] bytes);    

    /**
     * Salva um arquivo físico e retorna seu caminho completo.
     *
     * @param originalFilename nome original do arquivo
     * @param content fluxo de bytes do arquivo
     * @return {@link Path} representando o caminho físico ou remoto
     * @throws IOException se ocorrer falha de leitura/escrita
     */
    Path saveFile(String originalFilename, InputStream content) throws IOException;

    /**
     * Retorna uma URL pública (ou URI local) que permite acesso direto ao arquivo.
     *
     * @param path caminho físico ou remoto do arquivo
     * @return URL ou URI pública
     */
    String getPublicUrl(Path path);

    /**
     * Exclui fisicamente o arquivo especificado, se existir.
     *
     * @param path caminho físico ou remoto do arquivo
     * @throws IOException se ocorrer falha de exclusão
     */
    void deleteFile(Path path) throws IOException;
    
    /** 
     * Resolve o caminho final (no storage) para um storageName. 
     */
    Path resolveFinalPath(String storageName) throws IOException;

    /** 
     * Resolve o caminho final (no storage) para um storageName passando um novo subdir. 
     */
    Path resolveFinalPath(String subdir, String storageName) throws IOException;
    
    /**
     * Move um arquivo existente para um novo diretório dentro do storage.
     *
     * @param source caminho atual do arquivo
     * @param relativeDestinationPath subdiretório de destino (ex: "entityTen/123/")
     * @return novo caminho absoluto do arquivo movido
     * @throws IOException caso ocorra falha na operação
     */
    Path moveFile(Path source, String relativeDestinationPath) throws IOException;
}