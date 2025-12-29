export class BodegaRequest{
    bodega:string;
    siloId:number;

    constructor(bodega:string,siloId:number){
        this.bodega=bodega;
        this.siloId=siloId;
    }
}