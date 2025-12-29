package com.bachoco.service.usecase;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import com.bachoco.exception.PersistenceException;
import com.bachoco.exception.RegistroNoCreadoException;
import com.bachoco.exception.UsuarioDuplicadoException;
import com.bachoco.model.Empleado;
import com.bachoco.model.EmpleadoInterno;
import com.bachoco.model.EmpleadoInternoRequest;
import com.bachoco.model.EmpleadoInternoResponse;
import com.bachoco.model.EmpleadoResponse;
import com.bachoco.port.EmpleadoInternoRepositoryPort;
import com.bachoco.port.EmpleadoRepositoryPort;
import com.bachoco.port.PerfilRepositoryPort;
import com.bachoco.port.UsuarioRepositoryPort;

public class EmpleadoUseCase {

	private final UsuarioRepositoryPort usuarioRepositoryPort;
	private final EmpleadoRepositoryPort empleadoRepositoryPort;
	private final EmpleadoInternoRepositoryPort empleadoInternoRepositoryPort;
	private final PerfilRepositoryPort perfilRepositoryPort;
	private final GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase;

	public EmpleadoUseCase(UsuarioRepositoryPort usuarioRepositoryPort, EmpleadoRepositoryPort empleadoRepositoryPort,
			EmpleadoInternoRepositoryPort empleadoInternoRepositoryPort,PerfilRepositoryPort perfilRepositoryPort,
			GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase) {
		this.usuarioRepositoryPort = usuarioRepositoryPort;
		this.empleadoRepositoryPort = empleadoRepositoryPort;
		this.empleadoInternoRepositoryPort = empleadoInternoRepositoryPort;
		this.perfilRepositoryPort=perfilRepositoryPort;
		this.generarYEnviarOtpUseCase=generarYEnviarOtpUseCase;
	}

	public Empleado crearEmpleadoConUsuario(Empleado empleado) {
		Empleado emp = new Empleado();
		var resultEmpelado = this.empleadoRepositoryPort.findByCorreo(empleado.getCorreo());
		if (resultEmpelado.isPresent() && resultEmpelado.get().getId() != null) {
			throw new UsuarioDuplicadoException("El correo ya existe");
		}
		empleado.setUsuario(empleado.getUsuario());
		emp = this.empleadoRepositoryPort.save(empleado);
		return emp;
	}

	public void update(Integer id,EmpleadoInternoRequest req) {
		this.empleadoInternoRepositoryPort.update(id, req);
	}
	public EmpleadoInterno asignarDepartamentoConPuestoInterno(Integer empleadoId, Integer departamentoId,
			Integer puestoId) {
		return this.empleadoInternoRepositoryPort.save(empleadoId, departamentoId, puestoId).get();
	}

	public void asinadPerfilAUsuario(Integer usuarioId,Integer perfilId) {
		try{
			this.usuarioRepositoryPort.addUsuatioToPerfil(usuarioId, perfilId);
		}catch (Exception e) {
			throw new PersistenceException("No se pudo asignar el perfil al usuario");
		}
	}
	
	public List<EmpleadoInterno> findAll(){
		return this.empleadoRepositoryPort.findAll();
	}
	
	public List<EmpleadoInternoResponse> findAllEmpleadoResponse(){
		return this.empleadoInternoRepositoryPort.findAll();
	}
	
	public void saveEmpleadoInterno(EmpleadoInternoRequest req) {
		var resultEmpelado = this.empleadoRepositoryPort.findByCorreo(req.getCorreo());
		if (resultEmpelado.isPresent() && resultEmpelado.get().getId() != null) {
			throw new UsuarioDuplicadoException("El correo ya existe");
		}
		try {
			int idEmpleado=this.empleadoInternoRepositoryPort.save(req);
			if(idEmpleado!=-1) {
				String passwordDeafault=generateDefaultPassword(req.getCorreo());
				this.generarYEnviarOtpUseCase.sendPasswordByCorreo(passwordDeafault,req.getCorreo());
				this.usuarioRepositoryPort.addPasswordDefault(passwordDeafault,req.getCorreo(), idEmpleado);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RegistroNoCreadoException("Hubo error al crear el registro");
		}
	}
	
	public Optional<EmpleadoResponse> findByUsuarioOrCorreo(String value){
		return this.empleadoRepositoryPort.findByClaveUsuarioOrCorreo(value);
	}
	
	public void delete(Integer id) {
		this.empleadoInternoRepositoryPort.delete(id);
	}
	
	public static String generateDefaultPassword(String baseString) {
        String firstSixChars;
        if (baseString == null || baseString.isEmpty()) {
            firstSixChars = "";
        } else {
            // Usa Math.min para asegurar que no se exceda la longitud de la cadena.
            firstSixChars = baseString.substring(0, Math.min(baseString.length(), 6));
        }
        SecureRandom secureRandom = new SecureRandom();
        int randomFourDigits = secureRandom.nextInt(10000);
        String randomKey = String.format("%04d", randomFourDigits);
        return firstSixChars + randomKey;
    }
}
