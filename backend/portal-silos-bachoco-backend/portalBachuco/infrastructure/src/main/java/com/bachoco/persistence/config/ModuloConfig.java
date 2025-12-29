package com.bachoco.persistence.config;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

//En cualquier módulo JAR (sin spring boot starter)
//@Configuration
//@PropertySource("classpath:application.properties")
public class ModuloConfig {
 

 //@Autowired
 private Environment env;
 

}