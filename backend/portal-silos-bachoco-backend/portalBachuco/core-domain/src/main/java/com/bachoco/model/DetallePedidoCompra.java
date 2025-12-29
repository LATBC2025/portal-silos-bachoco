package com.bachoco.model;

public class DetallePedidoCompra {

	private Integer id;
	private Float cantidadEntregada;
	private Float cantidadDespachada;
	private Float cantidadPendiente;
	public DetallePedidoCompra() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Float getCantidadEntregada() {
		return cantidadEntregada;
	}
	public void setCantidadEntregada(Float cantidadEntregada) {
		this.cantidadEntregada = cantidadEntregada;
	}
	public Float getCantidadDespachada() {
		return cantidadDespachada;
	}
	public void setCantidadDespachada(Float cantidadDespachada) {
		this.cantidadDespachada = cantidadDespachada;
	}
	public Float getCantidadPendiente() {
		return cantidadPendiente;
	}
	public void setCantidadPendiente(Float cantidadPendiente) {
		this.cantidadPendiente = cantidadPendiente;
	}
}
