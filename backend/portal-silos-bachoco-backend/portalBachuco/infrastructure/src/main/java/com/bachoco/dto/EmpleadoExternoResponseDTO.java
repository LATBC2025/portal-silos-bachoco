package com.bachoco.dto;

public class EmpleadoExternoResponseDTO {

	private Integer id;
	private String nombre;
	private String rfc;
	private String correo;
	private String usuario;
	private String siloId;
	
	public EmpleadoExternoResponseDTO() {
	}

	public EmpleadoExternoResponseDTO(Integer id, String nombre, String rfc, String correo, String usuario,
			String siloId) {
		this.id = id;
		this.nombre = nombre;
		this.rfc = rfc;
		this.correo = correo;
		this.usuario = usuario;
		this.siloId = siloId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
	
}
