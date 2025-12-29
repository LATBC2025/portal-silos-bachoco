package com.bachoco.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bachoco.model.PedidoTrasladoSapResponseDTO;

public class ComparadorTrasladosSAP {

	public List<PedidoTrasladoSapResponseDTO> encontrarTrasladosParaActualizar(
			List<Map<String, Object>> datosActualesBD, List<PedidoTrasladoSapResponseDTO> pedidosSapNoExistBd) {

		// Convertir datos de BD a Map para acceso rápido
		Map<String, Map<String, Object>> actualesMap = convertToFolioMapTraslado(datosActualesBD);

		List<PedidoTrasladoSapResponseDTO> trasladosParaActualizar = new ArrayList<>();

		for (PedidoTrasladoSapResponseDTO sapDTO : pedidosSapNoExistBd) {
			// Construir el folio (numeroPedTraslado + posicion)
			String folio = construirFolioTraslado(sapDTO);

			// Obtener datos actuales de BD
			Map<String, Object> datosActuales = actualesMap.get(folio);

			//if (datosActuales != null && tienenDiferenciasTraslado(datosActuales, sapDTO)) {
				trasladosParaActualizar.add(sapDTO);
			//}
		}

		return trasladosParaActualizar;
	}

	private String construirFolioTraslado(PedidoTrasladoSapResponseDTO sapDTO) {
		return sapDTO.getNumeroPedTraslado() +"-"+ sapDTO.getPosicion();
	}

	private boolean tienenDiferenciasTraslado(Map<String, Object> datosActuales, PedidoTrasladoSapResponseDTO sapDTO) {
		// Comparar todos los campos relevantes
		return !compararCampo("cantidadPedido", datosActuales, sapDTO.getCantidadPedido())
				|| !compararCampo("cantidadTraslado", datosActuales, sapDTO.getCantidadEnTraslado())
				|| !compararCampo("cantidadRecibidaEnPA", datosActuales, sapDTO.getCantidadRecibidaEnPa())
				|| !compararCampo("cantidadPendienteTraslado", datosActuales, sapDTO.getCantidadPendienteTraslado())
				|| !compararCampo("pedidoTraslado", datosActuales, sapDTO.getCantidaddespacho());
	}

	private boolean compararCampo(String campoBD, Map<String, Object> datosActuales, String valorSAP) {
		try {
			Float valorBD = ((Number) datosActuales.get(campoBD)).floatValue();
			Float valorSAPFloat = Float.parseFloat(valorSAP);
			return Math.abs(valorBD - valorSAPFloat) < 0.001f; // Tolerancia para floats
		} catch (Exception e) {
			// Si hay error en conversión, considerar que son diferentes
			return false;
		}
	}

	public Map<String, Map<String, Object>> convertToFolioMapTraslado(List<Map<String, Object>> resultados) {
		return resultados.stream().collect(Collectors.toMap(row -> (String) row.get("folio"), row -> row));
	}
}
