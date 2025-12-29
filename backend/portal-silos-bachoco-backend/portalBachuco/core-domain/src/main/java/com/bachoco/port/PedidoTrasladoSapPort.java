package com.bachoco.port;

import java.util.List;

import com.bachoco.model.PedidoTrasladoSapResponseDTO;

public interface PedidoTrasladoSapPort {
	public List<PedidoTrasladoSapResponseDTO> findAllPedTraslado(String claveSilo,String claveMaterial,String plantaDestino,String fechaInicio,String fechaFin,String rutaUrl);
}
