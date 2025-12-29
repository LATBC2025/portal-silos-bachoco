package com.bachoco.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bachoco.persistence.entity.MaterialEntity;

public interface MateriaJpalRepository extends JpaRepository<MaterialEntity,Integer>{

	public Optional<MaterialEntity> findByNumero(String numero);
	public List<MaterialEntity> findAll();
	public Optional<MaterialEntity> findByMaterialId(Integer id);
}
