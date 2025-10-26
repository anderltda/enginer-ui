package br.com.enginer.infrastructure.adapter.outbound.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.usercase.port.outbound.storage.FileChunkStorageOutboundPort;


@Component
public class LocalFileChunkStorageAdapterPort implements FileChunkStorageOutboundPort {

    private static final Path TEMP_UPLOAD_DIR = Path.of("/Users/anderson/Downloads/uploads/temp");

    @Override
    public Path ensureSessionFolder(String uploadId) throws IOException {
        Path folder = TEMP_UPLOAD_DIR.resolve(uploadId);
        Files.createDirectories(folder);
        return folder;
    }

    @Override
    public Path saveChunk(String uploadId, int chunkIndex, InputStream content) throws IOException {
        Path session = ensureSessionFolder(uploadId);
        Path chunk = session.resolve(String.format("%05d.part", chunkIndex));
        Files.copy(content, chunk, StandardCopyOption.REPLACE_EXISTING);
        return chunk;
    }

    @Override
    public List<Path> listChunks(String uploadId) throws IOException {
        Path session = ensureSessionFolder(uploadId);
        try (var stream = Files.list(session)) {
            return stream.sorted(Comparator.comparing(Path::getFileName))
                         .collect(Collectors.toList());
        }
    }

    @Override
    public Path mergeChunks(String uploadId, Path finalPath) throws IOException {
        Files.createDirectories(finalPath.getParent());
        try (var out = Files.newOutputStream(finalPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Path chunk : listChunks(uploadId)) {
                Files.copy(chunk, out);
            }
        }
        return finalPath;
    }

    @Override
    public void cleanupSession(String uploadId) throws IOException {
        Path session = TEMP_UPLOAD_DIR.resolve(uploadId);
        if (Files.exists(session)) {
            try (var walk = Files.walk(session)) {
                walk.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
            }
        }
    }
}