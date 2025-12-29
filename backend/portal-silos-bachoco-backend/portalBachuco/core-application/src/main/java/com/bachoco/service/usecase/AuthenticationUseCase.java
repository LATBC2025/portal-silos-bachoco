package com.bachoco.service.usecase;

public class AuthenticationUseCase {

	/*private final UsuarioRepositoryPort userRepositoryPort;
	private final PasswordEncoderPort passwordEncoder;
	private final TokenProviderPort tokenGeneratorPort;

	public AuthenticationUseCase(UsuarioRepositoryPort userRepositoryPort, PasswordEncoderPort passwordEncoder,
			TokenProviderPort tokenGeneratorPort) {
		this.userRepositoryPort = userRepositoryPort;
		this.passwordEncoder = passwordEncoder;
		this.tokenGeneratorPort = tokenGeneratorPort;
	}
	
	public Optional<String> authenticate(String username, String password) {
		Usuario usuario = userRepositoryPort.findByUsuario(username).orElseThrow(CredencialesInvalidasException::new);
		if (!passwordEncoder.matches(password, usuario.getPassword())) {
			throw new CredencialesInvalidasException();
		}
		this.tokenGeneratorPort.generateToken(usuario);
		return userRepositoryPort.findByUsuario(username).filter(user -> user.getUsuario().equals(username))
				.map(tokenGeneratorPort::generateToken);
	}
	public Boolean isExistPassword(String username) {
		var usuario = userRepositoryPort.findByUsuario(username);
		if (usuario.isEmpty()) {
			throw new CredencialesInvalidasException();
		}
		Usuario user=usuario.get();
		if (user.getPassword()==null || user.getPassword().trim().length()==0) {
			throw new NotFoundPasswordException("No existe el password");
		}
		return true;
	}
	
	public void createPassword(String username,String password) {
		
	}*/
}
