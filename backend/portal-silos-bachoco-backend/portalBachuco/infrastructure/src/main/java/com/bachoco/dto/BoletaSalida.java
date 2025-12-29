package com.bachoco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoletaSalida {

	private Cliente proveedor;
    private Cliente cliente;
    private String numeroBoleta;
    private String folio;
    private String fecha;
    private String hora;
    private String producto;
    private String transportista;
    private String guia;
    private String chofer;
    private String placasVehiculo;
    private String placasRemolque;
    private String numeroTicketBascula;
    private String observaciones;
    private String pedidoTraslado;
    private String nombreSilo;
    private double pesoBruto;
    private double tara;
    private double pesoNeto;
    private String humedad;
    private String impurezas;
    private String quebrado;
    private String danado;
}
