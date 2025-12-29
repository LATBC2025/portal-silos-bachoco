package com.bachoco.port;

import com.bachoco.exception.SendEmailException;
import com.bachoco.model.EmailMensaje;

public interface EmailSender {

	int enviar(EmailMensaje email) throws SendEmailException;
}
