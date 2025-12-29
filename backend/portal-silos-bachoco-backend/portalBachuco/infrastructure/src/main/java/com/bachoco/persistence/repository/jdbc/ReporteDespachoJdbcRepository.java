package com.bachoco.persistence.repository.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bachoco.mapper.rowMapper.ReporteDespachoRowMapper;
import com.bachoco.model.ReporteDespacho;

@Repository
public class ReporteDespachoJdbcRepository {

	private final JdbcTemplate jdbcTemplate;
	private final ReporteDespachoRowMapper reporteDespachoRowMapper;
	
	public ReporteDespachoJdbcRepository(JdbcTemplate jdbcTemplate, ReporteDespachoRowMapper reporteDespachoRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.reporteDespachoRowMapper = reporteDespachoRowMapper;
	}
	
	
	public List<ReporteDespacho> obtenerPedidosFiltrados(Integer siloId, String fechaInicio,String fechaFin) {
		String sql = "{call ObtenerReporteDespachosFiltrados(?, ?, ?)}";
		List<ReporteDespacho> response= new ArrayList<>();
		if(fechaInicio.equals("-1")) {
			fechaInicio=null;
		}
		if(fechaFin.equals("-1")) {
			fechaFin=null;
		}
		try {
			return jdbcTemplate.query(sql, reporteDespachoRowMapper, siloId, fechaInicio, fechaFin);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public List<ReporteDespacho> obtenerPedidosFiltradosJdbc(Integer siloId, String fechaInicio, String fechaFin) {
	    String sql = """
	        SELECT
	            p.NOMBRE AS BODEGA_DESCRIPCION,
	             d.NUMERO_MOV_SAP AS FOLIO,
	            d.FECHA_EMBARQUE,
	            d.NUMERO_BOLETA,
	            d.PESO_BRUTO,
	            d.PESO_TARA,
	            (d.PESO_BRUTO - d.PESO_TARA) as PESO_NETO,
	            d.HUMEDAD,
	            d.CHOFER,
	            '' as TRACTOR,
	            d.PLACA_JAULA,
	            d.LINEA_TRANSPORTISTA
	        FROM tc_confirmacion_despacho d
	        JOIN tc_pedido_traslado pt ON d.TC_PEDIDO_TRASLADO_ID=pt.PEDIDO_TRASLADO_ID
			JOIN tc_silo s on pt.TC_SILO_ID=s.SILO_ID
            JOIN tc_planta p on d.TC_PLANTA_ID=p.PLANTA_ID
	        WHERE s.SILO_ID = ?
	            AND (? IS NULL OR d.FECHA_EMBARQUE >= STR_TO_DATE(?, '%Y-%m-%d'))
	            AND (? IS NULL OR d.FECHA_EMBARQUE <= STR_TO_DATE(?, '%Y-%m-%d'))
	        """;
	    // Normalizar fechas
	    String fechaInicioNormalizada = (fechaInicio == null || fechaInicio.equals("-1")) ? null : fechaInicio;
	    String fechaFinNormalizada = (fechaFin == null || fechaFin.equals("-1")) ? null : fechaFin;
	    try {
	        return jdbcTemplate.query(sql, reporteDespachoRowMapper, 
	        		siloId, 
	            fechaInicioNormalizada, fechaInicioNormalizada,
	            fechaFinNormalizada, fechaFinNormalizada
	        );
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	
	
}
