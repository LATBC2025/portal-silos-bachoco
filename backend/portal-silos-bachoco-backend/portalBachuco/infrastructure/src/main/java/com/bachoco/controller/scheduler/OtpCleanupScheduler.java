package com.bachoco.controller.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bachoco.service.usecase.GenerarYEnviarOtpUseCase;

@Component
public class OtpCleanupScheduler {
	
	private final GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase;

	public OtpCleanupScheduler(GenerarYEnviarOtpUseCase generarYEnviarOtpUseCase) {
		this.generarYEnviarOtpUseCase = generarYEnviarOtpUseCase;
	}

	@Scheduled(cron = "0 * * * * ?")
    public void cleanupExpiredOtps() {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        int deletedCount = generarYEnviarOtpUseCase.deleteOtpExpired(fiveMinutesAgo); 
    }
}
