export interface PedidoCompraResponse {
    pedidoCompraId:         number;
    numeroPedido:           string;
    cantidadPedida:         number;
    cantidadEntregada:      number;
    cantidadDespachada:     number;
    cantidadPendienteDespachada: number;
    contratoLegal:          string;
    urlCertificadoDeposito: string;
    tipoExtencion:          string;
}
