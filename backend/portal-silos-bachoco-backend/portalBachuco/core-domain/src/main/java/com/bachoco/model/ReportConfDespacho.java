package com.bachoco.model;

public class ReportConfDespacho {

	private String numeroSap;
	private String folio;
	private String numPedTraslado;
	private String fechaEmbarque;
	private String plantaDestino;
	private String silo;
	private String material;
	private String numBoleta;
	private String pesoBruto;
	private String pesoTara;
	private String humedad;
	private String chofer;
	private String placaJaula;
	private String lineaTransportista;
	
	public ReportConfDespacho() {
	}

	public ReportConfDespacho(String numeroSap, String folio, String numPedTraslado, String fechaEmbarque,
			String plantaDestino, String silo, String material, String numBoleta, String pesoBruto, String pesoTara,
			String humedad, String chofer, String placaJaula, String lineaTransportista) {
		this.numeroSap = numeroSap;
		this.folio = folio;
		this.numPedTraslado = numPedTraslado;
		this.fechaEmbarque = fechaEmbarque;
		this.plantaDestino = plantaDestino;
		this.silo = silo;
		this.material = material;
		this.numBoleta = numBoleta;
		this.pesoBruto = pesoBruto;
		this.pesoTara = pesoTara;
		this.humedad = humedad;
		this.chofer = chofer;
		this.placaJaula = placaJaula;
		this.lineaTransportista = lineaTransportista;
	}

	public String getNumeroSap() {
		return numeroSap;
	}

	public void setNumeroSap(String numeroSap) {
		this.numeroSap = numeroSap;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getNumPedTraslado() {
		return numPedTraslado;
	}

	public void setNumPedTraslado(String numPedTraslado) {
		this.numPedTraslado = numPedTraslado;
	}

	public String getFechaEmbarque() {
		return fechaEmbarque;
	}

	public void setFechaEmbarque(String fechaEmbarque) {
		this.fechaEmbarque = fechaEmbarque;
	}

	public String getPlantaDestino() {
		return plantaDestino;
	}

	public void setPlantaDestino(String plantaDestino) {
		this.plantaDestino = plantaDestino;
	}

	public String getSilo() {
		return silo;
	}

	public void setSilo(String silo) {
		this.silo = silo;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getNumBoleta() {
		return numBoleta;
	}

	public void setNumBoleta(String numBoleta) {
		this.numBoleta = numBoleta;
	}

	public String getPesoBruto() {
		return pesoBruto;
	}

	public void setPesoBruto(String pesoBruto) {
		this.pesoBruto = pesoBruto;
	}

	public String getPesoTara() {
		return pesoTara;
	}

	public void setPesoTara(String pesoTara) {
		this.pesoTara = pesoTara;
	}

	public String getHumedad() {
		return humedad;
	}

	public void setHumedad(String humedad) {
		this.humedad = humedad;
	}

	public String getChofer() {
		return chofer;
	}

	public void setChofer(String chofer) {
		this.chofer = chofer;
	}

	public String getPlacaJaula() {
		return placaJaula;
	}

	public void setPlacaJaula(String placaJaula) {
		this.placaJaula = placaJaula;
	}

	public String getLineaTransportista() {
		return lineaTransportista;
	}

	public void setLineaTransportista(String lineaTransportista) {
		this.lineaTransportista = lineaTransportista;
	}

	
}
