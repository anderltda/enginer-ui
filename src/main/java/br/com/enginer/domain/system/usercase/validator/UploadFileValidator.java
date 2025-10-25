package br.com.enginer.domain.system.usercase.validator;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.system.dto.entity.UploadFile;
import br.com.enginer.domain.system.usercase.UploadFileUserCase;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.port.outbound.logger.LoggerOutboundPort;

/**
 * Classe responsável por centralizar todas as regras de validação
 * de negócio relacionadas ao UploadFile.
 *
 * <p>Ela substitui constraints do banco (como UNIQUE, NOT NULL, CHECK, etc.)
 * permitindo que a integridade seja garantida em nível de domínio.</p>
 *
 * <p>É usada pelo {@link UploadFileUserCase} antes de qualquer persistência.</p>
 */
public class UploadFileValidator {

    private final LoggerOutboundPort logger;
    private final UploadFileUserCase userCase;

    /**
     * Construtor com dependências necessárias.
     */
    public UploadFileValidator(LoggerOutboundPort logger, UploadFileUserCase userCase) {
        this.logger = logger;
        this.userCase = userCase;
    }

    /**
     * Valida se o arquivo enviado contém dados básicos obrigatórios.
     *
     * @param uploadFile arquivo a ser validado
     * @throws UncheckedException se alguma validação falhar
     */
    public void validateRequiredFields(UploadFile uploadFile) throws UncheckedException {
    	
        if (uploadFile == null) {
            throw new UncheckedException("UploadFile não pode ser nulo.");
        }

        if (uploadFile.getName() == null || uploadFile.getName().isBlank()) {
            throw new UncheckedException("Nome do arquivo não informado.");
        }

        if (uploadFile.getBytes() == null || uploadFile.getBytes().length == 0) {
            throw new UncheckedException("Arquivo vazio ou não enviado.");
        }

        if (uploadFile.getDomain() == null || uploadFile.getDomain().isBlank()) {
            throw new UncheckedException("Domínio não informado.");
        }

        if (uploadFile.getType() == null || uploadFile.getType().isBlank()) {
            logger.warn(getClass(), "Tipo MIME não informado — atribuindo 'application/octet-stream'.");
            uploadFile.setType("application/octet-stream");
        }
    }

    /**
     * Valida se já existe duplicidade lógica no contexto atual.
     * <p>
     * Regra substitui a constraint UNIQUE(domain, domainId, name).
     * </p>
     *
     * @param uploadFile entidade a ser verificada
     * @throws UncheckedException se um duplicado for detectado
     */
    public void validateDuplicity(UploadFile uploadFile) throws UncheckedException {
    	
        try {
        	
        	if(uploadFile.getDomainId() == null) {
        		return;
        	}
        	
            Map<String, Object> filter = Map.of(
                "domain", uploadFile.getDomain(),
                "domainId", uploadFile.getDomainId(),
                "name", uploadFile.getName()
            );

            List<UploadFile> duplicates = userCase.buscarTodos(new UploadFile(), filter);

            if (!duplicates.isEmpty()) {
                logger.warn(getClass(), String.format("Duplicidade detectada para arquivo '%s' (domain=%s, domainId=%s)",
                    uploadFile.getName(), uploadFile.getDomain(), uploadFile.getDomainId()
                ));
                throw new UncheckedException(String.format("Duplicidade detectada para arquivo '%s' (domain=%s, domainId=%s)",
                        uploadFile.getName(), uploadFile.getDomain(), uploadFile.getDomainId()));
            }

        } catch (Exception e) {
            logger.error(getClass(), "Erro ao verificar duplicidade: " + e.getMessage(), e);
            throw new UncheckedException("Falha ao verificar duplicidade.", e);
        }
    }

    /**
     * Garante que o storageName e checksum foram gerados antes de salvar.
     */
    public void validateBeforeSave(UploadFile uploadFile) throws UncheckedException {
        if (uploadFile.getStorageName() == null || uploadFile.getStorageName().isBlank()) {
            throw new UncheckedException("Nome físico (storageName) não foi gerado.");
        }
        if (uploadFile.getChecksumSha256() == null || uploadFile.getChecksumSha256().isBlank()) {
            throw new UncheckedException("Checksum SHA-256 não foi calculado.");
        }
    }
}
