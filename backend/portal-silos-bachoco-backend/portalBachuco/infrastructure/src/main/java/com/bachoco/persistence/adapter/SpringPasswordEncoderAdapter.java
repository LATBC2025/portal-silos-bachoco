package com.bachoco.persistence.adapter;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bachoco.port.PasswordEncoderPort;

@Component
public class SpringPasswordEncoderAdapter implements PasswordEncoderPort {

	private final PasswordEncoder passwordEncoder;
	
	public SpringPasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public String encode(String password) {
		return passwordEncoder.encode(password);
	}

	@Override
	public boolean matches(String password, String passwordEncoded) {
		return passwordEncoder.matches(password, passwordEncoded);
	}

}
