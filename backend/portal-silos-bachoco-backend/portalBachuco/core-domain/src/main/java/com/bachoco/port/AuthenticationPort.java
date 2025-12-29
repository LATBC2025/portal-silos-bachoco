package com.bachoco.port;


public interface AuthenticationPort {

	String authenticate(String username,String password);
	String verifyOtpAnGenerateToken(String username,String otp);
	Boolean passwordExpiration(String username);
}
