import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { ReporteDespachoResponse } from '../../models/reporte-despacho/Reporte.Despacho.Response';

@Injectable({
  providedIn: 'root'
})
export class ReportDespachosServiceService {
 private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"

  constructor(private http: HttpClient) { }

  findAllByFilters(siloId: number, fechaI: string, fechaF: string): Observable<ReporteDespachoResponse[]> {
    let params = new HttpParams();
    params = params.set("siloId", siloId);
    params = params.set("fechaI", fechaI);
    params = params.set("fechaF", fechaF);
    return this.http.get<ReporteDespachoResponse[]>(`${this.apiUrl}/reporte-despacho`, { params });
  }
}
