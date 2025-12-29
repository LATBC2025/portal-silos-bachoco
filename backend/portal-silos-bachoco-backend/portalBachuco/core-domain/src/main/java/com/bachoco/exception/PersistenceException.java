package com.bachoco.exception;

public class PersistenceException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public PersistenceException(String mensaje) {
		super(mensaje);
	}
}
