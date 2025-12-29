package com.bachoco.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bachoco.model.ReporteDespacho;
import com.bachoco.persistence.repository.jdbc.ReporteDespachoJdbcRepository;
import com.bachoco.port.ReporteDespachosPort;

@Repository
public class ReporteDespachoRepositoryAdapter implements ReporteDespachosPort {
 
	private final ReporteDespachoJdbcRepository reporteDespachoJdbcRepository;
	
	public ReporteDespachoRepositoryAdapter(ReporteDespachoJdbcRepository reporteDespachoJdbcRepository) {
		this.reporteDespachoJdbcRepository = reporteDespachoJdbcRepository;
	}

	@Override
	public List<ReporteDespacho> findAllReportDespachos(Integer siloId, String fechaI, String fechaF) {
		return reporteDespachoJdbcRepository.obtenerPedidosFiltradosJdbc(siloId, fechaI, fechaF);
	}

}
