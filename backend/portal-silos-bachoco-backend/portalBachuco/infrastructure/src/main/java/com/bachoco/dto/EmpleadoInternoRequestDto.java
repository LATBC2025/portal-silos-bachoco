package com.bachoco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoInternoRequestDto {
	
	private String nombre;
	private String correo;
	private String usuario;
	private Integer departamentoId;
	private Integer puestoId;
	private Integer perfilId;
}
