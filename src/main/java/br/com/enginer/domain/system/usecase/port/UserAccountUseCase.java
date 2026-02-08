package br.com.enginer.domain.system.usecase.port;

import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.action.ActionUseCase;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.user.vo.JwtVo;

/**
 * 
 */
public interface UserAccountUseCase extends ActionUseCase<UserAccount> {
	
	/**
	 * @param jwtVo
	 * @return
	 * @throws UncheckedException
	 */
	public UserAccount login(JwtVo jwtVo) throws UncheckedException;

	/**
	 * @param jwtVo
	 * @return
	 * @throws UncheckedException
	 */
	public UserAccount onboard(JwtVo jwtVo) throws UncheckedException;

}
