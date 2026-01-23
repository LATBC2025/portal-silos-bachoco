package com.bachoco.persistence.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bachoco.dto.ReporteProgramArriboDTO;
import com.bachoco.mapper.rowMapper.ReporteProgramArriboRowMapper;

@Repository
public class ReporteProgramArriboRepository {

	private final JdbcTemplate jdbcTemplate;
	private final ReporteProgramArriboRowMapper reporteProgramArriboRowMapper;
	
	public ReporteProgramArriboRepository(JdbcTemplate jdbcTemplate,
			ReporteProgramArriboRowMapper reporteProgramArriboRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.reporteProgramArriboRowMapper = reporteProgramArriboRowMapper;
	}
	
	public List<ReporteProgramArriboDTO> obtenerPedidosFiltrados(
	        Integer siloId, Integer proveedorId, String fechaInicio, String fechaFin) {

	    try {
	    	String proveedor = String.valueOf(proveedorId);
	    	
	        if ("-1".equals(fechaInicio)) fechaInicio = null;
	        if ("-1".equals(fechaFin)) fechaFin = null;

	        String sql = "{call ObtenerReporteProgramArriboFiltrados(?, ?, ?, ?)}";
	        return jdbcTemplate.query(sql, reporteProgramArriboRowMapper,
	                siloId, proveedor, fechaInicio, fechaFin);

	    } catch (Exception e) {
	        e.printStackTrace(); // solo debug local
	        throw e; // 🔴 CLAVE: re-lanza la excepción
	    }
	}

}
