package com.bachoco.model;

public class Documento {

	private byte[] contenido;
	private String nombre;
	private String ruta;
	private String extencion;

	public Documento() {
	}

	public Documento(byte[] contenido, String nombre, String ruta, String extencion) {
		this.contenido = contenido;
		this.nombre = nombre;
		this.ruta = ruta;
		this.extencion = extencion;
	}

	public byte[] getContenido() {
		return contenido;
	}

	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getExtencion() {
		return extencion;
	}

	public void setExtencion(String extencion) {
		this.extencion = extencion;
	}

	
	
}
