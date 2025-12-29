package com.bachoco.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bachoco.exception.SendEmailException;
import com.bachoco.model.EmailMensaje;
import com.bachoco.port.EmailSender;

@Component
public class SmtpEmailSenderAdapter implements EmailSender {

	private static final Logger logger = LoggerFactory.getLogger(SmtpEmailSenderAdapter.class);
	@Autowired
	private AsyncEmailSender emailSender;
	
	@Override
	public int enviar(EmailMensaje email) throws SendEmailException{
		int respuesta=-1;
		try {
				emailSender.sendEmailAsync(
		                email.getDestinatario(),
		                email.getAsunto(),
		                email.getCuerpo()
		        );
		        respuesta=1;
		}catch (Exception e) {
			logger.warn("Hubo un error en el envio de corrreo: "+e.getMessage());
			throw new SendEmailException("Hubo un error en el envio de correo del doble factor de autenticacion");
		}
		return respuesta;
	}

}
