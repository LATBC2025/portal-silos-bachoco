package com.bachoco.secutiry.utils;

import java.time.LocalDateTime;

public class PasswordExpirationValidator {

	// Define la política de expiración en días.
    private static final long DIAS_DE_EXPIRACION = 30;
    private static final long MINUTOS_DE_EXPIRACION = 2;
    public static boolean hasExpired(LocalDateTime lastPasswordChangeDate) {
        // 1. Determinar la fecha límite de expiración: Fecha de cambio + 30 días.
        LocalDateTime expirationDate = lastPasswordChangeDate.plusDays(DIAS_DE_EXPIRACION);
        // 2. Obtener la fecha actual.
        LocalDateTime today = LocalDateTime.now();
        // 3. Comparar: si la fecha actual es igual o posterior a la fecha de expiración.
        //    Esto significa que el día 30 la contraseña ya expiró.
        return today.isEqual(expirationDate) || today.isAfter(expirationDate);
        // O de forma más concisa:
        // return !today.isBefore(expirationDate);
    }
}
