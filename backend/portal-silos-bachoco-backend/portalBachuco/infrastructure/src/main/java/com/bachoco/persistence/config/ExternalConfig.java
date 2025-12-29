package com.bachoco.persistence.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
    value = "file:config/config.properties", 
    ignoreResourceNotFound = true
)
public class ExternalConfig {
}