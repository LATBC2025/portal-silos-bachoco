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
	
	public List<PedidoTrasladoDTO> findByFiltersToLiberarSaldo(String claveSilo, String claveMaterial,
	        String plantaDestino, String fechaInicio, String fechaFin) {
	    try {
	        List<PedidoTrasladoDTO> pedidos = this.pedidoTrasladoJdbcRepositoryPort
	                .findByFilterSiloAndMaterialAnFecha(claveSilo, claveMaterial, plantaDestino, fechaInicio, fechaFin);

	        // Si no hay pedidos, regresa tal cual
	        if (pedidos == null || pedidos.isEmpty()) {
	            return pedidos;
	        }

	        // ====== Reglas por fecha (liberar saldo un día después) ======
	        java.time.LocalDate hoy = java.time.LocalDate.now();
	        java.time.LocalDate ayer = hoy.minusDays(1);

	        // ====== Consultas agregadas (batch) ======
	        // Key recomendada: numPedidoTraslado (o pedidoTrasladoId si es el que usan en tus tablas)
	        var confHastaAyer = this.pedidoTrasladoJdbcRepositoryPort
	                .sumConfirmadosHastaFecha(claveSilo, claveMaterial, plantaDestino, ayer);

	        var confHoy = this.pedidoTrasladoJdbcRepositoryPort
	                .sumConfirmadosEnFecha(claveSilo, claveMaterial, plantaDestino, hoy);

	        var progDesdeHoy = this.pedidoTrasladoJdbcRepositoryPort
	                .sumProgramadosDesdeFecha(claveSilo, claveMaterial, plantaDestino, hoy);

	        // ====== Enriquecer DTO ======
	        for (PedidoTrasladoDTO p : pedidos) {
	            String key = p.getNumPedidoTraslado(); // o String.valueOf(p.getPedidoTrasladoId())

	            double confirmadoAyer = confHastaAyer.getOrDefault(key, 0.0);
	            double confirmadoHoy = confHoy.getOrDefault(key, 0.0);
	            double programadoFuturo = progDesdeHoy.getOrDefault(key, 0.0);

	            // Columna 1: embarcado real (confirmado real total)
	            p.setCantidadEmbarcadaReal(confirmadoAyer + confirmadoHoy);

	            // Columna 2: pendiente por programar (saldo liberado)
	            double pendientePorProgramar = p.getCantidadPedido() - confirmadoAyer - programadoFuturo;
	            if (pendientePorProgramar < 0) pendientePorProgramar = 0;

	            p.setCantidadPendientePorProgramar(pendientePorProgramar);

	            // Bandera para mensaje (opcional)
	            p.setSaldoSeLiberaManana(confirmadoHoy > 0);
	        }

	        return pedidos;
	        

	    } catch (NotFoundPlantaDestinoException ex) {
	        throw ex;
	    } catch (NotFoundMaterialException ex) {
	        throw ex;
	    } catch (NotFoundPedCompraException ex) {
	        throw ex;
	    } catch (Exception e) {
	        throw new RegistroNoCreadoException(e.getMessage());
	    }
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
	public List<PedidoTrasladoArriboDTO> findByFilterProgramArribo(Integer siloId,String planta,Integer materialId,String proveedor){
		return this.pedidoTrasladoJdbcRepositoryPort.findByFilterProgramArribo(siloId,planta,materialId,proveedor);
	}
	public List<PedTrasladoArriboConfigDespachoDTO> findByPedTrasladoByConfDespacho(Integer siloId,Integer materialId,String fechaInicio,String fechaFin){
		return this.pedidoTrasladoJdbcRepositoryPort.findByPedTrasladoByConfDespacho(siloId,materialId,fechaInicio,fechaFin);
	}
}
