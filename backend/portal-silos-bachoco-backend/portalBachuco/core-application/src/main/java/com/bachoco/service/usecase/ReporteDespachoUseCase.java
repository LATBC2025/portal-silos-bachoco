package com.bachoco.service.usecase;

import java.util.List;

import com.bachoco.model.ReporteDespacho;
import com.bachoco.port.ReporteDespachosPort;

public class ReporteDespachoUseCase {
	
	private final ReporteDespachosPort despachosPort;

	public ReporteDespachoUseCase(ReporteDespachosPort despachosPort) {
		this.despachosPort = despachosPort;
	}
	
	public List<ReporteDespacho> findAll(Integer siloId,String fechaI,String fechaF){
		return this.despachosPort.findAllReportDespachos(siloId, fechaI, fechaF);
	}

}
