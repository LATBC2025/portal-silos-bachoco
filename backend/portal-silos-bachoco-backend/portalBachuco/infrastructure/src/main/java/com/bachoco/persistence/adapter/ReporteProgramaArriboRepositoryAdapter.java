package com.bachoco.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bachoco.dto.ReporteProgramArriboDTO;
import com.bachoco.model.ReportePorgramArribo;
import com.bachoco.persistence.repository.ReporteProgramArriboRepository;
import com.bachoco.port.ReporteProgramArriboPort;

@Component
public class ReporteProgramaArriboRepositoryAdapter implements ReporteProgramArriboPort{

	private final ReporteProgramArriboRepository reporteProgramArriboRepository;
	
	public ReporteProgramaArriboRepositoryAdapter(ReporteProgramArriboRepository reporteProgramArriboRepository) {
		this.reporteProgramArriboRepository = reporteProgramArriboRepository;
	}

	@Override
	public List<ReportePorgramArribo> findAllFilters(Integer siloId, Integer idProveedor,String fechaI, String fechaF) {
		List<ReporteProgramArriboDTO> reporte=this.reporteProgramArriboRepository.obtenerPedidosFiltrados(siloId,idProveedor, fechaI, fechaF);
		return reporte.stream().map(r->toDomain(r)).toList();
	}
	
	public ReportePorgramArribo toDomain(ReporteProgramArriboDTO e) {
		ReportePorgramArribo arribo= new ReportePorgramArribo();
		arribo.setId(e.getId());
		arribo.setToneladas(e.getToneladas());
		arribo.setFecha(e.getFecha());
		arribo.setMaterial(e.getMaterial());
		arribo.setNumeroPedido(e.getNumeroPedido());
		arribo.setDestinoPlanta(e.getDestinoPlanta());
		arribo.setNombreProveedor(e.getNombreProveedor());
		return arribo;
	}

}
