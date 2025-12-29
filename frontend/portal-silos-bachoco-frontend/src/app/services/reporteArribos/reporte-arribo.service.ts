import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ReporteArriboResponse } from '../../models/reporte-program-arribo/Reporte.Arribo.Response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReporteArriboService {
  private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"

  constructor(private http: HttpClient) { }

  findAllByFilters(siloId: number, fechaI: string, fechaF: string): Observable<ReporteArriboResponse[]> {
    let params = new HttpParams();
    params = params.set("siloId", siloId);
    params = params.set("fechaI", fechaI);
    params = params.set("fechaF", fechaF);
    return this.http.get<ReporteArriboResponse[]>(`${this.apiUrl}/reporte-program-arribo`, { params });
  }
}
