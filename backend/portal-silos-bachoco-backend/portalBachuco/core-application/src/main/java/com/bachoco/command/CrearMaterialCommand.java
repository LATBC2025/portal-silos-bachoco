package com.bachoco.command;

public class CrearMaterialCommand {
	
	private String numero;
	private String descripcion;
	private Integer categoriaId;
	
	public CrearMaterialCommand(String numero, String descripcion, Integer categoriaId) {
		this.numero = numero;
		this.descripcion = descripcion;
		this.categoriaId = categoriaId;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}

	
}
