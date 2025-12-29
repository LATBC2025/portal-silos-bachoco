package com.bachoco.model;

public class PedidoTrasladoSapResponseDTO {

	    private String numeroPedTraslado;
	    private String plantaDestino;
	    private String cantidaddespacho;
	    private String cantidadPedido;
	    private String cantidadEnTraslado;
	    private String cantidadRecibidaEnPa;
	    private String cantidadPendienteTraslado;
	    private Long pedidoDeComprasAsociado;
	    private String trasladosPendientes;
	    private String material;
	    private String posicion;
	    
		public PedidoTrasladoSapResponseDTO() {
		}
		
		public String getPlantaDestino() {
			return plantaDestino;
		}
		public void setPlantaDestino(String plantaDestino) {
			this.plantaDestino = plantaDestino;
		}
		public String getCantidadPedido() {
			return cantidadPedido;
		}
		public void setCantidadPedido(String cantidadPedido) {
			this.cantidadPedido = cantidadPedido;
		}
		public String getCantidadEnTraslado() {
			return cantidadEnTraslado;
		}
		public void setCantidadEnTraslado(String cantidadEnTraslado) {
			this.cantidadEnTraslado = cantidadEnTraslado;
		}
		public String getCantidadRecibidaEnPa() {
			return cantidadRecibidaEnPa;
		}
		public void setCantidadRecibidaEnPa(String cantidadRecibidaEnPa) {
			this.cantidadRecibidaEnPa = cantidadRecibidaEnPa;
		}
		public String getCantidadPendienteTraslado() {
			return cantidadPendienteTraslado;
		}
		public void setCantidadPendienteTraslado(String cantidadPendienteTraslado) {
			this.cantidadPendienteTraslado = cantidadPendienteTraslado;
		}
		public Long getPedidoDeComprasAsociado() {
			return pedidoDeComprasAsociado;
		}
		public void setPedidoDeComprasAsociado(Long pedidoDeComprasAsociado) {
			this.pedidoDeComprasAsociado = pedidoDeComprasAsociado;
		}
		public String getTrasladosPendientes() {
			return trasladosPendientes;
		}
		public void setTrasladosPendientes(String trasladosPendientes) {
			this.trasladosPendientes = trasladosPendientes;
		}
		public String getMaterial() {
			return material;
		}
		public void setMaterial(String material) {
			this.material = material;
		}
		public String getPosicion() {
			return posicion;
		}
		public void setPosicion(String posicion) {
			this.posicion = posicion;
		}

		public String getNumeroPedTraslado() {
			return numeroPedTraslado;
		}

		public void setNumeroPedTraslado(String numeroPedTraslado) {
			this.numeroPedTraslado = numeroPedTraslado;
		}

		public String getCantidaddespacho() {
			return cantidaddespacho;
		}

		public void setCantidaddespacho(String cantidaddespacho) {
			this.cantidaddespacho = cantidaddespacho;
		}
}
