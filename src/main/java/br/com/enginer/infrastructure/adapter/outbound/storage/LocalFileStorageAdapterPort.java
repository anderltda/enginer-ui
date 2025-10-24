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
     *
     */
    @Override
    public Path saveFile(String originalFilename, InputStream content) throws IOException {
        Files.createDirectories(UPLOAD_DIR);
        String filename = StringUtils.cleanPath(originalFilename);
        Path destination = UPLOAD_DIR.resolve(filename);
        Files.copy(content, destination, StandardCopyOption.REPLACE_EXISTING);
        return destination;
    }

    /**
     *
     */
    @Override
    public String getPublicUrl(Path path) {
        return path.toUri().toString();
    }
}