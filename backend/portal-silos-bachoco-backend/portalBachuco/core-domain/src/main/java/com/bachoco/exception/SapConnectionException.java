package com.bachoco.exception;

public class SapConnectionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public SapConnectionException(String mensaje) {
		super(mensaje);
	}
}