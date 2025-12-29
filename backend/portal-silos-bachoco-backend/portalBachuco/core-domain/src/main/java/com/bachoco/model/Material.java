package com.bachoco.model;

public class Material {
	
	private Integer materialId;
	private String numero;
	private String descripcion;
	
	private Categoria categoria;
	
	public Material() {
	}
	
	public Material(Integer materialId, String numero, String descripcion, Categoria categoria) {
		this.materialId = materialId;
		this.numero = numero;
		this.descripcion = descripcion;
		this.categoria = categoria;
	}

	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}


	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
}
