package com.bachoco.persistence.adapter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bachoco.model.Perfil;
import com.bachoco.persistence.entity.PerfilEntity;
import com.bachoco.persistence.repository.PerfilJpaRepository;
import com.bachoco.port.PerfilRepositoryPort;

@Component
public class PerfilJpaRepositoryAdapter implements PerfilRepositoryPort {

	private final PerfilJpaRepository perfilJpaRepository;
	
	public PerfilJpaRepositoryAdapter(PerfilJpaRepository perfilJpaRepository) {
		this.perfilJpaRepository = perfilJpaRepository;
	}

	@Override
	public Perfil save(Perfil perfil) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Perfil update(Perfil perfil) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Perfil> findById(Integer id) {
		try {
			Optional<PerfilEntity> perfil= this.perfilJpaRepository.findById(id);
			if(!perfil.isEmpty() && perfil.get().getId()!=null) {
				return Optional.ofNullable(toDomain(perfil.get()));
			}
		}catch (NoSuchElementException e) {
			return Optional.empty();
		}catch (Exception e) {
			return Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	public Optional<Perfil> findByClave(String clave) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Perfil> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public Perfil toDomain(PerfilEntity entity) {
		Perfil perfil= new Perfil();
		perfil.setId(entity.getId());
		perfil.setClave(entity.getClave());
		perfil.setDescripcion(entity.getDescripcion());
		return perfil;
	}
}
