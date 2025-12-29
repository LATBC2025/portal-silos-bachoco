package com.bachoco.exception;

public class SendEmailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SendEmailException(String mensaje) {
        super(mensaje);
    }
}
