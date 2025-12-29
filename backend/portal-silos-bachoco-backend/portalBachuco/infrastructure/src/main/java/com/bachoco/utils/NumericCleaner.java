package com.bachoco.utils;

import java.util.regex.Pattern;

public class NumericCleaner {

	  // Patrón para números válidos: opcionalmente negativo, con decimales opcionales
    private static final Pattern VALID_NUMBER_PATTERN = 
        Pattern.compile("^-?\\d+(\\.\\d+)?$");
    
    
    /**
     * Limpia y valida un string numérico
     * @param numericString String a limpiar
     * @return String limpio o null si no es válido
     */
    public static String cleanNumericString(String numericString) {
        if (numericString == null) {
            return null;
        }
        
        // Eliminar espacios en blanco
        String cleaned = numericString.trim();
        
        if (cleaned.isEmpty()) {
            return null;
        }
        
        // Caso especial: si hay un - al final, moverlo al principio
        if (cleaned.endsWith("-")) {
            cleaned = "-" + cleaned.substring(0, cleaned.length() - 1);
        }
        
        // Eliminar caracteres no numéricos excepto punto decimal y signo negativo al inicio
        cleaned = removeInvalidCharacters(cleaned);
        
        // Validar que el formato sea correcto
        if (!VALID_NUMBER_PATTERN.matcher(cleaned).matches()) {
            return null;
        }
        
        return cleaned;
    }
    
    /**
     * Elimina caracteres no válidos manteniendo solo números, punto decimal y - al inicio
     */
    private static String removeInvalidCharacters(String input) {
        StringBuilder result = new StringBuilder();
        boolean hasDecimalPoint = false;
        boolean hasNegativeSign = false;
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            
            if (Character.isDigit(c)) {
                result.append(c);
            } 
            else if (c == '.' && !hasDecimalPoint) {
                // Permitir solo un punto decimal
                result.append(c);
                hasDecimalPoint = true;
            }
            else if (c == '-' && i == 0 && !hasNegativeSign) {
                // Permitir - solo al inicio
                result.append(c);
                hasNegativeSign = true;
            }
            // Ignorar otros caracteres no numéricos
        }
        
        return result.toString();
    }
    
    /**
     * Convierte string limpio a Double
     */
    public static Double parseCleanDouble(String numericString) {
        String cleaned = cleanNumericString(numericString);
        if (cleaned == null) {
            return null;
        }
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
