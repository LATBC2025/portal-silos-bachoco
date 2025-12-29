package com.bachoco.model.procedores;

public class PedTrasladoArriboConfigDespachoDTO {

	private Integer pedidoTrasladoId;
	private String numeroPedPosicion;
	private String numeroPedido;
	private Float cantidad;
	private String plantaDestino;
	
	public PedTrasladoArriboConfigDespachoDTO() {
	}

	public PedTrasladoArriboConfigDespachoDTO(Integer pedidoTrasladoId, String numeroPedPosicion, String numeroPedido,
			Float cantidad, String plantaDestino) {
		this.pedidoTrasladoId = pedidoTrasladoId;
		this.numeroPedPosicion = numeroPedPosicion;
		this.numeroPedido = numeroPedido;
		this.cantidad = cantidad;
		this.plantaDestino = plantaDestino;
	}

	public Integer getPedidoTrasladoId() {
		return pedidoTrasladoId;
	}

	public void setPedidoTrasladoId(Integer pedidoTrasladoId) {
		this.pedidoTrasladoId = pedidoTrasladoId;
	}

	public String getNumeroPedPosicion() {
		return numeroPedPosicion;
	}

	public void setNumeroPedPosicion(String numeroPedPosicion) {
		this.numeroPedPosicion = numeroPedPosicion;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public Float getCantidad() {
		return cantidad;
	}

	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}

	public String getPlantaDestino() {
		return plantaDestino;
	}

	public void setPlantaDestino(String plantaDestino) {
		this.plantaDestino = plantaDestino;
	}
}
