package com.bachoco.port;

import java.util.List;
import java.util.Optional;

import com.bachoco.model.Planta;

public interface PlantaRepositoryPort {

	public Optional<Planta> save(Planta planta);
	public Optional<Planta> update(Planta planta);
	public void delete(Integer id);
	public Optional<Planta> findBydId(Integer id);
	public Optional<Planta> findByClave(String clave);
	public List<Planta> findAll();
	public List<Planta> findAllByProgramArribo(String fechaInicio,String fechaFin);
}
