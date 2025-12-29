package com.bachoco.service.usecase;

import java.util.List;

import com.bachoco.model.ConfDespachoPesosRequest;
import com.bachoco.model.ConfirmDespachoResponse;
import com.bachoco.model.ConfirmacionDespachoRequest;
import com.bachoco.model.ConfirmacionDespachoResponse;
import com.bachoco.port.ConfirmacionDespachoJdbcRepository;

public class ConfirmacionDespachoUseCase {

	private final ConfirmacionDespachoJdbcRepository confDespachoJdbcRepository;

	public ConfirmacionDespachoUseCase(ConfirmacionDespachoJdbcRepository confDespachoJdbcRepository) {
		this.confDespachoJdbcRepository = confDespachoJdbcRepository;
	}
	
	public ConfirmacionDespachoResponse save(ConfirmacionDespachoRequest req) {
		return this.confDespachoJdbcRepository.save(req);
	}
	public ConfirmacionDespachoResponse updateSap(ConfirmacionDespachoRequest req) {
		return this.confDespachoJdbcRepository.updateSap(req);
	}
	public ConfirmacionDespachoResponse updateSinSap(ConfirmacionDespachoRequest req) {
		return this.confDespachoJdbcRepository.updateBd(req);
	}
	public ConfirmacionDespachoResponse esIgualLosPesosPorId(ConfDespachoPesosRequest req) {
		return this.confDespachoJdbcRepository.esIgualPesosPorId(req);
	}
	public Float findPromedioCantidadDespacho(Integer siloId) {
		return this.confDespachoJdbcRepository.findCantidadPromediotrasporte(siloId);
	}
	
	public List<ConfirmDespachoResponse> findAllConfirmacionDespacho(String silo,String material,String fechaInicio,String fechaFin){
		return this.confDespachoJdbcRepository.findAllConfirmacionDespacho(silo, material, fechaInicio, fechaFin);
	}
	
	public ConfirmacionDespachoResponse delete(ConfirmacionDespachoRequest req ) {
		return this.confDespachoJdbcRepository.delete(req);
	}
}
