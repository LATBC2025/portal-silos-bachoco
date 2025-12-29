export class MaterialRequest{
    numero:string;
    descripcion:string;
    siloId:number;

    constructor(numero:string,descripcion:string,siloId:number){
        this.numero=numero;
        this.descripcion=descripcion;
        this.siloId=siloId;
    }
}