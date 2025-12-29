package com.bachoco.service.impl;

import java.security.SecureRandom;

import com.bachoco.service.IOtpGenerator;

public class OtpGeneratorImpl implements IOtpGenerator {

	private static final int KEY_LENGTH = 4;
	private final SecureRandom secureRandom = new SecureRandom();

	@Override
	public String generarCodigo() {
		// return String.format("%06d",new Random().nextInt(999999));
		StringBuilder key = new StringBuilder(KEY_LENGTH);
		for (int i = 0; i < KEY_LENGTH; i++) {
			key.append(secureRandom.nextInt(10));
		}
		return key.toString();
	}

}
