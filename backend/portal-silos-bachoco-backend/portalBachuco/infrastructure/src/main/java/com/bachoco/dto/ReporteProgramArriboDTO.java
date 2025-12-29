package com.bachoco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteProgramArriboDTO {

	private Integer id;
	private Float toneladas;
	private String material;
	private String fecha;
	private String numeroPedido;
	private String destinoPlanta;
}
