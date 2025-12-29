package com.bachoco.mapper.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.bachoco.model.ConfirmDespachoResponse;

@Component 
public class ConfirmDespachoRowMapper implements RowMapper<ConfirmDespachoResponse> {
	
        @Override
        public ConfirmDespachoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ConfirmDespachoResponse(
                rs.getString("claveBodega"),
                rs.getString("claveSilo"),
                rs.getString("claveMaterial"),
                rs.getString("fechaEmbarque"),
                rs.getString("numBoleta"),
                rs.getDouble("pesoBruto"),
                rs.getDouble("pesoTara"),
                rs.getString("humedad"),
                rs.getString("chofer"),
                rs.getString("placaJaula"),
                rs.getString("lineaTransportista"),
                rs.getString("claveDestino"),
                rs.getString("numPedidoTraslado"),
                rs.getString("tipoMovimiento"),
                rs.getString("idconfDespacho"),
                rs.getString("idPedTraslado"),
                rs.getString("numeroSap"),
                rs.getString("FOLIO")
            );
        }
}