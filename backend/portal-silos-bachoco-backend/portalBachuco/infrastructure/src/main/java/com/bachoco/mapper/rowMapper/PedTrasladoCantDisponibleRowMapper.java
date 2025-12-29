package com.bachoco.mapper.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.bachoco.model.procedores.PedidoTrasladoArriboDTO;

@Component
public class PedTrasladoCantDisponibleRowMapper implements RowMapper<PedidoTrasladoArriboDTO>{

	@Override
	public PedidoTrasladoArriboDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		PedidoTrasladoArriboDTO ped= new PedidoTrasladoArriboDTO();
		ped.setPedidoTrasladoId(rs.getInt("PEDIDO_TRASLADO_ID"));
		ped.setNumeroPedPosicion(rs.getString("FOLIO_NUM_PED_POSICION"));
		ped.setNumeroPedido(rs.getString("num_pedido"));
		ped.setCantidad(rs.getFloat("cantidad_pedido"));
		return ped;
	}

}
