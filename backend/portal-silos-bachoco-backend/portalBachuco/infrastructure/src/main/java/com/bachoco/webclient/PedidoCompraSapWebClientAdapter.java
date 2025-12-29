package com.bachoco.webclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bachoco.dto.PedidoCompraSapClientWrapper;
import com.bachoco.exception.SapConnectionException;
import com.bachoco.mapper.PedidoSapClientMapper;
import com.bachoco.model.PedidoSapResponseDTO;
import com.bachoco.persistence.config.HttpUrlConnectionClient;
import com.bachoco.persistence.config.SapProperties;
import com.bachoco.port.PedidoCompraSapPort;
import com.bachoco.utils.UrlConnectionUtil;
import com.bachoco.utils.WebClientUtils;

@Component
public class PedidoCompraSapWebClientAdapter implements PedidoCompraSapPort {

	private final SapProperties sapProperties;
	@Value("${app.url.endpoit.sap.filter.silo}")
	private int filtersiloSilo;
	private static final Logger logger = LoggerFactory.getLogger(PedidoCompraSapWebClientAdapter.class);

	public PedidoCompraSapWebClientAdapter(SapProperties sapProperties) {
		this.sapProperties = sapProperties;
	}

	@Override
	public List<PedidoSapResponseDTO> findAllPedidoCompra(String claveSilo, String claveMaterial,String fechaInicio, String fechaFin,String rutaUrl) {
		List<PedidoSapResponseDTO> response= new ArrayList<>();
		String endpoint;
		logger.info("===============PEDIDO COMPRA====================== ");
		logger.info("URL::: "+rutaUrl);
		HttpUrlConnectionClient client=new HttpUrlConnectionClient(rutaUrl,
				sapProperties.getUserName(),sapProperties.getPassWord());
		if(filtersiloSilo==1) {
			endpoint=WebClientUtils.buildUrlPedidoCompra(claveSilo, "", "", "", false); 
		}else {
			endpoint=WebClientUtils.buildUrlPedidoCompra(claveSilo, fechaInicio, fechaFin,Integer.valueOf(claveMaterial), true); 
		}
		try {
			String jsonResponse = client.get(endpoint);
			boolean isValidJson=UrlConnectionUtil.isValidJsonResponse(jsonResponse);
		    if (isValidJson==false) {
		    	 logger.warn("Respuesta de SAP vacía (posiblemente sin datos para los filtros).");
			     return response;
		    }
		     PedidoCompraSapClientWrapper wrapper = UrlConnectionUtil.getObjectMapper()
		                .readValue(jsonResponse, PedidoCompraSapClientWrapper.class);
		     return extractItemsFromWrapper(wrapper);
		}catch (IOException e) {
			e.printStackTrace();
			logger.error("Hubo un error de conexion a sap de pedido compra: "+e.getMessage());
			throw new SapConnectionException("Hubo error en conexion a SAP: "+e.getCause());
		}
	}
	
	private List<PedidoSapResponseDTO> extractItemsFromWrapper(PedidoCompraSapClientWrapper wrapper) {
	    if (wrapper != null && wrapper.getItems() != null && !wrapper.getItems().isEmpty()) {
	        return wrapper.getItems().stream()
	                .map(PedidoSapClientMapper::toDomain)
	                .collect(Collectors.toList());
	    } else {
	        return Collections.emptyList();
	    }
	}
	

}
