package br.com.enginer.domain.system.usercase.port.outbound.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import br.com.enginer.domain.system.usercase.port.OutboundPort;

public interface FileChunkStorageOutboundPort extends OutboundPort {

    /** 
     * Garante a pasta temporária da sessão (idempotente).
     *  
     */
    Path ensureSessionFolder(String uploadId) throws IOException;

    /** 
     * Persiste um chunk da sessão (ex.: 00001.part). 
     */
    Path saveChunk(String uploadId, int chunkIndex, InputStream content) throws IOException;

    /** 
     * Lista todos os chunks armazenados (ordenados por nome). 
     * 
     */
    List<Path> listChunks(String uploadId) throws IOException;

    /** 
     * Junta os chunks em um arquivo final (streaming), retornando o Path final. 
     * 
     */
    Path mergeChunks(String uploadId, Path finalPath) throws IOException;

    /** 
     * Limpa os temporários da sessão. 
     * 
     */
    void cleanupSession(String uploadId) throws IOException;
    
}