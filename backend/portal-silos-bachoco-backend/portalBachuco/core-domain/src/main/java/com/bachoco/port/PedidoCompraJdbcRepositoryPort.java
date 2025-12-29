package com.bachoco.port;

import java.util.List;

import com.bachoco.model.procedores.PedidoCompraDTO;

public interface PedidoCompraJdbcRepositoryPort {

	public List<PedidoCompraDTO> findByFilterSiloAndMaterialAnFecha(String claveSilo,String claveMaterial,String plantaDestino,String fechaInicio,String fechaFin);
	public void executePedidoCompraByFilter(String claveSilo,String claveMaterial,String plantaDestino,String fechaInicio,String fechaFin);
	public List<PedidoCompraDTO> findAllComprasSapByFilters(String claveSilo,String claveMaterial,String fechaInicio,String fechaFin);
}
