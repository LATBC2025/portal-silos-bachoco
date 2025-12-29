package com.bachoco.persistence.entity;

import jakarta.persistence.CascadeType;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tc_empleado")
@Entity(name="empleado")
public class EmpleadoEntity {

	@Id
	@Column(name="EMPLEADO_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="NOMBRE")
	private String nombre;
	@Column(name="APELLIDO_PATERNO")
	private String apellidoPaterno;
	@Column(name="APELLIDO_MATERNO")
	private String apellidoMaterno;
	@Column(name="CORREO")
	private String correo;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "TC_USUARIO_ID", referencedColumnName = "USUARIO_ID") 
	private UsuarioEntity usuario;
}
