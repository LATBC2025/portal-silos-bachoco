package com.bachoco.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.bachoco.model.DetallePedidoCompra;
import com.bachoco.persistence.entity.DetallePedidoCompraEntity;

public class DetallePedidoCompraMapper {

	public static DetallePedidoCompra toDomain(DetallePedidoCompraEntity entity) {
		DetallePedidoCompra detalle= new DetallePedidoCompra();
		if(entity.getCantidadDespachada()!=null) {
			detalle.setCantidadDespachada(entity.getCantidadDespachada());
		}
		if(entity.getCantidadEntregada()!=null) {
			detalle.setCantidadEntregada(entity.getCantidadEntregada());
		}
		if(entity.getCantidadPendienteDespacho()!=null) {
			detalle.setCantidadDespachada(entity.getCantidadDespachada());
		}
		return detalle;
	}
	
	public static Set<DetallePedidoCompra> toDomain(Set<DetallePedidoCompraEntity> entities){
		return entities.stream().map(DetallePedidoCompraMapper::toDomain).collect(Collectors.toSet());
	}
	
	public static Set<DetallePedidoCompraEntity> toEntity(Set<DetallePedidoCompra> detalles){
		return detalles.stream().map(DetallePedidoCompraMapper::toEntity).collect(Collectors.toSet());
	}
	
	public static DetallePedidoCompraEntity toEntity(DetallePedidoCompra detalle) {
		DetallePedidoCompraEntity entity= new DetallePedidoCompraEntity();
		if(detalle.getCantidadDespachada()!=null) {
			entity.setCantidadDespachada(detalle.getCantidadDespachada());
		}
		if(detalle.getCantidadEntregada()!=null) {
			entity.setCantidadEntregada(detalle.getCantidadEntregada());
		}
		if(detalle.getCantidadPendiente()!=null) {
			entity.setCantidadPendienteDespacho(detalle.getCantidadPendiente());
		}
		return entity;
	}
}
