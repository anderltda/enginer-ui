package br.com.enginer.domain.system.dto.entity.user;

/**
 * 
 */
public record RefreshResponse(String accessToken, String refreshToken, Integer expiresIn, String tokenType) {
}
