package com.bachoco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoletaDTO {

	 private String numero;
	 private String fecha;
	 private String cliente;
	 private String chofer;
	 private String placas;
	 private String observaciones;
}
