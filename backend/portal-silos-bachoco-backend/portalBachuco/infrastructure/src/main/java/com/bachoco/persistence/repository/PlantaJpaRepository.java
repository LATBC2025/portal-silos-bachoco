package com.bachoco.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bachoco.persistence.entity.PlantaEntity;

public interface PlantaJpaRepository extends JpaRepository<PlantaEntity, Integer> {
	public List<PlantaEntity> findAll();

	public Optional<PlantaEntity> findById(Integer id);

	public Optional<PlantaEntity> findByPlanta(String clavePlanta);

	@Query(value = """
			 SELECT p.PLANTA_ID, p.NOMBRE, p.PLANTA_CLAVE, p.SOCIEDAD
			        FROM tc_planta p
			        WHERE EXISTS (
			            SELECT 1 
			            FROM tc_arribo a 
			            JOIN tc_detalle_arribo da ON a.ARRIBO_ID = da.TC_ARRIBO_ID
			            WHERE a.TC_PLANTA_ID = p.PLANTA_ID
			            AND (:pFechaInicio IS NULL OR da.FECHA_PROGRAMADA >= STR_TO_DATE(:pFechaInicio, '%Y-%m-%d'))
			            AND (:pFechaFin IS NULL OR da.FECHA_PROGRAMADA <= STR_TO_DATE(:pFechaFin, '%Y-%m-%d'))
			        )
			""", nativeQuery = true)
	List<PlantaEntity> findPlantasConArribos(@Param("pFechaInicio") String pFechaInicio,@Param("pFechaFin") String pFechaFin);

}
