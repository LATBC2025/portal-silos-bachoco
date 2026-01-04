export class ConfirmacionDespachoResponse {
  constructor(
    public claveBodega: string,
    public folio:string,
    public claveSilo: string,
    public claveMaterial: string,
    public fechaEmbarque: string,
    public numBoleta: string,
    public pesoBruto: number,
    public pesoTara: number,
    public humedad: string,
    public chofer: string,
    public placaTractor: string,
    public placaJaula: string,
    public lineaTransportista: string,
    public claveDestino: string,
    public numPedidoTraslado: number,
    public tipoMovimiento: string,
    public idconfDespacho: string,
    public idPedTraslado: number,
    public numeroSap:string
  ) {}
}
