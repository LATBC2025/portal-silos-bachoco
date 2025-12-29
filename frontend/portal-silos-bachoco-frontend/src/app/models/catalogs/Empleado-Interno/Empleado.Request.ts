export class EmpleadoInternoRequest{
    nombre:string;
    correo:string;
    usuario:string;
    perfilId:number;
    departamentoId:number;
    puestoId:number;


    constructor(nombre:string,correo:string,usuario:string,perfilId:number,departamentoId:number,puestoId:number){
        this.nombre=nombre;
        this.correo=correo;
        this.usuario=usuario;
        this.perfilId=perfilId;
        this.departamentoId=departamentoId;
        this.puestoId=puestoId;
    }
}