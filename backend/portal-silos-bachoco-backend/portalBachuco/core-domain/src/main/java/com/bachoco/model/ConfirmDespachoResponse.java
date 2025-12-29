package com.bachoco.model;

public class ConfirmDespachoResponse {
	private final String claveBodega;
	private final String claveSilo;
	private final String claveMaterial;
	private final String fechaEmbarque;
	private final String numBoleta;
	private final double pesoBruto;
	private final double pesoTara;
	private final String humedad;
	private final String chofer;
	private final String placaJaula;
	private final String lineaTransportista;
	private final String claveDestino;
	private final String numPedidoTraslado;
	private final String tipoMovimiento;
	private final String idconfDespacho;
	private final String idPedTraslado;
	private final String numeroSap;
	private final String folio;

	public ConfirmDespachoResponse(String claveBodega, String claveSilo, String claveMaterial, String fechaEmbarque,
			String numBoleta, double pesoBruto, double pesoTara, String humedad, String chofer, String placaJaula,
			String lineaTransportista, String claveDestino, String numPedidoTraslado, String tipoMovimiento,
			String idconfDespacho, String idPedTraslado, String numeroSap,String folio) {
		this.claveBodega = claveBodega;
		this.claveSilo = claveSilo;
		this.claveMaterial = claveMaterial;
		this.fechaEmbarque = fechaEmbarque;
		this.numBoleta = numBoleta;
		this.pesoBruto = pesoBruto;
		this.pesoTara = pesoTara;
		this.humedad = humedad;
		this.chofer = chofer;
		this.placaJaula = placaJaula;
		this.lineaTransportista = lineaTransportista;
		this.claveDestino = claveDestino;
		this.numPedidoTraslado = numPedidoTraslado;
		this.tipoMovimiento = tipoMovimiento;
		this.idconfDespacho = idconfDespacho;
		this.idPedTraslado = idPedTraslado;
		this.numeroSap = numeroSap;
		this.folio=folio;
	}

	public String getFolio() {
		return folio;
	}

	public String getClaveBodega() {
		return claveBodega;
	}

	public String getClaveSilo() {
		return claveSilo;
	}

	public String getClaveMaterial() {
		return claveMaterial;
	}

	public String getFechaEmbarque() {
		return fechaEmbarque;
	}

	public String getNumBoleta() {
		return numBoleta;
	}

	public double getPesoBruto() {
		return pesoBruto;
	}

	public double getPesoTara() {
		return pesoTara;
	}

	public String getHumedad() {
		return humedad;
	}

	public String getChofer() {
		return chofer;
	}

	public String getPlacaJaula() {
		return placaJaula;
	}

	public String getLineaTransportista() {
		return lineaTransportista;
	}

	public String getClaveDestino() {
		return claveDestino;
	}

	public String getNumPedidoTraslado() {
		return numPedidoTraslado;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public String getIdconfDespacho() {
		return idconfDespacho;
	}

	public String getIdPedTraslado() {
		return idPedTraslado;
	}

	public String getNumeroSap() {
		return numeroSap;
	}

}