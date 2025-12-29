package com.bachoco.exception;

public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NotFoundException(String mensaje) {
		super(mensaje);
	}
}
