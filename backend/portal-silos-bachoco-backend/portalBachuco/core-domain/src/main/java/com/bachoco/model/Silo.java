package com.bachoco.model;

public class Silo {
	
	private Integer id;
	private String silo;
	private String nombre;
	private String sociedad;
	private Double stock;
	
	public Silo() {
	}

	public Silo(Integer id, String silo, String nombre, String sociedad, Double stock) {
		this.id = id;
		this.silo = silo;
		this.nombre = nombre;
		this.sociedad = sociedad;
		this.stock = stock;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSilo() {
		return silo;
	}

	public void setSilo(String silo) {
		this.silo = silo;
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

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

}
