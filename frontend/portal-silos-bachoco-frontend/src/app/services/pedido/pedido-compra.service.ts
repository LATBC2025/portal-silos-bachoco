import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { PedidoCompraResponse } from '../../models/PedidoCompraResponse';

@Injectable({
  providedIn: 'root'
})
export class PedidoCompraService {

  private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"

  constructor(private http: HttpClient) { }

  findAll(claveSilo: string, claveMaterial: string, plantaDestino: string, fechaInicio: string, fechaFin: string): Observable<any> {
    let params = new HttpParams();
    params = params.set("claveSilo", claveSilo);
    params = params.set("claveMaterial", claveMaterial);
    params = params.set("plantaDestino", plantaDestino);
    params = params.set("fechaInicio", fechaInicio);
    params = params.set("fechaFin", fechaFin);
    return this.http.get<any>(`${this.apiUrl}/pedido-compra/filters`, { params })
  }
  savePedTrasladoBySap(claveSilo: string, claveMaterial: string, fechaInicio: string, fechaFin: string): Observable<any> {
    let params = new HttpParams();
    params = params.set("claveSilo", claveSilo);
    params = params.set("claveMaterial", claveMaterial);
    params = params.set("fechaInicio", fechaInicio);
    params = params.set("fechaFin", fechaFin);
    return this.http.get<any>(`${this.apiUrl}/pedido-compra/filters-dowload-ped-traslado`, { params })
  }

  savePedCompraDowloadBySap(claveSilo: string, claveMaterial: string, plantaDestino: string, fechaInicio: string, fechaFin: string): Observable<any> {
    let params = new HttpParams();
    params = params.set("claveSilo", claveSilo);
    params = params.set("claveMaterial", claveMaterial);
    params = params.set("plantaDestino", plantaDestino);
    params = params.set("fechaInicio", fechaInicio);
    params = params.set("fechaFin", fechaFin);
    return this.http.get<any>(`${this.apiUrl}/pedido-compra/filters-dowload-ped-compra`, { params })
  }

  dowloadPdf(filename: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/pedido-compra/dowload-pdf/${filename}`, { responseType: 'blob' });
  }

  saveFile(file: FormData): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/pedido-compra/upload-pdf`, file,
      { responseType: 'text' as 'json' }
    );
  }

  delete(compraId: number, urlRecurso: string): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/pedido-compra/${compraId}/${urlRecurso}`);
  }
}
