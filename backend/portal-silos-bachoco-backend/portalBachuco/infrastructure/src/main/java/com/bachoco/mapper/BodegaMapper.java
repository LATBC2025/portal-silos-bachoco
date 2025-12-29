package com.bachoco.mapper;

import java.util.List;

import com.bachoco.dto.BodegaResponseDTO;
import com.bachoco.model.Bodega;
import com.bachoco.persistence.entity.BodegaEntity;

public class BodegaMapper {
	
	public static BodegaEntity toEntity(Bodega bodega) {
		return new BodegaEntity().builder()
				.clave(bodega.getNombre())
				.activo(1)
				.descripcion(bodega.getDescripcion()).build();
	}
	
	public static Bodega toDomain(BodegaEntity entity) {
		Bodega bodega=new Bodega();
		bodega.setId(entity.getId());
		bodega.setNombre(entity.getClave());
		bodega.setSilo(SiloMapper.toDomain(entity.getSilo()));
		return bodega;
	}
	
	public static List<Bodega> toDomain(List<BodegaEntity> list){
		return list.stream().map(b->BodegaMapper.toDomain(b)).toList();
	}
	
	public static BodegaResponseDTO toRequest(Bodega bodega){
		return new BodegaResponseDTO(bodega.getId(),bodega.getNombre(),bodega.getSilo().getId(),bodega.getSilo().getSilo(),bodega.getSilo().getNombre());
	}
	
	public static List<BodegaResponseDTO> toRequest(List<Bodega> bodegas){
		return bodegas.stream().map(b->BodegaMapper.toRequest(b)).toList();
	}
	

}
