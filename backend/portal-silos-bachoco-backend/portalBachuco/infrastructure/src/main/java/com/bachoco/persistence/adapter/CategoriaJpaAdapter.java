package com.bachoco.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.bachoco.model.Categoria;
import com.bachoco.persistence.entity.CategoriaEntity;
import com.bachoco.persistence.repository.CategoriaJpaRepository;
import com.bachoco.port.CategoriaRepositoryPort;

@Repository
public class CategoriaJpaAdapter implements CategoriaRepositoryPort {
	
	private final CategoriaJpaRepository categoriaJpaRepository;
	

	public CategoriaJpaAdapter(CategoriaJpaRepository categoriaJpaRepository) {
		this.categoriaJpaRepository = categoriaJpaRepository;
	}

	@Override
	public Optional<Categoria> findById(Integer categoriaId) {
		Optional<CategoriaEntity> categoriaEntity= this.categoriaJpaRepository.findById(categoriaId);
		if(!categoriaEntity.isEmpty()) {
			Categoria categoria= new Categoria(categoriaEntity.get().getId(),categoriaEntity.get().getNombre(),categoriaEntity.get().getDescripcion());
			return Optional.ofNullable(categoria);
		}
		return Optional.empty();
	}

}
