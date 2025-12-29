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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.dto.SiloRequestDto;
import com.bachoco.dto.SiloResponseDto;
import com.bachoco.mapper.SiloMapper;
import com.bachoco.model.ApiResponse;
import com.bachoco.model.Silo;
import com.bachoco.service.usecase.SiloUseCase;

@RestController
@RequestMapping("v1/silo")
public class SiloController {

	private final SiloUseCase siloUsecase;

	public SiloController(SiloUseCase siloUsecase) {
		this.siloUsecase = siloUsecase;
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<SiloResponseDto>> save(@RequestBody SiloRequestDto request){
		Silo silo=SiloMapper.toDomain(request);
		ApiResponse<SiloResponseDto> response= new ApiResponse();
		ApiResponse<Silo> result=this.siloUsecase.save(silo);
		if(result.getCode().equals("0")) {
			response.setCode("0");
			response.setData(SiloMapper.toResponse(result.getData()));
		}else {
			response.setCode(result.getCode());
			response.setMessage(result.getMessage());
		}
		return new ResponseEntity<ApiResponse<SiloResponseDto>>(response,HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<SiloResponseDto>> getAll(){
		List<Silo> silos=this.siloUsecase.findAll();
		List<SiloResponseDto> siloResponse=silos.stream().map(silo-> new SiloResponseDto(silo.getId(),silo.getSilo(),silo.getNombre(),silo.getSociedad())).toList();
		return new ResponseEntity<List<SiloResponseDto>>(siloResponse,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		this.siloUsecase.delete(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SiloResponseDto> updateRecurso(@PathVariable Integer id,@RequestBody SiloRequestDto request){
		Silo silo=SiloMapper.toDomain(request);
		this.siloUsecase.update(silo,id);
		return new ResponseEntity<SiloResponseDto>(SiloMapper.toResponse(silo),HttpStatus.OK);
	}
	
	@GetMapping("/stock")
	public ResponseEntity<Float> findStockById(@RequestParam Integer siloId){
		return new ResponseEntity<Float>(this.siloUsecase.findStockById(siloId),HttpStatus.OK);
	}
	
}
