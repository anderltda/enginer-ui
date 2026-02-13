package br.com.enginer.infrastructure.configuration.cache;

import java.util.Objects;

import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.exception.CheckedException;
import br.com.enginer.domain.system.usecase.core.fn.SerializableLambda;
import br.com.enginer.domain.system.usecase.core.fn.SerializableTriConsumer;
import br.com.enginer.domain.system.usecase.port.inbound.api.ActionInboundPort;
import br.com.enginer.domain.system.usecase.user.UserAccountUseCase;
import br.com.enginer.infrastructure.configuration.security.ProviderIdentity;

/**
 * Cache global com TTL de UserAccount entre requests.
 * Chave: provider|tenant|subject
 */
@Component
public class UserAccountCache {

    private final SimpleTtlCache<String, UserAccount> cache;
    private final ActionInboundPort<?> actionInboundPort;
    private final UserAccountUseCase userAccountUseCase;

    /**
     * @param props
     * @param actionInboundPort
     */
    public UserAccountCache(UserAccountCacheProperties props, ActionInboundPort<?> actionInboundPort) {
        this.cache = new SimpleTtlCache<>(props.getTtlMs());
        this.actionInboundPort = actionInboundPort;
        this.userAccountUseCase = new UserAccountUseCase();
    }

    /**
     * @param identity
     * @return
     * @throws CheckedException
     */
    public UserAccount getOrLoad(ProviderIdentity identity) throws CheckedException {
    	
        if (identity == null) return null;

        String key = key(identity);

        // carrega só se faltar/expirar
        return cache.getOrLoad(key, () -> {
            try {
                return loadFromBackend(identity);
            } catch (Exception e) {
                // não cacheia erro
                return null;
            }
        });
    }

    /**
     * @param identity
     */
    public void invalidate(ProviderIdentity identity) {
        if (identity == null) return;
        cache.invalidate(key(identity));
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

    private String key(ProviderIdentity identity) {
        return identity.provider()
                + "|" + Objects.toString(identity.providerTenant(), "")
                + "|" + identity.providerSubject();
    }

    private UserAccount loadFromBackend(ProviderIdentity identity) throws Exception {

        String sessionUserAccount = SerializableLambda.extractMethodName((SerializableTriConsumer<String, String, String>) userAccountUseCase::sessionUserAccount);

        return (UserAccount) actionInboundPort.methodName(
                new UserAccount(),
                sessionUserAccount,
                identity.provider(),
                identity.providerTenant(),
                identity.providerSubject()
        );
    }
}