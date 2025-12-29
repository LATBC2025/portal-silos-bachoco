package com.bachoco.utils;

import java.util.concurrent.ThreadLocalRandom;

public class UtileriaNumeros {

	
    public static int generarNumeroAleatorio() {
        return (int) (Math.random() * 201) + 100;
    }
    
    public static int generarNumero(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
