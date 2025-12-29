package com.bachoco.model;

public class EmpleadoExterno {

	private Integer id;
	private Empleado empleado;
	private Usuario usuario;
	private Integer siloId;
	
	public EmpleadoExterno() {
	}

	public EmpleadoExterno(Integer id, Empleado empleado, Usuario usuario, Integer siloId) {
		this.id = id;
		this.empleado = empleado;
		this.usuario = usuario;
		this.siloId = siloId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getSiloId() {
		return siloId;
	}

	public void setSiloId(Integer siloId) {
		this.siloId = siloId;
	}
	
}
