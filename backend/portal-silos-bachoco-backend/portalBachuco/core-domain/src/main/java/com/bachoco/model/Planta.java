package com.bachoco.model;

public class Planta {
	
	private Integer id;
	private String planta;
	private String nombre;
	private String sociedad;
	
	public Planta() {
	}

	public Planta(Integer id, String planta, String nombre, String sociedad) {
		this.id = id;
		this.planta = planta;
		this.nombre = nombre;
		this.sociedad = sociedad;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlanta() {
		return planta;
	}

	public void setPlanta(String planta) {
		this.planta = planta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSociedad() {
		return sociedad;
	}

	public void setSociedad(String sociedad) {
		this.sociedad = sociedad;
	}
	

}
