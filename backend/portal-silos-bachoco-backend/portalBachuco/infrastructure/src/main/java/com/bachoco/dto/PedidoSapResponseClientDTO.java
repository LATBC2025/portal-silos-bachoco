package com.bachoco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PedidoSapResponseClientDTO {

   
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
    
    // Los campos param1, param2, etc. se quedarán en null si no vienen en el JSON
    // (A menos que SAP los envíe con esos nombres exactos).
    private String param1;
    private String param2;
    private String param3;
}
