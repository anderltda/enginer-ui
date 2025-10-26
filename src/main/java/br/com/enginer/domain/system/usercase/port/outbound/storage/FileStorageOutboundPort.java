package br.com.enginer.domain.system.usercase.port.outbound.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import br.com.enginer.domain.system.usercase.port.OutboundPort;

/**
 * Porta de saída genérica para armazenamento de arquivos.
 * Implementações podem ser locais, em nuvem (S3), etc.
 */
public interface FileStorageOutboundPort extends OutboundPort {

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
}