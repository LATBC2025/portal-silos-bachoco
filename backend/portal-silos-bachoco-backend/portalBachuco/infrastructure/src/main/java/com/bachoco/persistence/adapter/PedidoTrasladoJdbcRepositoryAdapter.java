package com.bachoco.persistence.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bachoco.dto.CompraAsociadoKey;
import com.bachoco.exception.NotFoundMaterialException;
import com.bachoco.exception.NotFoundPedCompraException;
import com.bachoco.exception.NotFoundPlantaDestinoException;
import com.bachoco.exception.RegistroNoCreadoException;
import com.bachoco.model.PedidoTrasladoSapResponseDTO;
import com.bachoco.model.procedores.PedTrasladoArriboConfigDespachoDTO;
import com.bachoco.model.procedores.PedidoTrasladoArriboDTO;
import com.bachoco.model.procedores.PedidoTrasladoDTO;
import com.bachoco.persistence.config.SapProperties;
import com.bachoco.persistence.entity.MaterialEntity;
import com.bachoco.persistence.entity.PlantaEntity;
import com.bachoco.persistence.repository.MateriaJpalRepository;
import com.bachoco.persistence.repository.PlantaJpaRepository;
import com.bachoco.persistence.repository.jdbc.CatalogJdbcRepository;
import com.bachoco.persistence.repository.jdbc.PedidoTrasladoJdbcRepository;
import com.bachoco.port.PedidoTrasladoJdbcRepositoryPort;
import com.bachoco.secutiry.utils.BatchUtils;
import com.bachoco.service.ActualizacionTrasladosService;
import com.bachoco.utils.PedidoTrasladoUtil;
import com.bachoco.utils.StringExtractionUtils;
import com.bachoco.webclient.PedidoTrasladoSapWebClientoAdapter;


@Repository
public class PedidoTrasladoJdbcRepositoryAdapter implements PedidoTrasladoJdbcRepositoryPort {

	private final PedidoTrasladoJdbcRepository pedidoTrasladoJdbcRepository;
	private final PedidoTrasladoSapWebClientoAdapter pedidoTrasladoSapWebClientoAdapter;
	private final CatalogJdbcRepository catalogJdbcRepository;
	private final PlantaJpaRepository plantaJpaRepository;
	private final MateriaJpalRepository materiaJpalRepository;
	private final ActualizacionTrasladosService actualizacionTrasladosService;
	private final SapProperties sapProperties;
	private static final Logger logger = LoggerFactory.getLogger(PedidoTrasladoJdbcRepositoryAdapter.class);
	

	public PedidoTrasladoJdbcRepositoryAdapter(PedidoTrasladoJdbcRepository pedidoTrasladoJdbcRepository,
			PedidoTrasladoSapWebClientoAdapter pedidoTrasladoSapWebClientoAdapter,
			CatalogJdbcRepository catalogJdbcRepository, PlantaJpaRepository plantaJpaRepository,
			MateriaJpalRepository materiaJpalRepository, SapProperties sapProperties,
			ActualizacionTrasladosService actualizacionTrasladosService) {
		this.pedidoTrasladoJdbcRepository = pedidoTrasladoJdbcRepository;
		this.pedidoTrasladoSapWebClientoAdapter = pedidoTrasladoSapWebClientoAdapter;
		this.catalogJdbcRepository = catalogJdbcRepository;
		this.plantaJpaRepository = plantaJpaRepository;
		this.materiaJpalRepository = materiaJpalRepository;
		this.sapProperties = sapProperties;
		this.actualizacionTrasladosService=actualizacionTrasladosService;
	}
	
	private List<PedidoTrasladoSapResponseDTO> filterPlantasNoexistentesEnBd(List<PedidoTrasladoSapResponseDTO> pedidos,List<String> plantasSap,String planta){
		List<PedidoTrasladoSapResponseDTO> pedidosFiltrados= new ArrayList<>();
		if (planta != null && !planta.trim().isEmpty()) {
		    // Caso 1: Filtrar por planta destino específica
		    String plantaDestinoFiltro = planta.trim();
		    pedidosFiltrados = pedidos.stream()
		        .filter(pedido -> plantaDestinoFiltro.equals(pedido.getPlantaDestino()))
		        .collect(Collectors.toList());
		} else {
		    // Caso 2: Filtrar excluyendo plantas no existentes
		    Set<String> plantasNoExistentes = this.validateExistePlantaDestinoSet(plantasSap);
		    if (plantasNoExistentes.isEmpty()) {
		        // Si no hay plantas no existentes, mantener todos los pedidos
		    	pedidosFiltrados = new ArrayList<>(pedidos);
		    } else {
		        // Filtrar EXCLUYENDO las plantas no existentes (una sola operación)
		    	pedidosFiltrados = pedidos.stream()
		            .filter(pedido -> !plantasNoExistentes.contains(pedido.getPlantaDestino()))
		            .collect(Collectors.toList());
		    }
		}
		return pedidosFiltrados;
	}
	
