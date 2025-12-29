import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ConfirmacionDespachoRequest } from '../../models/confirmacion-despacho/Confirmacion.Despacho.Request';
import { Observable } from 'rxjs';
import { ConfDespachoTransactionResponse } from '../../models/confirmacion-despacho/Confirmacion.Despacho.Transaction.response';
import { ConfirmacionDespachoResponse } from '../../models/confirmacion-despacho/Confirmacion.Despacho.Response';

@Injectable({
  providedIn: 'root'
})
export class ConfirmDespachoService {
  private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);

  constructor(private http: HttpClient) { }

  save(request: ConfirmacionDespachoRequest): Observable<ConfDespachoTransactionResponse> {
    return this.http.post<ConfDespachoTransactionResponse>(`${this.apiUrl}/confirmacion-despacho`, request);
  }
  updateSap(request: ConfirmacionDespachoRequest): Observable<ConfDespachoTransactionResponse> {
    return this.http.put<ConfDespachoTransactionResponse>(`${this.apiUrl}/confirmacion-despacho/update-sap`, request);
  }
  updateSinSap(request: ConfirmacionDespachoRequest): Observable<ConfDespachoTransactionResponse> {
    return this.http.put<ConfDespachoTransactionResponse>(`${this.apiUrl}/confirmacion-despacho/update-sin-sap`, request);
  }
  validatePessos(id:string,pesobruto:string,pesotara:string): Observable<ConfDespachoTransactionResponse> {
    let params = new HttpParams();
    params = params.set("id", id);
    params = params.set("pesobruto", pesobruto);
    params = params.set("pesotara", pesotara);
    return this.http.get<ConfDespachoTransactionResponse>(`${this.apiUrl}/confirmacion-despacho/filters-pesos`,{params});
  }
  findCantidadPromedioArribo(siloId:number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/confirmacion-despacho/filter-promedio-arribo/${siloId}`);
  }
  dowloadPdf(id:number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/confirmacion-despacho/dowload-pdf/${id}`, { responseType: 'blob' });
  }
  
  findAllConfirmDespachos(silo: number, material: number, fechaInicio: string, fechaFin: string): Observable<ConfirmacionDespachoResponse[]> {
    let params = new HttpParams();
    params = params.set("silo", silo.toString());
    params = params.set("material", material.toString());
    params = params.set("fechaInicio", fechaInicio);
    params = params.set("fechaFin", fechaFin);
    return this.http.get<ConfirmacionDespachoResponse[]>(`${this.apiUrl}/confirmacion-despacho/filter-list-conf-despacho`, { params })
  }

  delete(request: ConfirmacionDespachoRequest): Observable<ConfDespachoTransactionResponse> {
    return this.http.post<ConfDespachoTransactionResponse>(`${this.apiUrl}/confirmacion-despacho/delete`, request);
  }
}
