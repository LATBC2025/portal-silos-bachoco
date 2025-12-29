package com.bachoco.model;

public class EmpleadoInternoRequest {

	private String nombre;
	private String correo;
	private String usuario;
	private Integer departamentoId;
	private Integer puestoId;
	private Integer perfilId;
	
	public EmpleadoInternoRequest() {
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getDepartamentoId() {
		return departamentoId;
	}

	public void setDepartamentoId(Integer departamentoId) {
		this.departamentoId = departamentoId;
	}

	public Integer getPuestoId() {
		return puestoId;
	}

	public void setPuestoId(Integer puestoId) {
		this.puestoId = puestoId;
	}

	public Integer getPerfilId() {
		return perfilId;
	}

	public void setPerfilId(Integer perfilId) {
		this.perfilId = perfilId;
	}
	
}
