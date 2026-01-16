package com.bachoco.model;

public class EmpleadoExternoRequest {

	private String nombre;
	private String numeroProveedor;
	private String rfc;
	private String correo;
	private String usuario;
	private String siloId;
	
	public EmpleadoExternoRequest() {
	}

	public EmpleadoExternoRequest(String nombre,String numeroProveedor, String rfc, String correo, String usuario, String siloId) {
		this.nombre = nombre;
		this.numeroProveedor = numeroProveedor;
		this.rfc = rfc;
		this.correo = correo;
		this.usuario = usuario;
		this.siloId = siloId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
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

	public String getSiloId() {
		return siloId;
	}

	public void setSiloId(String siloId) {
		this.siloId = siloId;
	}

	public String getNumeroProveedor() {
		return numeroProveedor;
	}

	public void setNumeroProveedor(String numeroProveedor) {
		this.numeroProveedor = numeroProveedor;
	}
	
	
}
