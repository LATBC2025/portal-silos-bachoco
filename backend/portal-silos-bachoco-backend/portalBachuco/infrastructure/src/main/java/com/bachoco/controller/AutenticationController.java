package com.bachoco.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.dto.AutenticationResponse;
import com.bachoco.dto.LoginRequest;
import com.bachoco.dto.OtpRequest;
import com.bachoco.persistence.adapter.AuthenticationAdapter;

@RestController
@RequestMapping("/v1/auth")
public class AutenticationController {

	private final AuthenticationAdapter authenticationAdapter;

	public AutenticationController(AuthenticationAdapter authenticationAdapter) {
		this.authenticationAdapter = authenticationAdapter;
	}
	@GetMapping("/login/ok")
	public ResponseEntity<Void> login() {
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AutenticationResponse> login(@RequestBody LoginRequest request) {
		String token = this.authenticationAdapter.authenticate(request.username(), request.password());
		AutenticationResponse response = new AutenticationResponse(token, request.username());
		return new ResponseEntity<AutenticationResponse>(response, HttpStatus.OK);
	}
	@PostMapping("/verity-otp")
	public ResponseEntity<AutenticationResponse> validateOtpAndGenerateToken(@RequestBody OtpRequest otpRequest){
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            userDetails.getUsername();
	    }
		String token=this.authenticationAdapter.verifyOtpAnGenerateToken(otpRequest.username(), otpRequest.otp());
		AutenticationResponse response= new AutenticationResponse(token,otpRequest.username());
		return new ResponseEntity<AutenticationResponse>(response,HttpStatus.OK);
	}
	@PostMapping("/createPassword")
	public ResponseEntity<?> createPassword(@RequestBody LoginRequest request) {
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
}
