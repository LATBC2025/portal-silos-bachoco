package com.bachoco.exception;

public class CannotRegisterProgramArriboException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	  public CannotRegisterProgramArriboException() { 
		  super("No se puedo registrar la programacion arribo"); 
	}
}
