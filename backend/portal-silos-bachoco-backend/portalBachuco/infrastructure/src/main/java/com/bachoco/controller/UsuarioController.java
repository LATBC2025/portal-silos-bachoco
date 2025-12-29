package com.bachoco.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.dto.PasswordOtpRequest;
import com.bachoco.dto.UsuarioUpdatePasswordExpiredRequest;
import com.bachoco.dto.UsuarioUpdatePasswordRequest;
import com.bachoco.service.usecase.UsuarioUseCase;

@RestController
@RequestMapping("/v1/usuario")
public class UsuarioController {

	public final UsuarioUseCase usuarioUseCase;

	public UsuarioController(UsuarioUseCase usuarioUseCase) {
		this.usuarioUseCase = usuarioUseCase;
	}
	
	@PostMapping("/update-password")
	public ResponseEntity<Void> updatePassword(@RequestBody UsuarioUpdatePasswordRequest request){
		this.usuarioUseCase.updatePassword(request.username(),request.password());
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	@PostMapping("/send-clave-email")
	public ResponseEntity<Void> sendClaveByUsuario(@RequestBody PasswordOtpRequest request){
		this.usuarioUseCase.sendClaveOtpByUsuario(request.usuario());
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	@PutMapping("/update-password-expired")
	public ResponseEntity<Void> updatePasswordExprired(@RequestBody UsuarioUpdatePasswordExpiredRequest request){
		this.usuarioUseCase.updatePasswordExpired(request.username(),request.passwordActual(),request.nuevoPassword());
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
}
