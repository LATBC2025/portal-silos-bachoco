import { ConfirmacionDespachoRequest } from "../../../models/confirmacion-despacho/Confirmacion.Despacho.Request";
import { ConfirmacionDespachoResponse } from "../../../models/confirmacion-despacho/Confirmacion.Despacho.Response";

// confirmation-despacho.mapper.ts
export class ConfirmationDespachoMapper {
  
  // Mapear de Response a Request
  static responseToRequest(response: ConfirmacionDespachoResponse): ConfirmacionDespachoRequest {
    const request: any = {};
    
    // Mapear campos con nombres idénticos
    const commonFields = [
      'claveBodega', 'claveSilo', 'claveMaterial', 'fechaEmbarque',
      'numBoleta', 'pesoBruto', 'pesoTara', 'humedad', 'chofer',
      'placaJaula', 'lineaTransportista', 'claveDestino', 'tipoMovimiento'
    ];
    
    commonFields.forEach(field => {
      if (field in response) {
        request[field] = response[field as keyof ConfirmacionDespachoResponse];
      }
    });
    
    // Campos con nombres diferentes o conversiones especiales
    request.idConfDespacho = response.idconfDespacho;
    request.numPedidoTraslado = response.numPedidoTraslado?.toString() || '0';
    request.folio = response.folio;
    request.idPedTraslado = response.idPedTraslado;
    
    // Campos que no existen en Response (se dejan como están o se asignan valores por defecto)
    request.destinoId = 0; // O el valor que corresponda
    
    return new ConfirmacionDespachoRequest(
      request.claveBodega,
      request.claveSilo,
      request.claveMaterial,
      request.fechaEmbarque,
      request.numBoleta,
      request.pesoBruto,
      request.pesoTara,
      request.humedad,
      request.chofer,
      request.placaJaula,
      request.lineaTransportista,
      request.claveDestino,
      request.numPedidoTraslado,
      request.tipoMovimiento,
      request.idConfDespacho,
      request.destinoId,
      request.idPedTraslado,
      request.folio
    );
  }
  
  // Mapear de Request a Response
  static requestToResponse(request: ConfirmacionDespachoRequest): ConfirmacionDespachoResponse {
    const response: any = {};
    
    // Mapear campos con nombres idénticos
    const commonFields = [
      'claveBodega', 'claveSilo', 'claveMaterial', 'fechaEmbarque',
      'numBoleta', 'pesoBruto', 'pesoTara', 'humedad', 'chofer',
      'placaJaula', 'lineaTransportista', 'claveDestino', 'tipoMovimiento'
    ];
    
    commonFields.forEach(field => {
      if (field in request) {
        response[field] = request[field as keyof ConfirmacionDespachoRequest];
      }
    });
    
    // Campos con nombres diferentes o conversiones especiales
    response.idconfDespacho = request.idConfDespacho;
    response.numPedidoTraslado = Number(request.numPedidoTraslado) || 0;
    response.folio = request.folio;
    response.idPedTraslado = request.idPedTraslado || 0;
    
    // Campos que no existen en Request
    response.numeroSap = ''; // O el valor por defecto que corresponda
    
    return new ConfirmacionDespachoResponse(
      response.claveBodega,
      response.folio,
      response.claveSilo,
      response.claveMaterial,
      response.fechaEmbarque,
      response.numBoleta,
      response.pesoBruto,
      response.pesoTara,
      response.humedad,
      response.chofer,
      response.placaJaula,
      response.lineaTransportista,
      response.claveDestino,
      response.numPedidoTraslado,
      response.tipoMovimiento,
      response.idconfDespacho,
      response.idPedTraslado,
      response.numeroSap
    );
  }

   // Mapear de Response a Request (versión simplificada)
  static responseToRequestv2(response: ConfirmacionDespachoResponse): ConfirmacionDespachoRequest {
    const mappedData: any = {
      ...response,
      // Mapear campos con nombres diferentes
      idConfDespacho: response.idconfDespacho,
      numPedidoTraslado: response.numPedidoTraslado?.toString() || '0',
      // Asignar valores por defecto para campos que no existen en Response
      destinoId: 0
    };
    
    // Eliminar campos que no existen en Request
    delete mappedData.idconfDespacho;
    delete mappedData.numeroSap;
    
    return new ConfirmacionDespachoRequest(
      mappedData.claveBodega,
      mappedData.claveSilo,
      mappedData.claveMaterial,
      mappedData.fechaEmbarque,
      mappedData.numBoleta,
      mappedData.pesoBruto,
      mappedData.pesoTara,
      mappedData.humedad,
      mappedData.chofer,
      mappedData.placaJaula,
      mappedData.lineaTransportista,
      mappedData.claveDestino,
      mappedData.numPedidoTraslado,
      mappedData.tipoMovimiento,
      mappedData.idConfDespacho,
      mappedData.destinoId,
      mappedData.idPedTraslado,
      mappedData.folio
    );
  }
  
  // Mapear de Request a Response (versión simplificada)
  static requestToResponseV2(request: ConfirmacionDespachoRequest): ConfirmacionDespachoResponse {
    const mappedData: any = {
      ...request,
      // Mapear campos con nombres diferentes
      idconfDespacho: request.idConfDespacho,
      numPedidoTraslado: Number(request.numPedidoTraslado) || 0,
      // Asignar valores por defecto para campos que no existen en Request
      numeroSap: ''
    };
    
    // Eliminar campos que no existen en Response
    delete mappedData.idConfDespacho;
    delete mappedData.destinoId;
    
    return new ConfirmacionDespachoResponse(
      mappedData.claveBodega,
      mappedData.folio,
      mappedData.claveSilo,
      mappedData.claveMaterial,
      mappedData.fechaEmbarque,
      mappedData.numBoleta,
      mappedData.pesoBruto,
      mappedData.pesoTara,
      mappedData.humedad,
      mappedData.chofer,
      mappedData.placaJaula,
      mappedData.lineaTransportista,
      mappedData.claveDestino,
      mappedData.numPedidoTraslado,
      mappedData.tipoMovimiento,
      mappedData.idconfDespacho,
      mappedData.idPedTraslado,
      mappedData.numeroSap
    );
  }
}