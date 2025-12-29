package com.bachoco.service;

import java.time.LocalDateTime;

public interface IGenerarYEnviarOtp {
	int ejecutar(Integer usuarioId,String username,String destino);
	int sendClaveByCorreo(String correo);
	int sendPasswordByCorreo(String password,String correo);
	void deleteOtp(Integer usuarioId,String username);
	int deleteOtpExpired(LocalDateTime cutoffTime);
}
