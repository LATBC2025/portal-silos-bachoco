package com.bachoco.utils;

import org.springframework.web.util.UriComponentsBuilder;

public class WebClientUtils {

	public static String buildUrlPedidoCompra(String silo,String fechaInicio,String fechaFin,
			String material,boolean isPedidoTraslado) {
		UriComponentsBuilder uri= UriComponentsBuilder.fromPath("/consulta-pedido-compra");
				if(silo.trim().length()!=0) {
					uri.queryParam("Silo", silo);
				}
				if(fechaInicio.trim().length()!=0) {
					uri.queryParam("FechaIni", fechaInicio);
				}
			    if(fechaFin.trim().length()!=0) {
			    	uri.queryParam("FechaFin", fechaFin);
			    }
			    if(material.trim().length()!=0) {
			    	uri.queryParam("Material", material);
			    }
			    return uri.build().toString();
	}
	
	public static String buildUrlPedidoCompra(String silo,String fechaInicio,String fechaFin,
			Integer material,boolean isPedidoTraslado) {
		UriComponentsBuilder uri= UriComponentsBuilder.fromPath("/consulta-pedido-compra");
				if(silo.trim().length()!=0) {
					uri.queryParam("Silo", silo);
				}
				if(fechaInicio.trim().length()!=0) {
					uri.queryParam("FechaIni", fechaInicio);
				}
			    if(fechaFin.trim().length()!=0) {
			    	uri.queryParam("FechaFin", fechaFin);
			    }
			    if(material.toString().length()!=0) {
			    	uri.queryParam("Material", material);
			    }
			    return uri.build().toString();
	}
	
	public static String buildUrlPedioTraslado(String silo,String fechaInicio,String fechaFin,
			String material,boolean isPedidoTraslado) {
		UriComponentsBuilder uri= UriComponentsBuilder.fromPath("/consulta-pedido-compra");
				if(silo.trim().length()!=0) {
					uri.queryParam("Silo", silo);
				}
				if(fechaInicio.trim().length()!=0) {
					uri.queryParam("FechaIni", fechaInicio);
				}
			    if(fechaFin.trim().length()!=0) {
			    	uri.queryParam("FechaFin", fechaFin);
			    }
			    if(material.trim().length()!=0) {
			    	uri.queryParam("Material", material);
			    }
			    if(isPedidoTraslado) {
			    	uri.queryParam("Traslado", "X");
			    }
			    return uri.build().toString();
	}
	public static String buildUrlPedioTraslado(String silo,String fechaInicio,String fechaFin,
			Integer material,String plantaDestino,boolean isPedidoTraslado) {
		UriComponentsBuilder uri= UriComponentsBuilder.fromPath("/consulta-pedido-compra");
				if(silo.trim().length()!=0) {
					uri.queryParam("Silo", silo);
				}
				if(fechaInicio.trim().length()!=0) {
					uri.queryParam("FechaIni", fechaInicio);
				}
			    if(fechaFin.trim().length()!=0) {
			    	uri.queryParam("FechaFin", fechaFin);
			    }
			    if(material.toString().length()!=0) {
			    	uri.queryParam("Material", material);
			    }
			    if(plantaDestino.trim().length()!=0) {
			    	uri.queryParam("Param2", plantaDestino);
			    }
			    if(isPedidoTraslado) {
			    	uri.queryParam("Traslado", "X");
			    }
			    return uri.build().toString();
	}
	
	public static String buildUrlStockSilo(String silo,Integer material,boolean isPedidoTraslado) {
		UriComponentsBuilder uri= UriComponentsBuilder.fromPath("/consulta-pedido-compra");
				if(silo.trim().length()!=0) {
					uri.queryParam("Silo", silo);
				}
				if(material.toString().length()!=0) {
					uri.queryParam("Material", material);
				}
			    if(isPedidoTraslado) {
			    	uri.queryParam("Param1", "X");
			    }
			    return uri.build().toString();
	}
}
