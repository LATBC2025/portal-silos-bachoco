package com.bachoco.port;

import java.util.List;
import java.util.Optional;

import com.bachoco.model.Silo;

public interface SiloRepositoryPort {
	
	Optional<Silo> save(Silo silo);
	Optional<Silo> update(Silo silo);
	void delete(Integer id);
	Optional<Silo> findById(Integer id);
	Optional<Silo> findByClave(String clave);
	List<Silo> findAll();
	Optional<Float> stock(Integer siloId);

}
