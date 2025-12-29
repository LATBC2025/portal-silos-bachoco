package com.bachoco.mapper;

import com.bachoco.model.Departamento;
import com.bachoco.model.Puesto;
import com.bachoco.persistence.entity.DepartamentoEntity;
import com.bachoco.persistence.entity.PuestoEntity;

public class CatalogMapper {

	public static Departamento toDomain(DepartamentoEntity entity) {
		Departamento depto= new Departamento();
		depto.setId(entity.getId());
		depto.setNombre(entity.getNombre());
		return depto;
	}
	
	public static Puesto toDomain(PuestoEntity entity) {
		Puesto puesto= new Puesto();
		puesto.setId(entity.getId());
		puesto.setDescripcion(entity.getDescripcion());
		return puesto;
	}
}
