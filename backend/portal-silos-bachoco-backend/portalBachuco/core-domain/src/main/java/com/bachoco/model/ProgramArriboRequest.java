package com.bachoco.model;

public class ProgramArriboRequest {

	private String numeroPedidoTraslado;
	private Float cantidad;
	private String fechaProgramada;
	private String claveSilo;
	private Integer siloId;
	private Integer materialId;
	private Integer plantaId;
	private Integer pedidoTrasladoId;
	private String isRestaCantidad;
	
	public ProgramArriboRequest() {
	}

	public String getNumeroPedidoTraslado() {
		return numeroPedidoTraslado;
	}

	public void setNumeroPedidoTraslado(String numeroPedidoTraslado) {
		this.numeroPedidoTraslado = numeroPedidoTraslado;
	}

	public Float getCantidad() {
		return cantidad;
	}

	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}

	public String getFechaProgramada() {
		return fechaProgramada;
	}

	public void setFechaProgramada(String fechaProgramada) {
		this.fechaProgramada = fechaProgramada;
	}

	public String getClaveSilo() {
		return claveSilo;
	}

	public void setClaveSilo(String claveSilo) {
		this.claveSilo = claveSilo;
	}

	public Integer getSiloId() {
		return siloId;
	}

	public void setSiloId(Integer siloId) {
		this.siloId = siloId;
	}

	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	public Integer getPlantaId() {
		return plantaId;
	}

	public void setPlantaId(Integer plantaId) {
		this.plantaId = plantaId;
	}

	public Integer getPedidoTrasladoId() {
		return pedidoTrasladoId;
	}

	public void setPedidoTrasladoId(Integer pedidoTrasladoId) {
		this.pedidoTrasladoId = pedidoTrasladoId;
	}

	public String getIsRestaCantidad() {
		return isRestaCantidad;
	}

	public void setIsRestaCantidad(String isRestaCantidad) {
		this.isRestaCantidad = isRestaCantidad;
	}
}
