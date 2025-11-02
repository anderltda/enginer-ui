package br.com.enginer.infrastructure.adapter.outbound.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.enginer.domain.system.usercase.port.outbound.storage.FileStorageOutboundPort;


/**
 * Implementação local do FileStorageOutboundPort.
 * Salva arquivos no sistema de arquivos local.
 */
@Component
public class LocalFileStorageAdapterPort implements FileStorageOutboundPort {

    private static final Path UPLOAD_DIR = Path.of("/Users/anderson/Downloads/uploads/");

    /**
     * Salva o arquivo localmente usando o nome físico único (storageName).
     */
    @Override
    public Path saveFile(String storageName, InputStream content) throws IOException {
        Files.createDirectories(UPLOAD_DIR);
        String filename = StringUtils.cleanPath(storageName);
        Path destination = UPLOAD_DIR.resolve(filename);
        Files.copy(content, destination, StandardCopyOption.REPLACE_EXISTING);
        return destination;
    }

    /**
     * Exclui o arquivo fisicamente do disco.
     */
    @Override
    public void deleteFile(Path path) throws IOException {
        if (path != null && Files.exists(path)) {
            Files.delete(path);
        }
    }

    /**
     * Retorna a URL local do arquivo (útil para debug ou download local).
     */
    @Override
    public String getPublicUrl(Path path) {
        return path != null ? path.toUri().toString() : null;
    }
    
    /** 
     * Resolve o caminho final (no storage) para um storageName. 
     */
    @Override
    public Path resolveFinalPath(String storageName) throws IOException {
        Files.createDirectories(UPLOAD_DIR);
        String clean = StringUtils.cleanPath(storageName);
        return UPLOAD_DIR.resolve(clean);
    }
    
    /**
     * Move um arquivo existente para um novo diretório dentro do storage.
     *
     * @param source caminho atual do arquivo
     * @param relativeDestinationPath subdiretório de destino (ex: "entityTen/123/")
     * @return novo caminho absoluto do arquivo movido
     * @throws IOException caso ocorra falha na operação
     */
    @Override
    public Path moveFile(Path source, String relativeDestinationPath) throws IOException {
    	
        Path destinationDir = UPLOAD_DIR.resolve(relativeDestinationPath);

        Files.createDirectories(destinationDir);

        Path destination = destinationDir.resolve(source.getFileName());
        Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);

        return destination;
    }    
}