package com.bachoco.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bachoco.persistence.entity.OtpEntity;

import jakarta.transaction.Transactional;

public interface OtpJpaRepository extends JpaRepository<OtpEntity, Long>{
	
	Optional<OtpEntity> findByUsuario(String usuario);
	@Query(value = "SELECT o FROM otp o WHERE o.usuario = :usuario ORDER BY o.id DESC LIMIT 1")
	Optional<OtpEntity> findLatestByUsuario(@Param("usuario") String usuario);
	
	@Modifying
    @Transactional
    // El nombre del método hace que Spring JPA construya la consulta:
    // DELETE FROM OtpEntity WHERE creationTime < ?1
    int deleteByExpiracionBefore(LocalDateTime cutoffTime);

}
