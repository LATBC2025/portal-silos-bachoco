package com.bachoco.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.bachoco.model.Planta;
import com.bachoco.persistence.entity.PlantaEntity;

public class PlantaMapper {

	public static PlantaEntity toEntity(Planta planta) {
		return PlantaEntity.builder()
				.planta(planta.getPlanta())
				.nombre(planta.getNombre())
				.sociedad(planta.getSociedad()).build();
	}
	
	public static Planta toDomain(PlantaEntity planta) {
		Planta plantaDomain= new Planta();
		plantaDomain.setId(planta.getId());
		plantaDomain.setPlanta(planta.getPlanta());
		plantaDomain.setNombre(planta.getNombre());
		plantaDomain.setSociedad(planta.getSociedad());
		return plantaDomain;
	}
	
	public static List<Planta> toListDomain(List<PlantaEntity> plantas){
		return plantas.stream().map(p->new Planta(p.getId(),p.getPlanta(),p.getNombre(),p.getSociedad())).collect(Collectors.toList());
	}
	
}
