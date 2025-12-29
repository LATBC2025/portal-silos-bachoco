package com.bachoco.exception;

public class CredencialesInvalidasException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	  public CredencialesInvalidasException() { 
		  super("Credenciales inválidas"); 
	}
}
