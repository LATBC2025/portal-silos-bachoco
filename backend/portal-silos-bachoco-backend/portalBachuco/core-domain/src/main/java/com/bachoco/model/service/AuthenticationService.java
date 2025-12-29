package com.bachoco.model.service;

import java.util.Optional;

import com.bachoco.model.Usuario;

public interface AuthenticationService {

	Optional<Usuario> authenticate(String username,String password); 
}
