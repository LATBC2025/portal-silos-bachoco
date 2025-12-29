package com.bachoco.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="bodega")
@Table(name="TC_BODEGA")
public class BodegaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="BODEGA_ID")
	private Integer id;
	@Column(name="BODEGA_CLAVE")
	private String clave;
	@Column(name="ACTIVO")
	private Integer activo;
	@Column(name="BODEGA_DESCRIPCION")
	private String descripcion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TC_SILO_ID")
	private SiloEntity silo;
}
