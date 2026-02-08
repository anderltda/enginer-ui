package br.com.enginer.domain.system.usecase.user.vo;

/**
 * 
 */
public record JwtVo(String provider, String subject, String tenant, String email, String username, String name) {

}
