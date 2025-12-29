package com.bachoco.mapper.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.bachoco.dto.ReporteProgramArriboDTO;

@Component
public class ReporteProgramArriboRowMapper implements RowMapper<ReporteProgramArriboDTO> {

	@Override
	public ReporteProgramArriboDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReporteProgramArriboDTO reporte=new ReporteProgramArriboDTO();
		
		LocalDateTime dateTime = rs.getTimestamp("FECHA_PROGRAMADA").toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		reporte.setId(rs.getInt("ARRIBO_ID"));
		reporte.setToneladas(rs.getFloat("CANTIDAD"));
		reporte.setMaterial(rs.getString("MATERIAL_DESCRIPCION"));
		reporte.setFecha(dateTime.format(formatter));
		reporte.setNumeroPedido(rs.getString("NUMERO_PED_TRASLADO"));
		reporte.setDestinoPlanta(rs.getString("NOMBRE"));
		return reporte;
	}

}
