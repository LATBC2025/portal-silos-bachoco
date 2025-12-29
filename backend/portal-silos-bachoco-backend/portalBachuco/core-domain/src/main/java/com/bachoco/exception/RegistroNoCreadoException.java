package com.bachoco.exception;

public class RegistroNoCreadoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public RegistroNoCreadoException(String mensaje) {
        super(mensaje);
    }

    public RegistroNoCreadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}