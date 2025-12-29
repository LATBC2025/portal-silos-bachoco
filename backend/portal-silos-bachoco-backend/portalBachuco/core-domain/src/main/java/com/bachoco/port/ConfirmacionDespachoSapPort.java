package com.bachoco.port;

public interface ConfirmacionDespachoSapPort {

	public String sendConfirmacionDespacho(String claveSilo,String claveMaterial,String claveNumPedTraslado,
			String claveMovimiento,String numBoleta,String pesoNeto,String destino,String ruta);
}
