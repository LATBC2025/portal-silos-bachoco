package com.bachoco.service.usecase;

import java.util.List;

import com.bachoco.model.Departamento;
import com.bachoco.port.DepartamentoRepositoryPort;

public class DepartamentoUseCase {
	private final DepartamentoRepositoryPort departamentoRepositoryPort;

	public DepartamentoUseCase(DepartamentoRepositoryPort departamentoRepositoryPort) {
		this.departamentoRepositoryPort = departamentoRepositoryPort;
	}

	public List<Departamento> findAll(){
		return this.departamentoRepositoryPort.findAll();
	}

}
