package com.bachoco.port;

import java.util.List;

import com.bachoco.model.ReporteDespacho;

public interface ReporteDespachosPort {

	public List<ReporteDespacho> findAllReportDespachos(Integer siloId,String fechaI,String fechaF);
}
