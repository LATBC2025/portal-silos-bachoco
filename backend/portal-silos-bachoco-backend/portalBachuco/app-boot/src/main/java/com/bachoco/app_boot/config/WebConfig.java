package com.bachoco.app_boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirige todas las rutas no manejadas al index.html de Angular
        registry.addViewController("/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
    }
}