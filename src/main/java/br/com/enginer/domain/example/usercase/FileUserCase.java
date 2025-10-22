package br.com.enginer.domain.example.usercase;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.example.dto.entity.File;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

public class FileUserCase extends AbstractUserCase {
	
	public Domain<?> salvarEntityId(Domain<?> domain, Object id) throws UncheckedException {
		File file = (File) domain;
		File existingFile = (File) buscarPorId(file);
		existingFile.setEntityId(id.toString());
		return super.salvar(existingFile);
	}
	
	public List<File> buscarPorEntityIdAndDomain(Object entityId, String domain) throws UncheckedException {
		
		Map<String, Object> filter = Map.of(
			"entityId", entityId.toString(),
			"domain", domain
		);
		
		List<File> files = super.buscarTodos(new File(), filter);
		
		return files;
	}

}
