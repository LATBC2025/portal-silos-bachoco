package com.bachoco.service.usecase;

import java.util.List;
import java.util.Optional;

import com.bachoco.model.ApiResponse;
import com.bachoco.model.Planta;
import com.bachoco.model.Silo;
import com.bachoco.port.PlantaRepositoryPort;

public class PlantaUseCase {

	private final PlantaRepositoryPort plantaRepositoryPort;

	public PlantaUseCase(PlantaRepositoryPort plantaRepositoryPort) {
		this.plantaRepositoryPort = plantaRepositoryPort;
	}
	
	public ApiResponse<Planta>  save(Planta planta) {
		ApiResponse<Planta> response= new ApiResponse<>();
		Optional<Planta> opt=this.plantaRepositoryPort.findByClave(planta.getPlanta());
		if(opt.isPresent() && opt.get().getPlanta().equalsIgnoreCase(planta.getPlanta())) {
			response.setCode("BUSI-P1");
			response.setMessage("Ya existe la planta");
			return response;
		}
		Planta result=this.plantaRepositoryPort.save(planta).get();
		response.setCode("0");
		response.setData(result);
		return response;
	}
	
	public Planta update(Planta planta) {
		return this.plantaRepositoryPort.update(planta).get();
	}
	
	public void delete(Integer id) {
		this.plantaRepositoryPort.delete(id);
	}
	
	public Optional<Planta> findById(Integer id){
		return this.plantaRepositoryPort.findBydId(id);
	}
	
	public List<Planta> findAll(){
		return this.plantaRepositoryPort.findAll();
	}
	public List<Planta> findAllByProgramArribo(String fechaInicio,String fechaFin){
		return this.plantaRepositoryPort.findAllByProgramArribo(fechaInicio,fechaFin);
	}
}
