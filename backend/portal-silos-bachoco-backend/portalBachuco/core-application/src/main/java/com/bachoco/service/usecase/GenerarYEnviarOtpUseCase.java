package com.bachoco.service.usecase;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bachoco.exception.SendEmailException;
import com.bachoco.model.EmailMensaje;
import com.bachoco.model.Otp;
import com.bachoco.port.EmailSender;
import com.bachoco.port.EmpleadoRepositoryPort;
import com.bachoco.port.OtpRepository;
import com.bachoco.service.IGenerarYEnviarOtp;
import com.bachoco.service.IOtpGenerator;

public class GenerarYEnviarOtpUseCase implements IGenerarYEnviarOtp {

	private final OtpRepository otpRepository;
	private final EmailSender emailSender;
	private final IOtpGenerator optGenerator;
	private final EmpleadoRepositoryPort empleadoRepositoryPort;

	public GenerarYEnviarOtpUseCase(OtpRepository otpRepository, EmailSender emailSender, IOtpGenerator otpGenerator,
			EmpleadoRepositoryPort empleadoRepositoryPort) {
		this.otpRepository = otpRepository;
		this.emailSender = emailSender;
		this.optGenerator = otpGenerator;
		this.empleadoRepositoryPort = empleadoRepositoryPort;
	}

	@Override
	public int ejecutar(Integer usuarioId, String username, String destino) {
		Optional<String> correo = this.empleadoRepositoryPort.getCorreo(usuarioId);
		int response = -1;
		if (correo.isPresent()) {
			try {
				String codigo = optGenerator.generarCodigo();
				Otp otp = new Otp(username, codigo, LocalDateTime.now().plusMinutes(3), false);
				otpRepository.save(otp);
				EmailMensaje email = new EmailMensaje(correo.get(), "Codigo OTP", "El código de acceso es: " + codigo);
				response = emailSender.enviar(email);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SendEmailException("Hubo un error en el envio de correo del doble factor de autenticacion");
			}
		}
		return response;
	}

	@Override
	public void deleteOtp(Integer usuarioId, String username) {
		this.otpRepository.deleteOtp(usuarioId, username);
	}

	@Override
	public int deleteOtpExpired(LocalDateTime cutoffTime) {
		return this.otpRepository.deleteExpired(cutoffTime);
	}

	@Override
	public int sendClaveByCorreo(String correo) {
		int response = -1;
		try {
			String codigo = optGenerator.generarCodigo();
			Otp otp = new Otp(correo, codigo, LocalDateTime.now().plusMinutes(3), false);
			otpRepository.save(otp);
			EmailMensaje email = new EmailMensaje(correo, "Codigo OTP", "El código de acceso es: " + codigo);
			response = emailSender.enviar(email);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SendEmailException("Hubo un error en el envio de correo del doble factor de autenticacion");
		}
		return response;
	}

	@Override
	public int sendPasswordByCorreo(String password, String correo) {
		int response = -1;
		try {
			EmailMensaje email = new EmailMensaje(correo, "CONTRASEÑA INICIAL", "Su contraseña de acceso inicial para el Portal Silos Bachoco es: : " +password);
			response = emailSender.enviar(email);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SendEmailException("Hubo un error en el envio de correo con la contraseña incial ");
		}
		return response;
	}

}
