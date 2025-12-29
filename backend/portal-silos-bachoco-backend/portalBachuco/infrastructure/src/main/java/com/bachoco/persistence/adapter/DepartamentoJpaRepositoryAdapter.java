package com.bachoco.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bachoco.mapper.CatalogMapper;
import com.bachoco.model.Departamento;
import com.bachoco.persistence.entity.DepartamentoEntity;
import com.bachoco.persistence.repository.DepartamentoJpaRepository;
import com.bachoco.port.DepartamentoRepositoryPort;

@Repository
public class DepartamentoJpaRepositoryAdapter implements DepartamentoRepositoryPort{

	private final DepartamentoJpaRepository departamentoRepository;
	
	public DepartamentoJpaRepositoryAdapter(DepartamentoJpaRepository departamentoRepository) {
		this.departamentoRepository = departamentoRepository;
	}

	@Override
	public Departamento findById(Integer id) {
		return toDomain(this.departamentoRepository.findById(id).get());
	}
	
	public Departamento toDomain(DepartamentoEntity entity) {
		return new Departamento(entity.getId(),entity.getNombre());
	}

	@Override
	public List<Departamento> findAll() {
		return this.departamentoRepository.findAll().stream().map(d->CatalogMapper.toDomain(d)).toList();
	}
}
