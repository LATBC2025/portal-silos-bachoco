package com.bachoco.model;

import java.time.LocalDateTime;

public class Otp {

	private String usuarioId;
	private String codigo;
	private LocalDateTime expiracion;
	private boolean usado;
	
	public Otp(String usuarioId, String codigo, LocalDateTime expiracion, boolean usado) {
		this.usuarioId = usuarioId;
		this.codigo = codigo;
		this.expiracion = expiracion;
		this.usado = usado;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public String getCodigo() {
		return codigo;
	}

	public LocalDateTime getExpiracion() {
		return expiracion;
	}

	public boolean isUsado() {
		return usado;
	}

	public void marcarComoUsado() {
		this.usado=true;
	}
	
	public boolean estaValido() {
		return !usado && LocalDateTime.now().isBefore(expiracion);
	}
	
}
