package com.bachoco.persistence.adapter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bachoco.exception.CredencialesInvalidasException;
import com.bachoco.model.AuthenticationResponse;
import com.bachoco.model.Perfil;
import com.bachoco.model.Usuario;
import com.bachoco.persistence.entity.EmpleadoEntity;
import com.bachoco.persistence.entity.PerfilEntity;
import com.bachoco.persistence.entity.UsuarioEntity;
import com.bachoco.persistence.repository.EmpleadoJpaRepository;
import com.bachoco.persistence.repository.PerfilJpaRepository;
import com.bachoco.persistence.repository.UsuarioJpaRepository;
import com.bachoco.persistence.repository.jdbc.CatalogJdbcRepository;
import com.bachoco.port.PasswordEncoderPort;
import com.bachoco.port.UsuarioRepositoryPort;

@Repository
public class UsuarioJpaRepositoryAdapter implements UsuarioRepositoryPort {

	private final UsuarioJpaRepository usuarioJpaRepository;
	private final PasswordEncoder passwordEncoder;
	private final PerfilJpaRepository perfilJpaRepository;
	private final EmpleadoJpaRepository empleadoJpaRepository;
	private final CatalogJdbcRepository catalogJdbcRepository;
	private final PasswordEncoderPort passwordEncoderPort;

	public UsuarioJpaRepositoryAdapter(UsuarioJpaRepository usuarioJpaRepository, PasswordEncoder passwordEncoder,
			PerfilJpaRepository perfilJpaRepository, EmpleadoJpaRepository empleadoJpaRepository,
			CatalogJdbcRepository catalogJdbcRepository, PasswordEncoderPort passwordEncoderPort) {
		this.usuarioJpaRepository = usuarioJpaRepository;
		this.passwordEncoder = passwordEncoder;
		this.perfilJpaRepository = perfilJpaRepository;
		this.empleadoJpaRepository = empleadoJpaRepository;
		this.catalogJdbcRepository = catalogJdbcRepository;
		this.passwordEncoderPort = passwordEncoderPort;
	}

	@Override
	public Usuario save(Usuario usuario) {
		return toDomain(this.usuarioJpaRepository.save(toEntity(usuario)));
	}

	@Transactional
	@Override
	public Usuario update(Usuario usuario) {
		Usuario usuarioResponse=new Usuario();
		try {
			var empleadoEntity=this.empleadoJpaRepository.searchByUsuaurioOrCorreo(usuario.getUsuario());
			if(empleadoEntity.isPresent() && empleadoEntity.get().getId()!=null) {
				if(empleadoEntity.get().getUsuario()!=null) {
					UsuarioEntity userEntity=empleadoEntity.get().getUsuario();
					userEntity.setUsuario(usuario.getUsuario());
					usuarioResponse=toDomain(userEntity);
				}
			}
		}catch (NoSuchElementException e1) {
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}	
		return usuarioResponse;
	}

	@Override
	public List<Usuario> findAllByUsuario(String usuario) {
			List<UsuarioEntity> entity=this.usuarioJpaRepository.findAll();
			if(entity!=null) {
				return entity.stream().map(i->toDomain(i)).collect(Collectors.toList());
			}
			return Collections.emptyList();
	
	}

	@Override
	public Optional<Usuario> findByUsuario(String usuario) {
		try {
			var auth=this.catalogJdbcRepository.authResponse(usuario);
			if(auth.isPresent()) {
				return Optional.ofNullable(toDomain(auth.get()));
			}
		}catch (NoSuchElementException e1) {
			return Optional.empty();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}	
		return Optional.empty();
	}
	
