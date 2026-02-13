package br.com.enginer.infrastructure.configuration.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "enginer.cache.user-account")
public class UserAccountCacheProperties {

	/**
	 * TTL em milissegundos (default: 3 minutos).
	 */
	private long ttlMs = 3 * 60 * 1000L;

	public long getTtlMs() {
		return ttlMs;
	}

	public void setTtlMs(long ttlMs) {
		this.ttlMs = ttlMs;
	}
}