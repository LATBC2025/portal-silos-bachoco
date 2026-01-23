package com.bachoco.port;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;

import com.bachoco.model.procedores.PedTrasladoArriboConfigDespachoDTO;
import com.bachoco.model.procedores.PedidoTrasladoArriboDTO;
import com.bachoco.model.procedores.PedidoTrasladoDTO;

public interface PedidoTrasladoJdbcRepositoryPort {

	public List<PedidoTrasladoDTO> findByFilterSiloAndMaterialAnFecha(String claveSilo,String claveMaterial,String plantaDestino,String fechaInicio,String fechaFin);
	public void executeDowloadPedTrasladoBySap(String claveSilo,String claveMaterial,String plantaDestino,String fechaInicio,String fechaFin);
	public List<PedidoTrasladoDTO> findByFiltersCantidadDisponible(Integer siloId,Integer materialId,String fechaInicio,String fechaFin);
	public List<PedidoTrasladoArriboDTO> findByFilterProgramArribo(Integer siloId,String planta,Integer materialId,String proveedor);
	public List<PedTrasladoArriboConfigDespachoDTO> findByPedTrasladoByConfDespacho(Integer siloId,Integer materialId,String fechaInicio,String fechaFin);
	
	Map<String, Double> sumConfirmadosHastaFecha(String claveSilo, String claveMaterial, String plantaDestino, LocalDate fecha);
	Map<String, Double> sumConfirmadosEnFecha(String claveSilo, String claveMaterial, String plantaDestino, LocalDate fecha);
	Map<String, Double> sumProgramadosDesdeFecha(String claveSilo, String claveMaterial, String plantaDestino, LocalDate fecha);

}
