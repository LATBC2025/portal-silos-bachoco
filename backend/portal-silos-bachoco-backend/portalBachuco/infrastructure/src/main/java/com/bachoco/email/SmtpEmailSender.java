package com.bachoco.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.bachoco.exception.SendEmailException;
import com.bachoco.model.EmailMensaje;
import com.bachoco.port.EmailSender;

public class SmtpEmailSender implements EmailSender {

	private final JavaMailSender mailSender;

	public SmtpEmailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public int enviar(EmailMensaje email) {
		int respuesta=-1;
		try {
			 SimpleMailMessage message = new SimpleMailMessage();
			 message.setTo(email.getDestinatario());
		        message.setTo(email.getDestinatario());
		        message.setSubject(email.getAsunto());
		        message.setText(email.getCuerpo());
		        mailSender.send(message);
		        respuesta=1;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SendEmailException("Hubo un error en el envio de correo del doble factor de autenticacion");
		}
		return respuesta;
	}

}
