package com.bachoco.model;

public class EmpleadoResponse {
	private Integer empleadoId;
	private Integer tipoEmpleado;
	private Integer departamentoId;
	private Integer puestoId;
	private String nombrePuesto;
	private String nombre;
	private Integer siloId;
	
	public EmpleadoResponse() {
	}

	public EmpleadoResponse(Integer empleadoId, Integer tipoEmpleado, Integer departamentoId, Integer puestoId,
			String nombrePuesto, String nombre, Integer siloId) {
		this.empleadoId = empleadoId;
		this.tipoEmpleado = tipoEmpleado;
		this.departamentoId = departamentoId;
		this.puestoId = puestoId;
		this.nombrePuesto = nombrePuesto;
		this.nombre = nombre;
		this.siloId = siloId;
	}

	public Integer getEmpleadoId() {
		return empleadoId;
	}

	public void setEmpleadoId(Integer empleadoId) {
		this.empleadoId = empleadoId;
	}

	public Integer getTipoEmpleado() {
		return tipoEmpleado;
	}

	public void setTipoEmpleado(Integer tipoEmpleado) {
		this.tipoEmpleado = tipoEmpleado;
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

	public String getNombrePuesto() {
		return nombrePuesto;
	}

	public void setNombrePuesto(String nombrePuesto) {
		this.nombrePuesto = nombrePuesto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getSiloId() {
		return siloId;
	}

	public void setSiloId(Integer siloId) {
		this.siloId = siloId;
	}

	
}
