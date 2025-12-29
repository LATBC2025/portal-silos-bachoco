package com.bachoco.app_boot.ejecucions;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bachoco.model.EmpleadoInternoRequest;
import com.bachoco.model.EmpleadoResponse;
import com.bachoco.persistence.entity.DepartamentoEntity;
import com.bachoco.persistence.entity.PerfilEntity;
import com.bachoco.persistence.entity.PuestoEntity;
import com.bachoco.persistence.repository.DepartamentoJpaRepository;
import com.bachoco.persistence.repository.PerfilJpaRepository;
import com.bachoco.persistence.repository.PuestoJpaRepository;
import com.bachoco.service.usecase.EmpleadoUseCase;

@Component
@Order(Integer.MAX_VALUE)
public class DatosInicialesEmpleadoDefaultRunner implements CommandLineRunner {

	private final EmpleadoUseCase empleadoUseCase;
	private final PuestoJpaRepository puestoJpaRepository;
	private final DepartamentoJpaRepository departamentoJpaRepository;
	private final PerfilJpaRepository perfilJpaRepository;
	private static final Logger logger = LoggerFactory.getLogger(DatosInicialesEmpleadoDefaultRunner.class);
	
	public DatosInicialesEmpleadoDefaultRunner(EmpleadoUseCase empleadoUseCase, PuestoJpaRepository puestoJpaRepository,
			DepartamentoJpaRepository departamentoJpaRepository, PerfilJpaRepository perfilJpaRepository) {
		this.empleadoUseCase = empleadoUseCase;
		this.puestoJpaRepository = puestoJpaRepository;
		this.departamentoJpaRepository = departamentoJpaRepository;
		this.perfilJpaRepository = perfilJpaRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// 1. Definir los datos del Administrador.
	    //final String CORREO_ADMIN = "cendy.lara@bachoco.net";
	    final String CORREO_ADMIN = "latbc2025@gmail.com";
	    final String NOMBRE_PUESTO = "ADMINISTRADOR"; // Usamos la clave/nombre
	    final String NOMBRE_DEPARTAMENTO = "LOGISTICA"; // Usamos el nombre
	    final String CLAVE_PERFIL = "ADMINSTRADOR"; // Usamos la clave
	    final String NOMRE_EMPLEADO="Raymundo Ricardo Ortiz Luviano";
	    //final String NOMRE_EMPLEADO="Cendy Guadalupe Lara Perez";

	    
	    // 2. Verificar si el Administrador ya existe mediante una búsqueda directa.
	    // Esto es mucho más eficiente que un findAll().
	    Optional<EmpleadoResponse> result=this.empleadoUseCase.findByUsuarioOrCorreo(CORREO_ADMIN);
	    if (result.isPresent() && result.get().getEmpleadoId()!=null) {
	        System.out.println("El empleado Administrador ya existe. Omitiendo registro.");
	        return; // Detener la ejecución si ya existe.
	    }
	    // A. Obtener Puesto por Nombre
	    Optional<PuestoEntity> puesto = this.puestoJpaRepository.findByDescripcion(NOMBRE_PUESTO);
	    Optional<DepartamentoEntity> depto= this.departamentoJpaRepository.findByNombre(NOMBRE_DEPARTAMENTO);
	    Optional<PerfilEntity> perfil= this.perfilJpaRepository.findByClave(CLAVE_PERFIL);
	    EmpleadoInternoRequest emp = new EmpleadoInternoRequest();
	    
	    if((depto.isPresent() && depto.get().getId()!=null)
	    		&& (puesto.isPresent() && puesto.get().getId()!=null)
	    	&& (perfil.isPresent() && perfil.get().getId()!=null)){
	    		    emp.setCorreo(CORREO_ADMIN);
	    		    emp.setDepartamentoId(depto.get().getId());
	    		    emp.setPuestoId(puesto.get().getId());
	    		    emp.setPerfilId(perfil.get().getId());
	    		    emp.setUsuario(CORREO_ADMIN);
	    		    emp.setNombre(NOMRE_EMPLEADO);
	    	}
	    try {
	    	if(emp.getCorreo()!=null) {
		        this.empleadoUseCase.saveEmpleadoInterno(emp);
		        logger.info("Empleado Administrador registrado exitosamente.");
	    	}
	    } catch (Exception e) {
	        // Manejo de excepción adecuado: registra el error y relanza.
	        logger.error("ERROR al registrar al empleado Administrador: " + e.getMessage());
	        logger.error("ERROR al registrar al empleado Administrador causa: " + e.getCause());
	    }
	}

}
