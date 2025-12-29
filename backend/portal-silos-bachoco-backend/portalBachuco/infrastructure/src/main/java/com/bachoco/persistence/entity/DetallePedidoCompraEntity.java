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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="TC_DETALLE_PEDIDO_COMPRA")
@Entity
public class DetallePedidoCompraEntity {
	
	@Id
	@Column(name="DETALLE_PEDIDO_COMPRA_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="CANTIDAD_ENTREGADA")
	private Float cantidadEntregada;
	@Column(name="CANTIDAD_DESPACHADA")
	private Float cantidadDespachada;
	@Column(name="CANTIDAD_PENDIENTE_DESPACHO")
	private Float cantidadPendienteDespacho;

}
