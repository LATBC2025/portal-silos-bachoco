package com.bachoco.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bachoco.model.ConfDespachoPesosRequest;
import com.bachoco.model.ConfirmDespachoResponse;
import com.bachoco.model.ConfirmacionDespachoRequest;
import com.bachoco.model.ConfirmacionDespachoResponse;
import com.bachoco.service.ReporteConfDespachoService;
import com.bachoco.service.usecase.ConfirmacionDespachoUseCase;

@RestController
@RequestMapping("/v1/confirmacion-despacho")
public class ConfirmacionDespachoController {
	
	private final ReporteConfDespachoService reporteConfDespachoService;
	private final ConfirmacionDespachoUseCase confirmacionDespachoUseCase;

	public ConfirmacionDespachoController(ReporteConfDespachoService reporteConfDespachoService,
			ConfirmacionDespachoUseCase confirmacionDespachoUseCase) {
		this.reporteConfDespachoService = reporteConfDespachoService;
		this.confirmacionDespachoUseCase = confirmacionDespachoUseCase;
	}

	@PostMapping//registra una confirmacion despacho con movimiento 351
	public ResponseEntity<ConfirmacionDespachoResponse> save(@RequestBody ConfirmacionDespachoRequest req){
		ConfirmacionDespachoResponse response =this.confirmacionDespachoUseCase.save(req);
		return new ResponseEntity<ConfirmacionDespachoResponse>(response,HttpStatus.OK);
	}
	
	@PutMapping("/update-sap")//actualiza la confirmacion despacho con movimiento de sap 351 y 352
	public ResponseEntity<ConfirmacionDespachoResponse> updateSap(@RequestBody ConfirmacionDespachoRequest req){
		ConfirmacionDespachoResponse response =this.confirmacionDespachoUseCase.updateSap(req);
		return new ResponseEntity<ConfirmacionDespachoResponse>(response,HttpStatus.OK);
	}
	
	@PutMapping("/update-sin-sap")//actualiza la confirmacion despacho sin movimientos a sap 
	public ResponseEntity<ConfirmacionDespachoResponse> updateSinSap(@RequestBody ConfirmacionDespachoRequest req){
		ConfirmacionDespachoResponse response =this.confirmacionDespachoUseCase.updateSinSap(req);
		return new ResponseEntity<ConfirmacionDespachoResponse>(response,HttpStatus.OK);
	}

	@GetMapping("/filter-list-conf-despacho")
	public ResponseEntity<List<ConfirmDespachoResponse>> findAllConfirmacionDespacho(@RequestParam String silo,
			@RequestParam String material,@RequestParam String fechaInicio,@RequestParam String fechaFin){
		List<ConfirmDespachoResponse>  response =this.confirmacionDespachoUseCase.findAllConfirmacionDespacho(silo,material,fechaInicio,fechaFin);
		return new ResponseEntity<List<ConfirmDespachoResponse>> (response,HttpStatus.OK);
	}
	@GetMapping("/filters-pesos")//calcula el peso neto de la confirmacion despacho que cambio el peso tara o bruto para decidir si hacer un 351 y 352
	public ResponseEntity<ConfirmacionDespachoResponse> validatePesos(@RequestParam Integer id,
			@RequestParam Float pesobruto,@RequestParam Float pesotara){
		ConfDespachoPesosRequest req=new ConfDespachoPesosRequest();
		req.setIdconfDespacho(id);
		req.setPesoBruto(pesobruto);
		req.setPesoTara(pesotara);
		ConfirmacionDespachoResponse response =this.confirmacionDespachoUseCase.esIgualLosPesosPorId(req);
		return new ResponseEntity<ConfirmacionDespachoResponse>(response,HttpStatus.OK);
	}
	
	@GetMapping("/filter-promedio-arribo/{siloId}")
	public ResponseEntity<Float> findCantidadPromedioArribo(@PathVariable Integer siloId){
		return new ResponseEntity<Float>(this.confirmacionDespachoUseCase.findPromedioCantidadDespacho(siloId),HttpStatus.OK);
	}
	
	
	@GetMapping("/dowload-pdf/{id}")//genera reporte en pdf de la confirmacion despacho
    public ResponseEntity<Resource> descargarReporte(@PathVariable Integer id) throws IOException {
        try {
        	ByteArrayOutputStream data=this.reporteConfDespachoService.generarPdfBoleta(id);
             Resource resource = new ByteArrayResource(data.toByteArray());
            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "ReporteConfDespacho" + "\"");
                return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
	
	@PostMapping("/delete")//elimina la confirmacion despacho
	public ResponseEntity<ConfirmacionDespachoResponse> delete(@RequestBody ConfirmacionDespachoRequest req){
		ConfirmacionDespachoResponse response =this.confirmacionDespachoUseCase.delete(req);
		return new ResponseEntity<ConfirmacionDespachoResponse>(response,HttpStatus.OK);
	}
}
