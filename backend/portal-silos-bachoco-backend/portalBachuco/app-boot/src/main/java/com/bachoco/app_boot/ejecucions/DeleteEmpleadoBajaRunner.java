package com.bachoco.app_boot.ejecucions;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bachoco.persistence.adapter.EmpleadoExtJdbcRepositoryPort;
import com.bachoco.persistence.adapter.EmpleadoInternoJpaRepositoryAdapter;

@Component
public class DeleteEmpleadoBajaRunner implements CommandLineRunner {

	private final EmpleadoExtJdbcRepositoryPort empleadoExternoAdapter;
	private final EmpleadoInternoJpaRepositoryAdapter empleadoInbternoAdapter;
	private static final Logger logger = LoggerFactory.getLogger(DatosInicialesEmpleadoDefaultRunner.class);

	public DeleteEmpleadoBajaRunner(EmpleadoExtJdbcRepositoryPort empleadoExternoAdapter,
			EmpleadoInternoJpaRepositoryAdapter empleadoInbternoAdapter) {
		this.empleadoExternoAdapter = empleadoExternoAdapter;
		this.empleadoInbternoAdapter = empleadoInbternoAdapter;
	}

	@Override
	public void run(String... args) throws Exception {
		List<Integer> listaIdEmpInterno=new ArrayList<>();
		List<Integer> listaIdEmpExterno=new ArrayList<>();
	    try {
	    	listaIdEmpExterno=this.empleadoExternoAdapter.findAllIdEmpleadoBaja();
	    	listaIdEmpInterno=this.empleadoInbternoAdapter.findAllIdEmpleadoBaja();
	    	if(listaIdEmpExterno!=null && listaIdEmpExterno.size()>0) {
	    		for(Integer id:listaIdEmpExterno) {
	    			this.empleadoExternoAdapter.delete(id);
	    		}
	    	}
	    	if(listaIdEmpInterno!=null && listaIdEmpInterno.size()>0) {
	    		for(Integer id:listaIdEmpInterno) {
	    			this.empleadoInbternoAdapter.delete(id);
	    		}
	    	}
	    } catch (Exception e) {
	        // Manejo de excepción adecuado: registra el error y relanza.
	        logger.error("ERROR al eliminar los registros de empleados de baja: " + e.getMessage());
	        logger.error("ERROR al eliminar los registros de empleados de baja: " + e.getCause());
	    }
	}

}
