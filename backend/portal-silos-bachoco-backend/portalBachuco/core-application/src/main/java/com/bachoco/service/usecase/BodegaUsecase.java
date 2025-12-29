package com.bachoco.service.usecase;

import java.util.List;
import java.util.Optional;

import com.bachoco.model.Bodega;
import com.bachoco.model.Silo;
import com.bachoco.port.BodegaRepositoryPort;
import com.bachoco.port.SiloRepositoryPort;



public class BodegaUsecase {

	private final BodegaRepositoryPort bodegaRepositoryPort;
	private final SiloRepositoryPort siloRepositoryPort;

	public BodegaUsecase(BodegaRepositoryPort bodegaRepositoryPort,
			SiloRepositoryPort siloRepositoryPort) {
		this.bodegaRepositoryPort = bodegaRepositoryPort;
		this.siloRepositoryPort=siloRepositoryPort;
	}
	
	public Bodega save(String nombre,Integer siloId) {
		Optional<Silo> silo=this.siloRepositoryPort.findById(siloId);
		if(silo.isPresent()) {
			Bodega bodega=new Bodega();
			bodega.setNombre(nombre);
			bodega.setSilo(silo.get());
			Bodega newBodega=this.bodegaRepositoryPort.save(bodega);
			return newBodega;
		}
		return null;
	}
	
	public Bodega update(Integer bodegaId,String nombre,Integer siloId) {
		Optional<Silo> silo=this.siloRepositoryPort.findById(siloId);
		if(silo.isPresent()) {
			Optional<Bodega> bodega=this.bodegaRepositoryPort.findById(bodegaId);
			if(bodega.isPresent()) {
				Bodega updateBodega=bodega.get();
				updateBodega.setNombre(nombre);
				updateBodega.setSilo(silo.get());
				Bodega newBodega=this.bodegaRepositoryPort.update(updateBodega);
				return newBodega;
			}

		}
		return null;
	}
	
	public Bodega findById(String codigo) {
		return this.bodegaRepositoryPort.findByCodigo(codigo);
	}
	
	public List<Bodega> findAll(){
		return this.bodegaRepositoryPort.findAll();
	}
	
	public List<Bodega> findBySilo(Integer siloId){
		return this.bodegaRepositoryPort.findBySilo(siloId);
	}
	
	public void delete(Integer id) {
		this.bodegaRepositoryPort.delete(id);
	}
}
