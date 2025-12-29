package com.bachoco.mapper;
import java.util.List;

import com.bachoco.dto.MaterialResponseDTO;
import com.bachoco.model.Categoria;
import com.bachoco.model.Material;
import com.bachoco.persistence.entity.CategoriaEntity;
import com.bachoco.persistence.entity.MaterialEntity;

public class MaterialMapper {

	public static Material toDomain(MaterialEntity entity) {
		Material material= new Material();
		material.setMaterialId(entity.getMaterialId());
		if(entity.getNumero()!=null) {
			material.setNumero(entity.getNumero());
		}
		if(entity.getDescripcion()!=null) {
			material.setDescripcion(entity.getDescripcion());
		}
		return material;
	}
	
	public static MaterialEntity toEntity(Material material) {
		return MaterialEntity.builder()
				.numero(material.getNumero())
				.descripcion(material.getDescripcion())
				.categoria(toEntityCategoria(material.getCategoria())).build();
	}
	
	public static CategoriaEntity toEntityCategoria(Categoria cat) {
		CategoriaEntity entity=new CategoriaEntity();
		entity.setId(cat.getId());
		return entity;
	}
	
	public static List<Material> toDomain(List<MaterialEntity> entity) {
		return entity.stream().map(e->MaterialMapper.toDomain(e)).toList();
	}
	
	public static List<MaterialEntity> toEntity(List<Material> materiales) {
		return materiales.stream().map(e->MaterialMapper.toEntity(e)).toList();
	} 
	
	public static MaterialResponseDTO toResponse(Material material) {
		return new MaterialResponseDTO(material.getMaterialId(),material.getNumero(),material.getDescripcion(),0,"");
	}
	
	public static List<MaterialResponseDTO> toResponse(List<Material> materiales){
		return materiales.stream().map(m->MaterialMapper.toResponse(m)).toList();
	}
}
