package br.com.enginer.infrastructure.adapter.outbound.repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.core.utils.StringsUtils;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.UploadFileRepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.storage.FileStorageOutboundPort;

/**
 * Adapter outbound específico para o domínio {@link UploadFile}.
 *
 * <p>
 * Esta classe apenas especializa o {@link DelegatingRepositoryOutboundAdapter}
 * para {@link UploadFile}, delegando todas as operações ao repositório genérico
 * {@link RepositoryOutboundPort}<{@link Domain}<?>>.
 * </p>
 *
 * <p>
 * A existência deste adapter permite:
 * <ul>
 * <li>Evitar problemas de type erasure em runtime</li>
 * <li>Injeção limpa via Spring de {@link UploadFileRepositoryOutboundPort}</li>
 * <li>Uso seguro e tipado dentro dos UseCases</li>
 * </ul>
 * </p>
 */
@Component
public class UploadFileRepositoryOutboundAdapterPort extends DelegatingRepositoryOutboundAdapter<UploadFile> implements UploadFileRepositoryOutboundPort {

	/**
	 * Diretório final padrão para arquivos associados a entidades de domínio.
	 */
	private static final String PATH_DIR_FINAL = "final";
	
	/**
	 * 
	 */
	private List<UploadFile> files;

	/**
	 * @param delegate
	 * @param loggerOutboundPort
	 * @param fileStorageOutboundPort
	 */
	protected UploadFileRepositoryOutboundAdapterPort(RepositoryOutboundPort<Domain<?>> delegate, LoggerOutboundPort loggerOutboundPort, FileStorageOutboundPort fileStorageOutboundPort) {
		super(delegate, loggerOutboundPort, fileStorageOutboundPort);
	}	
	
	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void pull(Domain<?> domain) {

		try {
			
			if(domain == null) return;
			
			if (domain instanceof UploadFile) return;
		
			files = (List<UploadFile>) ReflectionUtils.execute(domain, StringsUtils.getMethod("files"));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *
	 */
	@Override
	public void push(Domain<?> domain) {

		try {
			
			if(domain == null) return;
			
			if (domain instanceof UploadFile) return;
		
			Optional.ofNullable(files)
					.filter(f -> !f.isEmpty())
					.ifPresent(f -> f.forEach(file -> salvarEntityId(file, domain.getId())));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Associa um arquivo a uma entidade de domínio existente, movendo-o
	 * para o diretório definitivo e atualizando seu metadado.
	 *
	 * @param uploadFile o arquivo previamente salvo (com domainId nulo)
	 * @param id identificador da entidade à qual o arquivo será associado
	 * @return UploadFile atualizado e persistido
	 * @throws UncheckedException caso ocorra erro de I/O ou persistência
	 */
	private UploadFile salvarEntityId(UploadFile uploadFile, Object id) throws UncheckedException {
		
	    try {
	    
	    	// Busca o metadado atual
	        UploadFile file = (UploadFile) delegate.findById(uploadFile);

	        if (file == null) {
	            throw new UncheckedException("Arquivo não encontrado para associação.");
	        }

	        // Define o novo domainId
	        file.setDomainId(id.toString());

	        // Define o diretório final: /uploads/<domain>/<domainId>/
	        //String finalDirectory = String.format("%s/%s/", file.getDomain(), id);
	        String finalDirectory = String.format("%s/", PATH_DIR_FINAL);

	        // Move fisicamente o arquivo via FileStorageOutboundPort
	        Path newPath = fileStorageOutboundPort.moveFile(Path.of(file.getPath()), finalDirectory);

	        // Atualiza o metadado com o novo path
	        file.setPath(newPath.toString());

	        // Persiste a atualização
	        file = (UploadFile) delegate.save(file);

	        loggerOutboundPort.info(getClass(), String.format("Arquivo [%s] movido para [%s]", file.getName(), newPath));

	        return file;

	    } catch (IOException e) {
	        throw new UncheckedException("Erro ao mover o arquivo após associação: " + e.getMessage(), e);
	    }
	}
}