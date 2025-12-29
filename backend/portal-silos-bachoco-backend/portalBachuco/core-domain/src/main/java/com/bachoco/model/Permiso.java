package com.bachoco.model;


public class Permiso {
	private Integer id;
	private String clave;
	private String descripcion;
	
	public Permiso() {
	}

	public Permiso(Integer id, String clave, String descripcion) {
		this.id = id;
		this.clave = clave;
		this.descripcion = descripcion;
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
	
	
}
