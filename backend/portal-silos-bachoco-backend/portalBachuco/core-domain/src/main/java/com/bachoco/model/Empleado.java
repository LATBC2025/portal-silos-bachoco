package com.bachoco.model;

import java.sql.Timestamp;

public class Empleado {
	
	private Integer id;
	private String nombre;
	private String apellidoPaterno;
	private String appelidoMaterno;
	private String correo;
	private Timestamp fechaContratacion;
	private Timestamp fechaBaja;
	private Usuario usuario;
	
	public Empleado() {
	}


	public Empleado(Integer id, String nombre, String apellidoPaterno, String appelidoMaterno, String correo,
			Timestamp fechaContratacion, Timestamp fechaBaja, Usuario usuario) {
		this.id = id;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.appelidoMaterno = appelidoMaterno;
		this.correo = correo;
		this.fechaContratacion = fechaContratacion;
		this.fechaBaja = fechaBaja;
		this.usuario = usuario;
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


	public String getApellidoPaterno() {
		return apellidoPaterno;
	}


	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}


	public String getAppelidoMaterno() {
		return appelidoMaterno;
	}


	public void setAppelidoMaterno(String appelidoMaterno) {
		this.appelidoMaterno = appelidoMaterno;
	}


	public String getCorreo() {
		return correo;
	}


	public void setCorreo(String correo) {
		this.correo = correo;
	}


	public Timestamp getFechaContratacion() {
		return fechaContratacion;
	}


	public void setFechaContratacion(Timestamp fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}


	public Timestamp getFechaBaja() {
		return fechaBaja;
	}


	public void setFechaBaja(Timestamp fechaBaja) {
		this.fechaBaja = fechaBaja;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
