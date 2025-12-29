package com.bachoco.model;

public class EmailMensaje {

	private String destinatario;
	private String asunto;
	private String cuerpo;
	
	public EmailMensaje(String destinatario, String asunto, String cuerpo) {
		this.destinatario = destinatario;
		this.asunto = asunto;
		this.cuerpo = cuerpo;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public String getAsunto() {
		return asunto;
	}

	public String getCuerpo() {
		return cuerpo;
	}
	
}
