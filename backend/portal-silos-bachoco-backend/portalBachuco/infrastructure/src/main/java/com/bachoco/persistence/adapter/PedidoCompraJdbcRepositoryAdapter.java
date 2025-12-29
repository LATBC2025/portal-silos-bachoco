package com.bachoco.persistence.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bachoco.model.PedidoSapResponseDTO;
import com.bachoco.model.PedidoTrasladoSapResponseDTO;
import com.bachoco.model.procedores.PedidoCompraDTO;
import com.bachoco.persistence.config.SapProperties;
import com.bachoco.persistence.repository.jdbc.PedidoCompraJdbcRepository;
import com.bachoco.port.PedidoCompraJdbcRepositoryPort;
import com.bachoco.port.PedidoCompraSapPort;
import com.bachoco.port.PedidoTrasladoSapPort;
import com.bachoco.secutiry.utils.BatchUtils;

@Component
public class PedidoCompraJdbcRepositoryAdapter implements PedidoCompraJdbcRepositoryPort {

	private final PedidoCompraJdbcRepository pedidoCompraJdbcRepository;
	private final PedidoCompraSapPort pedidoCompraSapWebClientAdapter;
	private final PedidoTrasladoSapPort pedidoTrasladoSapPort;
	private final SapProperties sapProperties;
	private static final Logger logger = LoggerFactory.getLogger(PedidoCompraJdbcRepositoryAdapter.class);

	public PedidoCompraJdbcRepositoryAdapter(PedidoCompraJdbcRepository pedidoCompraJdbcRepository,
			PedidoCompraSapPort pedidoCompraSapWebClientAdapter, PedidoTrasladoSapPort pedidoTrasladoSapPort,
			SapProperties sapProperties) {
		this.pedidoCompraJdbcRepository = pedidoCompraJdbcRepository;
		this.pedidoCompraSapWebClientAdapter = pedidoCompraSapWebClientAdapter;
		this.pedidoTrasladoSapPort = pedidoTrasladoSapPort;
		this.sapProperties = sapProperties;
	}

	private List<PedidoSapResponseDTO> findAllPedidoCompra(String claveSilo, String claveMaterial, String fechaInicio,
			String fechaFin) {
		return this.pedidoCompraSapWebClientAdapter.findAllPedidoCompra(claveSilo,claveMaterial, toReplace(fechaInicio),
				toReplace(fechaFin), sapProperties.getUrl());
	}

	private List<PedidoTrasladoSapResponseDTO> findAllPedidoCTraslado(String claveSilo, String claveMaterial,
			String plantaDestino,String fechaInicio, String fechaFin) {
		return this.pedidoTrasladoSapPort.findAllPedTraslado(claveSilo,claveMaterial,plantaDestino, toReplace(fechaInicio),
				toReplace(fechaFin), sapProperties.getUrl());
	}
	
	// Método helper
	private Float calcularTotal(PedidoTrasladoSapResponseDTO p) {
	    Float cantidadTraslado = p.getCantidadEnTraslado() != null ? 
	        Float.parseFloat(p.getCantidadEnTraslado()) : 0.0F;
	    Float cantidadRecibida = p.getCantidadRecibidaEnPa() != null ? 
	        Float.parseFloat(p.getCantidadRecibidaEnPa()) : 0.0F;
	    return cantidadRecibida + cantidadTraslado;
	}

	private List<PedidoSapResponseDTO> assignedCantidadDespacho(List<PedidoSapResponseDTO> compra,
			List<PedidoTrasladoSapResponseDTO> traslado) {
		Map<String, Float> mapFolios = new HashMap<>();
		//se agrega la suma de cada numero de pedido compra del campo  getCantidadEnTraslado + getCantidadRecibidaEnPa con el merge de map
		traslado.forEach(p -> {
		    Float total = calcularTotal(p);
		    String folio = String.valueOf(p.getPedidoDeComprasAsociado());
		    mapFolios.merge(folio, total, Float::sum);
		});
		List<PedidoSapResponseDTO> nuevaListCompras = compra.stream().map(item -> {
			String folio = item.getPedCompra();
			Float cantidadespachada = mapFolios.get(folio);
			Float cantPedida=0.0F;
			if (cantidadespachada != null) {
				if(item.getCantidadPedido()!=null) {
					cantPedida=Float.parseFloat(item.getCantidadPedido());
					item.setCantidadPendienteDespacho(String.valueOf(cantPedida-cantidadespachada));
				}
				// Crear nuevo objeto modificado (no mutamos el original)
				item.setCantidadDespacho(String.valueOf(cantidadespachada));
				return item;
			} else {
				if(item.getCantidadPedido()!=null){
					cantPedida=Float.parseFloat(item.getCantidadPedido());
				}
				item.setCantidadPendienteDespacho(String.valueOf(cantPedida-0));
				return item; // se modifica la cantidad pendiente despacho de la resta de cantidad peida menos 0
			}
		}).toList();
		return nuevaListCompras;
	}
	private List<String> createFolioNumPedTrasladoAndPosicion(List<PedidoSapResponseDTO> updateCompras){
		return  updateCompras.stream()
				.map(p -> buildFolioPedCompraAnPosicion(p.getPedCompra(), p.getPosicion())).toList();
	}
	
	private List<List<String>> batchesSearchFolios(List<String> folioPedCompraPosicion){
		return BatchUtils.partition(folioPedCompraPosicion, 100);
	}

	private List<String> findAllFoliosByNumPedAnPosicion(List<List<String>> batches){
		List<String> foliosExists = new ArrayList<>();
		for (List<String> batch : batches) {
			foliosExists.addAll(this.pedidoCompraJdbcRepository.findAllFoliosExist(batch));
		}
		return foliosExists;
	}
	
