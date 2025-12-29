package com.bachoco.mapper.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.bachoco.model.procedores.PedidoTrasladoDTO;

@Component
public class PedidoTrasladoRowMapper implements RowMapper<PedidoTrasladoDTO> {

	@Override
	public PedidoTrasladoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		PedidoTrasladoDTO pedido=new PedidoTrasladoDTO();
		pedido.setPedidoTrasladoId(rs.getInt("PEDIDO_TRASLADO_ID"));
		pedido.setFolioNumPosicion(rs.getString("FOLIO_NUM_PED_POSICION"));
		pedido.setNombrePlantaDestino(rs.getString("PLANTA_DESTINO"));
		pedido.setNumPedidoTraslado(rs.getString("NUMERO_PED_TRASLADO"));
		pedido.setCantidadPedido(rs.getFloat("CANTIDAD_PEDIDO"));
		pedido.setCantidadTraslado(rs.getFloat("CANTIDAD_TRASLADO"));
		pedido.setCantidadRecibidaPa(rs.getFloat("CANTIDAD_RECIBIDA_PA"));
		pedido.setCantidadPendienteTraslado(rs.getFloat("PENDIENTE_TRASLADO"));
		pedido.setNumCompraAsociado(rs.getString("NUM_COMPRA_ASOCIADO"));
		pedido.setTrasladosPendFact(rs.getInt("TRASLADO_PENDIENTE_FACTURA"));
		pedido.setPosicion(rs.getString("POSICION"));
		return pedido;
	}

}