	private Set<String> folioPedidoTrasladoAndPosicion(List<PedidoTrasladoSapResponseDTO> pedTraslado){
		return pedTraslado.stream()
			    .filter(pedido -> pedido.getPedidoDeComprasAsociado() != null)
			    .filter(pedido -> {
			        String posicion = pedido.getPosicion();
			        return posicion != null && !posicion.trim().isEmpty();
			    })
			    .map(pedido -> buildFolioPedCompraAnPosicion(
			        String.valueOf(pedido.getPedidoDeComprasAsociado()), 
			        pedido.getPosicion()
			    ))
			    .filter(folio -> !folio.trim().isEmpty())
			    .collect(Collectors.toSet());
	}
	private Set<String> folioNumPedidoCompraAsociado(List<PedidoTrasladoSapResponseDTO> pedTraslado){
		return pedTraslado.stream()
			    .filter(pedido -> pedido.getPedidoDeComprasAsociado() != null)
			    .map(pedido ->String.valueOf(pedido.getPedidoDeComprasAsociado()))
			    .filter(folio -> !folio.trim().isEmpty())
			    .collect(Collectors.toSet());
	}
	private List<PedidoTrasladoSapResponseDTO> deletePedTrasladoNoCoincideFolioAndFolioIsNull(List<PedidoTrasladoSapResponseDTO> pedTraslado,List<String> pedCompra){
		if (!pedCompra.isEmpty()) {
		    // Pre-procesar TODAS las exclusiones una sola vez
		    Set<CompraAsociadoKey> exclusionKeys = pedCompra.stream()
		        .map(value -> new CompraAsociadoKey(value.trim()))
		        .collect(Collectors.toSet());
		    
		    // Una sola operación de filtrado
		    pedTraslado.removeIf(pedido -> 
		        exclusionKeys.contains(new CompraAsociadoKey(
		            String.valueOf(pedido.getPedidoDeComprasAsociado())
		        ))
		    );
		}
		return pedTraslado;
	}
	@Override
	public List<PedidoTrasladoDTO> findByFilterSiloAndMaterialAnFecha(String claveSilo,String claveMaterial,
			String plantaDestino,String fechaInicio, String fechaFin) {
		List<PedidoTrasladoDTO> response=new ArrayList<>();
		List<String> foliosExists=new ArrayList<>();
		List<String> materialesSap=new ArrayList<String>();
		List<String> plantasDestinoUnicos=new ArrayList<String>();
		try {
			List<PedidoTrasladoSapResponseDTO> pedCompraSap=this.pedidoTrasladoSapWebClientoAdapter.findAllPedTraslado(claveSilo,claveMaterial,plantaDestino,toReplace(fechaInicio),toReplace(fechaFin),sapProperties.getUrl());
			List<PedidoTrasladoSapResponseDTO> pedidosSapOriginal = new ArrayList<>(pedCompraSap);
			
			//extraer solo plantas unicos que no sean nulos
			plantasDestinoUnicos=StringExtractionUtils.extraerStringsUnicosNoVacios(pedidosSapOriginal, PedidoTrasladoSapResponseDTO::getPlantaDestino);
			
			String resultValidacionPlantas=this.validateExistePlantaDestino(plantasDestinoUnicos);
			if(!resultValidacionPlantas.equals("")) {
				throw  new NotFoundPlantaDestinoException(resultValidacionPlantas);
			}

			//limpieza de lista de pedido tarslado de plantas no existentes en base de datos
			List<PedidoTrasladoSapResponseDTO> pedTrasladoFiltroPorPlantas=filterPlantasNoexistentesEnBd(pedidosSapOriginal,plantasDestinoUnicos,plantaDestino);
			
			//crear una lista de materiales de pedido traslado por plantas no existentes
			materialesSap=StringExtractionUtils.extraerStringsUnicosNoVacios(pedTrasladoFiltroPorPlantas,PedidoTrasladoSapResponseDTO::getMaterial);
			String resultValidacionMateriales=this.validateExisteMateriales(materialesSap);
			if(!resultValidacionMateriales.equals("")) {
				throw  new NotFoundMaterialException(resultValidacionMateriales);
			}
			
			//validacion que existan los pedido traslado que este asociado a pedido compra
			Set<String> folioNumPedidoCompraAsociado=folioNumPedidoCompraAsociado(pedTrasladoFiltroPorPlantas);
			//lista de pedido compra exietentes en la base de datos
			List<String> resultValidacionPedidoCompras=this.validateExistePedidoCompra(folioNumPedidoCompraAsociado,claveSilo,claveMaterial);
			//se filtran los pedidos traslados que no tengan pedido compra asociado y posision
			List<PedidoTrasladoSapResponseDTO> pedidosSapFiltradosFolioNull=PedidoTrasladoUtil.cleanPedidoTrasladoPedCompraAndPosicionIsNull(pedTrasladoFiltroPorPlantas);
			//se filtran los pedidos traslados que si tengan pedido compra asociado de pedido compra en la base de datos
			pedidosSapFiltradosFolioNull=deletePedTrasladoNoCoincideFolioAndFolioIsNull(pedidosSapFiltradosFolioNull,resultValidacionPedidoCompras);
			if(pedidosSapFiltradosFolioNull.size()>0) {
				List<String> folios=pedidosSapFiltradosFolioNull.stream().map(s->s.getNumeroPedTraslado().trim()).toList();
				List<List<String>> batches = BatchUtils.partition(folios, 100);
				for (List<String> batch : batches) {
					foliosExists.addAll(this.pedidoTrasladoJdbcRepository.findAllFoliosPedTrasladoExist(batch,claveSilo,claveMaterial));
				}
				Set<String> foliosExist = new HashSet<>(foliosExists);
				List<PedidoTrasladoSapResponseDTO> pedidosSapNoExistBd=pedidosSapFiltradosFolioNull.stream().filter(p->!foliosExist.contains(p.getNumeroPedTraslado())).toList();
				if(pedidosSapNoExistBd.size()>0){
					logger.info("REGISTRO ");
					this.pedidoTrasladoJdbcRepository.savePedidoTraslado(pedidosSapNoExistBd,claveSilo);
				}
				actualizacionTrasladosService.procesarActualizacionTraslados(pedidosSapFiltradosFolioNull,claveSilo,claveMaterial);
				for (List<String> batch : batches) {
					logger.info("CONSULTA DE REGISTROS DE LA BD DE FOLIOS DE SAP ");
					response.addAll(this.pedidoTrasladoJdbcRepository.findAllByFolioNumCompra(batch,claveSilo,claveMaterial));
				}
			}
		}catch (NotFoundPlantaDestinoException ex) {
			throw ex;
		}catch(NotFoundMaterialException ex) {
			throw ex;
		}catch(NotFoundPedCompraException ex) {
			throw ex;
		}catch (RegistroNoCreadoException e) {
			throw  new RegistroNoCreadoException(e.getMessage());
		}catch (Exception e) {
			throw e;
		}
		return response;
	}
	
