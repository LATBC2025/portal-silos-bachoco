package com.bachoco.service;

import java.util.Optional;

import com.bachoco.model.Usuario;

public interface IAuthenticate {
	
	public Optional<Usuario> ejecutar(String username,String password);

}