	@Override
	public List<PedidoCompraDTO> findByFilterSiloAndMaterialAnFecha(String claveSilo, String claveMaterial,
			String plantaDestino,String fechaInicio, String fechaFin) {
		List<PedidoCompraDTO> response = new ArrayList<>();
		List<String> foliosExists = new ArrayList<>();

			List<PedidoTrasladoSapResponseDTO> pedidosTrasladoSap = findAllPedidoCTraslado(claveSilo, claveMaterial,
					plantaDestino,fechaInicio, fechaFin);
			List<PedidoSapResponseDTO> pedidosSap = findAllPedidoCompra(claveSilo, claveMaterial, fechaInicio, fechaFin);
			List<PedidoSapResponseDTO> updateCompras = assignedCantidadDespacho(pedidosSap, pedidosTrasladoSap);
			if (updateCompras.size() > 0) {
				List<Map<String, Object>> foliosCantidadesModificar= new ArrayList<>();
				//se crean folio de pedido traslado (numero pedido traslado con posicion)
				List<String> folioPedCompraPosicion = createFolioNumPedTrasladoAndPosicion(updateCompras);
				
				//se crean lista de 100 en 100 para busquedas de pedido compra con el folio(numero pedido compra + posicion)
				List<List<String>> batches=batchesSearchFolios(folioPedCompraPosicion);
				//se llena la lista de folios existentes en base de datos con el batches de folio(numero pedido compra + posicion) de consultas de SAP
				foliosExists.addAll(findAllFoliosByNumPedAnPosicion(batches));
				
				//se eliminan folios duplicados con set
				Set<String> foliosExist = new HashSet<>(foliosExists);
				//se obtienen pedido compra que no estan registrados en base de datos
				List<PedidoSapResponseDTO> pedidosSapNoExistBd = updateCompras.stream().filter(
						p -> !foliosExist.contains(buildFolioPedCompraAnPosicion(p.getPedCompra(), p.getPosicion())))
						.toList();
				//se obtienen folios(numero pedido compra + posicion) de pedido compra que no estan registrados
				List<String> foliosNoExistSap = pedidosSapNoExistBd.stream()
						.map(p -> buildFolioPedCompraAnPosicion(p.getPedCompra(), p.getPosicion())).toList();
				if (pedidosSapNoExistBd.size() > 0) {
					this.pedidoCompraJdbcRepository.savePedidoCompra(pedidosSapNoExistBd,claveSilo);
				}
				this.pedidoCompraJdbcRepository.updatePedidoCompra(updateCompras,claveSilo,claveMaterial);
				for (List<String> batch : batches) {
					response.addAll(this.pedidoCompraJdbcRepository.findAllByFolioNumCompra(batch));
				}
			}
			return response;
	}
	
	public Map<String, Map<String, Object>> convertToFolioMap(List<Map<String, Object>> resultados) {
	    return resultados.stream()
	        .collect(Collectors.toMap(
	            row -> (String) row.get("folio"),
	            row -> row
	        ));
	}
	
	@Override
	public void executePedidoCompraByFilter(String claveSilo, String claveMaterial,String plantaDestino, String fechaInicio,
			String fechaFin) {
		List<String> foliosExists = new ArrayList<>();
		List<PedidoTrasladoSapResponseDTO> pedidosTrasladoSap = findAllPedidoCTraslado(claveSilo, claveMaterial,
				plantaDestino,fechaInicio, fechaFin);
		List<PedidoSapResponseDTO> pedidosSap = findAllPedidoCompra(claveSilo, claveMaterial, fechaInicio, fechaFin);
		List<PedidoSapResponseDTO> updateCompras = assignedCantidadDespacho(pedidosSap, pedidosTrasladoSap);
		if (updateCompras.size() > 0) {
			List<String> folioPedCompraPosicion = updateCompras.stream()
					.map(p -> buildFolioPedCompraAnPosicion(p.getPedCompra(), p.getPosicion())).toList();
			List<List<String>> batches = BatchUtils.partition(folioPedCompraPosicion, 100);
			for (List<String> batch : batches) {
				foliosExists.addAll(this.pedidoCompraJdbcRepository.findAllFoliosExist(batch));
			}
			Set<String> foliosExist = new HashSet<>(foliosExists);
			List<PedidoSapResponseDTO> pedidosSapNoExistBd = updateCompras.stream().filter(
					p -> !foliosExist.contains(buildFolioPedCompraAnPosicion(p.getPedCompra(), p.getPosicion())))
					.toList();
			if (pedidosSapNoExistBd.size() > 0) {
				this.pedidoCompraJdbcRepository.savePedidoCompra(pedidosSapNoExistBd,claveSilo);
			}
		}
	}

	@Override
	public List<PedidoCompraDTO> findAllComprasSapByFilters(String claveSilo, String claveMaterial, String fechaInicio,
			String fechaFin) {
		List<PedidoSapResponseDTO> pedidos = new ArrayList<>();
		if (pedidos.size() > 0) {
			List<String> folioPedCompraPosicion = pedidos.stream()
					.map(p -> buildFolioPedCompraAnPosicion(p.getPedCompra(), p.getPosicion())).toList();
			List<List<String>> resultFolios = pedidoCompraByNumPed(folioPedCompraPosicion);
			return null;
		}
		return null;
	}

	private List<List<String>> pedidoCompraByNumPed(List<String> folioNumPedCompraPosicion) {
		List<List<String>> lotes = BatchUtils.partition(folioNumPedCompraPosicion, 100);
		return lotes;
	}

	private String toReplace(String value) {
		return value.replace("-", "");
	}

	private String buildFolioPedCompraAnPosicion(String pedCompra, String posicion) {
		return pedCompra.trim().concat("-").concat(posicion.trim());
	}
}
