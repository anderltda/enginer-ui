package br.com.enginer.domain.system.usecase.port;

import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.action.ActionUseCase;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.core.exception.UserInactiveException;
import br.com.enginer.domain.system.usecase.user.vo.ProviderIdentityVo;

/**
 * 
 */
public interface UserAccountUseCase extends ActionUseCase<UserAccount> {
	
	/**
	 * @param jwtVo
	 * @return
	 * @throws UncheckedException
	 */
	public UserAccount login(ProviderIdentityVo jwtVo) throws UserInactiveException;

	/**
	 * @param jwtVo
	 * @return
	 * @throws UncheckedException
	 */
	public UserAccount onboard(ProviderIdentityVo jwtVo) throws UncheckedException;
	
	/**
	 * @param provider
	 * @param providerTenant
	 * @param providerSubject
	 * @return
	 * @throws UncheckedException
	 */
	public UserAccount sessionUserAccount(String provider, String providerTenant, String providerSubject) throws UncheckedException;

}
