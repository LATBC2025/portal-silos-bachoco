package com.bachoco.port;

import java.util.List;

import com.bachoco.model.PedidoSapResponseDTO;

public interface PedidoCompraSapPort {
	public List<PedidoSapResponseDTO> findAllPedidoCompra(String claveSilo,String claveMaterial,String fechaInicio,String fechaFin,String rutaUrl);
}
