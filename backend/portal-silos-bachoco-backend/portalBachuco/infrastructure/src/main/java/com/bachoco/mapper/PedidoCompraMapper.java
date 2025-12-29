package com.bachoco.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bachoco.model.PedidoCompraSapDTO;
import com.bachoco.persistence.entity.DetallePedidoCompraEntity;
import com.bachoco.persistence.entity.PedidoCompraEntity;

public class PedidoCompraMapper {

	public static PedidoCompraEntity toEntity(PedidoCompraSapDTO p) {
		PedidoCompraEntity entity = new PedidoCompraEntity();
		if (p.getPosicion() != null) {
			entity.setPosicion(String.valueOf(p.getPosicion()));
		}
		if (p.getContratoLegal() != null) {
			entity.setContratoLegal(p.getContratoLegal());
		}
		if (p.getPlantaReceptor() != null) {
			entity.setPlantaSilo(p.getPlantaReceptor());
		}

		return entity;
	}

	public static DetallePedidoCompraEntity toDetalleEntity(PedidoCompraSapDTO p) {
		PedidoCompraEntity entity = new PedidoCompraEntity();
		DetallePedidoCompraEntity detalle = new DetallePedidoCompraEntity();
		if (p.getCantidadDespacho() != null) {
			detalle.setCantidadDespachada(Float.parseFloat(p.getCantidadDespacho()));
		}
		if (p.getCantidadEntrega() != null) {
			detalle.setCantidadEntregada(Float.parseFloat(p.getCantidadEntrega()));
		}
		return detalle;
	}

}
