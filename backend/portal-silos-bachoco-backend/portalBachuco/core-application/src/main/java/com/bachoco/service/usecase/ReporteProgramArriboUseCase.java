package com.bachoco.service.usecase;

import java.util.List;

import com.bachoco.model.ReportePorgramArribo;
import com.bachoco.port.ReporteProgramArriboPort;

public class ReporteProgramArriboUseCase {
	
	private final ReporteProgramArriboPort programArriboPort;

	public ReporteProgramArriboUseCase(ReporteProgramArriboPort programArriboPort) {
		this.programArriboPort = programArriboPort;
	}
	
	public List<ReportePorgramArribo> findAll(Integer siloId, Integer idProveedor,String fechaI,String fechaF){
		return this.programArriboPort.findAllFilters(siloId, idProveedor,fechaI, fechaF);
	}
}
