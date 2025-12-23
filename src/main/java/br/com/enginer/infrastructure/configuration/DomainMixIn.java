package br.com.enginer.infrastructure.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * MixIn do Jackson utilizado na camada de infraestrutura para ocultar
 * métodos internos do domínio durante a serialização JSON.
 *
 * <p>
 * Esta classe evita que métodos de uso exclusivo do backend (ex: validações
 * e regras internas como {@code isIdNull()}) sejam expostos ao frontend,
 * sem adicionar dependências de serialização no domínio.
 * </p>
 *
 * <p>
 * Mantém o domínio puro e desacoplado de frameworks externos,
 * respeitando a arquitetura hexagonal.
 * </p>
 */
public abstract class DomainMixIn {

	/**
	 * Método interno do domínio que não deve ser serializado para JSON.
	 */
	@JsonIgnore
	public abstract Boolean isIdNull();
}
