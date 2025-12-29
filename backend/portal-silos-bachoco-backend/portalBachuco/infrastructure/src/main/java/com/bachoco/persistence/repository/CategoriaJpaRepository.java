package com.bachoco.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bachoco.persistence.entity.CategoriaEntity;

public interface CategoriaJpaRepository extends JpaRepository<CategoriaEntity, Integer> {

	public Optional<CategoriaEntity> findById(Integer id);
	List<CategoriaEntity> findAll();
}
