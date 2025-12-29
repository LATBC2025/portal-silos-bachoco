package com.bachoco.model;

public class PedidoCompraSapDTO {

		private String pedCompra;
	    private String contratoLegal;
	    private String pedidoRelacionado;
	    private String cantidadPedido;
	    private String cantidadEntrega;
	    private String cantidadDespacho;
	    private String material;
	    private String posicion;
	    private String plantaReceptor;
	    private String claseMov;
	    private String tipoPedido;
	    private String claseped;
	    
		public PedidoCompraSapDTO() {
		}

		public PedidoCompraSapDTO(String pedCompra, String contratoLegal, String pedidoRelacionado,
				String cantidadPedido, String cantidadEntrega, String cantidadDespacho, String material,
				String posicion, String plantaReceptor, String claseMov, String tipoPedido, String claseped) {
			this.pedCompra = pedCompra;
			this.contratoLegal = contratoLegal;
			this.pedidoRelacionado = pedidoRelacionado;
			this.cantidadPedido = cantidadPedido;
			this.cantidadEntrega = cantidadEntrega;
			this.cantidadDespacho = cantidadDespacho;
			this.material = material;
			this.posicion = posicion;
			this.plantaReceptor = plantaReceptor;
			this.claseMov = claseMov;
			this.tipoPedido = tipoPedido;
			this.claseped = claseped;
		}

		public String getPedCompra() {
			return pedCompra;
		}

		public void setPedCompra(String pedCompra) {
			this.pedCompra = pedCompra;
		}

		public String getContratoLegal() {
			return contratoLegal;
		}

		public void setContratoLegal(String contratoLegal) {
			this.contratoLegal = contratoLegal;
		}

		public String getPedidoRelacionado() {
			return pedidoRelacionado;
		}

		public void setPedidoRelacionado(String pedidoRelacionado) {
			this.pedidoRelacionado = pedidoRelacionado;
		}

		public String getCantidadPedido() {
			return cantidadPedido;
		}

		public void setCantidadPedido(String cantidadPedido) {
			this.cantidadPedido = cantidadPedido;
		}

		public String getCantidadEntrega() {
			return cantidadEntrega;
		}

		public void setCantidadEntrega(String cantidadEntrega) {
			this.cantidadEntrega = cantidadEntrega;
		}

		public String getCantidadDespacho() {
			return cantidadDespacho;
		}

		public void setCantidadDespacho(String cantidadDespacho) {
			this.cantidadDespacho = cantidadDespacho;
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

		public String getPlantaReceptor() {
			return plantaReceptor;
		}

		public void setPlantaReceptor(String plantaReceptor) {
			this.plantaReceptor = plantaReceptor;
		}

		public String getClaseMov() {
			return claseMov;
		}

		public void setClaseMov(String claseMov) {
			this.claseMov = claseMov;
		}

		public String getTipoPedido() {
			return tipoPedido;
		}

		public void setTipoPedido(String tipoPedido) {
			this.tipoPedido = tipoPedido;
		}

		public String getClaseped() {
			return claseped;
		}

		public void setClaseped(String claseped) {
			this.claseped = claseped;
		}

		
}
