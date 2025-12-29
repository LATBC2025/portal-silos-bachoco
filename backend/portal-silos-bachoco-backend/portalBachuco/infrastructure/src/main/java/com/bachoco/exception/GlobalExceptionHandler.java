package com.bachoco.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bachoco.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UsuarioDuplicadoException.class)
	public ResponseEntity<ApiError> handleUsuarioNoEncontrado(UsuarioDuplicadoException ex,
			HttpServletRequest request) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:1001",  ex.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(CredencialesInvalidasException.class)
	public ResponseEntity<ApiError> handleCredencialesNovalidas(CredencialesInvalidasException ex,
			HttpServletRequest request) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:CREENTIALS-NOVALIDAS-1", ex.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(NotFoundException ex,
			HttpServletRequest request) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:SEND-C-001", ex.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(NotFoundPlantaDestinoException.class)
	public ResponseEntity<ApiError> handleNotFoundPlantaDestino(NotFoundPlantaDestinoException ex,
			HttpServletRequest request) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:NO-EXISTE-PLANTAS", ex.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(NotFoundMaterialException.class)
	public ResponseEntity<ApiError> handleNotFoundMateriales(NotFoundMaterialException ex,
			HttpServletRequest request) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:NO-EXISTE-MATERIALES", ex.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(NotFoundPedCompraException.class)
	public ResponseEntity<ApiError> handleNotFoundPedCompra(NotFoundPedCompraException ex,
			HttpServletRequest request) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:NO-EXISTE-PED-COMPRA", ex.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(NotFoundPasswordException.class)
	public ResponseEntity<ApiError> handleValorNoEncontrado(NotFoundPasswordException ex,
			HttpServletRequest request) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "no-existe-password", ex.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(RegistroNoCreadoException.class)
	public ResponseEntity<ApiError> handleException(RegistroNoCreadoException io,
			HttpServletRequest request){
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:EX-001", io.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(SapConnectionException.class)
	public ResponseEntity<ApiError> handleSapConnectionException(SapConnectionException io,
			HttpServletRequest request){
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:CON-SAP-01", io.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiError> handleAuthException(AuthenticationException ex,
			HttpServletRequest request) {
		String messageError="";
		String errorCode="AUTH_ERROR";
		if(ex instanceof BadCredentialsException) {
			messageError="Credenciales Invalidas";
		}
		if(ex instanceof UsernameNotFoundException) {
			messageError="Credenciales Invalidas";
		}
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:"+errorCode, messageError,
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(SendEmailException.class)
	public ResponseEntity<ApiError> SendMailOptException(SendEmailException ex,
			HttpServletRequest request) {
		String messageError="";
		String errorCode="SEND-MAIL-01";
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:"+errorCode, messageError,
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(ExpirationJwtException.class)
	public ResponseEntity<ApiError> handleExpriredException(ExpirationJwtException ex,
			HttpServletRequest request) {
		String messageError="";
		String errorCode="EXPIRED-001";
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:"+errorCode, messageError,
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	// Mapea tu excepción personalizada a una respuesta HTTP de error
    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<ApiError> handleSendEmailException(MailSendException ex,HttpServletRequest request) {
    	String messageError="";
		String errorCode="SEND-EMAIL-001";
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:"+errorCode, messageError,
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
	@ExceptionHandler(PasswordExpirationException.class)
	public ResponseEntity<ApiError> handleAuthException(PasswordExpirationException ex,
			HttpServletRequest request) {
		String messageError="";
		String errorCode="EXP-PWD";
		messageError="Su contraseña ha caducado";
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:"+errorCode, messageError,
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(CannotDeleteResourceException.class)
	public ResponseEntity<ApiError> handleIoCannotDeleteResourceException(CannotDeleteResourceException io,
			HttpServletRequest request){
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:violation-integration", io.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(CannotRegisterProgramArriboException.class)
	public ResponseEntity<ApiError> handleCannotRegisterProgramArriboException(CannotRegisterProgramArriboException io,
			HttpServletRequest request){
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:no-reg-program-01", io.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<ApiError> handleIoException(IOException io,
			HttpServletRequest request){
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:DOC-001", io.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex,HttpServletRequest request) {
    	ApiError error = new ApiError(HttpStatus.FORBIDDEN.value(), "error-code:no-auth-operation", ex.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleException(Exception io,
			HttpServletRequest request){
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "error-code:EX-001", io.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
}
