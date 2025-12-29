package com.bachoco.persistence.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationConfig {

	String usuario = "CONS_LATBC01";
	String password = "Snorlax25";
	String authHeader = "Basic " + Base64.getEncoder()
    .encodeToString((usuario + ":" + password).getBytes(StandardCharsets.UTF_8)); 
	
	@Bean
    public RestClient pedidoRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("http://bopaxdev01.bachoco.net:50000/RESTAdapter/portal-cosecha")
                .defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0") 
                //.defaultHeader(HttpHeaders.AUTHORIZATION, buildBasicAuthHeader("CONS_LATBC01", "Snorlax25"))
                .build();
    }
	
	private static String buildBasicAuthHeader(String username, String password) {
        String creds = username + ":" + password;
        String base64Creds = Base64.getEncoder().encodeToString(creds.getBytes(StandardCharsets.UTF_8));
        return "Basic " + base64Creds;
    }
}
