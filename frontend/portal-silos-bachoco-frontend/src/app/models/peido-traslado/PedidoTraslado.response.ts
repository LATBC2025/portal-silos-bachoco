export interface PedidoTrasladoResponse {
    pedidoTrasladoId:          number;
    nombrePlantaDestino:       string;
    numPedidoTraslado:         string;
    cantidadPedido:            number;
    cantidadTraslado:          number;
    cantidadRecibidaPa:          number;
    cantidadPendienteTraslado: number;
    numCompraAsociado:         string;
    trasladosPendFact:         number;

    // ✅ NUEVOS
  cantidadEmbarcadaReal?: number;
  cantidadPendientePorProgramar?: number;
  saldoSeLiberaManana?: boolean;

  numeroProveedor?: string; // ✅ NUEVO


}
