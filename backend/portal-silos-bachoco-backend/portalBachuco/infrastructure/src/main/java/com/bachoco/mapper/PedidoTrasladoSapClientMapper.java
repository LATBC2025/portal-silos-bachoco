package com.bachoco.mapper;


import com.bachoco.dto.pedTraslado.dto.SapItem;
import com.bachoco.model.PedidoTrasladoSapResponseDTO;

public class PedidoTrasladoSapClientMapper {

	public static PedidoTrasladoSapResponseDTO toDomain(SapItem item) {
		PedidoTrasladoSapResponseDTO domain=new PedidoTrasladoSapResponseDTO();
		domain.setNumeroPedTraslado(item.getNumeroPedTraslado());
		domain.setCantidaddespacho(item.getCantidadDespacho());
		domain.setCantidadEnTraslado(item.getCantidadEnTraslado());
		domain.setCantidadPedido(item.getCantidadPedido());
		domain.setCantidadPendienteTraslado(item.getCantidadPendienteTraslado());
		domain.setCantidadRecibidaEnPa(item.getCantidadRecibidaEnPa());
		domain.setMaterial(item.getMaterial());
		domain.setPedidoDeComprasAsociado(item.getPedidoDeComprasAsociado());
		domain.setPlantaDestino(item.getPlantaDestino());
		domain.setPosicion(item.getPosicion());
		domain.setTrasladosPendientes(item.getTrasladosPendientes());
		return domain;
	}
}
