package com.bachoco.secutiry.utils;

import java.util.Arrays;
import java.util.List;

public class SecurityTextUtils {

	// VARIABLES DE USUARIO DEFAULT
	public static List<String> ROL_DEFAULT_USER = Arrays.asList("USER");
	public static final String[] WHITE_LIST_URL = { "/api/sca/usuario/auth/**", "/swagger-ui/**", "/v3/api-docs/**",
			"/swagger-ui.html" };
	public static final String[] DEFAULT_URL = { "/**" };
	public static final String[] WHITE_LIST_URL_FRONT_ACCESS = {"http://localhost:19083"};
	public static final String AUTHORIZATION = "Authorization";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String[] PETITION_METHOD = { "GET", "POST", "PUT", "DELETE", "OPTIONS" };
}
