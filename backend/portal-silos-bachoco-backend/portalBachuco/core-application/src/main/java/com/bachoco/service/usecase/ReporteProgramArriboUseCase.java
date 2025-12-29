package com.bachoco.service.usecase;

import java.util.List;

import com.bachoco.model.ReportePorgramArribo;
import com.bachoco.port.ReporteProgramArriboPort;

public class ReporteProgramArriboUseCase {
	
	private final ReporteProgramArriboPort programArriboPort;

	public ReporteProgramArriboUseCase(ReporteProgramArriboPort programArriboPort) {
		this.programArriboPort = programArriboPort;
	}
	
	public List<ReportePorgramArribo> findAll(Integer siloId,String fechaI,String fechaF){
		return this.programArriboPort.findAllFilters(siloId, fechaI, fechaF);
	}
}
