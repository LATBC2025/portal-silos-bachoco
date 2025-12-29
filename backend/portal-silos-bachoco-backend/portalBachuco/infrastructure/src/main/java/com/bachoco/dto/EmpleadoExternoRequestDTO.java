package com.bachoco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoExternoRequestDTO {

	private String nombre;
	private String rfc;
	private String correo;
	private String usuario;
	private String siloId;
}
