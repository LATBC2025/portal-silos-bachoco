package com.bachoco.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bachoco.persistence.config.EmailConfig;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class AsyncEmailSender {

	    private final EmailConfig config;
	    private final ExecutorService executor;
	    private static final Logger logger = LoggerFactory.getLogger(AsyncEmailSender.class);

	    public AsyncEmailSender(EmailConfig config) {
	        this.config = config;
	        this.executor = Executors.newFixedThreadPool(config.getPoolSize());
	    }
	    public void sendEmailAsync(String to, String subject, String body) {
	        executor.submit(() -> {
	            try {
	                Session session = Session.getInstance(
	                        config.buildJavaMail(),
	                        new Authenticator() {
	                            @Override
	                            protected PasswordAuthentication getPasswordAuthentication() {
	                            	if (config.getUsername() == null) {
	                                    return null;
	                                }
	                                return new PasswordAuthentication(
	                                		config.getUsername(),
	                                		config.getPassword()
	                                );
	                            }
	                        }
	                );
	                Message msg = new MimeMessage(session);
	                msg.setFrom(new InternetAddress(config.getSender()));
	                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	                msg.setSubject(subject);
	                msg.setContent(body, "text/html; charset=UTF-8");
	                Transport.send(msg);
	                logger.info("Correo enviado: " + to);
	            } catch (Exception e) {
	            	logger.error("Error enviando email a " + to);
	            	logger.error("Mensaje de error " + e.getMessage());
	            }
	        });
	    }
}
