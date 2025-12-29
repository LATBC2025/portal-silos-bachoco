package com.bachoco.exception;

public class ExpirationJwtException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ExpirationJwtException(String mensaje) {
		super(mensaje);
	}

	
}
