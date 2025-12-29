package com.bachoco.port;

import java.util.List;
import java.util.Optional;

import com.bachoco.model.Bodega;

public interface BodegaRepositoryPort {
	Bodega save(Bodega bodega);
	Bodega update(Bodega bodega);
	void delete(Integer id);
	Bodega findByCodigo(String codigo);
	Optional<Bodega> findById(Integer id);
	List<Bodega> findAll();
	List<Bodega> findBySilo(Integer siloId);
}
