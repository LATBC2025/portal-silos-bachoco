package com.bachoco.service.usecase;

import java.util.List;

import com.bachoco.model.Puesto;
import com.bachoco.port.PuestoRepositoryPort;

public class PuestoUseCase {

	private final PuestoRepositoryPort puestoRepositoryPort;

	public PuestoUseCase(PuestoRepositoryPort puestoRepositoryPort) {
		this.puestoRepositoryPort = puestoRepositoryPort;
	}
	
	public List<Puesto> findAll(){
		return this.puestoRepositoryPort.findAll();
	}
	
}
