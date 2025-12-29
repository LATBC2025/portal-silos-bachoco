package com.bachoco.secutiry.utils;

public class Utileria {

	public static String comilla="\"";
	private static String buildFolioNumPosicion(String pedCompra,String posicion) {
		return pedCompra.trim().concat("-").concat(posicion.trim());
	}
}
