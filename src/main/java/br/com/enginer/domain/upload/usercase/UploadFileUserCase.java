package br.com.enginer.domain.upload.usercase;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.domain.upload.dto.entity.UploadFile;

public class UploadFileUserCase extends AbstractUserCase {
	
	public Domain<?> salvarEntityId(Domain<?> domain, Object id) throws UncheckedException {
		UploadFile file = (UploadFile) domain;
		UploadFile existingFile = (UploadFile) buscarPorId(file);
		existingFile.setDomainId(id.toString());
		return super.salvar(existingFile);
	}
	
	public List<UploadFile> buscarPorEntityIdAndDomain(Object entityId, String domain) throws UncheckedException {
		
		Map<String, Object> filter = Map.of("domain", domain, "domainId", entityId.toString());
		
		List<UploadFile> files = super.buscarTodos(new UploadFile(), filter);
		
		return files;
	}

}
