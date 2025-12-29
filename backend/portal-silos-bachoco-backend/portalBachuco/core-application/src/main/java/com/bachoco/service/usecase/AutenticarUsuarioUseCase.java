package com.bachoco.service.usecase;

import java.util.Optional;

import com.bachoco.model.Usuario;
import com.bachoco.model.service.AuthenticationService;
import com.bachoco.service.IAuthenticate;

public class AutenticarUsuarioUseCase implements IAuthenticate{

	private final AuthenticationService authenticationService;

	public AutenticarUsuarioUseCase(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
	
	@Override
	public Optional<Usuario> ejecutar(String username, String password) {
		return this.authenticationService.authenticate(username, password);
	}
}
