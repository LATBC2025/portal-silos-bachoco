package com.bachoco.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.bachoco.exception.CannotDeleteResourceException;
import com.bachoco.mapper.PlantaMapper;
import com.bachoco.model.Planta;
import com.bachoco.persistence.entity.PlantaEntity;
import com.bachoco.persistence.repository.PlantaJpaRepository;
import com.bachoco.port.PlantaRepositoryPort;

@Repository
public class PlantaJpaRepositoryAdapter implements PlantaRepositoryPort {

	private final PlantaJpaRepository plantaRepository;

	public PlantaJpaRepositoryAdapter(PlantaJpaRepository plantaRepository) {
		this.plantaRepository = plantaRepository;
	}

	@Override
	public Optional<Planta> save(Planta planta) {
		try {
			PlantaEntity newPlanta = this.plantaRepository.save(PlantaMapper.toEntity(planta));
			return Optional.ofNullable(PlantaMapper.toDomain(newPlanta));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Optional.empty();
	}

	@Override
	public Optional<Planta> update(Planta planta) {
		try {
			Optional<PlantaEntity> plantaEntity = this.plantaRepository.findById(planta.getId());
			if (plantaEntity.isPresent()) {
				PlantaEntity modifiedPlanta = plantaEntity.get();
				modifiedPlanta.setPlanta(planta.getPlanta());
				modifiedPlanta.setNombre(planta.getNombre());
				modifiedPlanta.setSociedad(planta.getSociedad());
				PlantaEntity modified = this.plantaRepository.save(modifiedPlanta);
				return Optional.ofNullable(PlantaMapper.toDomain(modified));
			} else {
				return Optional.empty();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Optional.empty();
	}

	@Override
	public void delete(Integer id) {
		Optional<PlantaEntity> plantaEntity = this.plantaRepository.findById(id);
		if (plantaEntity.isPresent()) {
			try {
				this.plantaRepository.delete(plantaEntity.get());
			} catch (DataIntegrityViolationException e) {
				throw new CannotDeleteResourceException("No se puede eliminar el producto, existen pedidos asociados.",
						e);
			}
		}

	}

	@Override
	public Optional<Planta> findBydId(Integer id) {
		try {
			Optional<PlantaEntity> plantaEntity = this.plantaRepository.findById(id);
			return Optional.ofNullable(PlantaMapper.toDomain(plantaEntity.get()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Optional.empty();
	}

	@Override
	public List<Planta> findAll() {
		List<PlantaEntity> listaPlantaEntity = this.plantaRepository.findAll();
		return PlantaMapper.toListDomain(listaPlantaEntity);
	}

	@Override
	public Optional<Planta> findByClave(String clave) {
		try {
			Optional<PlantaEntity> plantaEntity = this.plantaRepository.findByPlanta(clave);
			if (plantaEntity.isPresent()) {
				return Optional.ofNullable(PlantaMapper.toDomain(plantaEntity.get()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Optional.empty();
	}

	@Override
	public List<Planta> findAllByProgramArribo(String fechaInicio,String fechaFin) {
		List<PlantaEntity> listaPlantaEntity=this.plantaRepository.findPlantasConArribos(fechaInicio,fechaFin);
		return PlantaMapper.toListDomain(listaPlantaEntity); 
	}

}
