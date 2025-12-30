export class EmpleadoExternoRequest{
    nombre:string;
    rfc:string;
    correo:string;
    usuario:string;
    siloId:number;
     sapVendor?: string; // Número proveedor SAP
     bodegasIds?: number[]; //Bodegas por Id


    constructor(nombre:string,rfc:string,correo:string,usuario:string,siloId:number, sapVendor?: string, bodegasIds?: number[]){
        this.nombre=nombre;
        this.rfc=rfc;
        this.correo=correo;
        this.usuario=usuario;
        this.siloId=siloId;
        this.sapVendor=sapVendor;
        this.bodegasIds=bodegasIds;
    }
}
