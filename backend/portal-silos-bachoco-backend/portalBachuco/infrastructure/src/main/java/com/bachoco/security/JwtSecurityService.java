package com.bachoco.security;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtSecurityService {

	private long jwtExpiration = 7200000;//
	private long refreshExpiration = 604800000;
	private String secretKey="tJAnZ6euwYpWh/6Kx4PQoPyIW0IDtzDFU0Wgys5rN0I=";
	
	public String generateToken2FA(String username,String perfil) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", perfil);
		return Jwts.builder().setClaims(claims).setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000)).signWith(getSignInKey()).compact();
	}

	public String generateToken(Authentication authentication) {
		Map<String, Object> claims = new HashMap<>();
		String roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		claims.put("roles", roles);
		return Jwts.builder().setClaims(claims).setSubject(authentication.getName())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)).signWith(getSignInKey()).compact();
	}

	// EXTRAE EL USERNAME DEL TOKEN
	public String getUsernameFromToken(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// EXTRAE TODOS LOS DETALLES DEL USUARIO DEL TOKEN
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	// VALIDA SI EL TOKEN YA ESTA EXPIRADO
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// EXTRAE LA FECHA DE EXPIRACION DEL TOKEN
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// EXTRAER LOS CLAIMS/DATOS DEL USUARIO DEL TOKEN
	public Claims extractAllClaims(String token) {
		//return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
		  return Jwts.parser()
	                .verifyWith(getSignInKey())
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();
	}

	
	// DECODIFICA LA CLAVE SECRETA
	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateSecretKey() {
		// length means (32 bytes are required for 256-bit key)
		int length = 32;
		// Create a secure random generator
		SecureRandom secureRandom = new SecureRandom();
		// Create a byte array to hold the random bytes
		byte[] keyBytes = new byte[length];
		// Generate the random bytes
		secureRandom.nextBytes(keyBytes);
		// Encode the key in Base64 format for easier storage and usage
		return Base64.getEncoder().encodeToString(keyBytes);
	}

}
