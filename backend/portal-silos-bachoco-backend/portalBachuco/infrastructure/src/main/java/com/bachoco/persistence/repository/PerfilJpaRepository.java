package com.bachoco.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bachoco.persistence.entity.PerfilEntity;

public interface PerfilJpaRepository extends JpaRepository<PerfilEntity,Integer> {

	public Optional<PerfilEntity> findById(Integer id);
    Optional<PerfilEntity> findByClave(String descripcion);
	List<PerfilEntity> findAll();
}
