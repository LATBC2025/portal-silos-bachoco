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

import com.bachoco.dto.BodegaRequestDto;
import com.bachoco.dto.BodegaResponseDTO;
import com.bachoco.mapper.BodegaMapper;
import com.bachoco.model.Bodega;
import com.bachoco.service.usecase.BodegaUsecase;

@RestController
@RequestMapping("/v1/bodega")
public class BodegaController {
	
	private final BodegaUsecase bodegaUsecase;
	
	public BodegaController(BodegaUsecase bodegaUsecase) {
		this.bodegaUsecase=bodegaUsecase;
	}
	
	@GetMapping
	public ResponseEntity<List<BodegaResponseDTO>> findAll(){
		List<Bodega> response=this.bodegaUsecase.findAll();
		return new ResponseEntity<List<BodegaResponseDTO>>(BodegaMapper.toRequest(response),HttpStatus.OK);
	}
	
	@GetMapping("/filter-silo")
	public ResponseEntity<List<BodegaResponseDTO>> findBySilo(@RequestParam Integer siloId){
		List<Bodega> response=this.bodegaUsecase.findBySilo(siloId);
		return new ResponseEntity<List<BodegaResponseDTO>>(BodegaMapper.toRequest(response),HttpStatus.OK);
	}
	
	
	@PostMapping
	public ResponseEntity<BodegaResponseDTO> save(@RequestBody BodegaRequestDto request){
		Bodega response= this.bodegaUsecase.save(request.bodega(),request.siloId());
		return new ResponseEntity<BodegaResponseDTO>(BodegaMapper.toRequest(response),HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<BodegaResponseDTO> update(@PathVariable Integer id,@RequestBody BodegaRequestDto request){
		Bodega response=this.bodegaUsecase.update(id,request.bodega(),request.siloId());
		return new ResponseEntity<BodegaResponseDTO>(BodegaMapper.toRequest(response),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		this.bodegaUsecase.delete(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
