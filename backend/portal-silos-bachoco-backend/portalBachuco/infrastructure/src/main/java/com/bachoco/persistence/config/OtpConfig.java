package com.bachoco.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bachoco.port.EmailSender;
import com.bachoco.port.EmpleadoRepositoryPort;
import com.bachoco.port.OtpRepository;
import com.bachoco.service.IOtpGenerator;
import com.bachoco.service.impl.OtpGeneratorImpl;
import com.bachoco.service.usecase.GenerarYEnviarOtpUseCase;

@Configuration
public class OtpConfig {
	
    @Bean//IOtpGenerator y OtpGeneratorImpl vienen de application
    public IOtpGenerator otpGenerator() {
        return new OtpGeneratorImpl();
    }
    
	@Bean
	public GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase(OtpRepository otpRepository,EmailSender emailSender,IOtpGenerator otpGenerator,EmpleadoRepositoryPort empleadoRepositoryPort) {
	   return new GenerarYEnviarOtpUseCase(otpRepository, emailSender,otpGenerator,empleadoRepositoryPort);
	}
	   
	/*@Bean//opt y OtpRepository viene de la capa de domain
    public OtpRepository otpRepository(OtpJpaRepository jpaRepository) {
        return new JpaOtpRepositoryImpl(jpaRepository);
    }*/


    /*@Bean//GenerarYEnviarOtpUseCase y OtpGeneratorImpl viene de la capa de application, OtpRepository y EmailSender viene de la capa de domain
    public GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase(
            OtpRepository otpRepository,
            EmailSender emailSender,
            OtpGeneratorImpl otpGenerator) {
        return new GenerarYEnviarOtpUseCase(otpRepository, emailSender, otpGenerator);
    }*/

}