	@Override
	public void executeDowloadPedTrasladoBySap(String claveSilo, String claveMaterial,String plantaDestino, String fechaInicio,
			String fechaFin) {
		List<String> foliosExists=new ArrayList<>();
		try {
			List<PedidoTrasladoSapResponseDTO> pedidosSap=this.pedidoTrasladoSapWebClientoAdapter.findAllPedTraslado(claveSilo,claveMaterial,plantaDestino,toReplace(fechaInicio),toReplace(fechaFin),sapProperties.getUrl());
			if(pedidosSap.size()>0) {
				List<String> folios=pedidosSap.stream().map(s->s.getNumeroPedTraslado().trim()).toList();
				List<List<String>> batches = BatchUtils.partition(folios, 100);
				for (List<String> batch : batches) {
					foliosExists.addAll(this.pedidoTrasladoJdbcRepository.findAllFoliosPedTrasladoExist(batch,claveSilo,claveMaterial));
				}
				Set<String> foliosExist = new HashSet<>(foliosExists);
				List<PedidoTrasladoSapResponseDTO> pedidosSapNoExistBd=pedidosSap.stream().filter(p->!foliosExist.contains(p.getNumeroPedTraslado().toString().trim())).toList();
				if(pedidosSapNoExistBd.size()>0){
					this.pedidoTrasladoJdbcRepository.savePedidoTraslado(pedidosSapNoExistBd,claveSilo);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw  new RegistroNoCreadoException(e.getMessage());
		}
	}

	@Override
	public List<PedidoTrasladoArriboDTO> findByFilterProgramArribo(Integer siloId, String planta,Integer materialId) {
		return this.pedidoTrasladoJdbcRepository.obtenerPedidosTrasladoParaArribo(siloId,planta, materialId);
	}
	
	@Override
	public List<PedTrasladoArriboConfigDespachoDTO> findByPedTrasladoByConfDespacho(Integer siloId,
			Integer materialId,String fechaInicio,String fechaFin) {
		return this.pedidoTrasladoJdbcRepository.findByPedidosTrasladoParaConfDespacho(siloId, materialId,fechaInicio,fechaFin);
	}
	@Override
	public List<PedidoTrasladoDTO> findByFiltersCantidadDisponible(Integer siloId,Integer materialId,
			String fechaInicio, String fechaFin) {
		return pedidoTrasladoJdbcRepository.obtenerPedidosFiltradosCantidadDisponible(siloId,materialId,fechaInicio,fechaFin);
	}
	public String folioPedTraslado(String numPed,String posicion) {
		return numPed+"-"+posicion;
	}
	private String toReplace(String value) {
		return value.replace("-", "");
	}
	private String buildFolioPedCompraAnPosicion(String pedCompra,String posicion) {
		return pedCompra.trim().concat("-").concat(posicion.trim());
	}
	
	private String validateExistePlantaDestino(List<String> plantas) {
		// 1. Obtiene las plantas de SAP y las convierte a un Set de nombres (ya lo tienes).
		Set<String> plantasSap = new HashSet<>(plantas);
		// 2. Obtiene TODAS las plantas de la DB (JpaRepository)
		List<PlantaEntity> resultDB = this.plantaJpaRepository.findAll();
		// 3. ¡Nuevo Set! Crea un Set de NOMBRES de las plantas que ya existen en la DB.
		// Esto es el corazón de la eficiencia para la comparación.
		Set<String> nombresPlantaDB = resultDB.stream()
		    .map(PlantaEntity::getPlanta)
		    .collect(Collectors.toSet());
		// 4. Filtra el Set de SAP para encontrar cuáles NO existen en el Set de la DB.
		// El stream se ejecuta sobre el Set de SAP (plantasSap).
		Set<String> plantasNoExisten = plantasSap.stream()
		    .filter(nombre -> !nombresPlantaDB.contains(nombre))
		    .collect(Collectors.toSet());
		if(plantasNoExisten.size()>0) {
			return plantasNoExisten.stream().collect(Collectors.joining(","));
		}else {
			return "";
		}
	}

	private Set<String> validateExistePlantaDestinoSet(List<String> plantas) {
		Set<String> plantasSap = new HashSet<>(plantas);
		List<PlantaEntity> resultDB = this.plantaJpaRepository.findAll();
		Set<String> nombresPlantaDB = resultDB.stream()
		    .map(PlantaEntity::getPlanta)
		    .collect(Collectors.toSet());
		Set<String> plantasNoExisten = plantasSap.stream()
		    .filter(nombre -> !nombresPlantaDB.contains(nombre))
		    .collect(Collectors.toSet());
		return plantasNoExisten;
	}
	
	private String validateExisteMateriales(List<String> materiales) {
		Set<String> materialesSap = new HashSet<>(materiales);
		List<MaterialEntity> resultDB = this.materiaJpalRepository.findAll();
		Set<String> numMaterialDB = resultDB.stream()
		    .map(MaterialEntity::getNumero)
		    .collect(Collectors.toSet());
		Set<String> materialesNoExisten = materialesSap.stream()
		    .filter(nombre -> !numMaterialDB.contains(nombre))
		    .collect(Collectors.toSet());
		if(materialesNoExisten.size()>0) {
			return materialesNoExisten.stream().collect(Collectors.joining(","));
		}else {
			return "";
		}
	}
	
	private List<String> validateExistePedidoCompra(Set<String> pedCompraAsociadoSap,String claveSilo,String claveMaterial) {
		if(pedCompraAsociadoSap.size()==0) return Collections.EMPTY_LIST;
		List<String> resultDB = this.catalogJdbcRepository.findAllClavePedCompra(claveSilo,claveMaterial);
		Set<String> pedCompraDB = new HashSet<>(resultDB);
		Set<String> pedCompraNoExisten = pedCompraAsociadoSap.stream()
		    .filter(nombre -> !pedCompraDB.contains(nombre))
		    .collect(Collectors.toSet());
		return pedCompraNoExisten.stream().collect(Collectors.toList());
	}

}
