package com.bachoco.port;

import java.util.Optional;

import com.bachoco.model.Categoria;

public interface CategoriaRepositoryPort {
	
	public Optional<Categoria> findById(Integer materialId);

}
