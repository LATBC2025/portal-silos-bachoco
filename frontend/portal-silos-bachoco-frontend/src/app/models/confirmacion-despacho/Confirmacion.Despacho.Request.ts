export class ConfirmacionDespachoRequest {
  constructor(
    public claveBodega: string,
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
    public numPedidoTraslado: string,
    public tipoMovimiento: string,
    public idConfDespacho: string,
    public destinoId:number,
    public idPedTraslado?:number,
    public folio?:string
  ) {}
}
