import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProgramPedTrasladoResponse } from '../../models/programacion-arribo/Program.Pedido.Traslado';
import { ProgramArriboRequest } from '../../models/programacion-arribo/Program.Arribo.Request';

@Injectable({
  providedIn: 'root'
})
export class ProgramArriboService {
  private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);

  constructor(private http: HttpClient) { }

  save(request: ProgramArriboRequest[]): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/program-arribo`, request);
  }
  findStock(claveSilo: string,material:string): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/program-arribo/${claveSilo}/${material}`,);
  }
  findPedidosTralados(claveSilo: string, clavePlanta: string, claveMaterial: string): Observable<ProgramPedTrasladoResponse[]> {
    let params = new HttpParams();
    params = params.set("claveSilo", claveSilo);
    params = params.set("clavePlanta", clavePlanta);
    params = params.set("claveMaterial", claveMaterial);
    return this.http.get<ProgramPedTrasladoResponse[]>(`${this.apiUrl}/program-arribo/pedido-traslado`, { params });
  }

  findTotalProgramArriboByNumPedTraslado(numePedTraslado:string,claveSilo: string,claveMaterial: string, clavePlanta: string, fechaInicio:string,fechaFin:string): Observable<number> {
    let params = new HttpParams();
    params = params.set("numPedidoTraslados", numePedTraslado);
    params = params.set("claveSilo", claveSilo);
    params = params.set("claveMaterial", claveMaterial);
    params = params.set("clavePlanta", clavePlanta);
    params = params.set("fechaInicio", fechaInicio);
    params = params.set("fechaFin", fechaFin);
    return this.http.get<number>(`${this.apiUrl}/program-arribo/findTotal-ProgramArriboByNumPedTraslado`, { params });
  }

}
