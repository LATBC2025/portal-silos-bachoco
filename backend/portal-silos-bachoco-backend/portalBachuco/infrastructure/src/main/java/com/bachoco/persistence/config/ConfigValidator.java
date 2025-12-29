package com.bachoco.persistence.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigValidator implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private Environment environment;
    private SapProperties sapProperties;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("=========================================");
        System.out.println("EXTERNAL CONFIGURATION VALIDATION");
        System.out.println("=========================================");
        // Verificar propiedades del archivo externo
        checkProperty("sap.url", true);
        checkProperty("sap.username", true);
        checkProperty("sap.password", true);
        // Verificar propiedades del archivo interno
        checkProperty("spring.datasource.jndi-name", false);
        System.out.println("=========================================");
        // Verificar la clase SapProperties
        if (sapProperties != null) {
            System.out.println("SapPropeties: ✓ INJECTED SUCCESSFULLY");
            System.out.println("username loaded: " + (sapProperties.getUserName() != null));
            System.out.println("password loaded: " + (sapProperties.getPassWord() != null));
        } else {
            System.out.println("SapPropeties: ✗ NOT INJECTED");
        }
        System.out.println("=========================================");
    }
    
    private void checkProperty(String key, boolean isSensitive) {
        String value = environment.getProperty(key, "NOT_FOUND");
        String status = value.equals("NOT_FOUND") ? "✗ NOT FOUND" : "✓ LOADED";
        String type = isSensitive ? "SENSITIVE" : "NORMAL";
        System.out.println(type + " - " + key + ": " + status);
        if (!value.equals("NOT_FOUND") && isSensitive) {
            System.out.println("      Value: " + value.substring(0, Math.min(5, value.length())) + "***");
        }
    }
}