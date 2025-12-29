package com.bachoco.exception;

public class NotFoundMaterialException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NotFoundMaterialException(String mensaje) {
		super(mensaje);
	}
}
