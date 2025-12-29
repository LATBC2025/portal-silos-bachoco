package com.bachoco.persistence.entity;

import java.sql.Timestamp;
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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
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
@Entity(name="pedidoCompra")
@Table(name = "TC_PEDIDO_COMPRA")
@NamedQueries({
    @NamedQuery(
        name = "PedidoCompra.findByFilters",
        query = "SELECT p FROM pedidoCompra p WHERE p.material.materialId = :materialId AND p.silo.id = :siloId AND p.fechaCompra BETWEEN :fechaInicio AND :fechaFin"
    )
})
public class PedidoCompraEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PEDIDO_COMPRA_ID")
	private Integer id;
	@Column(name = "NUMERO_PEDIDO")
	private String numeroPedido;
	@Column(name = "CANTIDAD_PEDIDA")
	private Float cantidadPedida;
	@Column(name = "URL_CER_CONSERVACION")
	private String urlCerDeposito;
	@Column(name = "CONTRATO_LEGAL")
	private String contratoLegal;
	@Column(name = "MATERIAL")
	private String nombreMaterial;
	@Column(name = "POSICION")
	private String posicion;
	@Column(name = "PLANTA_DESTINO")
	private String plantaSilo;
	@Column(name="FECHA_COMPRA")
	private Timestamp fechaCompra;
	@Column(name="CER_DEPOSITO")
	private byte[] certificadoDeposito;
	@Column(name="TIPO_EXTENCION")
	private String extencion;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "DETALLE_PEDIDO_COMPRA_ID")
	private Set<DetallePedidoCompraEntity> detallePedidoCompra = new HashSet<>();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "TC_SILO_ID", referencedColumnName = "SILO_ID")
	private SiloEntity silo;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "TC_MATERIAL_ID", referencedColumnName = "MATERIAL_ID")
	private MaterialEntity material;
}
