package com.bachoco.mapper.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.bachoco.model.ReporteDespacho;

@Component
public class ReporteDespachoRowMapper implements RowMapper<ReporteDespacho> {

	@Override
	public ReporteDespacho mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReporteDespacho despacho= new ReporteDespacho();
		//despacho.setId(rs.getInt("ARRIBO_ID"));
		LocalDateTime dateTime = rs.getTimestamp("FECHA_EMBARQUE").toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		despacho.setBodega(rs.getString("BODEGA_DESCRIPCION"));
		despacho.setFolio(rs.getString("FOLIO"));
		despacho.setFechaEmbarque(dateTime.format(formatter));
		despacho.setNumeroBoleta(rs.getString("NUMERO_BOLETA"));
		despacho.setPesoBruto(rs.getFloat("PESO_BRUTO"));
		despacho.setPesoTara(rs.getFloat("PESO_TARA"));
		despacho.setPesoNeto(rs.getFloat("PESO_NETO"));
		despacho.setHumedad(rs.getString("HUMEDAD"));
		despacho.setChofer(rs.getString("CHOFER"));
		despacho.setTractor(rs.getString("TRACTOR"));
		despacho.setJaula(rs.getString("PLACA_JAULA"));
		despacho.setLineaTransportista(rs.getString("LINEA_TRANSPORTISTA"));
		return despacho;
	}

}
