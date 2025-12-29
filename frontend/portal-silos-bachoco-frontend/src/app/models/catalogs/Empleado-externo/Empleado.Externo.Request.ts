export class EmpleadoExternoRequest{
    nombre:string;
    rfc:string;
    correo:string;
    usuario:string;
    siloId:number;


    constructor(nombre:string,rfc:string,correo:string,usuario:string,siloId:number){
        this.nombre=nombre;
        this.rfc=rfc;
        this.correo=correo;
        this.usuario=usuario;
        this.siloId=siloId;
    }
}