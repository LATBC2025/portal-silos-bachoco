package com.bachoco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.model.ProgramArriboRequest;
import com.bachoco.model.procedores.PedidoTrasladoArriboDTO;
import com.bachoco.service.usecase.PedidoTrasladoJdbcUseCase;
import com.bachoco.service.usecase.ProgramArriboUseCase;

@RestController
@RequestMapping("/v1/program-arribo")
public class ProgramArriboController {

	private final ProgramArriboUseCase programArriboUseCase;
	private final PedidoTrasladoJdbcUseCase pedidoTrasladoJdbcUseCase;

	public ProgramArriboController(ProgramArriboUseCase programArriboUseCase,
			PedidoTrasladoJdbcUseCase pedidoTrasladoJdbcUseCase) {
		this.programArriboUseCase = programArriboUseCase;
		this.pedidoTrasladoJdbcUseCase = pedidoTrasladoJdbcUseCase;
	}

	@GetMapping("/{claveSilo}/{material}")
	public ResponseEntity<String> saveProgramArivo(@PathVariable String claveSilo,@PathVariable String material) {
		Double response=this.programArriboUseCase.stockSilo(claveSilo,material);
		return new ResponseEntity<String>(String.valueOf(response),HttpStatus.OK);
	}
	
	@GetMapping("/pedido-traslado")
	public ResponseEntity<List<PedidoTrasladoArriboDTO>> findPedidoTraslado(
			@RequestParam String claveSilo,@RequestParam String clavePlanta,
			@RequestParam String claveMaterial){
		Integer siloId=Integer.parseInt(claveSilo);
		Integer materialId=Integer.parseInt(claveMaterial);
		List<PedidoTrasladoArriboDTO> response=this.pedidoTrasladoJdbcUseCase.findByFilterProgramArribo(siloId,clavePlanta,materialId);
		return new ResponseEntity<List<PedidoTrasladoArriboDTO>>(response,HttpStatus.OK);
	}
	
	@GetMapping("/findTotal-ProgramArriboByNumPedTraslado")
	public ResponseEntity<Float> findPesoNetoByNumPedTraslado(
			@RequestParam String numPedidoTraslados, @RequestParam String claveSilo,@RequestParam String claveMaterial,@RequestParam String clavePlanta,
			@RequestParam String fechaInicio,@RequestParam String fechaFin){
		Float response=this.programArriboUseCase.findPesoNetoByNumPedTraslado(List.of(numPedidoTraslados.split(",")), claveSilo,claveMaterial,clavePlanta, fechaInicio,fechaFin);
		return new ResponseEntity<Float>(response,HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Void> saveProgramArivo(@RequestBody List<ProgramArriboRequest> req){
		String response=this.programArriboUseCase.saveProgramArribo(req);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}

