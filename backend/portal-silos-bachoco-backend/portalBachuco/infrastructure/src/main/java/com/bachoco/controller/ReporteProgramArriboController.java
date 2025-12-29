package com.bachoco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.model.ReportePorgramArribo;
import com.bachoco.service.usecase.ReporteProgramArriboUseCase;

@RestController
@RequestMapping("/v1/reporte-program-arribo")
public class ReporteProgramArriboController {

	private final ReporteProgramArriboUseCase reporteProgramArriboUseCase;

	public ReporteProgramArriboController(ReporteProgramArriboUseCase reporteProgramArriboUseCase) {
		this.reporteProgramArriboUseCase = reporteProgramArriboUseCase;
	}
	
	@GetMapping
	public ResponseEntity<List<ReportePorgramArribo>> findAll(
			@RequestParam Integer siloId,@RequestParam String fechaI,@RequestParam String fechaF){
		List<ReportePorgramArribo> arribos= this.reporteProgramArriboUseCase.findAll(siloId, fechaI, fechaF);
		return new ResponseEntity<List<ReportePorgramArribo>>(arribos,HttpStatus.OK);
	}
}
