package com.bachoco.persistence.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.bachoco.model.AuthenticationResponse;
import com.bachoco.model.EmpleadoResponse;

@Component
public class CatalogJdbcRepository {

	private final JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private final DataSource dataSource;

	public CatalogJdbcRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
			DataSource dataSource) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.dataSource = dataSource;
	}

	public int sumaCantidadStockSilo(Float cantidad, String claveSilo, Integer siloId) {
		String sql = """
				UPDATE tc_silo s
					SET s.STOCK_SILO=s.STOCK_SILO+?
				WHERE s.SILO_CLAVE=?
				   """;
		return jdbcTemplate.update(sql, cantidad, claveSilo);
	}

	public int restaCantidadStockSilo(Float cantidad, String claveSilo, Integer siloId) {
		String sql = """
				UPDATE tc_silo s
					SET s.STOCK_SILO=s.STOCK_SILO+?
				WHERE s.SILO_CLAVE=?
				   """;
		return jdbcTemplate.update(sql, cantidad, claveSilo);
	}

	public Optional<AuthenticationResponse> authResponse(String usuario) {
		String sql = """
				SELECT
					u.USUARIO_ID,
					CASE WHEN e.TIPO_EMPLEADO=2 THEN e.CORREO ELSE u.USUARIO END AS USUARIO,
					u.USUARIO_PASSWORD,e.CORREO,
					u.USUARIO_TIPO,
					e.TIPO_EMPLEADO,
					u.ULTIMA_MOD_PWD
				FROM tc_empleado e
				JOIN tc_usuario u ON e.TC_USUARIO_ID=u.USUARIO_ID
				LEFT JOIN tc_empleado_interno ei ON e.EMPLEADO_ID=ei.TC_EMPLEADO_ID
				LEFT JOIN tc_empleado_externo ex ON e.EMPLEADO_ID=ex.TC_EMPLEADO_ID
				where u.USUARIO=? or e.CORREO=?
				LIMIT 1
				""";
		String sqlAuth = """
							SELECT
								u.USUARIO_ID,
								CASE WHEN e.TIPO_EMPLEADO=2 THEN e.CORREO ELSE u.USUARIO END AS USUARIO,
								u.USUARIO_PASSWORD,
				                   e.CORREO,
								u.USUARIO_TIPO,
								e.TIPO_EMPLEADO,
								u.ULTIMA_MOD_PWD,
				                   GROUP_CONCAT(
								DISTINCT CASE
									WHEN pt.PUESTO_DESCRIPCION IS NOT NULL
									THEN pt.PUESTO_DESCRIPCION
									ELSE 'EXTERNO'
								END
								SEPARATOR ','
							) AS roles
							FROM tc_empleado e
							JOIN tc_usuario u ON e.TC_USUARIO_ID=u.USUARIO_ID
							LEFT JOIN tc_empleado_interno ei ON e.EMPLEADO_ID=ei.TC_EMPLEADO_ID
							LEFT JOIN tc_empleado_externo ex ON e.EMPLEADO_ID=ex.TC_EMPLEADO_ID
				               LEFT JOIN tc_puesto pt ON ei.TC_PUESTO_ID=pt.PUESTO_ID
							where u.USUARIO=? or e.CORREO=?
							GROUP BY u.USUARIO_ID,
								USUARIO,
								u.USUARIO_PASSWORD,
				                   e.CORREO,
								u.USUARIO_TIPO,
								e.TIPO_EMPLEADO,
								u.ULTIMA_MOD_PWD

				""";
		try {
			AuthenticationResponse result = jdbcTemplate.queryForObject(sqlAuth, new Object[] { usuario, usuario },
					new RowMapper<AuthenticationResponse>() {
						@Override
						public AuthenticationResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
							AuthenticationResponse auth = new AuthenticationResponse();
							auth.setUsuarioId(rs.getInt("USUARIO_ID"));
							auth.setUsuario(rs.getString("USUARIO"));
							auth.setPassword(rs.getString("USUARIO_PASSWORD"));
							auth.setCorreo(rs.getString("CORREO"));
							Set<String> roles = Optional.ofNullable(rs.getString("roles")).stream()
									.flatMap(s -> java.util.Arrays.stream(s.split("\\s*,\\s*")))
									.filter(str -> !str.isEmpty()).map(role -> role.replace(" ", "_"))
									.collect(Collectors.toSet());
							auth.setRoles(roles);
							auth.setUsuarioTipo(rs.getInt("USUARIO_TIPO"));
							auth.setEmpleadoActivo(rs.getInt("TIPO_EMPLEADO"));
							Timestamp timestampDB = rs.getTimestamp("ULTIMA_MOD_PWD");
							if (timestampDB != null) {
								LocalDateTime localDateTime = timestampDB.toLocalDateTime();
								auth.setUltimoModifiPwd(localDateTime);
							}
							return auth;
						}
					});
			if (result != null) {
				return Optional.of(result);
			} else {
				return Optional.empty();
			}
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<EmpleadoResponse> findByUsuarioOrCorreo(String value) {
		String sql = """
				SELECT
				 	e.EMPLEADO_ID,
				 	e.TIPO_EMPLEADO,
				    i.TC_DEPARTAMENTO_ID,
				    i.TC_PUESTO_ID,
				    ex.TC_SILO_ID,
				    CASE WHEN p.PUESTO_DESCRIPCION is null then 'EXTERNO'
				                ELSE p.PUESTO_DESCRIPCION END as PUESTO_DESCRIPCION
				FROM tc_empleado e
				LEFT JOIN tc_usuario u on e.TC_USUARIO_ID=u.USUARIO_ID
				LEFT JOIN tc_empleado_externo ex on e.EMPLEADO_ID=ex.TC_EMPLEADO_ID
				LEFT JOIN tc_empleado_interno i on e.EMPLEADO_ID=i.TC_EMPLEADO_ID
				            LEFT JOIN tc_puesto p ON i.TC_PUESTO_ID=p.PUESTO_ID
				where e.CORREO=? or u.USUARIO=?
				LIMIT 1
				""";
		try {
			EmpleadoResponse result = jdbcTemplate.queryForObject(sql, new Object[] { value, value },
					new RowMapper<EmpleadoResponse>() {
						@Override
						public EmpleadoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
							EmpleadoResponse p = new EmpleadoResponse();
							p.setEmpleadoId(rs.getInt("EMPLEADO_ID"));
							p.setTipoEmpleado(rs.getInt("TIPO_EMPLEADO"));
							p.setDepartamentoId(rs.getInt("TC_DEPARTAMENTO_ID"));
							p.setPuestoId(rs.getInt("TC_PUESTO_ID"));
							p.setSiloId(rs.getInt("TC_SILO_ID"));
							p.setNombrePuesto(rs.getString("PUESTO_DESCRIPCION"));
							return p;
						}
					});
			if (result != null) {
				return Optional.of(result);
			} else {
				return Optional.empty();
			}
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Integer obtenerSiloId(String siloNombre) {
		String sql = "SELECT SILO_ID " + "FROM tc_silo " + "WHERE SILO_NOMBRE = ? OR SILO_CLAVE = ? " + "LIMIT 1";
		try {
			Integer siloId = jdbcTemplate.queryForObject(sql, Integer.class, siloNombre, siloNombre);
			return siloId;
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			System.out.println("Silo no encontrado: " + siloNombre);
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public List<String> findAllClavePedCompra() {
		String sql = """
				SELECT pc.FOLIO_NUM_PED_POSICION from tc_pedido_compra pc
				""";
		try {
			List<String> folios = jdbcTemplate.queryForList(sql, String.class);
			return folios;
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public List<String> findAllClavePedCompra(String claveSilo, String claveMaterial) {
		String sql = """
					SELECT DISTINCT pc.NUMERO_PEDIDO from tc_pedido_compra pc
					JOIN tc_silo s ON pc.TC_SILO_ID=s.SILO_ID
					JOIN tc_material m ON TC_MATERIAL_ID=m.MATERIAL_ID
					where
					(s.SILO_NOMBRE=? OR s.SILO_CLAVE=?)
					AND (m.NUMERO_MATERIAL=? or m.MATERIAL_DESCRIPCION=?)
				""";
		try {
			List<String> folios = jdbcTemplate.queryForList(sql, String.class, claveSilo, claveSilo, claveMaterial,
					claveMaterial);
			return folios;
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public int resetStockSilo(String cantidad, String claveSilo, Integer idSilo) {
		Integer id = obtenerSiloId(claveSilo);
		int response = -1;
		if (id != null) {
			String sql = """
					UPDATE tc_silo s
					SET s.STOCK_SILO=?
					WHERE s.SILO_ID=?
					            and s.STOCK_SILO=0
					   """;
			response = jdbcTemplate.update(sql, cantidad, id);
		}
		return response;
	}

	public Float totalProgramArriboByPedTraslado(List<String> numPedidoTraslados, String claveSilo,
			String claveMaterial, String clavePlanta, String fechaInicio, String fechaFin) {
		String sql = """
				SELECT SUM(da.CANTIDAD) as total
				FROM tc_arribo a
				JOIN tc_detalle_arribo da ON a.ARRIBO_ID = da.TC_ARRIBO_ID
				JOIN tc_pedido_traslado pt ON da.TC_PEDIDO_TRASLADO_ID = pt.PEDIDO_TRASLADO_ID
				JOIN tc_silo s ON a.SILO_ID = s.SILO_ID
				JOIN tc_material m ON a.MATERIAL_ID = m.MATERIAL_ID
				JOIN tc_planta p ON a.TC_PLANTA_ID = p.PLANTA_ID
				WHERE 1=1
				  AND  m.NUMERO_MATERIAL = :claveMaterial
				  AND pt.NUMERO_PED_TRASLADO IN (:numPedidoTraslados)
				  AND (:clavePlanta IS NULL OR p.NOMBRE = :clavePlanta OR p.PLANTA_CLAVE = :clavePlanta)
				  AND (:claveSilo IS NULL OR s.SILO_NOMBRE = :claveSilo OR s.SILO_CLAVE = :claveSilo)
				  AND (:fechaInicio IS NULL OR da.FECHA_PROGRAMADA >= STR_TO_DATE(:fechaInicio, '%Y-%m-%d'))
				  AND (:fechaFin IS NULL OR da.FECHA_PROGRAMADA <= STR_TO_DATE(:fechaFin, '%Y-%m-%d'))
				  """;

		Map<String, Object> params = new HashMap<>();
		params.put("claveMaterial", claveMaterial);
		params.put("numPedidoTraslados", numPedidoTraslados);
		params.put("clavePlanta", clavePlanta);
		params.put("claveSilo", claveSilo);
		params.put("fechaInicio", fechaInicio);
		params.put("fechaFin", fechaFin);
		try {
			return namedParameterJdbcTemplate.queryForObject(sql, params, Float.class);
		} catch (EmptyResultDataAccessException e) {
			return 0.0F;
		}
	}

}
