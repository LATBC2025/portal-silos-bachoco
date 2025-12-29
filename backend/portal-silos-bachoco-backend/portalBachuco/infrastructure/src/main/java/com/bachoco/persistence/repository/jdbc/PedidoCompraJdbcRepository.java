package com.bachoco.persistence.repository.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.bachoco.mapper.rowMapper.PedidoCompraRowMapper;
import com.bachoco.model.PedidoCompraSapDTO;
import com.bachoco.model.PedidoSapResponseDTO;
import com.bachoco.model.procedores.PedidoCompraDTO;
import com.bachoco.utils.UtileriaComparators;

@Repository
public class PedidoCompraJdbcRepository {

	private final JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private SimpleJdbcCall simpleJdbcCall;
	private final DataSource dataSource;
	private final PedidoCompraRowMapper pedidoCompraRowMapper;

	public PedidoCompraJdbcRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
			DataSource dataSource, PedidoCompraRowMapper pedidoCompraRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.dataSource = dataSource;
		this.pedidoCompraRowMapper = pedidoCompraRowMapper;
	}

	public List<PedidoCompraDTO> obtenerPedidosPorSilo(String claveSilo) {
		String sql = "{call ObtenerPedidosCompraPorSilo(?)}";
		return jdbcTemplate.query(sql, pedidoCompraRowMapper, claveSilo);
	}

	public List<PedidoCompraDTO> obtenerPedidosFiltrados(Integer siloId, Integer materialId, String fechaInicio,
			String fechaFin) {
		String sql = "{call ObtenerPedidosCompraFiltrados(?, ?, ?, ?)}";
		if (fechaInicio.equals("-1")) {
			fechaInicio = null;
		}
		if (fechaFin.equals("-1")) {
			fechaFin = null;
		}
		return jdbcTemplate.query(sql, pedidoCompraRowMapper, siloId, materialId, fechaInicio, fechaFin);
	}

	public List<PedidoCompraDTO> findAllByFolioNumCompra(List<String> folios) {
		String sql = """
				 SELECT
				pc.PEDIDO_COMPRA_ID,
				      pc.NUMERO_PEDIDO,
				      pc.POSICION,
				      pc.CANTIDAD_PEDIDA,
				      dtpc.CANTIDAD_ENTREGADA,
				      dtpc.CANTIDAD_DESPACHADA,
				      dtpc.CANTIDAD_PENDIENTE_DESPACHO,
				      pc.CONTRATO_LEGAL,
				      pc.URL_CER_CONSERVACION,
				      pc.CER_DEPOSITO,
				      pc.TIPO_EXTENCION
				  FROM
				      tc_pedido_compra pc
				  JOIN
				      tc_detalle_pedido_compra dtpc ON dtpc.TC_PEDIDO_COMPRA_ID = pc.PEDIDO_COMPRA_ID
				  WHERE
				      pc.FOLIO_NUM_PED_POSICION in (:folios)
				   """;

		Map<String, Object> params = new HashMap<>();
		params.put("folios", folios);
		return namedParameterJdbcTemplate.query(sql, params, pedidoCompraRowMapper);
	}

	public List<PedidoCompraDTO> findAllPedidoCompra() {
		String sql = """
				SELECT
				    pc.PEDIDO_COMPRA_ID,
				    pc.NUMERO_PEDIDO,
				    pc.POSICION,
				    pc.CANTIDAD_PEDIDA,
				    dtpc.CANTIDAD_ENTREGADA,
				    dtpc.CANTIDAD_DESPACHADA,
				    dtpc.CANTIDAD_PENDIENTE_DESPACHO,
				    pc.CONTRATO_LEGAL,
				    pc.URL_CER_CONSERVACION,
				    pc.CER_DEPOSITO,
				    pc.TIPO_EXTENCION
				FROM
				    tc_pedido_compra pc
				JOIN
				    tc_detalle_pedido_compra dtpc ON dtpc.TC_PEDIDO_COMPRA_ID = pc.PEDIDO_COMPRA_ID
				""";

		return jdbcTemplate.query(sql, pedidoCompraRowMapper);
	}

	public List<String> findAllFoliosExist(List<String> folios) {
		String sql = """
				SELECT
					pc.FOLIO_NUM_PED_POSICION,
				       dtpc.CANTIDAD_DESPACHADA,
				       dtpc.CANTIDAD_PENDIENTE_DESPACHO
				   FROM
				       tc_pedido_compra pc
				   JOIN
				       tc_detalle_pedido_compra dtpc ON dtpc.TC_PEDIDO_COMPRA_ID = pc.PEDIDO_COMPRA_ID
				                where
				                pc.FOLIO_NUM_PED_POSICION in (:folios)
				    """;

		Map<String, Object> params = new HashMap<>();
		params.put("folios", folios);
		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getString("FOLIO_NUM_PED_POSICION"));
	}

	public List<Map<String, Object>> findAllFoliosExistCantidades(List<String> folios) {
		String sql = """
				SELECT
				    pc.FOLIO_NUM_PED_POSICION,
				    pc.CANTIDAD_PEDIDA,
				    dtpc.CANTIDAD_DESPACHADA,
				    dtpc.CANTIDAD_PENDIENTE_DESPACHO,
				    dtpc.CANTIDAD_ENTREGADA,
				    pc.CONTRATO_LEGAL
				FROM
				    tc_pedido_compra pc
				JOIN
				    tc_detalle_pedido_compra dtpc ON dtpc.TC_PEDIDO_COMPRA_ID = pc.PEDIDO_COMPRA_ID
				WHERE
				    pc.FOLIO_NUM_PED_POSICION in (:folios)
				""";

		Map<String, Object> params = new HashMap<>();
		params.put("folios", folios);

		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Map<String, Object> row = new HashMap<>();
			row.put("folio", rs.getString("FOLIO_NUM_PED_POSICION"));
			row.put("cantidadPedida", rs.getBigDecimal("CANTIDAD_PEDIDA"));
			row.put("cantidadDespachada", rs.getBigDecimal("CANTIDAD_DESPACHADA"));
			row.put("cantidadPendienteDespacho", rs.getBigDecimal("CANTIDAD_PENDIENTE_DESPACHO"));
			row.put("cantidadEntregada", rs.getBigDecimal("CANTIDAD_ENTREGADA"));
			row.put("contratoLegal", rs.getString("CONTRATO_LEGAL"));
			return row;
		});
	}

	public List<String> findAllFoliosExistWithped(List<String> folios) {
		String sql = """
				SELECT pc.FOLIO_NUM_PED_POSICION, dtpc.CANTIDAD_DESPACHADA,
				       FROM tc_pedido_compra pc
				       WHERE pc.FOLIO_NUM_PED_POSICION IN (:folios)
				 """;

		Map<String, Object> params = new HashMap<>();
		params.put("folios", folios);
		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getString("FOLIO_NUM_PED_POSICION"));
	}

	public List<String> findAllNumeroCompraByFilterProgram(String claveSilo, Integer materialId) {
		String sql = """
				select CONCAT(p.NUMERO_PEDIDO,'-',p.POSICION) AS FOLIO_PED_COMPRA
				from tc_pedido_compra p
				            inner join tc_detalle_pedido_compra pc on p.PEDIDO_COMPRA_ID= pc.TC_PEDIDO_COMPRA_ID
				            inner join tc_silo s on p.TC_SILO_ID= s.SILO_ID
				            where s.SILO_NOMBRE=?
				    """;
		return jdbcTemplate.query(sql, new Object[] { claveSilo }, (rs, rowNum) -> rs.getString("FOLIO_PED_COMPRA"));
	}

	public void savePedidoCompra(List<PedidoSapResponseDTO> pedido, String silo) {
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("sp_insert_compra_venta");
		for (PedidoSapResponseDTO p : pedido) {
			Map<String, Object> params = new HashMap<>();
			params.put("p_numero_ped_compra", p.getPedCompra());
			params.put("p_cantidad_pedida", p.getCantidadPedido() != null ? p.getCantidadPedido() : 0);
			params.put("p_cantidad_entregada", p.getCantidadEntrega() != null ? p.getCantidadEntrega() : 0);
			params.put("p_cantidad_despachada", p.getCantidadDespacho() != null ? p.getCantidadDespacho() : 0);
			params.put("p_cantidad_pend_despacho", p.getCantidadPendienteDespacho());
			params.put("p_clave_material", p.getMaterial());
			params.put("p_posicion", p.getPosicion());
			params.put("p_contrato_legal", p.getContratoLegal());
			params.put("p_cert_deposito", "");
			params.put("p_silo", "CP12");
			params.put("p_material", "");
			params.put("p_planta_recep", p.getPlantaReceptor());
			try {
				simpleJdbcCall.execute(params);
				this.asignacionSiloAndMaterial(silo, p.getMaterial(), p.getPedCompra(), p.getPosicion());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void asignacionSiloAndMaterial(String silo, String material, String pedCompra, String posicion) {
		String folio = pedCompra.concat("-").concat(posicion);
		this.procesarCompraVentaOptimizado(folio, silo, material);
	}

	public int procesarCompraVentaOptimizado(String folio, String silo, String material) {
		// Consulta única para obtener ambos IDs
		String queryIds = """
				SELECT mat.MATERIAL_ID, s.SILO_ID
				FROM tc_material mat
				CROSS JOIN tc_silo s
				WHERE mat.NUMERO_MATERIAL = ?
				AND s.SILO_CLAVE = ?
				LIMIT 1
				""";

		Map<String, Integer> ids = jdbcTemplate.query(queryIds, rs -> {
			Map<String, Integer> result = new HashMap<>();
			if (rs.next()) {
				result.put("materialId", rs.getInt("MATERIAL_ID"));
				result.put("siloId", rs.getInt("SILO_ID"));
			}
			return result;
		}, material, silo);

		if (ids == null || ids.isEmpty()) {
			throw new RuntimeException("Material o Silo no encontrado");
		}

		Integer materialId = ids.get("materialId");
		Integer siloId = ids.get("siloId");
		return this.actualizarPedidoCompra(folio, siloId, materialId);
	}

	public int actualizarPedidoCompra(String folio, Integer idSilo, Integer idMaterial) {
		String sql = """
				UPDATE tc_pedido_compra
				SET TC_SILO_ID = ?,
				    TC_MATERIAL_ID = ?
				WHERE FOLIO_NUM_PED_POSICION = ?
				""";
		return jdbcTemplate.update(sql, idSilo, idMaterial, folio);
	}

	public void UpdateCaPedidoCompra(PedidoSapResponseDTO p) {
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("sp_insert_compra_venta");
		Map<String, Object> params = new HashMap<>();
		params.put("p_numero_ped_compra", p.getPedCompra());
		params.put("p_cantidad_pedida", p.getCantidadPedido() != null ? p.getCantidadPedido() : 0);
		params.put("p_cantidad_entregada", p.getCantidadEntrega() != null ? p.getCantidadEntrega() : 0);
		params.put("p_cantidad_despachada", p.getCantidadDespacho() != null ? p.getCantidadDespacho() : 0);
		params.put("p_cantidad_pend_despacho", p.getCantidadPendienteDespacho());
		params.put("p_clave_material", p.getMaterial());
		params.put("p_posicion", p.getPosicion());
		params.put("p_contrato_legal", p.getContratoLegal());
		params.put("p_cert_deposito", "");
		params.put("p_silo", "CP12");
		params.put("p_material", "");
		params.put("p_planta_recep", p.getPlantaReceptor());
		try {
			simpleJdbcCall.execute(params);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int actualizarPedidoCompraCompleto(String folio, Float cantidadPedida, Float cantidadEntregada,
			Float cantidadDespachada, Float cantidadPendienteDespacho) {

		String sql = """
				UPDATE tc_pedido_compra pc
				JOIN tc_detalle_pedido_compra dtpc ON dtpc.TC_PEDIDO_COMPRA_ID = pc.PEDIDO_COMPRA_ID
				SET pc.CANTIDAD_PEDIDA = :cantidadPedida,
				dtpc.CANTIDAD_ENTREGADA = :cantidadEntregada,
				dtpc.CANTIDAD_DESPACHADA = :cantidadDespachada,
				dtpc.CANTIDAD_PENDIENTE_DESPACHO = :cantidadPendienteDespacho
				WHERE pc.FOLIO_NUM_PED_POSICION = :folio
				""";
		Map<String, Object> params = new HashMap<>();
		params.put("folio", folio);
		params.put("cantidadPedida", cantidadPedida);
		params.put("cantidadEntregada", cantidadEntregada);
		params.put("cantidadDespachada", cantidadDespachada);
		params.put("cantidadPendienteDespacho", cantidadPendienteDespacho);
		try {
			return namedParameterJdbcTemplate.update(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int actualizarCantidadEntregada(String folio, Float cantidadPedida, Float cantidadEntregada,
			Float cantidadDespachada, Float cantidadPendienteDespacho) {

		String sql = """
				UPDATE tc_pedido_compra pc
				JOIN tc_detalle_pedido_compra dtpc ON dtpc.TC_PEDIDO_COMPRA_ID = pc.PEDIDO_COMPRA_ID
				SET pc.CANTIDAD_PEDIDA = :cantidadPedida,
				dtpc.CANTIDAD_ENTREGADA = :cantidadEntregada,
				dtpc.CANTIDAD_DESPACHADA = :cantidadDespachada,
				dtpc.CANTIDAD_PENDIENTE_DESPACHO = :cantidadPendienteDespacho
				WHERE pc.FOLIO_NUM_PED_POSICION = :folio
				""";
		Map<String, Object> params = new HashMap<>();
		params.put("folio", folio);
		params.put("cantidadPedida", cantidadPedida);
		params.put("cantidadEntregada", cantidadEntregada);
		params.put("cantidadDespachada", cantidadDespachada);
		params.put("cantidadPendienteDespacho", cantidadPendienteDespacho);
		try {
			return namedParameterJdbcTemplate.update(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private void ejecutarActualizacionPorLotes(List<MapSqlParameterSource> batchParams) {
		String sql = """
				UPDATE tc_pedido_compra pc
				JOIN tc_detalle_pedido_compra dtpc ON dtpc.TC_PEDIDO_COMPRA_ID = pc.PEDIDO_COMPRA_ID
				JOIN (
				    SELECT PEDIDO_COMPRA_ID 
				    FROM tc_pedido_compra 
				    WHERE FOLIO_NUM_PED_POSICION = :folio 
				    ORDER BY PEDIDO_COMPRA_ID 
				    LIMIT 1
				) AS filtered_pc ON pc.PEDIDO_COMPRA_ID = filtered_pc.PEDIDO_COMPRA_ID
				CROSS JOIN (SELECT m2.MATERIAL_ID FROM tc_material m2 WHERE m2.NUMERO_MATERIAL=:material or m2.MATERIAL_DESCRIPCION=:material LIMIT 1) AS m
				CROSS JOIN (SELECT s2.SILO_ID FROM tc_silo s2 WHERE s2.SILO_CLAVE = :silo OR s2.SILO_NOMBRE = :silo LIMIT 1) AS s
				SET pc.CANTIDAD_PEDIDA = :cantidadPedida,
				    dtpc.CANTIDAD_ENTREGADA = :cantidadEntregada,
				    dtpc.CANTIDAD_DESPACHADA = :cantidadDespachada,
				    dtpc.CANTIDAD_PENDIENTE_DESPACHO = :cantidadPendienteDespacho,
				    pc.CONTRATO_LEGAL = :contratoLegal,
				    pc.TC_MATERIAL_ID = m.MATERIAL_ID,
				    pc.TC_SILO_ID = s.SILO_ID
				WHERE 
				    NOT (COALESCE(pc.CANTIDAD_PEDIDA, 0) <=> COALESCE(:cantidadPedida, 0))
				    OR NOT (COALESCE(dtpc.CANTIDAD_ENTREGADA, 0) <=> COALESCE(:cantidadEntregada, 0))
				    OR NOT (COALESCE(dtpc.CANTIDAD_DESPACHADA, 0) <=> COALESCE(:cantidadDespachada, 0))
				    OR NOT (COALESCE(dtpc.CANTIDAD_PENDIENTE_DESPACHO, 0) <=> COALESCE(:cantidadPendienteDespacho, 0))
				    OR NOT (COALESCE(pc.CONTRATO_LEGAL, '') <=> COALESCE(:contratoLegal, ''))
				    OR NOT (COALESCE(pc.TC_MATERIAL_ID, 0) <=> COALESCE(m.MATERIAL_ID, 0))
				    OR NOT (COALESCE(pc.TC_SILO_ID, 0) <=> COALESCE(s.SILO_ID, 0))
				   """;

		namedParameterJdbcTemplate.batchUpdate(sql, batchParams.toArray(new MapSqlParameterSource[0]));
	}

	public void updateCantidades(List<PedidoSapResponseDTO> updateCompras, Map<String, Map<String, Object>> folioMap,
			String claveSilo, String claveMaterial) {
		List<MapSqlParameterSource> batchParams = new ArrayList();
		for (PedidoSapResponseDTO ped : updateCompras) {
			if (ped.getPedCompra() != null && ped.getPosicion() != null) {
				String folio = UtileriaComparators.buildFolioPedCompraAnPosicion(ped.getPedCompra(), ped.getPosicion());
				Map<String, Object> datos = folioMap.get(folio);
				boolean isCambio = tieneCambios(datos, ped);
				if (datos != null && !isCambio) {
					MapSqlParameterSource params = new MapSqlParameterSource();
					params.addValue("cantidadPedida", UtileriaComparators.parseFloatSafe(ped.getCantidadPedido()));
					params.addValue("cantidadEntregada", UtileriaComparators.parseFloatSafe(ped.getCantidadEntrega()));
					params.addValue("cantidadDespachada",
							UtileriaComparators.parseFloatSafe(ped.getCantidadDespacho()));
					params.addValue("cantidadPendienteDespacho",
							UtileriaComparators.parseFloatSafe(ped.getCantidadPendienteDespacho()));
					params.addValue("contratoLegal", ped.getContratoLegal());
					params.addValue("folio", folio);
					params.addValue("silo", claveSilo);
					params.addValue("material", claveMaterial);

					batchParams.add(params);
				}
			}
		}
		if (!batchParams.isEmpty()) {
			ejecutarActualizacionPorLotes(batchParams);
		}
	}

	public void updatePedidoCompra(List<PedidoSapResponseDTO> updateCompras, String claveSilo, String claveMaterial) {
		List<MapSqlParameterSource> batchParams = new ArrayList();
		for (PedidoSapResponseDTO ped : updateCompras) {
			if (ped.getPedCompra() != null && ped.getPosicion() != null) {
				String folio = UtileriaComparators.buildFolioPedCompraAnPosicion(ped.getPedCompra(), ped.getPosicion());
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue("cantidadPedida", UtileriaComparators.parseFloatSafe(ped.getCantidadPedido()));
				params.addValue("cantidadEntregada", UtileriaComparators.parseFloatSafe(ped.getCantidadEntrega()));
				params.addValue("cantidadDespachada", UtileriaComparators.parseFloatSafe(ped.getCantidadDespacho()));
				params.addValue("cantidadPendienteDespacho",
						UtileriaComparators.parseFloatSafe(ped.getCantidadPendienteDespacho()));
				params.addValue("contratoLegal", ped.getContratoLegal());
				params.addValue("folio", folio);
				params.addValue("silo", claveSilo);
				params.addValue("material", claveMaterial);
				batchParams.add(params);
			}
		}
		if (!batchParams.isEmpty()) {
			ejecutarActualizacionPorLotes(batchParams);
		}
	}

	private boolean tieneCambios(Map<String, Object> datosBD, PedidoSapResponseDTO pedSAP) {
		try {
			// Comparar todos los campos relevantes
			Float despachadaBD = 0.0F;
			Float pendienteDespachoBD = 0.0F;
			Float despachadaSAP = 0.0F;
			Float pendienteDespachoSAP = 0.0F;
			Float pedidaBD = 0.0F;
			Float pedidaSAP = 0.0F;
			Float entregadaBD = 0.0F;
			Float entregadaSAP = 0.0F;
			String contradoLegalSap = "";
			String contradoLegalBD = "";
			if (datosBD.get("cantidadDespachada") != null) {
				despachadaBD = Float.parseFloat(datosBD.get("cantidadDespachada").toString());
			}
			if (datosBD.get("cantidadPendienteDespacho") != null) {
				pendienteDespachoBD = Float.parseFloat(datosBD.get("cantidadPendienteDespacho").toString());
			}
			if (pedSAP.getCantidadDespacho() != null) {
				despachadaSAP = Float.parseFloat(pedSAP.getCantidadDespacho());
			}
			if (pedSAP.getCantidadPendienteDespacho() != null) {
				pendienteDespachoSAP = Float.parseFloat(pedSAP.getCantidadPendienteDespacho());
			}
			if (datosBD.containsKey("cantidadPedida")) {
				pedidaBD = Float.parseFloat(datosBD.get("cantidadPedida").toString());
			}
			if (pedSAP.getCantidadPedido() != null) {
				pedidaSAP = Float.parseFloat(pedSAP.getCantidadPedido().toString());
			}
			if (datosBD.containsKey("cantidadEntregada")) {
				entregadaBD = Float.parseFloat(datosBD.get("cantidadEntregada").toString());
			}
			if (pedSAP.getCantidadEntrega() != null) {
				entregadaSAP = Float.parseFloat(pedSAP.getCantidadEntrega().toString());
			}
			if (pedSAP.getContratoLegal() != null) {
				contradoLegalSap = pedSAP.getContratoLegal().trim();
			}
			if (datosBD.containsKey("contratoLegal")) {
				contradoLegalBD = String.valueOf(datosBD.containsKey("contratoLegal")).trim();
			}
			// Comparar si hay diferencias en alguno de los campos
			return despachadaBD.equals(despachadaSAP) && pendienteDespachoBD.equals(pendienteDespachoSAP)
					&& pedidaBD.equals(pedidaSAP) && entregadaBD.equals(entregadaSAP)
					&& contradoLegalBD.equalsIgnoreCase(contradoLegalSap);

		} catch (Exception e) {
			// En caso de error en conversión, considerar que sí hay cambios
			System.err.println("Error comparando folio: " + e.getMessage());
			return true;
		}
	}

	private boolean tieneCambiosAdicionales(Map<String, Object> datosBD, PedidoSapResponseDTO pedSAP) {
		try {
			// Comparar campos adicionales si existen en el Map
			if (datosBD.containsKey("cantidadPedida")) {
				Float pedidaBD = Float.parseFloat(datosBD.get("cantidadPedida").toString());
				Float pedidaSAP = Float.parseFloat(pedSAP.getCantidadPedido().toString());
				if (!pedidaBD.equals(pedidaSAP))
					return true;
			}
			if (datosBD.containsKey("cantidadEntregada")) {
				Float entregadaBD = Float.parseFloat(datosBD.get("cantidadEntregada").toString());
				Float entregadaSAP = Float.parseFloat(pedSAP.getCantidadEntrega().toString());
				if (!entregadaBD.equals(entregadaSAP))
					return true;
			}
			return false;
		} catch (Exception e) {
			return true;
		}
	}

}
