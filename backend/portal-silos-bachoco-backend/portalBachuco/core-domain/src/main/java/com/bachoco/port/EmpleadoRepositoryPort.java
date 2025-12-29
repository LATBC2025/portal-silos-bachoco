package com.bachoco.port;

import java.util.List;
import java.util.Optional;

import com.bachoco.model.Empleado;
import com.bachoco.model.EmpleadoInterno;
import com.bachoco.model.EmpleadoInternoRequest;
import com.bachoco.model.EmpleadoResponse;

public interface EmpleadoRepositoryPort {

	public Empleado save(Empleado empleado);
	public void update(Integer id,EmpleadoInternoRequest req);
	public Empleado findById(Integer id);
	public Optional<Empleado> findByCorreo(String correo);
	public Optional<String> getCorreo(Integer usuarioId);
	public List<EmpleadoInterno> findAll();
	public Optional<EmpleadoResponse> findByClaveUsuarioOrCorreo(String value);
}
