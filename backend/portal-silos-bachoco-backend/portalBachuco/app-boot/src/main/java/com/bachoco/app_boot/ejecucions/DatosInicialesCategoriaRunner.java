package com.bachoco.app_boot.ejecucions;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bachoco.persistence.entity.CategoriaEntity;
import com.bachoco.persistence.repository.CategoriaJpaRepository;

@Component
public class DatosInicialesCategoriaRunner implements CommandLineRunner {

	private final CategoriaJpaRepository categoriaJpaRepository;
	private static final Logger logger = LoggerFactory.getLogger(DatosInicialesCategoriaRunner.class);

	public DatosInicialesCategoriaRunner(CategoriaJpaRepository categoriaJpaRepository) {
		this.categoriaJpaRepository = categoriaJpaRepository;
	}

	@Override
	public void run(String... args) throws Exception {
	    final Set<String> categoriasDeseadas = Set.of(
	        "MAIZ",
	        "TRIGO",
	        "SOYA"
	    );
	    Set<String> descripcionesExistentes = this.categoriaJpaRepository.findAll().stream()
	        .map(categoria -> categoria.getDescripcion().toLowerCase())
	        .collect(Collectors.toSet());
	    // 3. Identificar las descripciones deseadas que *no* existen en la base de datos.
	    List<String> categoriasParaRegistrar = categoriasDeseadas.stream()
	        .map(String::toLowerCase) // Convertir a minúsculas para la comparación
	        .filter(descripcion -> !descripcionesExistentes.contains(descripcion))
	        .collect(Collectors.toList());
	    // 4. Si hay nuevas categorías, prepararlas y guardarlas.
	    if (!categoriasParaRegistrar.isEmpty()) {
	        // Mapear los nombres inexistentes a nuevas entidades CategoriaEntity.
	        List<CategoriaEntity> nuevasCategorias = categoriasParaRegistrar.stream()
	            .map(descripcion -> {
	                // Se usa toUpperCase() para guardar la descripción en mayúsculas para consistencia
	                String clave = descripcion.toUpperCase();
	                // Asume que el constructor es CategoriaEntity(Long id, String clave, String descripcion)
	                return new CategoriaEntity(null, clave, clave); 
	            })
	            .collect(Collectors.toList());
	        try {
	            this.categoriaJpaRepository.saveAll(nuevasCategorias);
	            logger.info("Se han registrado " + nuevasCategorias.size() + " nuevas categorías.");
	        } catch (Exception e) {
	            logger.warn("ERROR: No se pudieron registrar las categorías: " + e.getMessage());
	        }
	    } else {
	        logger.info("Todas las categorías iniciales ya existen. No se registró nada.");
	    }
	}
}
