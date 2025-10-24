package br.com.enginer.domain.system.usercase.port.outbound.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import br.com.enginer.domain.system.usercase.port.OutboundPort;

/**
 * Porta de saída genérica para armazenamento de arquivos.
 * Implementações podem ser locais, em nuvem (S3), FTP, etc.
 */
public interface FileStorageOutboundPort extends OutboundPort {

    /**
     * Salva um arquivo e retorna o caminho ou identificador remoto.
     *
     * @param originalFilename nome original do arquivo
     * @param content fluxo de bytes do arquivo
     * @return caminho físico ou remoto do arquivo salvo
     * @throws IOException se ocorrer falha de I/O
     */
    Path saveFile(String originalFilename, InputStream content) throws IOException;

    /**
     * Retorna a URL pública ou caminho para download.
     */
    String getPublicUrl(Path path);
}