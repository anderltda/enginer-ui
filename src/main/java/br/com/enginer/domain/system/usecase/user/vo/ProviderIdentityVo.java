package br.com.enginer.domain.system.usecase.user.vo;

import java.time.Instant;

/**
 * 
 */
public record ProviderIdentityVo(String provider, String providerSubject, String providerTenant, String email,
		String username, String name, Instant lastLoginAt, String lastLoginIp, String lastLoginUserAgent) {
}
