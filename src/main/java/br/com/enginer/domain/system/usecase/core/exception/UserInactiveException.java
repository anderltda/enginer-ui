package br.com.enginer.domain.system.usecase.core.exception;

public class UserInactiveException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UserInactiveException() {
		super("Usuário inativo no sistema");
	}
}