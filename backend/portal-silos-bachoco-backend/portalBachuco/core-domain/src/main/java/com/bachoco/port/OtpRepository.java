package com.bachoco.port;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bachoco.model.Otp;

public interface OtpRepository {
	void save(Otp otp);
	Optional<Otp> findByUsuarioIdAndCodigo(String usuarioId, String codigo);
	Optional<Otp> findByUsuario(String usuario);
	void markAsUsed(Otp otp);
	int deleteExpired(LocalDateTime cutoffTime);
	void deleteOtp(Integer usuarioId,String username);
}
