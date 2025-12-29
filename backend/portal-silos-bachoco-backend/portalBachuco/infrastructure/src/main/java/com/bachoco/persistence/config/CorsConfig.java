package com.bachoco.persistence.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.bachoco.secutiry.utils.SecurityTextUtils;

@Configuration
public class CorsConfig {
	
	/*@Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(SecurityTextUtils.WHITE_LIST_URL_FRONT_ACCESS));
        config.setAllowedHeaders(List.of(SecurityTextUtils.AUTHORIZATION, SecurityTextUtils.CONTENT_TYPE));
        config.setAllowedMethods(List.of(SecurityTextUtils.PETITION_METHOD));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }*/
	/*@Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowedMethods(List.of(SecurityTextUtils.PETITION_METHOD));
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(SecurityTextUtils.WHITE_LIST_URL_FRONT_ACCESS));
        config.setAllowedHeaders(List.of(SecurityTextUtils.AUTHORIZATION, SecurityTextUtils.CONTENT_TYPE));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }*/
	
	 /*@Bean
	  public CorsFilter corsFilter() {
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    CorsConfiguration config = new CorsConfiguration();
	    config.addAllowedOrigin("*");
	    config.setAllowedMethods(List.of(SecurityTextUtils.PETITION_METHOD));
	    config.setAllowCredentials(Boolean.valueOf(true));
	    config.setAllowedOrigins(List.of(SecurityTextUtils.WHITE_LIST_URL_FRONT_ACCESS));
	    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
	    source.registerCorsConfiguration("/**", config);
	    return new CorsFilter((CorsConfigurationSource)source);
	  }*/


}
