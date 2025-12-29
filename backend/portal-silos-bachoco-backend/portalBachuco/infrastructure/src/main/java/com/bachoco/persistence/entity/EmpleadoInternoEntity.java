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
@Table(name="tc_empleado_interno")
@Entity
public class EmpleadoInternoEntity {

	@Id
	@Column(name="EMP_INTERNO_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = false)
	@JoinColumn(name="TC_EMPLEADO_ID",referencedColumnName = "EMPLEADO_ID")
	private EmpleadoEntity empleado;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = false)
	@JoinColumn(name="TC_DEPARTAMENTO_ID",referencedColumnName = "DEPARTAMENTO_ID")
	private DepartamentoEntity departamento;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = false)
	@JoinColumn(name="TC_PUESTO_ID",referencedColumnName = "PUESTO_ID")
	private PuestoEntity puesto;

}
