package com.bachoco.port;

import java.util.List;
import java.util.Optional;

import com.bachoco.model.Usuario;

public interface UsuarioRepositoryPort {

	public Usuario save(Usuario usuario);
	public Usuario update(Usuario usuario);
	public List<Usuario> findAllByUsuario(String usuario);
	public Optional<Usuario> findByUsuario(String usuario);
	void updatePassword(String username, String password);
	void updatePasswordExprired(String username,String passwordActual, String nuevoPassword);
	Optional<Usuario> findByUsername(String username);
	void addUsuatioToPerfil(Integer usuarioId,Integer perfilId);
	void addPasswordDefault(String password,String usuario,int empleadoId);
}
