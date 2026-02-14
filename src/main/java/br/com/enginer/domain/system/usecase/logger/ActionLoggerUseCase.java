package br.com.enginer.domain.system.usecase.logger;

import java.util.Map;

import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.AbstractUseCase;
import br.com.enginer.domain.system.usecase.core.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.core.page.PageResult;
import br.com.enginer.domain.system.usecase.upload.UploadFileUseCase;
import br.com.enginer.domain.system.usecase.user.UserAccountUseCase;

/**
 * 
 */
public class ActionLoggerUseCase extends AbstractUseCase<ActionLogger> implements br.com.enginer.domain.system.usecase.port.ActionLoggerUseCase {

	@AutoDependencyInjector
	private UserAccountUseCase userAccountUseCase;
	
	@AutoDependencyInjector
	private UploadFileUseCase uploadFileUseCase;
	
	@Override
	public PageResult<ActionLogger> buscarTodosPaginado(ActionLogger domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<ActionLogger> result = (PageResult<ActionLogger>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(logger -> {
				
				UserAccount userAccount = (UserAccount) userAccountUseCase.buscarPorId(logger.getUserAccount());
				
				if(userAccount.getUploadFile() != null && userAccount.getUploadFile().getId() != null) {
					UploadFile uploadFile = (UploadFile) uploadFileUseCase.buscarPorId(userAccount.getUploadFile());
					userAccount.setUploadFile(uploadFile);
				}
				
				logger.setUserAccount(userAccount);
			});
		}
		
		return result;
	}
}
