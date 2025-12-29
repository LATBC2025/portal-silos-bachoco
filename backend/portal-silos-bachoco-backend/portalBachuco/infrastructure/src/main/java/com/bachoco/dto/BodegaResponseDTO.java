package com.bachoco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodegaResponseDTO {
	
	private Integer id;
	private String nombre;
	private Integer siloId;
	private String claveSilo;
	private String nombreSilo;

}
