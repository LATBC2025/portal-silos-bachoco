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
@Table(name="TC_EMPLEADO_EXTERNO")
@Entity
public class EmpleadoExternoEntity {

	@Id
	@Column(name="TC_EMP_EXTERNO_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="RFC")
	private String rfc;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = false)
	@JoinColumn(name="TC_EMPLEADO_ID",referencedColumnName = "EMPLEADO_ID")
	private EmpleadoEntity empleado;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = false)
	@JoinColumn(name="TC_SILO_ID",referencedColumnName = "SILO_ID")
	private SiloEntity silo;
	
}
