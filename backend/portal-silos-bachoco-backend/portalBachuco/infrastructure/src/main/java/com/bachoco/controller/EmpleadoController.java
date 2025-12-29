package com.bachoco.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.model.EmpleadoResponse;
import com.bachoco.service.usecase.EmpleadoUseCase;

@RestController
@RequestMapping("/v1/empleado")
public class EmpleadoController {

	private final EmpleadoUseCase empleadoUseCase;

	public EmpleadoController(EmpleadoUseCase empleadoUseCase) {
		this.empleadoUseCase = empleadoUseCase;
	}
	
	@GetMapping
	public ResponseEntity<EmpleadoResponse> findByUsuarioOrCorreo(@RequestParam String value){
		Optional<EmpleadoResponse> response=this.empleadoUseCase.findByUsuarioOrCorreo(value);
		return new ResponseEntity<EmpleadoResponse>(response.get(),HttpStatus.OK);
	}
}
