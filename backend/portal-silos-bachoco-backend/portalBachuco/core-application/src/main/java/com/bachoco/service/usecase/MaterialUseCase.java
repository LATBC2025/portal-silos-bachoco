package com.bachoco.service.usecase;

import java.util.List;
import java.util.Optional;

import com.bachoco.command.CrearMaterialCommand;
import com.bachoco.model.Categoria;
import com.bachoco.model.Material;
import com.bachoco.port.CategoriaRepositoryPort;
import com.bachoco.port.MaterialRepositoryPort;

public class MaterialUseCase {
	
	private final CategoriaRepositoryPort categoriaRepositoryPort;
	private final MaterialRepositoryPort materialRepositoryPort;

	public MaterialUseCase(MaterialRepositoryPort materialRepositoryPort,
			CategoriaRepositoryPort categoriaRepositoryPort) {
		this.materialRepositoryPort = materialRepositoryPort;
		this.categoriaRepositoryPort=categoriaRepositoryPort;
	}
	
	public void save(CrearMaterialCommand materialCommand) {
		Optional<Categoria> categoria=categoriaRepositoryPort.findById(materialCommand.getCategoriaId());
		Material material= new Material();
		material.setNumero(materialCommand.getNumero());
		material.setDescripcion(materialCommand.getDescripcion());
		material.setCategoria(categoria.get());
		this.materialRepositoryPort.save(material);
	}
	
	public void update(Material material) {
		Optional<Categoria> categoria = categoriaRepositoryPort.findById(material.getCategoria().getId());
		if (categoria.isPresent()) {
			Optional<Material> resultado = this.materialRepositoryPort.findById(material.getMaterialId());
			if (resultado.isPresent()) {
				Material update = resultado.get();
				update.setNumero(material.getNumero());
				update.setDescripcion(material.getDescripcion());
				update.setCategoria(categoria.get());
				this.materialRepositoryPort.update(material);
			}
		}
	}
	
	public Optional<Material> findById(Integer id){
		return this.materialRepositoryPort.findById(id);
	}
	
	public List<Material> findAll(){
		return this.materialRepositoryPort.findAll();
	}
	
	public void delete(Integer id) {
		this.materialRepositoryPort.delete(id);
	}

}
