package com.bachoco.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bachoco.persistence.entity.DepartamentoEntity;

public interface DepartamentoJpaRepository extends JpaRepository<DepartamentoEntity, Integer>{
	
	public Optional<DepartamentoEntity> findById(Integer id);
	public Optional<DepartamentoEntity> findByNombre(String clave);
	List<DepartamentoEntity> findAll();

}
