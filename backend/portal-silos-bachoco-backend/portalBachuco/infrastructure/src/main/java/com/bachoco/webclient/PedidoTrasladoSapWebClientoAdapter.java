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

import com.bachoco.dto.pedTraslado.dto.SapPedidoTrasladoResponse;
import com.bachoco.exception.SapConnectionException;
import com.bachoco.mapper.PedidoTrasladoSapClientMapper;
import com.bachoco.model.PedidoTrasladoSapResponseDTO;
import com.bachoco.persistence.config.HttpUrlConnectionClient;
import com.bachoco.persistence.config.SapProperties;
import com.bachoco.port.PedidoTrasladoSapPort;
import com.bachoco.utils.UrlConnectionUtil;
import com.bachoco.utils.WebClientUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@Component
public class PedidoTrasladoSapWebClientoAdapter implements PedidoTrasladoSapPort {

	private final SapProperties sapProperties;
	@Value("${app.url.endpoit.sap.filter.silo}")
	private int filtersiloSilo;
	private static final Logger logger = LoggerFactory.getLogger(PedidoTrasladoSapWebClientoAdapter.class);

	public PedidoTrasladoSapWebClientoAdapter(SapProperties sapProperties) {
		this.sapProperties = sapProperties;
	}

	@Override
	public List<PedidoTrasladoSapResponseDTO> findAllPedTraslado(String claveSilo, String claveMaterial,
			String plantaDestino, String fechaInicio, String fechaFin, String rutaUrl) {
		List<PedidoTrasladoSapResponseDTO> response = new ArrayList<>();
		String endpoint;
		logger.info("===============PEDIDO TRASLADO====================== ");
		logger.info("URL::: " + rutaUrl);
		HttpUrlConnectionClient client = new HttpUrlConnectionClient(rutaUrl, sapProperties.getUserName(),
				sapProperties.getPassWord());
		if (filtersiloSilo == 1) {
			endpoint = WebClientUtils.buildUrlPedioTraslado(claveSilo, "", "", "", true);
		} else {
			endpoint = WebClientUtils.buildUrlPedioTraslado(claveSilo, fechaInicio, fechaFin,
					Integer.valueOf(claveMaterial), plantaDestino, true);
		}
		try {
			String jsonResponse = client.get(endpoint);
			boolean isValidJson = UrlConnectionUtil.isValidJsonResponse(jsonResponse);
			if (isValidJson == false) {
				logger.warn("Respuesta de SAP vacía (posiblemente sin datos para los filtros).");
				return response;
			}
			SapPedidoTrasladoResponse responseSap = UrlConnectionUtil.getObjectMapper().readValue(jsonResponse,
					SapPedidoTrasladoResponse.class);
			return extractItemsFromWrapper(responseSap);
		} catch (InvalidFormatException e) {
			logger.error("Error en la conexion de SAP pedido traslado: " + e.getMessage());
			throw new SapConnectionException("Hubo error en conexion a SAP: " + e.getCause());
		} catch (IOException e) {
			logger.error("Error en la conexion de SAP pedido traslado: " + e.getMessage());
			throw new SapConnectionException("Hubo error en conexion a SAP: " + e.getCause());
		}
	}

	public List<PedidoTrasladoSapResponseDTO> extractItemsFromWrapper(SapPedidoTrasladoResponse responseSap) {
		if (responseSap != null && responseSap.getItems() != null && !responseSap.getItems().isEmpty()) {
			return responseSap.getItems().stream().map(PedidoTrasladoSapClientMapper::toDomain)
					.collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

}
