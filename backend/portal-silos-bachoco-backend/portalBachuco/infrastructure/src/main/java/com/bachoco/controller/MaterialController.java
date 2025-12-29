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

import com.bachoco.command.CrearMaterialCommand;
import com.bachoco.dto.MaterialRequestDTO;
import com.bachoco.dto.MaterialResponseDTO;
import com.bachoco.mapper.MaterialMapper;
import com.bachoco.model.Categoria;
import com.bachoco.model.Material;
import com.bachoco.service.usecase.MaterialUseCase;

@RestController
@RequestMapping("/v1/material")
public class MaterialController {

	private final MaterialUseCase materialUseCase;

	public MaterialController(MaterialUseCase materialUseCase) {
		this.materialUseCase = materialUseCase;
	}
	
	@PostMapping
	public ResponseEntity<Void> saveMaterial(@RequestBody MaterialRequestDTO requestMaterial){
		CrearMaterialCommand materialCommand= new CrearMaterialCommand(requestMaterial.numero(),requestMaterial.descripcion(),1);
		this.materialUseCase.save(materialCommand);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<MaterialResponseDTO>> findAll(){
		List<MaterialResponseDTO> response=MaterialMapper.toResponse(this.materialUseCase.findAll());
		return new ResponseEntity<List<MaterialResponseDTO>>(response,HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> update(@PathVariable Integer id,@RequestBody MaterialRequestDTO request){
		Material material=new Material();
		material.setMaterialId(id);
		material.setNumero(request.numero());
		material.setDescripcion(request.descripcion());
		Categoria categoria= new Categoria();
		categoria.setId(1);
		material.setCategoria(categoria);
		this.materialUseCase.update(material);
		return new ResponseEntity<String>("",HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id){
		this.materialUseCase.delete(id);
		return new ResponseEntity<String>("",HttpStatus.OK);
	}
	
}
