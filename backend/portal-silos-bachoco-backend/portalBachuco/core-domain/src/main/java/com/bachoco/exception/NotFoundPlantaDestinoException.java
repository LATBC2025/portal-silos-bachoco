package com.bachoco.exception;

public class NotFoundPlantaDestinoException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NotFoundPlantaDestinoException(String mensaje) {
		super(mensaje);
	}
}