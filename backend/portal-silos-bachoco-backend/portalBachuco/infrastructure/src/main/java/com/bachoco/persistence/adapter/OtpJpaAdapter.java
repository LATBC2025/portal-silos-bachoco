package com.bachoco.persistence.adapter;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bachoco.model.Otp;
import com.bachoco.persistence.entity.OtpEntity;
import com.bachoco.persistence.repository.OtpJpaRepository;
import com.bachoco.port.OtpRepository;

@Repository
public class OtpJpaAdapter implements OtpRepository{
	
	private final OtpJpaRepository otpJpaRepository;

	public OtpJpaAdapter(OtpJpaRepository otpJpaRepository) {
		this.otpJpaRepository = otpJpaRepository;
	}

	@Override
	public void save(Otp otp) {
		otpJpaRepository.save(fromDomain(otp));	
	}

	@Override
	public Optional<Otp> findByUsuarioIdAndCodigo(String usuarioId, String codigo) {
		return Optional.empty();
	}
	
	@Override
	public Optional<Otp> findByUsuario(String usuario) {
		Optional<OtpEntity> otp=this.otpJpaRepository.findLatestByUsuario(usuario);
		if(otp.isPresent()) {
			return Optional.ofNullable(toDomain(otp.get()));
		}
		return Optional.empty();
	}

	@Transactional
	@Override
	public void deleteOtp(Integer usuarioId,String username) {
		Optional<OtpEntity> otp=this.otpJpaRepository.findLatestByUsuario(username);
		if(otp.isPresent()) {
			this.otpJpaRepository.delete(otp.get());
		}
	}
	
	@Override
	public void markAsUsed(Otp otp) {
		
	}

	@Override
	public int deleteExpired(LocalDateTime cutoffTime) {
		return this.otpJpaRepository.deleteByExpiracionBefore(cutoffTime);
	}
	
    public OtpEntity fromDomain(Otp otp) {
        OtpEntity entity = new OtpEntity();
        entity.setUsuario(otp.getUsuarioId());
        entity.setCodigo(otp.getCodigo());
        entity.setExpiracion(otp.getExpiracion());
        return entity;
    }

    public Otp toDomain(OtpEntity entity) {
        return new Otp(entity.getUsuario(), entity.getCodigo(), entity.getExpiracion(),false);
    }

}
