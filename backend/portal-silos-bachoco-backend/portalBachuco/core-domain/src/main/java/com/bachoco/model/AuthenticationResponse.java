package com.bachoco.model;

import java.time.LocalDateTime;
import java.util.Set;

public class AuthenticationResponse {
	
	private Integer usuarioId;
	private String usuario;
	private String password;
	private String correo;
	private Set<String> roles;
	private Integer usuarioTipo;
	private Integer usuarioActivo;
	private Integer empleadoActivo;
	private LocalDateTime ultimoModifiPwd;
	
	public AuthenticationResponse() {
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Integer getUsuarioTipo() {
		return usuarioTipo;
	}

	public void setUsuarioTipo(Integer usuarioTipo) {
		this.usuarioTipo = usuarioTipo;
	}

	public Integer getUsuarioActivo() {
		return usuarioActivo;
	}

	public void setUsuarioActivo(Integer usuarioActivo) {
		this.usuarioActivo = usuarioActivo;
	}

	public Integer getEmpleadoActivo() {
		return empleadoActivo;
	}

	public void setEmpleadoActivo(Integer empleadoActivo) {
		this.empleadoActivo = empleadoActivo;
	}

	public LocalDateTime getUltimoModifiPwd() {
		return ultimoModifiPwd;
	}

	public void setUltimoModifiPwd(LocalDateTime ultimoModifiPwd) {
		this.ultimoModifiPwd = ultimoModifiPwd;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	
}
