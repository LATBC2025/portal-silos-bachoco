package com.bachoco.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tc_perfil")
public class PerfilEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PERFIL_ID")
	private Integer id;
	@Column(name = "PERFIL_CLAVE")
	private String clave;
	@Column(name = "PERFIL_DESCRIPCION")
	private String descripcion;
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tc_perfilpermiso", 
        joinColumns = @JoinColumn(name = "PERFIL_ID"), // CORREGIDO
        inverseJoinColumns = @JoinColumn(name = "PERMISO_ID") // CORREGIDO
    )
	private Set<PermisoEntity> permisos = new HashSet<>();

}
