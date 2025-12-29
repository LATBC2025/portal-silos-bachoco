package com.bachoco.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmacionDespachoResponse {

	private String numeroSap;
	private String code;
	private String mensaje;
	private String id;
}
