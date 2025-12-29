package com.bachoco.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bachoco.mapper.CatalogMapper;
import com.bachoco.model.Puesto;
import com.bachoco.persistence.entity.PuestoEntity;
import com.bachoco.persistence.repository.PuestoJpaRepository;
import com.bachoco.port.PuestoRepositoryPort;

@Repository
public class PuestoJpaRepositoryAdapter implements PuestoRepositoryPort {

	private final PuestoJpaRepository puestoRepository;
	
	public PuestoJpaRepositoryAdapter(PuestoJpaRepository puestoRepository) {
		this.puestoRepository = puestoRepository;
	}

	@Override
	public Puesto findById(Integer id) {
		return toDomain(this.puestoRepository.findById(id).get());
	}

	@Override
	public List<Puesto> findAll() {
		return this.puestoRepository.findAll().stream().map(p->CatalogMapper.toDomain(p)).toList();
	}
	
	private Puesto toDomain(PuestoEntity entity) {
		return new Puesto(entity.getId(),entity.getDescripcion());
	}

}
