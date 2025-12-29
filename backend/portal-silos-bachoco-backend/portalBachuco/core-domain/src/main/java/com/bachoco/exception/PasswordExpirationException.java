package com.bachoco.exception;

public class PasswordExpirationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	  public PasswordExpirationException() { 
		  super("Su Contraseña ha expirado"); 
	}
}
