package com.bachoco.utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringExtractionUtils {

	 // ✅ MÉTODO PRINCIPAL - Más genérico y reutilizable
    public static <T> List<String> extraerStringsNoVacios(
            List<T> lista, 
            Function<T, String> extractor) {
        
        if (lista == null || lista.isEmpty()) {
            return Collections.emptyList();
        }
        
        return lista.stream()
                   .map(extractor)
                   .filter(Objects::nonNull)
                   .map(String::trim)
                   .filter(planta -> !planta.isEmpty())
                   .toList(); // Java 16+ - inmutable
    }
    
    // ✅ VERSIÓN CON DISTINCT
    public static <T> List<String> extraerStringsUnicosNoVacios(
            List<T> lista, 
            Function<T, String> extractor) {
        
        if (lista == null || lista.isEmpty()) {
            return Collections.emptyList();
        }
        
        return lista.stream()
                   .map(extractor)
                   .filter(Objects::nonNull)
                   .map(String::trim)
                   .filter(value -> !value.isEmpty())
                   .distinct()
                   .toList();
    }
    
    // ✅ VERSIÓN PARA SET (cuando el orden no importa y quieres unicidad)
    public static <T> Set<String> extraerStringsUnicosSet(
            List<T> lista, 
            Function<T, String> extractor) {
        
        if (lista == null || lista.isEmpty()) {
            return Collections.emptySet();
        }
        
        return lista.stream()
                   .map(extractor)
                   .filter(Objects::nonNull)
                   .map(String::trim)
                   .filter(planta -> !planta.isEmpty())
                   .collect(Collectors.toSet());
    }
}
