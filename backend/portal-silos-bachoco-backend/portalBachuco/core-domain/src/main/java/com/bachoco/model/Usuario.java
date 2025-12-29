package com.bachoco.model;

import java.time.LocalDateTime;
import java.util.Set;

public class Usuario {

	private Integer id;
	private String usuarioTipo;
	private String usuario;
	private String password;
	private Integer activo;
	private LocalDateTime ultimoModPwd;
	private Set<Perfil> perfiles;
	private Set<String> roles;
	
	public Usuario() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsuarioTipo() {
		return usuarioTipo;
	}

	public void setUsuarioTipo(String usuarioTipo) {
		this.usuarioTipo = usuarioTipo;
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

	public Integer getActivo() {
		return activo;
	}

	public void setActivo(Integer activo) {
		this.activo = activo;
	}

	public Set<Perfil> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(Set<Perfil> perfiles) {
		this.perfiles = perfiles;
	}

	public LocalDateTime getUltimoModPwd() {
		return ultimoModPwd;
	}

	public void setUltimoModPwd(LocalDateTime ultimoModPwd) {
		this.ultimoModPwd = ultimoModPwd;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
}
