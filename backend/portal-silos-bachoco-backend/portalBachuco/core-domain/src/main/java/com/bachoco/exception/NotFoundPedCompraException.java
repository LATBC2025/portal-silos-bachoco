package com.bachoco.exception;

public class NotFoundPedCompraException  extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NotFoundPedCompraException(String mensaje) {
		super(mensaje);
	}
}
