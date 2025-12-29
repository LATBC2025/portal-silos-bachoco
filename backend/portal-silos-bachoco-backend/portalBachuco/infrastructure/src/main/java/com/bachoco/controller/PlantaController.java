package com.bachoco.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.dto.PlantaRequest;
import com.bachoco.dto.PlantaResponse;
import com.bachoco.model.ApiResponse;
import com.bachoco.model.Planta;
import com.bachoco.service.usecase.PlantaUseCase;

@RestController
@RequestMapping("/v1/planta")
public class PlantaController {
	
	private final PlantaUseCase plantaUseCase;

	public PlantaController(PlantaUseCase plantaUseCase) {
		this.plantaUseCase = plantaUseCase;
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<PlantaResponse>> save(@RequestBody PlantaRequest request){
		ApiResponse<PlantaResponse> response=new ApiResponse<>();
		Planta planta=new Planta();
		planta.setPlanta(request.getPlanta());
		planta.setNombre(request.getNombre());
		planta.setSociedad(request.getSociedad());
		ApiResponse<Planta> savePlanta=this.plantaUseCase.save(planta);
		if(savePlanta.getCode().equals("0")) {
			PlantaResponse result= PlantaResponse.builder()
					.id(savePlanta.getData().getId())
					.nombre(savePlanta.getData().getNombre())
					.sociedad(savePlanta.getData().getSociedad()).build();
			response.setCode("0");
			response.setData(result);
		}else {
			response.setCode(savePlanta.getCode());
			response.setMessage(savePlanta.getMessage());
		}
		return new ResponseEntity<ApiResponse<PlantaResponse>>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PlantaResponse> update(@PathVariable Integer id,@RequestBody PlantaRequest request){
		Planta planta=new Planta();
		planta.setId(id);
		planta.setPlanta(request.getPlanta());
		planta.setNombre(request.getNombre());
		planta.setSociedad(request.getSociedad());
		Planta savePlanta=this.plantaUseCase.update(planta);
		PlantaResponse response= PlantaResponse.builder()
				.id(savePlanta.getId())
				.planta(savePlanta.getPlanta())
				.nombre(savePlanta.getNombre())
				.sociedad(savePlanta.getSociedad()).build();
		return new ResponseEntity<PlantaResponse>(response,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PlantaResponse> findById(@PathVariable Integer id){

		Planta savePlanta=this.plantaUseCase.findById(id).get();
		PlantaResponse response= PlantaResponse.builder()
				.id(savePlanta.getId())
				.nombre(savePlanta.getNombre())
				.sociedad(savePlanta.getSociedad()).build();
		return new ResponseEntity<PlantaResponse>(response,HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		this.plantaUseCase.delete(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<PlantaResponse>> findAll(){
		List<Planta> savePlanta=this.plantaUseCase.findAll();
		List<PlantaResponse> response= savePlanta.stream().map(i-> new PlantaResponse(i.getId(),i.getPlanta(),i.getNombre(),i.getSociedad())).collect(Collectors.toList());
		return new ResponseEntity<List<PlantaResponse>>(response,HttpStatus.OK);
	}
	
	@GetMapping("/plantas-programadas-arribo")
	public ResponseEntity<List<PlantaResponse>> findAllByProgramArribo(@RequestParam String fechaInicio,@RequestParam String fechaFin){
		List<Planta> savePlanta=this.plantaUseCase.findAllByProgramArribo(fechaInicio,fechaFin);
		List<PlantaResponse> response= savePlanta.stream().map(i-> new PlantaResponse(i.getId(),i.getPlanta(),i.getNombre(),i.getSociedad())).collect(Collectors.toList());
		return new ResponseEntity<List<PlantaResponse>>(response,HttpStatus.OK);
	}

}
