export class EmpleadoExternoRequest{
    nombre:string;
    rfc:string;
    correo:string;
    usuario:string;
    siloId:number;
     numeroProveedor?: string; // Número proveedor SAP
     bodegasIds?: number[]; //Bodegas por Id


    constructor(nombre:string,rfc:string,correo:string,usuario:string,siloId:number, numeroProveedor?: string, bodegasIds?: number[]){
        this.nombre=nombre;
        this.rfc=rfc;
        this.correo=correo;
        this.usuario=usuario;
        this.siloId=siloId;
        this.numeroProveedor=numeroProveedor;
        this.bodegasIds=bodegasIds;
    }
}
