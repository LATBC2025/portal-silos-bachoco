package com.bachoco.model.service;

import java.io.IOException;

import com.bachoco.model.Documento;

public interface DocumentoRepositoryPort {

	String guardar(Documento documento,String ruta,Integer pedidoCompraId) throws IOException;
    boolean eliminar(String documentoId,String ruta,Integer pedidoCompraId)throws IOException;
    byte[] dowload(String documentoId,String ruta)throws IOException;
}
