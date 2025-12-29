package com.bachoco.model;

public class PedidoSapResponseDTO {
	
	private String pedidoRelacionado;
	private String cantidadPedido;
	private String cantidadEntrega;
	private String cantidadDespacho;
	private String cantidadPendienteDespacho;
	private String cantidadPendienteSurtir;
	private String material;
	private String posicion;
	private String plantaReceptor;
	private String claseMov;
	private String tipoPedido;
	private String clasePed;
	private String pedCompra;
	private String contratoLegal;
	private String param1;
	private String param2;
	private String param3;
	
	public PedidoSapResponseDTO() {
	}

	public PedidoSapResponseDTO(String pedidoRelacionado, String cantidadPedido, String cantidadEntrega,
			String cantidadDespacho, String cantidadPendienteDespacho, String cantidadPendienteSurtir, String material,
			String posicion, String plantaReceptor, String claseMov, String tipoPedido, String clasePed,
			String pedCompra, String contratoLegal, String param1, String param2, String param3) {
		this.pedidoRelacionado = pedidoRelacionado;
		this.cantidadPedido = cantidadPedido;
		this.cantidadEntrega = cantidadEntrega;
		this.cantidadDespacho = cantidadDespacho;
		this.cantidadPendienteDespacho = cantidadPendienteDespacho;
		this.cantidadPendienteSurtir = cantidadPendienteSurtir;
		this.material = material;
		this.posicion = posicion;
		this.plantaReceptor = plantaReceptor;
		this.claseMov = claseMov;
		this.tipoPedido = tipoPedido;
		this.clasePed = clasePed;
		this.pedCompra = pedCompra;
		this.contratoLegal = contratoLegal;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
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

	public String getCantidadPendienteDespacho() {
		return cantidadPendienteDespacho;
	}

	public void setCantidadPendienteDespacho(String cantidadPendienteDespacho) {
		this.cantidadPendienteDespacho = cantidadPendienteDespacho;
	}

	public String getCantidadPendienteSurtir() {
		return cantidadPendienteSurtir;
	}

	public void setCantidadPendienteSurtir(String cantidadPendienteSurtir) {
		this.cantidadPendienteSurtir = cantidadPendienteSurtir;
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

	public String getClasePed() {
		return clasePed;
	}

	public void setClasePed(String clasePed) {
		this.clasePed = clasePed;
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

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}
	
	
}
