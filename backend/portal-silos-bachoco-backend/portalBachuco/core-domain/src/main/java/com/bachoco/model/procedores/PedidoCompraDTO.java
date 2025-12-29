package com.bachoco.model.procedores;

public class PedidoCompraDTO {

	private Integer pedidoCompraId;
	private String numeroPedido;
	private Float cantidadPedida;
	private Float cantidadEntregada;
	private Float cantidadDespachada;
	private Float cantidadPendienteDespachada;
	private String contratoLegal;
	private String urlCertificadoDeposito;
	private String tipoExtencion;
	private String posicion;
	private String folioPedCompraPosicion;
	
	public PedidoCompraDTO() {
	}

	public Integer getPedidoCompraId() {
		return pedidoCompraId;
	}

	public void setPedidoCompraId(Integer pedidoCompraId) {
		this.pedidoCompraId = pedidoCompraId;
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

	public String getContratoLegal() {
		return contratoLegal;
	}

	public void setContratoLegal(String contratoLegal) {
		this.contratoLegal = contratoLegal;
	}

	public String getUrlCertificadoDeposito() {
		return urlCertificadoDeposito;
	}

	public void setUrlCertificadoDeposito(String urlCertificadoDeposito) {
		this.urlCertificadoDeposito = urlCertificadoDeposito;
	}

	public String getTipoExtencion() {
		return tipoExtencion;
	}

	public void setTipoExtencion(String tipoExtencion) {
		this.tipoExtencion = tipoExtencion;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public String getFolioPedCompraPosicion() {
		return folioPedCompraPosicion;
	}

	public void setFolioPedCompraPosicion(String folioPedCompraPosicion) {
		this.folioPedCompraPosicion = folioPedCompraPosicion;
	}

	public Float getCantidadPendienteDespachada() {
		return cantidadPendienteDespachada;
	}

	public void setCantidadPendienteDespachada(Float cantidadPendienteDespachada) {
		this.cantidadPendienteDespachada = cantidadPendienteDespachada;
	}
	
}
