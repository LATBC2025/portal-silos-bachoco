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
public class EmpleadoInternoResponseDto {
	
	private Integer id;
	private String nombre;
	private String correo;
	private String username;
	private Integer departamentoId;
	private String departamentoDesc;
	private Integer perfilId;
	private String perfilDesc;

}
