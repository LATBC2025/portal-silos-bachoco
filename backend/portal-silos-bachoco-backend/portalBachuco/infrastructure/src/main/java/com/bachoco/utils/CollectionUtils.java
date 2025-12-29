package com.bachoco.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CollectionUtils {

private CollectionUtils() {}
    
    public static <T> List<T> filtrarLista(List<T> lista, Predicate<T> filtro) {
    	
        if (lista == null) return new ArrayList<>();
        if (filtro == null) return new ArrayList<>(lista);
        
        List<T> resultado = new ArrayList<>(lista);
        resultado.removeIf(filtro);
        return resultado;
    }
}
