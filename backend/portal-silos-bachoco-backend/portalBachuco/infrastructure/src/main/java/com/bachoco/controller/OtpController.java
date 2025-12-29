package com.bachoco.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.service.usecase.GenerarYEnviarOtpUseCase;

@RestController
@RequestMapping("/v1/otp")
public class OtpController {

	private final GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase;

	public OtpController(GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase) {
		this.generarYEnviarOtpUseCase = generarYEnviarOtpUseCase;
	}
	
	@DeleteMapping("/{id}/{usuario}")
	public ResponseEntity<Void> deleteOpt(@PathVariable Integer id,@PathVariable String usuario){
		this.generarYEnviarOtpUseCase.deleteOtp(id,usuario);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	

}
