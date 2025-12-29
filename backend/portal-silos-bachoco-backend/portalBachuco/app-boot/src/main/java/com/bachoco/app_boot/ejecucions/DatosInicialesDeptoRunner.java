package com.bachoco.app_boot.ejecucions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bachoco.persistence.entity.DepartamentoEntity;
import com.bachoco.persistence.repository.DepartamentoJpaRepository;

@Component
public class DatosInicialesDeptoRunner implements CommandLineRunner{

	private final DepartamentoJpaRepository departamentoJpaRepository;
	private static final Logger logger = LoggerFactory.getLogger(DatosInicialesDeptoRunner.class);
	
	public DatosInicialesDeptoRunner(DepartamentoJpaRepository departamentoJpaRepository) {
		this.departamentoJpaRepository = departamentoJpaRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// 1. Definir los nombres deseados como un Set inmutable para búsquedas eficientes (O(1)).
	    final Set<String> nombresDeseados = Set.of(
	        "LOGISTICA", 
	        "COMPRAS",
	        "TI"
	    );
	    // 2. Obtener los registros existentes y crear un Set de sus nombres en minúsculas.
	    // Esto es el paso más costoso si la tabla es grande (debido a findAll()).
	    Set<String> nombresExistentes = this.departamentoJpaRepository.findAll().stream()
	        .map(departamento -> departamento.getNombre().toLowerCase())
	        .collect(Collectors.toSet());

	    // 3. Identificar los nombres deseados que *no* existen en la base de datos.
	    List<String> departamentosParaRegistrar = nombresDeseados.stream()
	        .map(String::toLowerCase) // Convertir a minúsculas para la comparación
	        .filter(nombre -> !nombresExistentes.contains(nombre))
	        .collect(Collectors.toList());

	    // 4. Si hay nuevos departamentos, prepararlos y guardarlos.
	    if (!departamentosParaRegistrar.isEmpty()) {
	        // Mapear los nombres inexistentes a nuevas entidades.
	        List<DepartamentoEntity> nuevosDepartamentos = departamentosParaRegistrar.stream()
	            // Se usa toUpperCase() para guardar el nombre de forma consistente,
	            // y se usa el constructor que omite el ID, asumiendo autoincremental.
	            .map(nombre -> new DepartamentoEntity(null, nombre.toUpperCase())) 
	            .collect(Collectors.toList());
	        try {
	            this.departamentoJpaRepository.saveAll(nuevosDepartamentos);
	            logger.info("Se registraron " + nuevosDepartamentos.size() + " nuevos departamentos.");
	        } catch (Exception e) {
	            // **Crucial**: Manejo de excepción adecuado. ¡No ignores los errores!
	        	logger.warn("ERROR: No se pudieron registrar los departamentos: " + e.getMessage());
	        }
	    } else {
	        logger.info("Todos los departamentos iniciales ya existen. No se registró nada.");
	    }
		
	}

}
