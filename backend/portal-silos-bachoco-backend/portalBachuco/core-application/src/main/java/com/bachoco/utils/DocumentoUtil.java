package com.bachoco.utils;

import java.util.UUID;

public class DocumentoUtil {

	public final static String RUTA_ALMACENAMIENTO = "archivos_pps/pedido_compra/";
	public final static String RUTA_ALMAC_EMPTY= "";
	public final static String typeDocumentoPdf = "pdf";
	public final static String PUNTO_VALUE=".";

	public static String toUUID() {
		return UUID.randomUUID().toString();
	}

	public static String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
			return fileName.substring(dotIndex + 1);
		}
		return typeDocumentoPdf;
	}

}
