package com.bachoco.port;

import java.util.List;
import java.util.Optional;

import com.bachoco.model.EmpleadoInterno;
import com.bachoco.model.EmpleadoInternoRequest;
import com.bachoco.model.EmpleadoInternoResponse;

public interface EmpleadoInternoRepositoryPort {
	
	//public EmpleadoInterno save(EmpleadoInterno empleadoInterno);
	public Optional<EmpleadoInterno> save(Integer empleadoId,Integer departamentoId,Integer puestoId);
	public void update(Integer id,EmpleadoInternoRequest request);
	public List<EmpleadoInternoResponse> findAll();
	public int save(EmpleadoInternoRequest request) throws Exception;
	public void delete(Integer id);
	public List<Integer> findAllIdEmpleadoBaja();

}
