package br.com.enginer.domain.system.usercase;

import java.io.File;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.system.dto.entity.UploadFile;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;

/**
 * 
 */
public class UploadFileUserCase extends AbstractUserCase<UploadFile> {
	
	/**
	 * @param uploadFile
	 * @param file
	 * @return
	 */
	public UploadFile upload(UploadFile uploadFile, File file) {
		
		return null;

	}
	
	/**
	 * @param uploadFile
	 * @param id
	 * @return
	 * @throws UncheckedException
	 */
	public UploadFile salvarEntityId(UploadFile uploadFile, Object id) throws UncheckedException {

		UploadFile file = (UploadFile) buscarPorId(uploadFile);
		
		file.setDomainId(id.toString());
		
		return super.salvar(file);
	}
	
	/**
	 * @param entityId
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	public List<UploadFile> buscarPorEntityIdAndDomain(Object entityId, String domain) throws UncheckedException {
		
		Map<String, Object> filter = Map.of("domain", domain, "domainId", entityId.toString());
		
		List<UploadFile> files = super.buscarTodos(new UploadFile(), filter);
		
		return files;
	}
	
}
