package com.bachoco.service.usecase;

import java.util.List;

import com.bachoco.exception.NotFoundMaterialException;
import com.bachoco.exception.NotFoundPedCompraException;
import com.bachoco.exception.NotFoundPlantaDestinoException;
import com.bachoco.exception.RegistroNoCreadoException;
import com.bachoco.model.procedores.PedTrasladoArriboConfigDespachoDTO;
import com.bachoco.model.procedores.PedidoTrasladoArriboDTO;
import com.bachoco.model.procedores.PedidoTrasladoDTO;
import com.bachoco.port.PedidoTrasladoJdbcRepositoryPort;

public class PedidoTrasladoJdbcUseCase {
	
	private final PedidoTrasladoJdbcRepositoryPort pedidoTrasladoJdbcRepositoryPort;

	public PedidoTrasladoJdbcUseCase(PedidoTrasladoJdbcRepositoryPort pedidoTrasladoJdbcRepositoryPort) {
		this.pedidoTrasladoJdbcRepositoryPort = pedidoTrasladoJdbcRepositoryPort;
	}
	public List<PedidoTrasladoDTO> findByFilters(String claveSilo,String claveMaterial,
			String plantaDestino,String fechaInicio,String fechaFin){
		try {
			return this.pedidoTrasladoJdbcRepositoryPort.findByFilterSiloAndMaterialAnFecha(claveSilo, claveMaterial,plantaDestino, fechaInicio, fechaFin);
		}catch(NotFoundPlantaDestinoException ex) {
			throw ex;
		}catch(NotFoundMaterialException ex) {
			throw ex;
		}catch(NotFoundPedCompraException ex) {
			throw ex;
		}
		catch (Exception e) {
			throw new RegistroNoCreadoException(e.getMessage());
		}
	}
	public void executeDowloadPedTrasladoBySap(String claveSilo,String claveMaterial,String plantaDestino,String fechaInicio,String fechaFin){
		try {
			this.pedidoTrasladoJdbcRepositoryPort.findByFilterSiloAndMaterialAnFecha(claveSilo, claveMaterial,plantaDestino, fechaInicio, fechaFin);
			//this.pedidoTrasladoJdbcRepositoryPort.executeDowloadPedTrasladoBySap(claveSilo, claveMaterial, fechaInicio, fechaFin);
		}catch (NotFoundPlantaDestinoException ex) {
			throw ex;
		}catch(NotFoundMaterialException ex) {
			throw ex;
		}catch(NotFoundPedCompraException ex) {
			throw ex;
		}catch (Exception e) {
			throw new RegistroNoCreadoException(e.getMessage());
		}
	}
	public List<PedidoTrasladoDTO> findByFiltersCantidadDisponible(Integer siloId,Integer materialId,String fechaInicio,String fechaFin){
		return this.pedidoTrasladoJdbcRepositoryPort.findByFiltersCantidadDisponible(siloId,materialId, fechaInicio, fechaFin);
	}
	public List<PedidoTrasladoArriboDTO> findByFilterProgramArribo(Integer siloId,String planta,Integer materialId){
		return this.pedidoTrasladoJdbcRepositoryPort.findByFilterProgramArribo(siloId,planta,materialId);
	}
	public List<PedTrasladoArriboConfigDespachoDTO> findByPedTrasladoByConfDespacho(Integer siloId,Integer materialId,String fechaInicio,String fechaFin){
		return this.pedidoTrasladoJdbcRepositoryPort.findByPedTrasladoByConfDespacho(siloId,materialId,fechaInicio,fechaFin);
	}
}
