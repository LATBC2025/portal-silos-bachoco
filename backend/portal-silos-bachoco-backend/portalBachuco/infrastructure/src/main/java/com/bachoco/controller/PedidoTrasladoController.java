package com.bachoco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.model.procedores.PedTrasladoArriboConfigDespachoDTO;
import com.bachoco.model.procedores.PedidoTrasladoArriboDTO;
import com.bachoco.model.procedores.PedidoTrasladoDTO;
import com.bachoco.service.usecase.PedidoTrasladoJdbcUseCase;

@RestController
@RequestMapping("/v1/pedido-traslado")
public class PedidoTrasladoController {

	private final PedidoTrasladoJdbcUseCase pedidoTrasladoJdbcUseCase;

	public PedidoTrasladoController(PedidoTrasladoJdbcUseCase pedidoTrasladoJdbcUseCase) {
		this.pedidoTrasladoJdbcUseCase = pedidoTrasladoJdbcUseCase;
	}
	
	@GetMapping("/filters")
	public ResponseEntity<List<PedidoTrasladoDTO>> findAllByFilters(@RequestParam String claveSilo,@RequestParam String claveMaterial,
			@RequestParam String plantaDestino,@RequestParam String fechaInicio,@RequestParam String fechaFin){
		List<PedidoTrasladoDTO> response=this.pedidoTrasladoJdbcUseCase.findByFilters(claveSilo, claveMaterial,plantaDestino, fechaInicio, fechaFin);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping("/filters-dowload-ped-traslado")
	public ResponseEntity<Void> findAllDowloadPedTraslado(@RequestParam String claveSilo,@RequestParam String claveMaterial,
			@RequestParam String plantaDestino,@RequestParam String fechaInicio,@RequestParam String fechaFin){
		this.pedidoTrasladoJdbcUseCase.executeDowloadPedTrasladoBySap(claveSilo, claveMaterial,plantaDestino, fechaInicio, fechaFin);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	
	@GetMapping("/filters/cantidad-disponible")
	public ResponseEntity<List<PedidoTrasladoDTO>> findAllpedidosCantidad(
			@RequestParam String siloId,
			@RequestParam String materialId,
			@RequestParam String fechaInicio,
			@RequestParam String fechaFin){
		List<PedidoTrasladoDTO> response=this.pedidoTrasladoJdbcUseCase
				.findByFiltersCantidadDisponible(Integer.parseInt(siloId),Integer.parseInt(materialId),fechaInicio, fechaFin);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	
	@GetMapping("/filters/program")
	public ResponseEntity<List<PedidoTrasladoArriboDTO>> findAllByFilterProgramArribo(@RequestParam Integer siloId,@RequestParam Integer materialId,
			@RequestParam String planta){
		List<PedidoTrasladoArriboDTO> response=this.pedidoTrasladoJdbcUseCase.findByFilterProgramArribo(siloId,planta,materialId);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping("/filters/conf-despacho")
	public ResponseEntity<List<PedTrasladoArriboConfigDespachoDTO>> findByPedTrasladoByConfDespacho(@RequestParam Integer siloId,@RequestParam Integer materialId,
			@RequestParam String fechaInicio,String fechaFin){
		List<PedTrasladoArriboConfigDespachoDTO> response=this.pedidoTrasladoJdbcUseCase.findByPedTrasladoByConfDespacho(siloId,materialId,fechaInicio,fechaFin);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
