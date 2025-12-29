package com.bachoco.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.bachoco.model.PedidoTrasladoSapResponseDTO;
import com.bachoco.utils.NumericCleaner;

@Component
public class ActualizacionTrasladosService {

	 	@Autowired
	    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	 	@Autowired
	 	private JdbcTemplate jdbcTemplate;
		private static final Logger logger = LoggerFactory.getLogger(ActualizacionTrasladosService.class);
	    
	    public void procesarActualizacionTraslados(List<PedidoTrasladoSapResponseDTO> pedidosSapNoExistBd,String claveSilo,String claveMaterial) {
	        if (pedidosSapNoExistBd == null || pedidosSapNoExistBd.isEmpty()) {
	            return;
	        }
	        actualizarTrasladosEnBaseDeDatos(pedidosSapNoExistBd,claveSilo);
	    }
	    
	    private void actualizarTrasladosEnBaseDeDatos(List<PedidoTrasladoSapResponseDTO> trasladosParaActualizar,String claveSilo) {
	        String sql2 = """
					UPDATE tc_detalle_pedido_traslado dtpt, tc_pedido_traslado pt1
					SET dtpt.PEDIDO_TRASLADO = :pedidoTraslado,
					    dtpt.CANTIDAD_PEDIDO = :cantidadPedido,
					    dtpt.CANTIDAD_TRASLADO = :cantidadTraslado,
					    dtpt.CANTIDAD_RECIBIDA = :cantidadRecibida,
					    dtpt.PENDIENTE_TRASLADO = :pendienteTraslado,
					    dtpt.TRASLADO_PENDIENTE_FACTURA = :numeroPedndFacturas,
					    pt1.PLANTA_DESTINO = :plantaDestino
					WHERE pt1.PEDIDO_TRASLADO_ID = dtpt.TC_PEDIDO_TRASLADO_ID
					  AND pt1.NUMERO_PED_TRASLADO = :numeroPedTraslado
					  AND pt1.POSICION = :posicion
					  AND pt1.TC_PEDIDO_COMPRA_ID IN (
					      SELECT pc.PEDIDO_COMPRA_ID
					      FROM tc_pedido_compra pc
					      WHERE pc.NUMERO_PEDIDO = :numeroPedidoCompra
					  )
	            """;
	        
	        String sql = """
					UPDATE tc_detalle_pedido_traslado dtpt
					JOIN tc_pedido_traslado pt1 ON pt1.PEDIDO_TRASLADO_ID = dtpt.TC_PEDIDO_TRASLADO_ID
					JOIN tc_pedido_compra pc ON pc.PEDIDO_COMPRA_ID = pt1.TC_PEDIDO_COMPRA_ID
					CROSS JOIN (
					    SELECT COALESCE(MATERIAL_ID, 0) AS MATERIAL_ID 
					    FROM tc_material 
					    WHERE NUMERO_MATERIAL = :material 
					    LIMIT 1
					) AS m
					CROSS JOIN (
					    SELECT COALESCE(SILO_ID, 0) AS SILO_ID 
					    FROM tc_silo 
					    WHERE SILO_CLAVE = :silo OR SILO_NOMBRE = :silo 
					    LIMIT 1
					) AS s
					SET dtpt.PEDIDO_TRASLADO = :pedidoTraslado,
					    dtpt.CANTIDAD_PEDIDO = :cantidadPedido,
					    dtpt.CANTIDAD_TRASLADO = :cantidadTraslado,
					    dtpt.CANTIDAD_RECIBIDA = :cantidadRecibida,
					    dtpt.PENDIENTE_TRASLADO = :pendienteTraslado,
					    dtpt.TRASLADO_PENDIENTE_FACTURA = :numeroPedndFacturas,
					    pt1.PLANTA_DESTINO = :plantaDestino,
					    pt1.TC_MATERIAL_ID = NULLIF(m.MATERIAL_ID, 0),
					    pt1.TC_SILO_ID = NULLIF(s.SILO_ID, 0)
					WHERE pt1.NUMERO_PED_TRASLADO = :numeroPedTraslado
					  AND pt1.POSICION = :posicion
					  AND pc.NUMERO_PEDIDO = :numeroPedidoCompra
					  AND (
					      -- Solo actualiza si al menos un campo cambió
					      NOT (COALESCE(dtpt.PEDIDO_TRASLADO, '') <=> COALESCE(:pedidoTraslado, ''))
					      OR NOT (COALESCE(dtpt.CANTIDAD_PEDIDO, 0) <=> COALESCE(:cantidadPedido, 0))
					      OR NOT (COALESCE(dtpt.CANTIDAD_TRASLADO, 0) <=> COALESCE(:cantidadTraslado, 0))
					      OR NOT (COALESCE(dtpt.CANTIDAD_RECIBIDA, 0) <=> COALESCE(:cantidadRecibida, 0))
					      OR NOT (COALESCE(dtpt.PENDIENTE_TRASLADO, 0) <=> COALESCE(:pendienteTraslado, 0))
					      OR NOT (COALESCE(dtpt.TRASLADO_PENDIENTE_FACTURA, 0) <=> COALESCE(:numeroPedndFacturas, 0))
					      OR NOT (COALESCE(pt1.PLANTA_DESTINO, '') <=> COALESCE(:plantaDestino, ''))
					      OR NOT (COALESCE(pt1.TC_MATERIAL_ID, 0) <=> COALESCE(NULLIF(m.MATERIAL_ID, 0), 0))
					      OR NOT (COALESCE(pt1.TC_SILO_ID, 0) <=> COALESCE(NULLIF(s.SILO_ID, 0), 0))
					  )
	            """;
	        List<MapSqlParameterSource> batchParams = new ArrayList<>();
	        for (PedidoTrasladoSapResponseDTO dto : trasladosParaActualizar) {
	            MapSqlParameterSource params = new MapSqlParameterSource();
	            String canti_ped_traslado=dto.getCantidadPendienteTraslado() != null ? NumericCleaner.cleanNumericString(dto.getCantidadPendienteTraslado()) : "0";
	            params.addValue("numeroPedTraslado", dto.getNumeroPedTraslado());
	            params.addValue("numeroPedidoCompra", dto.getPedidoDeComprasAsociado());
	            params.addValue("posicion", dto.getPosicion());
	            params.addValue("pedidoTraslado", parseFloatSafe(dto.getCantidaddespacho()));
	            params.addValue("cantidadPedido", parseFloatSafe(dto.getCantidadPedido()));
	            params.addValue("cantidadTraslado", parseFloatSafe(dto.getCantidadEnTraslado()));
	            params.addValue("cantidadRecibida", parseFloatSafe(dto.getCantidadRecibidaEnPa()));
	            params.addValue("pendienteTraslado", parseFloatSafe(canti_ped_traslado));
	            params.addValue("numeroPedndFacturas", parseFloatSafe(dto.getTrasladosPendientes()));
	            params.addValue("plantaDestino",dto.getPlantaDestino());
	            params.addValue("material",dto.getMaterial());
	            params.addValue("silo",claveSilo);
	            batchParams.add(params);
	        }
	        logger.info("SE ACTUALIZARON LOS PEDIDO TRASLADO");
	        // Ejecutar actualización por lotes
	        try {
		        namedParameterJdbcTemplate.batchUpdate(sql, batchParams.toArray(new MapSqlParameterSource[0]));
	        }catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    
	    private Float parseFloatSafe(String value) {
	        if (value == null || value.trim().isEmpty()) {
	            return 0.0f;
	        }
	        try {
	            return Float.parseFloat(value);
	        } catch (NumberFormatException e) {
	            return 0.0f;
	        }
	    }
	    
	    // Método para obtener datos de BD
	    public List<Map<String, Object>> findTrasladosByFolios(List<String> folios,String claveSilo,String claveMaterial) {
	        String sql = """
          SELECT
	                ped.FOLIO_NUM_PED_POSICION as folio,
	                ped.NUMERO_PED_TRASLADO,
	                dtpt.PEDIDO_TRASLADO,
	                dtpt.CANTIDAD_PEDIDO,
	                dtpt.CANTIDAD_TRASLADO AS CANTIDAD_EN_TRASLADO,
	                dtpt.CANTIDAD_RECIBIDA AS CANTIDAD_RECIBIDA_ENPA,
	                dtpt.PENDIENTE_TRASLADO AS CANTIDAD_PENDIENTE_TRASLADO,
                    pc.NUMERO_PEDIDO
	            FROM tc_pedido_traslado ped
	            JOIN tc_detalle_pedido_traslado dtpt ON dtpt.TC_PEDIDO_TRASLADO_ID = ped.PEDIDO_TRASLADO_ID
                JOIN tc_pedido_compra pc ON ped.TC_PEDIDO_COMPRA_ID=pc.PEDIDO_COMPRA_ID
				JOIN tc_silo s ON ped.TC_SILO_ID=s.SILO_ID
				JOIN tc_material m ON ped.TC_MATERIAL_ID=m.MATERIAL_ID
	            WHERE ped.NUMERO_PED_TRASLADO IN (:folios)
                AND (s.SILO_NOMBRE=:silo OR s.SILO_CLAVE=:silo)
				AND (m.NUMERO_MATERIAL=:material or m.MATERIAL_DESCRIPCION=:material)
	            """;

	        Map<String, Object> params = new HashMap<>();
	        params.put("folios", folios);
	        params.put("silo", claveSilo);
	        params.put("material", claveMaterial);

	        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
	            Map<String, Object> row = new HashMap<>();
	            row.put("folio", rs.getString("folio"));
	            row.put("pedidoTraslado", rs.getFloat("PEDIDO_TRASLADO"));
	            row.put("cantidadPedido", rs.getFloat("CANTIDAD_PEDIDO"));
	            row.put("cantidadTraslado", rs.getFloat("CANTIDAD_EN_TRASLADO"));
	            row.put("cantidadRecibidaEnPA", rs.getFloat("CANTIDAD_RECIBIDA_ENPA"));
	            row.put("cantidadPendienteTraslado", rs.getFloat("CANTIDAD_PENDIENTE_TRASLADO"));
	            row.put("numeroPedidoCompraAsociado", rs.getFloat("NUMERO_PEDIDO"));
	            row.put("num_pedido_traslado", rs.getFloat("NUMERO_PED_TRASLADO"));
	            return row;
	        });
	    }
	    
	    public Map<String, Map<String, Object>> convertToFolioMapTraslado(List<Map<String, Object>> resultados) {
	        return resultados.stream()
	            .collect(Collectors.toMap(
	                row -> (String) row.get("folio"),
	                row -> row
	            ));
	    }
}
