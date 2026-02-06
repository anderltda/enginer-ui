package br.com.enginer.domain.system.dto.entity.user;

/**
 * 
 */
public record LoginResponse(String accessToken, String refreshToken, Integer expiresIn, String tokenType,
		UserAccount user) {
}
