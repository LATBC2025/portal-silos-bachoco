package com.bachoco.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bachoco.dto.SiloRequestDto;
import com.bachoco.dto.SiloResponseDto;
import com.bachoco.model.Silo;
import com.bachoco.persistence.entity.SiloEntity;

public class SiloMapper {
	
	public static SiloEntity toEntity(Silo silo) {
		return SiloEntity.builder()
				.id(silo.getId())
				.silo(silo.getSilo())
				.nombre(silo.getNombre())
				.sociedad(silo.getSociedad()).build();
	}
	
	public static SiloEntity toEntitySave(Silo silo) {
		return SiloEntity.builder()
				.silo(silo.getSilo())
				.nombre(silo.getNombre())
				.stock(new BigDecimal(0))
				.sociedad(silo.getSociedad()).build();
	}


	public static Silo toDomain(SiloEntity siloEntity) {
		return new Silo(siloEntity.getId(),siloEntity.getSilo(),siloEntity.getNombre(),siloEntity.getSociedad(),siloEntity.getStock().doubleValue());
	}
	
	public static List<Silo> toListDomain(List<SiloEntity> lista) {
		List<Silo> listaSilo= new ArrayList<>();
		lista.stream().forEach(silo->listaSilo.add(new Silo(silo.getId(),silo.getSilo(),silo.getNombre(),silo.getSociedad(),silo.getStock().doubleValue())));
		return listaSilo;
	}
	
	public static Silo toDomain(SiloRequestDto request) {
		Silo silo= new Silo();
		silo.setSilo(request.silo());
		silo.setNombre(request.nombre());
		silo.setSociedad(request.sociedad());
		return silo;
	}
	
	public static SiloResponseDto toResponse(Silo silo) {
		return new SiloResponseDto(silo.getId(),silo.getSilo(),silo.getNombre(),silo.getSociedad());
	}
}
