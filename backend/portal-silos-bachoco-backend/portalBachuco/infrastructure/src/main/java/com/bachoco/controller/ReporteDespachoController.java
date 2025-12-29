package com.bachoco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.model.ReporteDespacho;
import com.bachoco.service.usecase.ReporteDespachoUseCase;

@RestController
@RequestMapping("/v1/reporte-despacho")
public class ReporteDespachoController {

	private final ReporteDespachoUseCase reporteDespachoUseCase;

	public ReporteDespachoController(ReporteDespachoUseCase reporteDespachoUseCase) {
		this.reporteDespachoUseCase = reporteDespachoUseCase;
	}
	
	@GetMapping
	public ResponseEntity<List<ReporteDespacho>> findAll(@RequestParam Integer siloId,
			@RequestParam String fechaI,@RequestParam String fechaF){
		List<ReporteDespacho> response=this.reporteDespachoUseCase.findAll(siloId, fechaI, fechaF);
		return new ResponseEntity<List<ReporteDespacho>>(response,HttpStatus.OK);
	}
	
}
