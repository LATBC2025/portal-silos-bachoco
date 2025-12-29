package com.bachoco.app_boot.ejecucions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bachoco.persistence.entity.PerfilEntity;
import com.bachoco.persistence.entity.PermisoEntity;
import com.bachoco.persistence.repository.PerfilJpaRepository;



@Component
public class DatosInicialesPerfilRunner implements CommandLineRunner{
	
	private final PerfilJpaRepository perfilJpaRepository;
	private static final Logger logger = LoggerFactory.getLogger(DatosInicialesPerfilRunner.class);

	public DatosInicialesPerfilRunner(PerfilJpaRepository perfilJpaRepository) {
		this.perfilJpaRepository = perfilJpaRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// 1. Definir las descripciones deseadas como un Set para búsquedas rápidas (O(1))
	    Set<String> descripcionesDeseadas = Set.of(
	        "ADMINSTRADOR", 
	        "ANALISTA DE LOGISTICA", 
	        "PERSONAL EXTERNO"
	    );
	    // Si tienes que usar findAll() por ahora, optimizamos el mapeo:
	    List<PerfilEntity> lista = this.perfilJpaRepository.findAll();
	    // 3. Crear un Set de las claves existentes en minúsculas para comparaciones eficientes
	    Set<String> clavesExistentes = lista.stream()
	        .map(p -> p.getClave().toLowerCase())
	        .collect(Collectors.toSet());
	    // 4. Filtrar las descripciones deseadas para encontrar las que *no* existen
	    List<String> noExistentes = descripcionesDeseadas.stream()
	        .map(String::toLowerCase) // Convertir a minúsculas
	        .filter(clave -> !clavesExistentes.contains(clave))
	        .collect(Collectors.toList());
	    // 5. Si hay nuevos perfiles, prepararlos y guardarlos
	    if (!noExistentes.isEmpty()) {
	        // Inicializar el Set de permisos vacío una sola vez
	        Set<PermisoEntity> permisosVacios = Set.of();
	        // Mapear los nombres inexistentes a nuevas entidades
	        List<PerfilEntity> registrar = noExistentes.stream()
	            .map(clave -> new PerfilEntity(null, clave.toUpperCase(), clave.toUpperCase(), permisosVacios))
	            .collect(Collectors.toList());
	        // Guardar todas las nuevas entidades
	        try {
	            this.perfilJpaRepository.saveAll(registrar);
	        } catch (Exception e) {
	            // Manejo de la excepción: es crucial loguear el error para saber qué falló
	            logger.error("Error al guardar nuevos perfiles: " + e.getMessage());
	        }
	    }
	}
	

}
