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

import com.bachoco.model.EmpleadoInternoRequest;
import com.bachoco.model.EmpleadoInternoResponse;
import com.bachoco.service.usecase.EmpleadoUseCase;

@RestController
@RequestMapping("/v1/empleado-interno")
public class EmpleadoInternoController {

	private final EmpleadoUseCase empleadoUseCase;

	public EmpleadoInternoController(EmpleadoUseCase empleadoUseCase) {
		this.empleadoUseCase = empleadoUseCase;
	}
	@PostMapping
	public ResponseEntity<Void> save(@RequestBody EmpleadoInternoRequest request){
		this.empleadoUseCase.saveEmpleadoInterno(request);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable Integer id,@RequestBody EmpleadoInternoRequest req){
		this.empleadoUseCase.update(id, req);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	@GetMapping
	public ResponseEntity<List<EmpleadoInternoResponse>> findAll(){
		List<EmpleadoInternoResponse> empleados= this.empleadoUseCase.findAllEmpleadoResponse();
		return new ResponseEntity<List<EmpleadoInternoResponse>>(empleados,HttpStatus.OK);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		this.empleadoUseCase.delete(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
