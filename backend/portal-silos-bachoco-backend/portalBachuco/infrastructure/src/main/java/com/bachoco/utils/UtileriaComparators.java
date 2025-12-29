package com.bachoco.utils;

public class UtileriaComparators {

	public static Float parseFloatSafe(String value) {
	    if (value == null || value.trim().isEmpty()) {
	        return 0.0f;
	    }
	    try {
	        return Float.parseFloat(value);
	    } catch (NumberFormatException e) {
	        return 0.0f;
	    }
	}
	
	public static String buildFolioPedCompraAnPosicion(String pedCompra, String posicion) {
		return pedCompra.trim().concat("-").concat(posicion.trim());
	}
}
