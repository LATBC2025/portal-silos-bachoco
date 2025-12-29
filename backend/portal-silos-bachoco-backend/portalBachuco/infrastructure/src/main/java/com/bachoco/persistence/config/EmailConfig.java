package com.bachoco.persistence.config;

import java.util.Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.spring.mail")
public class EmailConfig {

	private String host;
    private Integer port;
    private String username;      // opcional (gmail sí, corporativo no)
    private String password;      // opcional
    private Properties properties = new Properties();

    private String sender;        // app.mail.sender
    private Integer poolSize = 5; // default

    public Properties buildJavaMail() {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        // Merge de propiedades
        properties.forEach(props::put);
        return props;
    }
}
