package com.bachoco.persistence.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bachoco.model.EmpleadoExternoRequest;
import com.bachoco.model.EmpleadoExternoResponse;

@Repository
public class EmpleadoExternoJdbcRepository {

	private static final Logger logger = LoggerFactory.getLogger(EmpleadoExternoJdbcRepository.class);
	private final JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private SimpleJdbcCall simpleJdbcCall;
	private final DataSource dataSource;
	
	public EmpleadoExternoJdbcRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
			DataSource dataSource) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.dataSource = dataSource;
	}
	
	public List<EmpleadoExternoResponse> findAll(){
		String sql = """
				 SELECT
				    e.EMPLEADO_ID,
					e.NOMBRE,
			        e.CORREO,
			        e.TIPO_EMPLEADO,
				    ex.RFC,
				    ex.TC_SILO_ID
			    FROM tc_empleado e
			    JOIN tc_empleado_externo ex on e.EMPLEADO_ID=ex.TC_EMPLEADO_ID
			    JOIN tc_usuario u on e.TC_USUARIO_ID=u.USUARIO_ID
			     where e.TIPO_EMPLEADO=2
			    and (u.USUARIO_TIPO=2 OR u.USUARIO_TIPO=3)
			    and e.ESTATUS = '1'
			    GROUP BY 
			    e.EMPLEADO_ID,
					e.NOMBRE,
			        e.CORREO,
			        e.TIPO_EMPLEADO,
			        ex.RFC,
				    ex.TC_SILO_ID
				    """;
		
		return jdbcTemplate.query(sql, new RowMapper<EmpleadoExternoResponse>() {
            @Override
            public EmpleadoExternoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            	EmpleadoExternoResponse p = new EmpleadoExternoResponse();
                p.setId(rs.getInt("EMPLEADO_ID"));
                p.setNombre(rs.getString("NOMBRE"));
                p.setRfc(rs.getString("RFC"));;
                p.setCorreo(rs.getString("CORREO"));
                p.setSiloId(rs.getInt("TC_SILO_ID"));
                return p;
            }
        });
	}
	
	public EmpleadoExternoResponse findByCorreo(String correo){
		String sql = """
				    SELECT
				        e.EMPLEADO_ID,
						e.NOMBRE,
				        e.CORREO,
				        e.TIPO_EMPLEADO,
				        ex.RFC,
				        ex.TC_SILO_ID
				    FROM tc_empleado e
				    JOIN tc_usuario u ON e.TC_USUARIO_ID=u.USUARIO_ID
				    JOIN tc_empleado_externo ex ON e.EMPLEADO_ID=ex.TC_EMPLEADO_ID
				    where e.CORREO=?
				    AND e.EMP_FECHA_BAJA is null AND e.ESTATUS<>'0'
				    LIMIT 1;
				    """;
		
		try {
			return jdbcTemplate.queryForObject(sql,
					new Object[] { correo }, new RowMapper<EmpleadoExternoResponse>() {
	            @Override
	            public EmpleadoExternoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	EmpleadoExternoResponse p = new EmpleadoExternoResponse();
	                p.setId(rs.getInt("EMPLEADO_ID"));
	                p.setNombre(rs.getString("NOMBRE"));
	                p.setRfc(rs.getString("RFC"));;
	                p.setCorreo(rs.getString("CORREO"));
	                p.setSiloId(rs.getInt("TC_SILO_ID"));
	                return p;
	            }
	        });
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
		
	}
	
	public void update(Integer id,EmpleadoExternoRequest request) {
		 simpleJdbcCall = new SimpleJdbcCall(dataSource)
	                .withProcedureName("sp_update_empleado_externo");
		 try {
			 Map<String, Object> result = simpleJdbcCall.execute(
		                Map.of(
		                		"p_empleado_id", id,
		                        "p_nombre", request.getNombre(),
		                        "p_rfc", request.getRfc(),
		                        "p_correo", request.getCorreo(),
		                        "p_usuario", request.getUsuario(),
		                        "p_silo_id", request.getSiloId()
		                )
		        );
		 }catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delete(Integer id) {
		 String sql = "UPDATE tc_empleado SET ESTATUS = '0',EMP_FECHA_BAJA=sysdate() WHERE EMPLEADO_ID =?";
	     jdbcTemplate.update(sql,id);
	}
	
	public List<Integer> findAllEmpExternoBaja() {
		 String sql = """
		 			select DISTINCT e.EMPLEADO_ID from tc_empleado e 
					JOIN tc_empleado_externo ex ON e.EMPLEADO_ID=ex.TC_EMPLEADO_ID
					where e.ESTATUS='0'
		 		""";
		    List<Integer> resultados = jdbcTemplate.queryForList(sql, Integer.class);
		    return resultados;
	}
	
	@Transactional 
    public void eliminarEmpleadoCompleto(Integer empleadoId) {
        String deleteExterno = "DELETE FROM tc_empleado_externo ex WHERE ex.TC_EMPLEADO_ID = ?";
        jdbcTemplate.update(deleteExterno, empleadoId);
        String obtenerUsuarioId = "SELECT TC_USUARIO_ID FROM tc_empleado e WHERE e.EMPLEADO_ID = ?";
        Long usuarioId = jdbcTemplate.queryForObject(obtenerUsuarioId, Long.class, empleadoId);
        String deleteEmpleado = "DELETE FROM tc_empleado e1 WHERE e1.EMPLEADO_ID = ?";
        jdbcTemplate.update(deleteEmpleado, empleadoId);
        if (usuarioId != null) {
            String deleteUsuario = "DELETE FROM tc_usuario u1 WHERE u1.USUARIO_ID = ?";
            jdbcTemplate.update(deleteUsuario, usuarioId);
        }
    }
	
	public String validateCorreo(String correo){
		String sql = """
				       select 1 from tc_empleado emp
						where emp.CORREO=?
				    """;
		 return jdbcTemplate.queryForObject(sql, String.class, correo);
	}
	
	
	public EmpleadoExternoResponse save(EmpleadoExternoRequest request) {
		 simpleJdbcCall = new SimpleJdbcCall(dataSource)
	                .withProcedureName("sp_insert_emp_externo");
		 
		 Integer idGenerado=-1;
		 try {
			 Map<String, Object> result = simpleJdbcCall.execute(
		                Map.of(
		                        "p_nombre", request.getNombre(),
		                        "p_rfc", request.getRfc(),
		                        "p_correo", request.getCorreo(),
		                        "p_usuario", request.getCorreo(),
		                        "p_silo_id", request.getSiloId()
		                )
		        );
			 idGenerado = (Integer) result.get("p_emp_externo_id");
		 }catch (Exception e) {
			 logger.error("Error al registra el empleado externo: "+e.getMessage()+ " - causa: "+e.getCause());
			return null;
		}
		return toDomain(request,idGenerado);
	}
	
	
	private EmpleadoExternoResponse toDomain(EmpleadoExternoRequest request,Integer id) {
		if(!id.equals(-1)) {
			EmpleadoExternoResponse response= new EmpleadoExternoResponse();
			response.setId(id);
			response.setCorreo(request.getCorreo());
			response.setNombre(request.getNombre());
			response.setRfc(request.getRfc());
			response.setUsuario(request.getUsuario());
			response.setSiloId(Integer.parseInt(request.getSiloId()));
			return response;
		}
		return null;
	}
}
