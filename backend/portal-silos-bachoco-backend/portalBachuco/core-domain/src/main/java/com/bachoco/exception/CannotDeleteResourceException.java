package com.bachoco.exception;

public class CannotDeleteResourceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
    public CannotDeleteResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}