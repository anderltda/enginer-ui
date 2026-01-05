package br.com.enginer.domain.system.usecase.user;

import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.AbstractUseCase;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;

/**
 * 
 */
public class UserAccountUseCase extends AbstractUseCase<UserAccount> implements br.com.enginer.domain.system.usecase.port.UserAccountUseCase {
	
	@Override
	public UserAccount salvar(UserAccount domain) throws UncheckedException {
		UploadFile uploadFile = domain.getFiles().getFirst();
		domain.setUploadFile(uploadFile);
		return super.salvar(domain);
	}

}
