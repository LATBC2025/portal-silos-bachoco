package com.bachoco.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="TC_PERMISO")
public class PermisoEntity {
	
	@Id
	@Column(name="PERMISO_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="PERMISO_CLAVE")
	private String clave;
	@Column(name="PERMISO_DESCRIPCION")
	private String descripcion;

}
