package com.bachoco.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bachoco.persistence.entity.PedidoCompraEntity;

import jakarta.transaction.Transactional;

public interface PedidoCompraJpaRepository extends JpaRepository<PedidoCompraEntity, Integer> {

	@Modifying
	@Transactional
	@Query("UPDATE pedidoCompra p SET p.urlCerDeposito = :nuevoUrl, p.extencion=:tipoExtencion WHERE p.id = :pedidoCompraId")
	void actualizarUrlDocument(@Param("nuevoUrl") String nuevoUrl,@Param("tipoExtencion") String tipoExtencion, @Param("pedidoCompraId") Integer pedidoCompraId);
}
