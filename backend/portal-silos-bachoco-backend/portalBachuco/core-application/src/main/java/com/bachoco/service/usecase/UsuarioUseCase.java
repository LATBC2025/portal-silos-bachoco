package com.bachoco.service.usecase;

import java.util.Optional;

import com.bachoco.exception.NotFoundException;
import com.bachoco.exception.SendEmailException;
import com.bachoco.exception.UsuarioDuplicadoException;
import com.bachoco.model.Empleado;
import com.bachoco.model.Usuario;
import com.bachoco.port.EmpleadoRepositoryPort;
import com.bachoco.port.UsuarioRepositoryPort;

public class UsuarioUseCase {

	private final UsuarioRepositoryPort usuarioRepositoryPort;
	private final EmpleadoRepositoryPort empleadoRepositoryPort;
	private final GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase;
	

	public UsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort, EmpleadoRepositoryPort empleadoRepositoryPort,
			GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase) {
		this.usuarioRepositoryPort = usuarioRepositoryPort;
		this.empleadoRepositoryPort = empleadoRepositoryPort;
		this.generarYEnviarOtpUseCase = generarYEnviarOtpUseCase;
	}
	
	public Usuario save(Usuario usuario) {
		var result = this.usuarioRepositoryPort.findByUsuario(usuario.getUsuario());
		if (result.isPresent() && result.get().getId() != null) {
			throw new UsuarioDuplicadoException("El usuario ya existe");
		}
		return this.usuarioRepositoryPort.save(usuario);
	}
	public void updatePassword(String username,String password) {
		this.usuarioRepositoryPort.updatePassword(username, password);
	}
	public void sendClaveOtpByUsuario(String usuario) {
		Optional<Empleado> emp = this.empleadoRepositoryPort.findByCorreo(usuario);
		if (!emp.isPresent()) {
			throw new NotFoundException("El usuario no existe");
		}
		int responseEnvio=this.generarYEnviarOtpUseCase.sendClaveByCorreo(emp.get().getCorreo());
		if(responseEnvio==-1) {
			throw new SendEmailException("Hubo un error en el envio de correo del doble factor de autenticacion");
		}
	}
	public void updatePasswordExpired(String username,String passwordActual,String nuevoPassword) {
		this.usuarioRepositoryPort.updatePasswordExprired(username, passwordActual,nuevoPassword);
	}
}
