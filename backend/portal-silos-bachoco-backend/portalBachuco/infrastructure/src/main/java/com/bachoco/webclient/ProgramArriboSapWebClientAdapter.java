package com.bachoco.webclient;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bachoco.dto.sap.programArribo.StockSiloResponse;
import com.bachoco.persistence.config.HttpUrlConnectionClient;
import com.bachoco.persistence.config.SapProperties;
import com.bachoco.port.ProgramArriboSapRepositoryPort;
import com.bachoco.utils.WebClientUtils;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@Component
public class ProgramArriboSapWebClientAdapter implements ProgramArriboSapRepositoryPort {

	private static final Logger logger = LoggerFactory.getLogger(ProgramArriboSapWebClientAdapter.class);
	private final SapProperties sapProperties;
	
	public ProgramArriboSapWebClientAdapter(SapProperties sapProperties) {
		this.sapProperties = sapProperties;
	}

	@Override
	public Double stockSilo(String claveSilo,String material, String rutaUrl) {
		HttpUrlConnectionClient client=new HttpUrlConnectionClient(rutaUrl,
				sapProperties.getUserName(),sapProperties.getPassWord());
		 //String endpoint = String.format("/consulta-pedido-compra?Silo=%s&Param1=%s",claveSilo,"X");
			String endpoint=WebClientUtils.buildUrlStockSilo(claveSilo,Integer.valueOf(material),true);
		 try {
				String jsonResponse = client.get(endpoint);
				ObjectMapper objectMapper= new ObjectMapper();
				int endIndex = Math.min(jsonResponse.length(), 15);
			    String prefix = jsonResponse.substring(0, endIndex).trim();
			    if ("\"\"".equals(jsonResponse.trim())) {
			    	 logger.warn("Respuesta de SAP vacía (posiblemente sin datos para los filtros).");
				     return null;
			    }
				objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
				StockSiloResponse response= objectMapper.readValue(jsonResponse, StockSiloResponse.class);
				if(response.getStockSilo()!=null) {
					return Double.parseDouble(response.getStockSilo());
				}
				return null;
			}
			 catch (InvalidFormatException e) {
				 logger.error("Hubo un error de formato en la descarga de stock de silo desde sap: "+e.getMessage());
				return null;
			}catch (IOException e) {
				logger.error("Hubo un error general en la descarga de stock de silo desde sap: "+e.getMessage());
				return null;
			}
	}

}
