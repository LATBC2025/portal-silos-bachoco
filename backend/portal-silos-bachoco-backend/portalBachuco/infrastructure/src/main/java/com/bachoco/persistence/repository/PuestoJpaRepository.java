package com.bachoco.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bachoco.persistence.entity.PuestoEntity;

public interface PuestoJpaRepository extends JpaRepository<PuestoEntity,Integer> {

	public Optional<PuestoEntity> findById(Integer id);
	public Optional<PuestoEntity> findByDescripcion(String clave);
	public List<PuestoEntity> findAll();
}
