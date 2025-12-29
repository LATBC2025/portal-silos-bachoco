package com.bachoco.model;

public class ConfDespachoPesosRequest {

	private Float pesoBruto;
	private Float pesoTara;
	private Integer idconfDespacho;
	
	public ConfDespachoPesosRequest() {
	}

	public ConfDespachoPesosRequest(Float pesoBruto, Float pesoTara, Integer idconfDespacho) {
		this.pesoBruto = pesoBruto;
		this.pesoTara = pesoTara;
		this.idconfDespacho = idconfDespacho;
	}

	public Float getPesoBruto() {
		return pesoBruto;
	}

	public void setPesoBruto(Float pesoBruto) {
		this.pesoBruto = pesoBruto;
	}

	public Float getPesoTara() {
		return pesoTara;
	}

	public void setPesoTara(Float pesoTara) {
		this.pesoTara = pesoTara;
	}

	public Integer getIdconfDespacho() {
		return idconfDespacho;
	}

	public void setIdconfDespacho(Integer idconfDespacho) {
		this.idconfDespacho = idconfDespacho;
	}
	
}
