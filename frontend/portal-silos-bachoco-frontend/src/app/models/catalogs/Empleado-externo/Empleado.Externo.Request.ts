export class EmpleadoExternoRequest{
    nombre:string;
    rfc:string;
    correo:string;
    usuario:string;
    siloId:number;
     sapVendor?: string; // Número proveedor SAP


    constructor(nombre:string,rfc:string,correo:string,usuario:string,siloId:number, sapVendor?: string){
        this.nombre=nombre;
        this.rfc=rfc;
        this.correo=correo;
        this.usuario=usuario;
        this.siloId=siloId;
        this.sapVendor=sapVendor;
    }
}
