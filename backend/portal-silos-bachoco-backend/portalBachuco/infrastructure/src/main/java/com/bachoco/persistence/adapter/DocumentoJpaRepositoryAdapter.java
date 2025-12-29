package com.bachoco.persistence.adapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

import com.bachoco.model.Documento;
import com.bachoco.model.service.DocumentoRepositoryPort;
import com.bachoco.persistence.repository.PedidoCompraJpaRepository;
import com.bachoco.utils.DocumentoUtil;

@Component
public class DocumentoJpaRepositoryAdapter implements DocumentoRepositoryPort {

	private final PedidoCompraJpaRepository pedidoCompraJpaRepository;

	public DocumentoJpaRepositoryAdapter(PedidoCompraJpaRepository pedidoCompraJpaRepository) {
		this.pedidoCompraJpaRepository = pedidoCompraJpaRepository;
	}

	@Override
	public String guardar(Documento documento, String ruta,Integer pedidoCompraId) throws IOException {
		String nombreDocumento = DocumentoUtil.toUUID();
		Path uploadPath = Paths.get(ruta);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		Path destinationPath = Paths.get(ruta, nameFileConcatExtencion(nombreDocumento,DocumentoUtil.typeDocumentoPdf));
		Files.createDirectories(destinationPath.getParent());
		Files.write(destinationPath, documento.getContenido());
		String newPathFile=ruta.concat(nombreDocumento);
		pedidoCompraJpaRepository.actualizarUrlDocument(newPathFile,documento.getNombre(), pedidoCompraId);
		return newPathFile;
	}

	@Override
	public boolean eliminar(String documentoId, String ruta,Integer pedidoCompraId) throws IOException {
		Path path = Paths.get(ruta.concat(documentoId));
		if (Files.exists(path)) {
			boolean isDelete=Files.deleteIfExists(path);
			pedidoCompraJpaRepository.actualizarUrlDocument("","", pedidoCompraId);
			return isDelete;
		}else {
			return false;
		}
	}

	@Override
	public byte[] dowload(String documentoId,String ruta) throws IOException {
		byte[] data=null;
		Path filePath = Paths.get(getUrlDocument(ruta,documentoId));
		if (!Files.exists(filePath)) {
			throw new IOException("El archivo no se encontró en la ruta: " + filePath);
		}
		return Files.readAllBytes(filePath);
	}
	
	private String getUrlDocument(String ruta, String documentId) {
		return ruta.concat(documentId.concat(DocumentoUtil.PUNTO_VALUE.concat(DocumentoUtil.typeDocumentoPdf)));
	}

	private String nameFileConcatExtencion(String filename,String extencion) {
		return filename.concat(DocumentoUtil.PUNTO_VALUE.concat(extencion));
	}
}
