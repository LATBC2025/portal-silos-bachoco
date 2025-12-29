package com.bachoco.model;

public class ReportePorgramArribo {

	private Integer id;
	private Float toneladas;
	private String material;
	private String fecha;
	private String numeroPedido;
	private String destinoPlanta;
	
	public ReportePorgramArribo() {
	}

	public ReportePorgramArribo(Integer id, Float toneladas, String material, String fecha, String numeroPedido,
			String destinoPlanta) {
		this.id = id;
		this.toneladas = toneladas;
		this.material = material;
		this.fecha = fecha;
		this.numeroPedido = numeroPedido;
		this.destinoPlanta = destinoPlanta;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getToneladas() {
		return toneladas;
	}

	public void setToneladas(Float toneladas) {
		this.toneladas = toneladas;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public String getDestinoPlanta() {
		return destinoPlanta;
	}

	public void setDestinoPlanta(String destinoPlanta) {
		this.destinoPlanta = destinoPlanta;
	}
	
}
