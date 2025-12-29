package com.bachoco.port;

import java.util.List;

import com.bachoco.model.Departamento;

public interface DepartamentoRepositoryPort {

	public Departamento findById(Integer id);
	public List<Departamento> findAll();
}
