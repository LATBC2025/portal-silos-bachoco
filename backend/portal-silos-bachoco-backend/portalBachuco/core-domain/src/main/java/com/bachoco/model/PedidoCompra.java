package com.bachoco.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class PedidoCompra {
	
	private Integer id;
	private String numeroPedido;
	private Float cantidadPedida;
	private String urlcertificadoConservacion;
	private String contratoLegal;
	private String nombreMaterial;
	private String posicion;
	private String plantaSilo;
	private Timestamp fechaCompra;
	private byte[] certificadoDeposito;
	
	private Set<DetallePedidoCompra> detallePedidoCompra= new HashSet<>();
	private Silo silo;
	private Material material;
	
	public PedidoCompra() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public Float getCantidadPedida() {
		return cantidadPedida;
	}

	public void setCantidadPedida(Float cantidadPedida) {
		this.cantidadPedida = cantidadPedida;
	}

	public String getUrlcertificadoConservacion() {
		return urlcertificadoConservacion;
	}

	public void setUrlcertificadoConservacion(String urlcertificadoConservacion) {
		this.urlcertificadoConservacion = urlcertificadoConservacion;
	}

	public String getContratoLegal() {
		return contratoLegal;
	}

	public void setContratoLegal(String contratoLegal) {
		this.contratoLegal = contratoLegal;
	}

	public String getNombreMaterial() {
		return nombreMaterial;
	}

	public void setNombreMaterial(String nombreMaterial) {
		this.nombreMaterial = nombreMaterial;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public String getPlantaSilo() {
		return plantaSilo;
	}

	public void setPlantaSilo(String plantaSilo) {
		this.plantaSilo = plantaSilo;
	}

	public Timestamp getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra(Timestamp fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	public byte[] getCertificadoDeposito() {
		return certificadoDeposito;
	}

	public void setCertificadoDeposito(byte[] certificadoDeposito) {
		this.certificadoDeposito = certificadoDeposito;
	}

	public Set<DetallePedidoCompra> getDetallePedidoCompra() {
		return detallePedidoCompra;
	}

	public void setDetallePedidoCompra(Set<DetallePedidoCompra> detallePedidoCompra) {
		this.detallePedidoCompra = detallePedidoCompra;
	}

	public Silo getSilo() {
		return silo;
	}

	public void setSilo(Silo silo) {
		this.silo = silo;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
	
	
	
	
}
