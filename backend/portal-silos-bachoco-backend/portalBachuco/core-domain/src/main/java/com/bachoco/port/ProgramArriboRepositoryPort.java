package com.bachoco.port;

import java.util.List;

import com.bachoco.model.ProgramArriboRequest;

public interface ProgramArriboRepositoryPort {

	public Double stockSilo(String claveSilo,String material);
	public String saveProgramArrivo(List<ProgramArriboRequest> arribos);
	public Float findPesoNetoByNumPedTraslado(List<String> numPedidoTraslados, String claveSilo,
			String claveMaterial, String clavePlanta, String fechaInicio, String fechaFin);
}
