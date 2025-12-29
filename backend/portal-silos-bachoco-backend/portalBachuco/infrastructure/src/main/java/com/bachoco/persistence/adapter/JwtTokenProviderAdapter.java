package com.bachoco.persistence.adapter;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.bachoco.model.Usuario;
import com.bachoco.port.TokenProviderPort;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

//@Component
public class JwtTokenProviderAdapter implements TokenProviderPort {

	// @Value("${jwt.expiration}")
	private long jwtExpiration = 7200000;

	// Value("${jwt.expiracion}")
	private long refreshExpiration = 604800000;

	private Key key;
	


    public String generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        // Lógica para añadir claims al token (por ejemplo, roles)
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(getSignInKey())
                .compact();
    }

	@Override
	public String generateToken(Usuario usuario) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + jwtExpiration);
		return Jwts.builder().setSubject(usuario.getUsuario()).claim("roles", "ROLE_ADMIN").setIssuedAt(now)
				.setExpiration(exp).signWith(Keys.hmacShaKeyFor(generateSecretKey().getBytes(StandardCharsets.UTF_8)),
						SignatureAlgorithm.HS256)
				.compact();
	}

	@Override
	public boolean validateToken(String token) {
		// TODO Auto-generated method stub
		return false;
	}

	// EXTRAE EL USERNAME DEL TOKEN
	@Override
	public String getUsernameFromToken(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// EXTRAE TODOS LOS DETALLES DEL USUARIO DEL TOKEN
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	// GENERAR EL TOEKEN CON LOS DETALLES DEL USUARIO
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return buildToken(new HashMap<>(), userDetails, refreshExpiration);
	}

	// CONTRUIR EL TOKEN CON EL USERNAME, TIEMPO DE EXPIRACION Y EL TIPO DE
	// ALGORITMO FIRMADO
	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				// .signWith(secretKeyValue, SignatureAlgorithm.HS256).compact();
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
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
		byte[] keyBytes = Decoders.BASE64.decode(generateSecretKey());
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
