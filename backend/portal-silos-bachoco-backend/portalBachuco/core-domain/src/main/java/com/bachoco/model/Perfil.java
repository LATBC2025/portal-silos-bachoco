package com.bachoco.model;

import java.util.HashSet;
import java.util.Set;

public class Perfil {
	
	private Integer id;
	private String clave;
	private String descripcion;
	private Set<Permiso> permisos=new HashSet<>();
	
	public Perfil() {
	}

	public Perfil(Integer id, String clave, String descripcion, Set<Permiso> permisos) {
		this.id = id;
		this.clave = clave;
		this.descripcion = descripcion;
		this.permisos = permisos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set<Permiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(Set<Permiso> permisos) {
		this.permisos = permisos;
	}
	
}
