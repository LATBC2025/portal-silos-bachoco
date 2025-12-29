package com.bachoco.persistence.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "sap")
@Validated
public class SapProperties {

	private String url;
    private String userName;
    private String passWord;
    
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	@PostConstruct
    public void validateSensitiveConfig() {
        System.out.println("=== SENSITIVE CONFIGURATION LOADED ===");
        System.out.println("data url: " + (url != null ? "✓ SET" : "✗ MISSING"));
        System.out.println("data userName: " + (userName != null ? "✓ SET" : "✗ MISSING"));
        System.out.println("data passWord: " + (passWord != null ? "✓ SET" : "✗ MISSING"));
        System.out.println("External Config File: ✓ LOADED SUCCESSFULLY");
    }
    
}