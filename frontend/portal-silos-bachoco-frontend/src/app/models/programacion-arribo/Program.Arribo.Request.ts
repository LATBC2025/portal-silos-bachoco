export class ProgramArriboRequest {
    numeroPedidoTraslado: string|undefined;
    cantidad: number|undefined;
    fechaProgramada: string;
    claveSilo: string;
    siloId: number;
    materialId: number;
    plantaId: number;
    pedidoTrasladoId: number|undefined;
    isRestaCantidad:string;
    numeroProveedor?: string; // NUEVO

    constructor(
        numeroPedidoTraslado: string|undefined,
        cantidad: number|undefined,
        fechaProgramada: string,
        claveSilo: string,
        siloId: number,
        materialId: number,
        plantaId: number,
        pedidoTrasladoId: number|undefined,
         isRestaCantidad:string,
         numeroProveedor: string
    ) {
        this.numeroPedidoTraslado = numeroPedidoTraslado;
        this.cantidad = cantidad;
        this.fechaProgramada = fechaProgramada;
        this.claveSilo = claveSilo;
        this.siloId = siloId;
        this.materialId = materialId;
        this.plantaId = plantaId;
        this.pedidoTrasladoId = pedidoTrasladoId;
        this.isRestaCantidad=isRestaCantidad
        this.numeroProveedor=numeroProveedor;
    }
}
