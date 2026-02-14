package br.com.enginer.domain.system.usecase.user;

import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.dto.entity.user.UserAccountIdentity;
import br.com.enginer.domain.system.usecase.core.AbstractUseCase;
import br.com.enginer.domain.system.usecase.core.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.core.exception.UserInactiveException;
import br.com.enginer.domain.system.usecase.core.utils.StringsUtils;
import br.com.enginer.domain.system.usecase.upload.UploadFileUseCase;
import br.com.enginer.domain.system.usecase.user.vo.ProviderIdentityVo;

/**
 * 
 */
public class UserAccountUseCase extends AbstractUseCase<UserAccount> implements br.com.enginer.domain.system.usecase.port.UserAccountUseCase {

	@AutoDependencyInjector
	private UserAccountIdentityUseCase userAccountIdentityUseCase;
	
	@AutoDependencyInjector
	private UploadFileUseCase uploadFileUseCase;
	
	/**
	 *
	 */
	@Override
	public UserAccount sessionUserAccount(String provider, String providerTenant, String providerSubject) throws UncheckedException {

		Map<String, Object> filters = new HashMap<>();
		filters.put("provider", provider);
		filters.put("providerTenant", providerTenant);
		filters.put("providerSubject", providerSubject);
		
		UserAccountIdentity identity = userAccountIdentityUseCase.buscarPorRegistroUnico(new UserAccountIdentity(), filters);
		
		filters.clear();
		filters.put("id", identity.getUserAccount().getId());

		UserAccount userAccount = (UserAccount) buscarPorRegistroUnico(new UserAccount(), filters);
		
		return userAccount;
	}

	/**
	 *
	 */
	@Override
	public UserAccount salvar(UserAccount domain) throws UncheckedException {
		
		UploadFile uploadFile = domain.getFiles().getFirst();

		UploadFile uploadFileNew = uploadFileUseCase.buscarFormPorId(uploadFile);

		domain.setUploadFile(uploadFileNew);

		return super.salvar(domain);
	}

	/**
	 * @param jwtVo
	 * @return
	 * @throws UncheckedException
	 */
	@Override
	public UserAccount login(ProviderIdentityVo jwtVo) throws UncheckedException {

		Map<String, Object> filters = new HashMap<>();
		filters.put("provider", jwtVo.provider());
		filters.put("providerTenant", jwtVo.providerTenant());
		filters.put("providerSubject", jwtVo.providerSubject());

		UserAccountIdentity identity = userAccountIdentityUseCase.buscarPorRegistroUnico(new UserAccountIdentity(), filters);

		if (identity == null) return null;

		filters.clear();
		filters.put("id", identity.getUserAccount().getId());

		UserAccount userAccount = (UserAccount) buscarPorRegistroUnico(new UserAccount(), filters);
		
		Instant lastLoginAt = userAccount.getLastLoginAt();
		
		if (StringsUtils.isBlank(userAccount.getDisplayName()) && !StringsUtils.isBlank(jwtVo.name())) {
			userAccount.setDisplayName(jwtVo.name());
		}
		
		if (!StringsUtils.isBlank(jwtVo.username()) && (StringsUtils.isBlank(userAccount.getUsername()) || !userAccount.getUsername().equals(jwtVo.username()))) {
			userAccount.setUsername(jwtVo.username());
		}
		
		if (!StringsUtils.isBlank(jwtVo.email()) && (StringsUtils.isBlank(userAccount.getEmail()) || !userAccount.getEmail().equals(jwtVo.email()))) {
			userAccount.setEmail(jwtVo.email());
			userAccount.setEmailNormalized(jwtVo.email().trim().toLowerCase(Locale.ROOT));
		}

		if(userAccount.getUploadFile() != null && userAccount.getUploadFile().getId() != null) {
			UploadFile uploadFile = uploadFileUseCase.buscarFormPorId(userAccount.getUploadFile());
			userAccount.setUploadFile(uploadFile);
		} else {
			userAccount.setIdUploadFile(null);
		}

		userAccount.setLastLoginAt(jwtVo.lastLoginAt());
		userAccount.setLastLoginIp(jwtVo.lastLoginIp());
		userAccount.setLastLoginUserAgent(jwtVo.lastLoginUserAgent());

		super.salvar(userAccount);
		
		userAccount.setLastLoginAt(lastLoginAt);

		// se estiver inativo
		if (Boolean.FALSE.equals(userAccount.getActive())) {
			throw new UserInactiveException();
		}

		return userAccount;
	}

	/**
	 *
	 */
	@Override
	public UserAccount onboard(ProviderIdentityVo jwtVo) throws UncheckedException {

		UserAccount userAccount = new UserAccount();
		userAccount.setPublicId(UUID.randomUUID());
		userAccount.setDisplayName(jwtVo.name());
		userAccount.setUsername(jwtVo.username());
		userAccount.setEmail(jwtVo.email());
		userAccount.setLastLoginAt(jwtVo.lastLoginAt());
		userAccount.setLastLoginIp(jwtVo.lastLoginIp());
		userAccount.setLastLoginUserAgent(jwtVo.lastLoginUserAgent());
		userAccount.setActive(true);

		if (!StringsUtils.isBlank(jwtVo.email())) {
			userAccount.setEmailNormalized(jwtVo.email().trim().toLowerCase(Locale.ROOT));
		}

		UserAccount userAccountNew = super.salvar(userAccount);

		UserAccountIdentity identity = new UserAccountIdentity();
		identity.setProvider(jwtVo.provider());
		identity.setProviderTenant(jwtVo.providerTenant());
		identity.setProviderSubject(jwtVo.providerSubject());
		identity.setUserAccount(userAccountNew);

		userAccountIdentityUseCase.salvar(identity);

		return userAccountNew;
	}

}
