package com.bachoco.dto.pedTraslado.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class SapItem {

	@JsonProperty("TPEDIDOTRASLADO")
	private String numeroPedTraslado;
	
    @JsonProperty("TPLANTADESTINO")
    private String plantaDestino;

    @JsonProperty("TCANTIDADPEDIDO")
    private String cantidadPedido;

    @JsonProperty("TCANTIDADENTRASLADO")
    private String cantidadEnTraslado;

    @JsonProperty("TCANTIDADRECIBIDAENPA")
    private String cantidadRecibidaEnPa;

    @JsonProperty("TCANTIDADPENDIENTETRASLADO")
    private String cantidadPendienteTraslado;

    @JsonProperty("TPEDIDODECOMPRASASOCIADO")
    private Long pedidoDeComprasAsociado;

    @JsonProperty("TTRASLADOSPENDIENTES")
    private String trasladosPendientes;

    @JsonProperty("TMATERIAL")
    private String material;

    @JsonProperty("TPOSICION")
    private String posicion;

    @JsonProperty("CANTIDADDESPACHO")
    private String cantidadDespacho;

    // Getters y Setters
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

	public String getCantidadDespacho() {
		return cantidadDespacho;
	}

	public void setCantidadDespacho(String cantidadDespacho) {
		this.cantidadDespacho = cantidadDespacho;
	}
	
	
}