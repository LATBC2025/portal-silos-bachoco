package com.bachoco.service.usecase;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import com.bachoco.exception.RegistroNoCreadoException;
import com.bachoco.exception.UsuarioDuplicadoException;
import com.bachoco.model.EmpleadoExternoRequest;
import com.bachoco.model.EmpleadoExternoResponse;
import com.bachoco.port.EmpleadoExternoRepositoryPort;
import com.bachoco.port.UsuarioRepositoryPort;

public class EmpleadoExternoUseCase {
	
	private final UsuarioRepositoryPort usuarioRepositoryPort;
	private final EmpleadoExternoRepositoryPort empExternoRepositoryPort;
	private final GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase;

	public EmpleadoExternoUseCase(UsuarioRepositoryPort usuarioRepositoryPort,
			EmpleadoExternoRepositoryPort empExternoRepositoryPort, GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase) {
		this.usuarioRepositoryPort = usuarioRepositoryPort;
		this.empExternoRepositoryPort = empExternoRepositoryPort;
		this.generarYEnviarOtpUseCase = generarYEnviarOtpUseCase;
	}

	public EmpleadoExternoResponse save(EmpleadoExternoRequest req) {
		Optional<EmpleadoExternoResponse> result=this.empExternoRepositoryPort.findByCorreo(req.getCorreo());
		EmpleadoExternoResponse r= new EmpleadoExternoResponse();
		if(result.isPresent()) {
			throw new UsuarioDuplicadoException("Se suplica el correo");
		}
		try {
			Optional<EmpleadoExternoResponse> emp=this.empExternoRepositoryPort.save(req);
			if(emp.isPresent() && emp.get().getId()!=null) {
				String passwordDeafault=generateDefaultPassword(req.getCorreo());
				this.generarYEnviarOtpUseCase.sendPasswordByCorreo(passwordDeafault,req.getCorreo());
				this.usuarioRepositoryPort.addPasswordDefault(passwordDeafault,req.getCorreo(),emp.get().getId());
				return emp.get();
			}else {
				return r;
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RegistroNoCreadoException("Hubo error al crear el registro");
		}
	}

	public List<EmpleadoExternoResponse> findAll(){
		return this.empExternoRepositoryPort.findAll();
	}
	
	public void delete(Integer id) {
		this.empExternoRepositoryPort.delete(id);
	}
	
	public void update(Integer id,EmpleadoExternoRequest req) {
		this.empExternoRepositoryPort.update(id, req);
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
