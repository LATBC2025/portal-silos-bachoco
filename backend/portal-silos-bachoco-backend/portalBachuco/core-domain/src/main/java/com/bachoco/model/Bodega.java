package com.bachoco.model;

public class Bodega {

	private Integer id;
    private String nombre;
    private String descripcion;
    private Silo silo;
    
    public Bodega() {
    }

	public Bodega(Integer id, String nombre, String descripcion, Silo silo) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.silo = silo;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Silo getSilo() {
		return silo;
	}

	public void setSilo(Silo silo) {
		this.silo = silo;
	}
    
	
}
