package com.bachoco.port;

import java.util.List;

import com.bachoco.model.ConfDespachoPesosRequest;
import com.bachoco.model.ConfirmDespachoResponse;
import com.bachoco.model.ConfirmacionDespachoRequest;
import com.bachoco.model.ConfirmacionDespachoResponse;

public interface ConfirmacionDespachoJdbcRepository {

	public ConfirmacionDespachoResponse save(ConfirmacionDespachoRequest req);
	public ConfirmacionDespachoResponse updateBd(ConfirmacionDespachoRequest req);
	public ConfirmacionDespachoResponse updateSap(ConfirmacionDespachoRequest req);
	public ConfirmacionDespachoResponse delete(ConfirmacionDespachoRequest req);
	public ConfirmacionDespachoResponse esIgualPesosPorId(ConfDespachoPesosRequest req);
	public Float findCantidadPromediotrasporte(Integer siloId);
	public List<ConfirmDespachoResponse> findAllConfirmacionDespacho(String silo,String material,String fechaInicio,String fechaFin);
}
