package com.bachoco.persistence.entity;

import com.bachoco.model.Categoria;
import com.bachoco.model.Material;
import com.bachoco.model.Otp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "TC_MATERIAL")
public class MaterialEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MATERIAL_ID")
	private Integer materialId;
	@Column(name = "NUMERO_MATERIAL")
	private String numero;
	@Column(name = "MATERIAL_DESCRIPCION")
	private String descripcion;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TC_CATEGORIA_ID", referencedColumnName = "CATEGORIA_ID")
	private CategoriaEntity categoria;
}
