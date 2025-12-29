package com.bachoco.model;


public class EmpleadoInterno {
	
	private Integer id;
	private Empleado empleado;
	private Departamento departamento;
	private Puesto puesto;
	
	public EmpleadoInterno() {
	}

	public EmpleadoInterno(Integer id, Empleado empleado, Departamento departamento, Puesto puesto) {
		this.id = id;
		this.empleado = empleado;
		this.departamento = departamento;
		this.puesto = puesto;
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

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Puesto getPuesto() {
		return puesto;
	}

	public void setPuesto(Puesto puesto) {
		this.puesto = puesto;
	}
}
