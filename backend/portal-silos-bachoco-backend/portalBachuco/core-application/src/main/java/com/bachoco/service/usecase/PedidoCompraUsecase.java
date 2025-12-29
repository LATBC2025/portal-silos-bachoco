package com.bachoco.service.usecase;

import java.io.IOException;
import java.util.List;

import com.bachoco.model.Documento;
import com.bachoco.model.procedores.PedidoCompraDTO;
import com.bachoco.model.service.DocumentoRepositoryPort;
import com.bachoco.port.PedidoCompraJdbcRepositoryPort;
import com.bachoco.utils.DocumentoUtil;

public class PedidoCompraUsecase {
	
	private final PedidoCompraJdbcRepositoryPort pedidoCompraJdbcRepositoryPort;
	private final DocumentoRepositoryPort documentoRepositoryPort;

	public PedidoCompraUsecase(PedidoCompraJdbcRepositoryPort pedidoCompraJdbcRepositoryPort,
			DocumentoRepositoryPort documentoRepositoryPort) {
		this.pedidoCompraJdbcRepositoryPort = pedidoCompraJdbcRepositoryPort;
		this.documentoRepositoryPort = documentoRepositoryPort;
	}

	public List<PedidoCompraDTO> findAll(String claveSilo,String claveMaterial,String plantaDestino,String fechaInicio,String fechaFin){
		return this.pedidoCompraJdbcRepositoryPort.findByFilterSiloAndMaterialAnFecha(claveSilo, claveMaterial,plantaDestino, fechaInicio, fechaFin);
	}
	
	public void executePedidoCompraDowloadSap(String claveSilo,String claveMaterial,String plantaDestino,String fechaInicio,String fechaFin) {
		this.pedidoCompraJdbcRepositoryPort.executePedidoCompraByFilter(claveSilo, claveMaterial,plantaDestino, fechaInicio, fechaFin);
	}
	
	public List<PedidoCompraDTO> findAllPedidosCompraSap(String claveSilo,String claveMaterial,Integer materialId,String fechaInicio,String fechaFin){
		return this.pedidoCompraJdbcRepositoryPort.findAllComprasSapByFilters(claveSilo,claveMaterial, fechaInicio, fechaFin);
	}
	
	public String saveDocument(byte[] data,String fileName,String ruta,String extencion,Integer pedidoCompraId) {
		Documento documento=new Documento(data,fileName,ruta,extencion);
		try {
			return documentoRepositoryPort.guardar(documento,DocumentoUtil.RUTA_ALMACENAMIENTO,pedidoCompraId);
		}catch (IOException e) {
			new IOException("Error al guardar el documento");
		}catch (Exception e) {
			new Exception("Hubo un error");
		}
		return "";
	}
	
	public boolean deleteDocumento(String documentoId,String ruta,Integer pedidoCompraId) {
		try {
			return documentoRepositoryPort.eliminar(documentoId,ruta,pedidoCompraId);
		}catch (IOException e) {
			new IOException("Error al guardar el documento");
		}catch (Exception e) {
			new Exception("Hubo un error");
		}
		return false;
	}
	
	public byte[] dowload(String documentoId,String ruta) throws IOException {
		return this.documentoRepositoryPort.dowload(documentoId, ruta);
	}

}
