package com.bachoco.port;

import java.util.List;

import com.bachoco.model.ReportePorgramArribo;

public interface ReporteProgramArriboPort {

	public List<ReportePorgramArribo> findAllFilters(Integer siloId, Integer idProveedor,String fechaI, String fechaF);;
}
