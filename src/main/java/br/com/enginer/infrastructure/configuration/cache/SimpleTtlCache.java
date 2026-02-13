package br.com.enginer.infrastructure.configuration.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Cache simples em memória com TTL por chave. - Thread-safe
 * (ConcurrentHashMap). - Evita múltiplos loads concorrentes para a mesma key
 * usando compute.
 */
public final class SimpleTtlCache<K, V> {

	private static final class Entry<V> {
		final V value;
		final long expiresAtMs;

		Entry(V value, long expiresAtMs) {
			this.value = value;
			this.expiresAtMs = expiresAtMs;
		}

		boolean isExpired(long nowMs) {
			return nowMs >= expiresAtMs;
		}
	}

	private final Map<K, Entry<V>> map = new ConcurrentHashMap<>();
	private final long ttlMs;

	public SimpleTtlCache(long ttlMs) {
		if (ttlMs <= 0)
			throw new IllegalArgumentException("ttlMs must be > 0");
		this.ttlMs = ttlMs;
	}

	public V get(K key) {
		if (key == null)
			return null;
		Entry<V> e = map.get(key);
		if (e == null)
			return null;

		long now = System.currentTimeMillis();
		if (e.isExpired(now)) {
			map.remove(key);
			return null;
		}
		return e.value;
	}

	/**
	 * Retorna do cache se válido; se não, calcula via loader e salva com TTL. Usa
	 * compute para evitar loads concorrentes na mesma chave.
	 */
	public V getOrLoad(K key, Supplier<V> loader) {
		if (key == null)
			return null;
		if (loader == null)
			throw new IllegalArgumentException("loader is required");

		long now = System.currentTimeMillis();

		Entry<V> entry = map.compute(key, (k, existing) -> {
			if (existing != null && !existing.isExpired(now) && existing.value != null) {
				return existing;
			}

			V loaded = loader.get();
			if (loaded == null) {
				// se não conseguiu carregar, não cacheia (remove entrada)
				return null;
			}

			return new Entry<>(loaded, now + ttlMs);
		});

		return entry == null ? null : entry.value;
	}

	public void put(K key, V value) {
		if (key == null || value == null)
			return;
		long now = System.currentTimeMillis();
		map.put(key, new Entry<>(value, now + ttlMs));
	}

	public void invalidate(K key) {
		if (key == null)
			return;
		map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public int size() {
		return map.size();
	}
}