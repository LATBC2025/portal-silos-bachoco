package com.bachoco.port;

import com.bachoco.model.Usuario;

public interface TokenProviderPort {
	String generateToken(Usuario usuario);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}
