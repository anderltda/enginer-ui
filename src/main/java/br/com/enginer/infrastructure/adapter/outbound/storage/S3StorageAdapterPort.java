package br.com.enginer.infrastructure.adapter.outbound.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Implementação do FileStorageOutboundPort para AWS S3.
 */
@Component("s3StorageAdapter")
public class S3StorageAdapterPort {

    private final S3Client s3Client;
    private final String bucketName = "enginer-storage";

    public S3StorageAdapterPort(S3Client s3Client) {
        this.s3Client = s3Client;
    }

   // @Override
    public Path saveFile(String originalFilename, InputStream content) throws IOException {
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucketName)
                .key(originalFilename)
                .build(),
            RequestBody.fromInputStream(content, content.available())
        );
        return Path.of(originalFilename);
    }

   // @Override
    public String getPublicUrl(Path path) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + path.getFileName();
    }
}
