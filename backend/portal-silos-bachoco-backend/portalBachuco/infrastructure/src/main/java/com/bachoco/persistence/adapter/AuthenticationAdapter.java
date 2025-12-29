package com.bachoco.persistence.adapter;

import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bachoco.exception.CredencialesInvalidasException;
import com.bachoco.exception.NotFoundPasswordException;
import com.bachoco.exception.OtpInvalidException;
import com.bachoco.exception.PasswordExpirationException;
import com.bachoco.exception.SendEmailException;
import com.bachoco.model.AuthenticationResponse;
import com.bachoco.persistence.repository.jdbc.CatalogJdbcRepository;
import com.bachoco.port.AuthenticationPort;
import com.bachoco.port.OtpRepository;
import com.bachoco.port.PasswordEncoderPort;
import com.bachoco.security.CustomUserDetailsService;
import com.bachoco.security.JwtSecurityService;
import com.bachoco.secutiry.utils.PasswordExpirationValidator;
import com.bachoco.service.usecase.GenerarYEnviarOtpUseCase;
@Component
public class AuthenticationAdapter implements AuthenticationPort {
	
	private final JwtSecurityService jwtSecurityService;
	private final GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase;
	private final OtpRepository otpRepository;
	private final CustomUserDetailsService customUserDetailsService;
	private final CatalogJdbcRepository catalogJdbcRepository;
	private final PasswordEncoderPort passwordEncoderPort;
	private String ROL_PENDING_2FA="2FA_PENDING";
	


	public AuthenticationAdapter(JwtSecurityService jwtSecurityService,
			GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase, OtpRepository otpRepository,
			CustomUserDetailsService customUserDetailsService, CatalogJdbcRepository catalogJdbcRepository,
			PasswordEncoderPort passwordEncoderPort) {
		this.jwtSecurityService = jwtSecurityService;
		this.generarYEnviarOtpUseCase = generarYEnviarOtpUseCase;
		this.otpRepository = otpRepository;
		this.customUserDetailsService = customUserDetailsService;
		this.catalogJdbcRepository = catalogJdbcRepository;
		this.passwordEncoderPort = passwordEncoderPort;
	}

	@Override
	public String authenticate(String username, String password) {
		Optional<AuthenticationResponse> auth = this.catalogJdbcRepository.authResponse(username);
		if (auth.isEmpty()) {
			throw new CredencialesInvalidasException();
		}
		AuthenticationResponse user=auth.get();
		if (user.getUltimoModifiPwd()==null && user.getPassword().trim().length()!=0) {
			if(this.passwordEncoderPort.matches(password, user.getPassword())) {
				throw new NotFoundPasswordException("No existe el password");
			}else {
				throw new CredencialesInvalidasException();
			}
		}
		if(!this.passwordEncoderPort.matches(password, user.getPassword())) {
			throw new CredencialesInvalidasException();
		}
		if(user.getUltimoModifiPwd()!=null) {
			boolean isExprirePwd=PasswordExpirationValidator.hasExpired(user.getUltimoModifiPwd());
			if(isExprirePwd) {
				throw new PasswordExpirationException();
			}
		}
		int responseEnvio=this.generarYEnviarOtpUseCase.ejecutar(user.getUsuarioId(),user.getUsuario(), user.getCorreo());
		if(responseEnvio==-1) {
			throw new SendEmailException("Hubo un error en el envio de correo del doble factor de autenticacion");
		}
		String token=this.jwtSecurityService.generateToken2FA(username,ROL_PENDING_2FA);
		return token;
	}

	@Override
	public String verifyOtpAnGenerateToken(String username, String otp) {
		var resultOtp = otpRepository.findByUsuario(username);
		if(resultOtp.isPresent() && resultOtp.get().getCodigo().equals(otp)) {
			this.otpRepository.deleteOtp(-1,username);
			 UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
			 Authentication authentication = new UsernamePasswordAuthenticationToken(
		                userDetails, null, userDetails.getAuthorities()
		            );
			 SecurityContextHolder.getContext().setAuthentication(authentication);
			return this.jwtSecurityService.generateToken(authentication);
		}else {
			throw new OtpInvalidException("Clave Otp Invalido");
		}
		
	}

	@Override
	public Boolean passwordExpiration(String username) {
		return null;
	}

}
