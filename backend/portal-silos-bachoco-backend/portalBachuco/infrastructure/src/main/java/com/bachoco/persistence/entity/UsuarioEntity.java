package com.bachoco.persistence.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TC_USUARIO")
@Entity(name="usuario")
public class UsuarioEntity {
	
	@Id
	@Column(name="USUARIO_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="USUARIO_TIPO")
	private String tipoUsuario;
	@Column(name="USUARIO")
	private String usuario;
	@Column(name="USUARIO_PASSWORD")
	private String password;
	@Column(name="USUARIO_ESTATUS")
	private Integer estatus;
	@Column(name="ULTIMA_MOD_PWD")
	private LocalDateTime ultimoModPwd;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tc_usuarioperfil", joinColumns = @JoinColumn(name = "USUARIO_ID"), inverseJoinColumns = @JoinColumn(name = "PERFIL_ID"))
	private Set<PerfilEntity> perfiles= new HashSet<>();

}
