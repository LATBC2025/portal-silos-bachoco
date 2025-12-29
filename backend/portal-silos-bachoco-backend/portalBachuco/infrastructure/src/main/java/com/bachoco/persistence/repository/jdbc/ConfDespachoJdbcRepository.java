package com.bachoco.persistence.repository.jdbc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.bachoco.mapper.rowMapper.ConfirmDespachoRowMapper;
import com.bachoco.model.ConfirmDespachoResponse;
import com.bachoco.model.ConfirmacionDespachoRequest;
import com.bachoco.model.ReportConfDespacho;

@Repository
public class ConfDespachoJdbcRepository {

	private static final Logger logger = LoggerFactory.getLogger(ConfDespachoJdbcRepository.class);
	
	private final JdbcTemplate jdbcTemplate;
	private SimpleJdbcCall simpleJdbcCall;
	private final DataSource dataSource;
	private final ConfirmDespachoRowMapper confirmDespachoRowMapper;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//creacion de valores final en nombres de procedimientos
	private final String namedProcedoreInsertConfDespacho="sp_inserta_conf_despacho";
	private final String namedProcedoreModifConfDespacho="sp_modifica_conf_despacho";
	private final String EMPTY="";

	public ConfDespachoJdbcRepository(JdbcTemplate jdbcTemplate, DataSource dataSource,
			ConfirmDespachoRowMapper confirmDespachoRowMapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.dataSource = dataSource;
		this.confirmDespachoRowMapper = confirmDespachoRowMapper;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public ReportConfDespacho findConfDespachoById(Integer idConfDespacho) {
		String sql = """
				SELECT
					cd.NUMERO_MOV_SAP,
				    cd.FOLIO,
				    pt.NUMERO_PED_TRASLADO,
				    cd.FECHA_EMBARQUE,
				    p.NOMBRE,
				    s.SILO_NOMBRE,
				    m.MATERIAL_DESCRIPCION,
				    cd.NUMERO_BOLETA,
				    cd.PESO_BRUTO,
				    cd.PESO_TARA,
				    cd.HUMEDAD,
				    cd.CHOFER,
				    cd.PLACA_JAULA,
				    cd.LINEA_TRANSPORTISTA
				FROM tc_confirmacion_despacho cd
				JOIN tc_pedido_traslado pt ON cd.TC_PEDIDO_TRASLADO_ID=pt.PEDIDO_TRASLADO_ID
				JOIN tc_planta p ON cd.TC_PLANTA_ID= p.PLANTA_ID
				JOIN tc_material m ON cd.TC_MATERIAL_ID=m.MATERIAL_ID
				            JOIN tc_pedido_compra pc ON pt.TC_PEDIDO_COMPRA_ID=pc.PEDIDO_COMPRA_ID
				            JOIN tc_silo s ON pc.TC_SILO_ID=SILO_ID
				where cd.CONFIRMACION_DESPACHO_ID=?
				     """;
		return (ReportConfDespacho) jdbcTemplate.queryForObject(sql, new Object[] { idConfDespacho },
				(rs, rowNum) -> new ReportConfDespacho(rs.getString("NUMERO_MOV_SAP"), rs.getString("FOLIO"),
						rs.getString("NUMERO_PED_TRASLADO"), rs.getString("FECHA_EMBARQUE"), rs.getString("NOMBRE"),
						rs.getString("SILO_NOMBRE"), rs.getString("MATERIAL_DESCRIPCION"),
						rs.getString("NUMERO_BOLETA"), rs.getString("PESO_BRUTO"), rs.getString("PESO_TARA"),
						rs.getString("HUMEDAD"), rs.getString("CHOFER"), rs.getString("PLACA_JAULA"),
						rs.getString("LINEA_TRANSPORTISTA")));
	}

	public Boolean esIgualPesoBrutoYPesotaraPorId(Float pesoBruto, Float pesoTara, Integer idConfDespacho) {
		String sql = """
				    SELECT 1 FROM tc_confirmacion_despacho cd
				    WHERE cd.CONFIRMACION_DESPACHO_ID = ?
				    AND cd.PESO_BRUTO = ?
				    AND cd.PESO_TARA = ?
				""";
		try {
			Integer resultado = jdbcTemplate.queryForObject(sql, Integer.class,
					idConfDespacho, pesoBruto, pesoTara);
			return resultado != null && resultado.equals(1);
		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public Float finPromeiodPesoNetoInConfDespacho(Integer siloId) {
		String sql = """
				      	SELECT AVG(cd.PESO_NETO) AS promedio_general
						FROM tc_confirmacion_despacho cd
						JOIN tc_pedido_traslado pt ON cd.TC_PEDIDO_TRASLADO_ID=pt.PEDIDO_TRASLADO_ID
						WHERE pt.TC_SILO_ID=?
				""";
		try {
			Float resultado = jdbcTemplate.queryForObject(sql, Float.class, siloId);
			return resultado != null ? resultado : null;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public Float findPesoNetoByIdPedtraslado(Integer idConfDespacho) {
		String sql = """
				        SELECT cd.PESO_NETO FROM tc_confirmacion_despacho cd
				    WHERE cd.CONFIRMACION_DESPACHO_ID =?
				""";
		try {
			Float resultado = jdbcTemplate.queryForObject(sql, Float.class, idConfDespacho);
			return resultado != null ? resultado : null;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public Float findPromedioPesoNetoConfDespacho() {
		String sql = """
				      SELECT AVG(cd.PESO_NETO) AS promedio_peso_neto
						FROM tc_confirmacion_despacho cd;
				""";
		try {
			Float resultado = jdbcTemplate.queryForObject(sql, Float.class);
			return resultado != null ? resultado : null;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public Map<String, String> registroConfDespacho(ConfirmacionDespachoRequest req, String numeroSap, String estatus) {
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName(namedProcedoreInsertConfDespacho);
		Map<String, Object> params = new HashMap<>();
		Map<String, String> response = new HashMap<>();
		params.put("p_clave_bodega", req.getClaveBodega());
		params.put("p_clave_silo", req.getClaveSilo());
		params.put("p_clave_material", req.getClaveMaterial());
		params.put("p_fecha_embarque", java.sql.Date.valueOf(req.getFechaEmbarque()));
		params.put("p_numero_boleta", req.getNumBoleta());
		params.put("p_peso_bruto", req.getPesoBruto());
		params.put("p_peso_tara", req.getPesoTara());
		params.put("p_humedad", req.getHumedad());
		params.put("p_chofer", req.getChofer());
		params.put("p_placa_jaula", req.getPlacaJaula());
		params.put("p_linea_transportista", req.getLineaTransportista());
		params.put("p_clave_destino", req.getClaveDestino());
		params.put("p_num_pedido_traslado", req.getNumPedidoTraslado());
		params.put("p_tipo_movimiento", req.getTipoMovimiento());
		params.put("p_folio", numeroSap);
		params.put("p_numero_sap", numeroSap);
		params.put("p_pedido_traslado_num", req.getNumPedidoTraslado());
		params.put("p_estatus_conf_despacho", estatus);
		try {
			Map<String, Object> result = simpleJdbcCall.execute(params);
			Integer idGenerado = (Integer) result.get("p_conf_despacho_id");
			response.put("estatus", "0");
			response.put("folio", getFolio(idGenerado));
			response.put("id", idGenerado.toString());
		} catch (Exception e) {
			logger.error("Error al registrar confirmacion despacho: ",e.getMessage());
			response.put("estatus", "-1");
			response.put("id", "-1");
		}
		return response;
	}

	private String getFolio(Integer id) {
		String sql = """
				   select c.FOLIO from tc_confirmacion_despacho c where CONFIRMACION_DESPACHO_ID=?
				""";
		try {
			String resultado = jdbcTemplate.queryForObject(sql, String.class, id);
			return resultado;
		} catch (EmptyResultDataAccessException e) {
			logger.error("Error en obtener el folio en getFolio: ",e.getMessage());
		} catch (Exception e) {
			logger.error("Error en el metodo getFolio: ",e.getMessage());
		}
		return "";
	}

	public Map<String, String> updateConfDespacho(ConfirmacionDespachoRequest req, String numeroSap, String estatus) {
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName(namedProcedoreModifConfDespacho);
		Map<String, Object> params = new HashMap<>();
		Map<String, String> response = new HashMap<>();
		params.put("p_conf_despacho_id", req.getIdConfDespacho());
		params.put("p_clave_bodega", req.getClaveBodega());
		params.put("p_clave_silo", req.getClaveSilo());
		params.put("p_clave_material", req.getClaveMaterial());
		params.put("p_fecha_embarque", java.sql.Date.valueOf(req.getFechaEmbarque()));
		params.put("p_numero_boleta", req.getNumBoleta());
		params.put("p_peso_bruto", req.getPesoBruto());
		params.put("p_peso_tara", req.getPesoTara());
		params.put("p_humedad", req.getHumedad());
		params.put("p_chofer", req.getChofer());
		params.put("p_placa_jaula", req.getPlacaJaula());
		params.put("p_linea_transportista", req.getLineaTransportista());
		params.put("p_clave_destino", req.getDestinoId());
		params.put("p_num_pedido_traslado", req.getNumPedidoTraslado());
		params.put("p_tipo_movimiento", req.getTipoMovimiento());
		params.put("p_numero_sap", numeroSap);
		params.put("p_pedido_traslado_id", req.getIdPedTraslado());
		params.put("p_estatus_conf_despacho", estatus);
		try {
			Map<String, Object> result = simpleJdbcCall.execute(params);
			response.put("estatus", "0");
			response.put("id",EMPTY);
		} catch (Exception e) {
			logger.error("Error al actualizar en el metodo updateConfDespacho: ",e.getMessage());
			response.put("estatus", "-1");
			response.put("id", "-1");
		}
		return response;
	}

	public Map<String, String> updateConfDespachoSinSap(ConfirmacionDespachoRequest req, String estatus) {
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource)
				.withProcedureName("sp_modifica_conf_despacho_sin_response_sap");
		Map<String, Object> params = new HashMap<>();
		Map<String, String> response = new HashMap<>();
		params.put("p_conf_despacho_id", req.getIdConfDespacho());
		params.put("p_clave_bodega", req.getClaveBodega());
		params.put("p_clave_silo", req.getClaveSilo());
		params.put("p_clave_material", req.getClaveMaterial());
		params.put("p_fecha_embarque", java.sql.Date.valueOf(req.getFechaEmbarque()));
		params.put("p_numero_boleta", req.getNumBoleta());
		params.put("p_humedad", req.getHumedad());
		params.put("p_chofer", req.getChofer());
		params.put("p_placa_jaula", req.getPlacaJaula());
		params.put("p_linea_transportista", req.getLineaTransportista());
		params.put("p_clave_destino", req.getDestinoId());
		params.put("p_num_pedido_traslado", req.getNumPedidoTraslado());
		params.put("p_tipo_movimiento", req.getTipoMovimiento());
		params.put("p_pedido_traslado_id", req.getIdPedTraslado());
		params.put("p_estatus_conf_despacho", estatus);
		try {
			Map<String, Object> result = simpleJdbcCall.execute(params);
			response.put("estatus", "0");
			response.put("id", "");
		} catch (Exception e) {
			logger.error("Error al actualizar en el metodo updateConfDespachoSinSap: ",e.getMessage());
			response.put("estatus", "-1");
			response.put("id", "-1");
		}
		return response;
	}

	public List<ConfirmDespachoResponse> findAllConfirmacionesDespacho(String silo, String material, String fechaInicio,
			String fechaFin) {
		String sql = """
				     SELECT
				           cf.TC_BODEGA_ID AS claveBodega,
				           ped.TC_SILO_ID AS claveSilo,
				           cf.TC_MATERIAL_ID AS claveMaterial,
				           DATE_FORMAT(cf.FECHA_EMBARQUE, '%Y-%m-%d') AS fechaEmbarque,
				           cf.NUMERO_BOLETA AS numBoleta,
				           cf.PESO_BRUTO AS pesoBruto,
				           cf.PESO_TARA AS pesoTara,
				           cf.HUMEDAD AS humedad,
				           cf.CHOFER AS chofer,
				           cf.PLACA_JAULA AS placaJaula,
				           cf.LINEA_TRANSPORTISTA AS lineaTransportista,
				           cf.TC_PLANTA_ID AS claveDestino,
				           ped.NUMERO_PED_TRASLADO AS numPedidoTraslado,
				           cf.TIPO_MOVIMIENTO AS tipoMovimiento,
				           cf.CONFIRMACION_DESPACHO_ID AS idconfDespacho,
				           ped.PEDIDO_TRASLADO_ID AS idPedTraslado,
				           cf.NUMERO_MOV_SAP AS numeroSap,
				           cf.FOLIO
				       FROM tc_confirmacion_despacho cf
				       INNER JOIN tc_pedido_traslado ped ON cf.TC_PEDIDO_TRASLADO_ID = ped.PEDIDO_TRASLADO_ID
				                 WHERE
				            ped.TC_SILO_ID = :silo
				           AND cf.TC_MATERIAL_ID = :material
				           AND (:fechaInicio IS NULL OR cf.FECHA_EMBARQUE >= STR_TO_DATE(:fechaInicio, '%Y-%m-%d'))
				           AND (:fechaFin IS NULL OR cf.FECHA_EMBARQUE <= STR_TO_DATE(:fechaFin, '%Y-%m-%d'))
				       ORDER BY cf.FECHA_EMBARQUE DESC
				""";

		try {
			Map<String, Object> params = new HashMap<>();
			params.put("silo", silo);
			params.put("material", material);
			params.put("fechaInicio", fechaInicio);
			params.put("fechaFin", fechaFin);
			return namedParameterJdbcTemplate.query(sql, params, confirmDespachoRowMapper);
		} catch (EmptyResultDataAccessException e) {
			logger.error("Error de tipo EmptyResultDataAccessException en la consulta en findAllConfirmacionesDespacho: ",e.getMessage());
			return Collections.EMPTY_LIST;
		} catch (Exception e) {
			logger.error("Error en la consulta en findAllConfirmacionesDespacho: ",e.getMessage());
			return Collections.EMPTY_LIST;
		}
	}

	public int deleteById(Integer confirmacionDespachoId) {
		String sql = """
				 DELETE FROM tc_confirmacion_despacho WHERE CONFIRMACION_DESPACHO_ID = ?
				""";
		return jdbcTemplate.update(sql, confirmacionDespachoId);
	}

}
