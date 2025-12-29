package com.bachoco.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bachoco.persistence.entity.UsuarioEntity;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity,Integer> {

	public Optional<UsuarioEntity> findById(Integer id);
	@Query(name="SELECT u FROM usuario u JOIN u.perfiles u WHERE u.usuario = :usuario ")
	public Optional<UsuarioEntity> findByUsuario(String usuario);
}
