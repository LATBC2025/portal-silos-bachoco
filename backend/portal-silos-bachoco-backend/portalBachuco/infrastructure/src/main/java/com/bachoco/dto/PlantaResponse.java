package com.bachoco.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantaResponse {

	private Integer id;
	private String planta;
	private String nombre;
	private String sociedad;
}
