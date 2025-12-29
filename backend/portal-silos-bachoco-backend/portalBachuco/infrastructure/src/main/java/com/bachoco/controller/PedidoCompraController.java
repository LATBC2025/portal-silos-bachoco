package com.bachoco.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bachoco.model.procedores.PedidoCompraDTO;
import com.bachoco.service.usecase.PedidoCompraUsecase;
import com.bachoco.utils.DocumentoUtil;

@RestController
@RequestMapping("/v1/pedido-compra")
public class PedidoCompraController {

	private final PedidoCompraUsecase pedidoCompraUsecase;

	public PedidoCompraController(PedidoCompraUsecase pedidoCompraUsecase) {
		this.pedidoCompraUsecase = pedidoCompraUsecase;
	}
	@GetMapping("/filters")
	public ResponseEntity<List<PedidoCompraDTO>> findAllByFilter(@RequestParam String claveSilo,
			@RequestParam String claveMaterial, @RequestParam String plantaDestino,@RequestParam String fechaInicio, @RequestParam String fechaFin) {
		List<PedidoCompraDTO> response = this.pedidoCompraUsecase.findAll(claveSilo, claveMaterial,plantaDestino, fechaInicio, fechaFin);
			return new ResponseEntity<List<PedidoCompraDTO>>(response, HttpStatus.OK);
	}
	@GetMapping("/filters-dowload-ped-compra")
	public ResponseEntity<Void> findAllDowloadPedCompra(@RequestParam String claveSilo,
			@RequestParam String claveMaterial,@RequestParam String plantaDestino, @RequestParam String fechaInicio, @RequestParam String fechaFin) {
		this.pedidoCompraUsecase.executePedidoCompraDowloadSap(claveSilo, claveMaterial,plantaDestino, fechaInicio, fechaFin);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	@PostMapping("/upload-pdf")
	public ResponseEntity<Map<String, String>> saveFile(@RequestParam("file") MultipartFile file,@RequestParam("pedidoCompraId") Integer pedidoCompraId) {
		 Map<String, String> resp = new HashMap();
		if (file.isEmpty()) {
			 resp.put("status", "error");
			 resp.put("message", "Por favor seleccione un archivo para subir.");
			 resp.put("uuid", "");
			return ResponseEntity.ok(resp);
		}
		try {
			String extencion =DocumentoUtil.getFileExtension(file.getOriginalFilename());
			String uuid=pedidoCompraUsecase.saveDocument(file.getBytes(), file.getOriginalFilename(),DocumentoUtil.RUTA_ALMACENAMIENTO,extencion,pedidoCompraId);
		    resp.put("status", "ok");
		    resp.put("message", "Archivo subido correctamente");
		    resp.put("uuid", uuid);
			return ResponseEntity.ok(resp);
		} catch (IOException e) {
			e.printStackTrace();
			 resp.put("status", "error");
			 resp.put("message", "Fallo al subir el archivo: "+e.getMessage());
			 resp.put("uuid", "");
			return ResponseEntity.ok(resp);
		}
	}
	
	@DeleteMapping("{pedidoCompraId}/{documentId}")
	public ResponseEntity<String> delete(@PathVariable Integer pedidoCompraId,@PathVariable String documentId){
		boolean eliminado = pedidoCompraUsecase.deleteDocumento(documentId.concat(DocumentoUtil.PUNTO_VALUE.concat(DocumentoUtil.typeDocumentoPdf)),DocumentoUtil.RUTA_ALMACENAMIENTO,pedidoCompraId);
        if (eliminado) {
            return ResponseEntity.ok("1");
        } else {
            return ResponseEntity.ok("2");
        }
	}
	
	@GetMapping("/dowload-pdf/{fileName}")
    public ResponseEntity<Resource> descargarDocumento(@PathVariable String fileName) throws IOException {
        try {
        	byte[] data=this.pedidoCompraUsecase.dowload(fileName,DocumentoUtil.RUTA_ALMACENAMIENTO);
             Resource resource = new ByteArrayResource(data, fileName);
            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
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

	private String getUrlDocument(String ruta, String documentId) {
		return ruta.concat(documentId.concat(DocumentoUtil.PUNTO_VALUE.concat(DocumentoUtil.typeDocumentoPdf)));
	}
	
	private String getUrlDocumentAndType(String ruta, String documentId) {
		return ruta.concat(documentId.concat(DocumentoUtil.PUNTO_VALUE.concat(DocumentoUtil.typeDocumentoPdf)));
	}
}
