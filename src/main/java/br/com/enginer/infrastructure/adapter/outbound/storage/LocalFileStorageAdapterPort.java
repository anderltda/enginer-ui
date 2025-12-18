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

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.enginer.domain.system.usecase.port.outbound.storage.FileStorageOutboundPort;


/**
 * Implementação local do FileStorageOutboundPort.
 * Salva arquivos no sistema de arquivos local.
 */
@Component
public class LocalFileStorageAdapterPort implements FileStorageOutboundPort {

    private static final Path UPLOAD_DIR = Path.of("/Users/anderson/Downloads/uploads/");
    private static final Path TEMP_UPLOAD_DIR = Path.of("/Users/anderson/Downloads/uploads/temp");

    /** 
     * Garante a pasta temporária da sessão (idempotente).
     */    
    @Override
    public Path ensureSessionFolder(String uploadId) throws IOException {
    	
        Path folder = TEMP_UPLOAD_DIR.resolve(uploadId);
        
        Files.createDirectories(folder);
        
        return folder;
    }

    /** 
     * Persiste um chunk da sessão (ex.: 00001.part). 
     */    
    @Override
    public Path saveChunk(String uploadId, int chunkIndex, InputStream content) throws IOException {
    	
        Path session = ensureSessionFolder(uploadId);
        
        Path chunk = session.resolve(String.format("%05d.part", chunkIndex));
        
        Files.copy(content, chunk, StandardCopyOption.REPLACE_EXISTING);
        
        return chunk;
    }

    /** 
     * Lista todos os chunks armazenados (ordenados por nome). 
     */    
    @Override
    public List<Path> listChunks(String uploadId) throws IOException {
    	
        Path session = ensureSessionFolder(uploadId);
        
        try (var stream = Files.list(session)) {
            return stream.sorted(Comparator.comparing(Path::getFileName)).collect(Collectors.toList());
        }
    }

    /** 
     * Junta os chunks em um arquivo final (streaming), retornando o Path final. 
     */    
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

    /** 
     * Limpa os temporários da sessão. 
     */    
    @Override
    public void cleanupSession(String uploadId) throws IOException {
    	
        Path session = TEMP_UPLOAD_DIR.resolve(uploadId);
        
        if (Files.exists(session)) {
            try (var walk = Files.walk(session)) {
                walk.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
            }
        }
    }    

    /**
     * Gera o hash SHA-256 para o conteúdo fornecido.
     *
     * @param bytes conteúdo em bytes
     * @return hash hexadecimal SHA-256
     */    
    @Override
    public String generateSha256(byte[] bytes) {
        return DigestUtils.sha256Hex(bytes);
    }
    
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