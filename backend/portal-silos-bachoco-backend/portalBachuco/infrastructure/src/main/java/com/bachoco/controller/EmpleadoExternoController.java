package com.bachoco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.model.EmpleadoExternoRequest;
import com.bachoco.model.EmpleadoExternoResponse;
import com.bachoco.service.usecase.EmpleadoExternoUseCase;

@RestController
@RequestMapping("/v1/empleado-externo")
public class EmpleadoExternoController {

	private final EmpleadoExternoUseCase empleadoExternoUseCase;
	
	public EmpleadoExternoController(EmpleadoExternoUseCase empleadoExternoUseCase) {
		this.empleadoExternoUseCase = empleadoExternoUseCase;
	}

	@PostMapping
	public ResponseEntity<EmpleadoExternoResponse> save(@RequestBody EmpleadoExternoRequest req){
		EmpleadoExternoResponse response=this.empleadoExternoUseCase.save(req);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable Integer id,@RequestBody EmpleadoExternoRequest req){
		this.empleadoExternoUseCase.update(id, req);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	@GetMapping
	public ResponseEntity<List<EmpleadoExternoResponse>> findAll(){
		List<EmpleadoExternoResponse> response=this.empleadoExternoUseCase.findAll();
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		this.empleadoExternoUseCase.delete(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}
