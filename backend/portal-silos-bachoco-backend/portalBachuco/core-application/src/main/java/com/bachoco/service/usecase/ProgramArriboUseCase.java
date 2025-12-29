package com.bachoco.service.usecase;

import java.util.List;

import com.bachoco.model.ProgramArriboRequest;
import com.bachoco.port.ProgramArriboRepositoryPort;

public class ProgramArriboUseCase {

	private final ProgramArriboRepositoryPort programArriboRepositoryPort;

	public ProgramArriboUseCase(ProgramArriboRepositoryPort programArriboRepositoryPort) {
		this.programArriboRepositoryPort = programArriboRepositoryPort;
	}
	
	public Double stockSilo(String claveSilo,String material) {
		return this.programArriboRepositoryPort.stockSilo(claveSilo,material);
	}
	
	public String saveProgramArribo(List<ProgramArriboRequest> req) {
		return this.programArriboRepositoryPort.saveProgramArrivo(req);
	}
	
	public Float findPesoNetoByNumPedTraslado(List<String> numPedidoTraslados, String claveSilo,
			String claveMaterial, String clavePlanta, String fechaInicio, String fechaFinF){
		return this.programArriboRepositoryPort.findPesoNetoByNumPedTraslado(numPedidoTraslados, claveSilo,claveMaterial,clavePlanta, fechaInicio,fechaFinF);
	}
}
