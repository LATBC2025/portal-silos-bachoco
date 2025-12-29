package com.bachoco.app_boot.ejecucions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.bachoco.persistence.entity.PuestoEntity;
import com.bachoco.persistence.repository.PuestoJpaRepository;



@Component
public class DatosInicialesPuestosRunner implements CommandLineRunner {

	private final PuestoJpaRepository puestoRepository;
	private static final Logger logger = LoggerFactory.getLogger(DatosInicialesPuestosRunner.class);
	
	public DatosInicialesPuestosRunner(PuestoJpaRepository puestoRepository) {
		this.puestoRepository = puestoRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// 1. Definir los puestos deseados como un Set para búsquedas eficientes.
        final Set<String> puestosDeseados = Set.of(
            "ADMINISTRADOR",
            "USUARIO INTERNO",
            "USUARIO EXTERNO"
        );
        Set<String> descripcionesExistentes = this.puestoRepository.findAll().stream()
            .map(puesto -> puesto.getDescripcion().toLowerCase())
            .collect(Collectors.toSet());
        // 3. Filtrar los puestos deseados para encontrar los que *no* existen.
        // Se utiliza el Set para una comprobación O(1).
        List<String> puestosParaRegistrar = puestosDeseados.stream()
            .map(String::toLowerCase)
            .filter(descripcion -> !descripcionesExistentes.contains(descripcion))
            .collect(Collectors.toList());
        // 4. Si hay nuevos puestos, registrarlos.
        if (!puestosParaRegistrar.isEmpty()) {
            // Mapear los nombres inexistentes a nuevas entidades PuestoEntity.
            List<PuestoEntity> nuevosPuestos = puestosParaRegistrar.stream()
                // Se guarda la descripción en mayúsculas para consistencia (puedes cambiar esto).
                .map(descripcion -> new PuestoEntity(null, descripcion.toUpperCase()))
                .collect(Collectors.toList());
            try {
                this.puestoRepository.saveAll(nuevosPuestos);
                logger.info("Se han registrado " + nuevosPuestos.size() + " nuevos puestos.");
            } catch (Exception e) {
                logger.warn("ERROR: No se pudieron registrar los nuevos puestos.");
            }
        } else {
            logger.info("Todos los puestos iniciales ya existen. No se registraron nuevos.");
        }
	}

}
