package com.bachoco.model.procedores;

public class PedidoTrasladoDTO {
    
	private Integer pedidoTrasladoId;
	private String folioNumPosicion;
	private String nombrePlantaDestino;
	private String numPedidoTraslado;
	private Float cantidadPedido;
	private Float cantidadTraslado;
	private Float cantidadRecibidaPa;
	private Float cantidadPendienteTraslado;
	private String numCompraAsociado;
	private Integer trasladosPendFact;
	private String posicion;
	
	private Double cantidadEmbarcadaReal;          // confirmado real
	private Double cantidadPendientePorProgramar;  // saldo liberado
	private Boolean saldoSeLiberaManana;           // opcional (para mensaje)

	
	public PedidoTrasladoDTO() {
	}

	public Integer getPedidoTrasladoId() {
		return pedidoTrasladoId;
	}

	public void setPedidoTrasladoId(Integer pedidoTrasladoId) {
		this.pedidoTrasladoId = pedidoTrasladoId;
	}

	public String getFolioNumPosicion() {
		return folioNumPosicion;
	}

	public void setFolioNumPosicion(String folioNumPosicion) {
		this.folioNumPosicion = folioNumPosicion;
	}

	public String getNombrePlantaDestino() {
		return nombrePlantaDestino;
	}

	public void setNombrePlantaDestino(String nombrePlantaDestino) {
		this.nombrePlantaDestino = nombrePlantaDestino;
	}

	public String getNumPedidoTraslado() {
		return numPedidoTraslado;
	}

	public void setNumPedidoTraslado(String numPedidoTraslado) {
		this.numPedidoTraslado = numPedidoTraslado;
	}

	public Float getCantidadPedido() {
		return cantidadPedido;
	}

	public void setCantidadPedido(Float cantidadPedido) {
		this.cantidadPedido = cantidadPedido;
	}

	public Float getCantidadTraslado() {
		return cantidadTraslado;
	}

	public void setCantidadTraslado(Float cantidadTraslado) {
		this.cantidadTraslado = cantidadTraslado;
	}

	public Float getCantidadRecibidaPa() {
		return cantidadRecibidaPa;
	}

	public void setCantidadRecibidaPa(Float cantidadRecibidaPa) {
		this.cantidadRecibidaPa = cantidadRecibidaPa;
	}

	public Float getCantidadPendienteTraslado() {
		return cantidadPendienteTraslado;
	}

	public void setCantidadPendienteTraslado(Float cantidadPendienteTraslado) {
		this.cantidadPendienteTraslado = cantidadPendienteTraslado;
	}

	public String getNumCompraAsociado() {
		return numCompraAsociado;
	}

	public void setNumCompraAsociado(String numCompraAsociado) {
		this.numCompraAsociado = numCompraAsociado;
	}

	public Integer getTrasladosPendFact() {
		return trasladosPendFact;
	}

	public void setTrasladosPendFact(Integer trasladosPendFact) {
		this.trasladosPendFact = trasladosPendFact;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public Double getCantidadEmbarcadaReal() {
		return cantidadEmbarcadaReal;
	}

	public void setCantidadEmbarcadaReal(Double cantidadEmbarcadaReal) {
		this.cantidadEmbarcadaReal = cantidadEmbarcadaReal;
	}

	public Double getCantidadPendientePorProgramar() {
		return cantidadPendientePorProgramar;
	}

	public void setCantidadPendientePorProgramar(Double cantidadPendientePorProgramar) {
		this.cantidadPendientePorProgramar = cantidadPendientePorProgramar;
	}

	public Boolean getSaldoSeLiberaManana() {
		return saldoSeLiberaManana;
	}

	public void setSaldoSeLiberaManana(Boolean saldoSeLiberaManana) {
		this.saldoSeLiberaManana = saldoSeLiberaManana;
	}
	
	
}
