package com.bachoco.port;

import java.util.List;

import com.bachoco.model.Puesto;

public interface PuestoRepositoryPort {
	
	public Puesto findById(Integer id);
	public List<Puesto> findAll();

}
