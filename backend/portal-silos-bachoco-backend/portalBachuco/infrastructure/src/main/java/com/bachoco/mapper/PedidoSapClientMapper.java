package com.bachoco.mapper;

import com.bachoco.dto.PedidoSapResponseClientDTO;
import com.bachoco.model.PedidoSapResponseDTO;

public class PedidoSapClientMapper {

	public static PedidoSapResponseDTO toDomain(PedidoSapResponseClientDTO s) {
		PedidoSapResponseDTO pedido= new PedidoSapResponseDTO();
		pedido.setCantidadDespacho(s.getCantidadDespacho());
		pedido.setCantidadEntrega(s.getCantidadEntrega());
		pedido.setCantidadPedido(s.getCantidadPedido());
		pedido.setContratoLegal(s.getContratoLegal());
		pedido.setMaterial(s.getMaterial());
		pedido.setPedCompra(s.getPedCompra());
		pedido.setCantidadPendienteSurtir(s.getCantidadPendienteSurtir());
		pedido.setClaseMov(s.getClaseMov());
		pedido.setClasePed(s.getClasePed());
		pedido.setPedidoRelacionado(s.getPedidoRelacionado());
		pedido.setTipoPedido(s.getTipoPedido());
		pedido.setPosicion(s.getPosicion());
		pedido.setPlantaReceptor(s.getPlantaReceptor());
		return pedido;
	}
}
