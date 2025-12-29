package com.bachoco.persistence.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="silo")
@Table(name="TC_SILO")
public class SiloEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="SILO_ID")
	private Integer id;
	@Column(name="SILO_CLAVE")
	private String silo;
	@Column(name="SILO_NOMBRE")
	private String nombre;
	@Column(name="SOCIEDAD")
	private String sociedad;
	@Column(name="STOCK_SILO")
	private BigDecimal stock;

	 @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
	 @JoinColumn(name = "BODEGA_ID")
	 private Set<BodegaEntity> bodegas= new HashSet<>();
}
