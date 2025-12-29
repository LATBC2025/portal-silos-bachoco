package com.bachoco.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="TC_PLANTA")
public class PlantaEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="PLANTA_ID")
	private Integer id;
	
	@Column(name="PLANTA_CLAVE")
	private String planta;
	
	@Column(name="NOMBRE")
	private String nombre;
	
	@Column(name="SOCIEDAD")
	private String sociedad;

}
