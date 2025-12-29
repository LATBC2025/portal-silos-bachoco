package com.bachoco.model;

public class ReporteDespacho {

	private Integer id;
	private String bodega;
	private String folio;
	private String fechaEmbarque;
	private String numeroBoleta;
	private Float pesoBruto;
	private Float pesoTara;
	private Float pesoNeto;
	private String humedad;
	private String chofer;
	private String tractor;
	private String jaula;
	private String lineaTransportista;
	
	public ReporteDespacho() {
	}

	public ReporteDespacho(Integer id, String bodega, String folio, String fechaEmbarque, String numeroBoleta,
			Float pesoBruto, Float pesoTara, Float pesoNeto, String humedad, String chofer, String tractor,
			String jaula, String lineaTransportista) {
		this.id = id;
		this.bodega = bodega;
		this.folio = folio;
		this.fechaEmbarque = fechaEmbarque;
		this.numeroBoleta = numeroBoleta;
		this.pesoBruto = pesoBruto;
		this.pesoTara = pesoTara;
		this.pesoNeto = pesoNeto;
		this.humedad = humedad;
		this.chofer = chofer;
		this.tractor = tractor;
		this.jaula = jaula;
		this.lineaTransportista = lineaTransportista;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBodega() {
		return bodega;
	}

	public void setBodega(String bodega) {
		this.bodega = bodega;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getFechaEmbarque() {
		return fechaEmbarque;
	}

	public void setFechaEmbarque(String fechaEmbarque) {
		this.fechaEmbarque = fechaEmbarque;
	}

	public String getNumeroBoleta() {
		return numeroBoleta;
	}

	public void setNumeroBoleta(String numeroBoleta) {
		this.numeroBoleta = numeroBoleta;
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

	public Float getPesoNeto() {
		return pesoNeto;
	}

	public void setPesoNeto(Float pesoNeto) {
		this.pesoNeto = pesoNeto;
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

	public String getTractor() {
		return tractor;
	}

	public void setTractor(String tractor) {
		this.tractor = tractor;
	}

	public String getJaula() {
		return jaula;
	}

	public void setJaula(String jaula) {
		this.jaula = jaula;
	}

	public String getLineaTransportista() {
		return lineaTransportista;
	}

	public void setLineaTransportista(String lineaTransportista) {
		this.lineaTransportista = lineaTransportista;
	}

	

	
}
