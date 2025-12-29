package com.bachoco.security;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.bachoco.model.Usuario;
import com.bachoco.port.UsuarioRepositoryPort;

@Component
public class CustomUserDetailsService implements UserDetailsService{

	private final UsuarioRepositoryPort usuarioRepositoryPort;
	
	public CustomUserDetailsService(UsuarioRepositoryPort usuarioRepositoryPort) {
		this.usuarioRepositoryPort = usuarioRepositoryPort;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepositoryPort.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		if(usuario.getRoles()!=null) {
			var roles=usuario.getRoles().stream()
	                .map(role -> role)
	                .collect(Collectors.joining(","));
	        return org.springframework.security.core.userdetails.User.builder()
	                .username(usuario.getUsuario())
	                .password(usuario.getPassword())
	                .roles(roles)
	                .build();
		}else {
	        return org.springframework.security.core.userdetails.User.builder()
	                .username(usuario.getUsuario())
	                .password(usuario.getPassword())
	                .build();
		}
	}

}