	@Transactional
	@Override
	public void updatePassword(String username, String password) {
		try {
			Optional<EmpleadoEntity> empleadoEntity=this.empleadoJpaRepository.searchByUsuaurioOrCorreo(username);
			if(empleadoEntity.isPresent() && (empleadoEntity.get().getUsuario().getPassword()==null || empleadoEntity.get().getUsuario().getPassword().trim().length()==0)) {
				LocalDateTime ahora = LocalDateTime.now();
				UsuarioEntity userEntity=empleadoEntity.get().getUsuario();
				userEntity.setPassword(this.passwordEncoder.encode(password));
				userEntity.setUltimoModPwd(ahora);
				this.usuarioJpaRepository.save(userEntity);
			}else if(empleadoEntity.isPresent() && (empleadoEntity.get().getUsuario().getPassword()!=null || empleadoEntity.get().getUsuario().getPassword().trim().length()!=0)) {
				LocalDateTime ahora = LocalDateTime.now();
				UsuarioEntity userEntity=empleadoEntity.get().getUsuario();
				userEntity.setPassword(this.passwordEncoder.encode(password));
				userEntity.setUltimoModPwd(ahora);
				this.usuarioJpaRepository.save(userEntity);
			}
		}catch (NoSuchElementException e1) {
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	@Transactional
	@Override
	public void addPasswordDefault(String password, String usuario, int empleadoId) {
		try {
			Optional<EmpleadoEntity> empleadoEntity=this.empleadoJpaRepository.searchByUsuaurioOrCorreo(usuario);
			if(empleadoEntity.isPresent() && (empleadoEntity.get().getUsuario()!=null)) {
				UsuarioEntity userEntity=empleadoEntity.get().getUsuario();
				userEntity.setPassword(this.passwordEncoder.encode(password));
				this.usuarioJpaRepository.save(userEntity);
			}
		}catch (NoSuchElementException e1) {
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Transactional
	@Override
	public void updatePasswordExprired(String username, String passwordActual, String nuevoPassword) {
		try {
			Optional<EmpleadoEntity> empleadoEntity=this.empleadoJpaRepository.searchByUsuaurioOrCorreo(username);
			if(empleadoEntity.isPresent() && (empleadoEntity.get().getUsuario().getPassword()!=null)) {
				if(!this.passwordEncoderPort.matches(passwordActual,empleadoEntity.get().getUsuario().getPassword())) {
					throw new CredencialesInvalidasException();
				}
				LocalDateTime ahora = LocalDateTime.now();
				UsuarioEntity userEntity=empleadoEntity.get().getUsuario();
				userEntity.setPassword(this.passwordEncoder.encode(nuevoPassword));
				userEntity.setUltimoModPwd(ahora);
				this.usuarioJpaRepository.save(userEntity);
			}
		}catch (NoSuchElementException e1) {
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}	
		
	}

	@Override
	public Optional<Usuario> findByUsername(String username) {
		return Optional.empty();
	}
	
	@Transactional
	@Override
	public void addUsuatioToPerfil(Integer usuarioId, Integer perfilId) {
		Optional<PerfilEntity> perfil=this.perfilJpaRepository.findById(perfilId);
		if(perfil.isPresent()) {
			Optional<UsuarioEntity> usuario=this.usuarioJpaRepository.findById(usuarioId);
			if(usuario.isPresent()) {
				Set<PerfilEntity> listaPerfiles= new HashSet<>();
				listaPerfiles.add(perfil.get());
				UsuarioEntity entity=usuario.get();
				entity.setPerfiles(listaPerfiles);
				this.usuarioJpaRepository.save(entity);
			}
		}
	}
	
	private UsuarioEntity toEntity(Usuario usuario) {
		return new UsuarioEntity()
				.builder()
				.tipoUsuario(usuario.getUsuario())
				.usuario(usuario.getUsuario())
				.estatus(0)
				.tipoUsuario("1")
				.build();
	}
	
	private Usuario toDomain(AuthenticationResponse auth) {
		Usuario usuario= new Usuario();
		usuario.setId(auth.getUsuarioId());
		usuario.setUsuarioTipo(auth.getUsuarioTipo().toString());
		usuario.setActivo(auth.getUsuarioActivo());
		usuario.setPassword(auth.getPassword());
		usuario.setActivo(auth.getUsuarioTipo());
		usuario.setUsuario(auth.getUsuario());
		usuario.setUsuarioTipo(auth.getUsuarioTipo().toString());
		usuario.setRoles(auth.getRoles());
		return usuario;
	}

	private Usuario toDomain(UsuarioEntity entity) {
		Usuario usuario= new Usuario();
		Set<Perfil> listPerfiles= new HashSet<>();
		usuario.setId(entity.getId());
		if(entity.getUsuario()!=null) {
			usuario.setUsuario(entity.getUsuario());
		}
		usuario.setUsuarioTipo(entity.getTipoUsuario());
		usuario.setActivo(entity.getEstatus());
		if(entity.getUltimoModPwd()!=null) {
			usuario.setUltimoModPwd(entity.getUltimoModPwd());
		}
		if(entity.getPerfiles()!=null && entity.getPerfiles().size()>0) {
			entity.getPerfiles().stream().forEach(i->listPerfiles.add(toDomainPerfil(i)));
			usuario.setPerfiles(listPerfiles);
		}
		if(entity.getPassword()!=null) {
			usuario.setPassword(entity.getPassword());
		}
		return usuario;
	}
	
	private Perfil toDomainPerfil(PerfilEntity perfilEntity) {
		return new Perfil(perfilEntity.getId(),perfilEntity.getClave(),perfilEntity.getDescripcion(),new HashSet<>());
	}
	
}
