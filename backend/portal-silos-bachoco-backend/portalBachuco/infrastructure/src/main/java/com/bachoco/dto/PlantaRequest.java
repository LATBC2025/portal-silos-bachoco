package com.bachoco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantaRequest {

	private String planta;
	private String nombre;
	private String sociedad;
}
