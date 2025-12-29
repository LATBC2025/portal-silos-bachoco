import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { PlantaResponse } from '../../models/catalogs/planta/Planta.Response';
import { PlantaRequest } from '../../models/catalogs/planta/Planta.Request';
import { Observable } from 'rxjs';
import { ApiResponse } from '../../models/ApiResponse';

@Injectable({
  providedIn: 'root'
})
export class PlantaServiceService {
  private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"

  constructor(private http: HttpClient) { }

  findAll(): Observable<PlantaResponse[]> {
    return this.http.get<PlantaResponse[]>(`${this.apiUrl}/planta`);
  }

  findAllPlantasByProgramArribo(fechaInicio: string, fechaFin: string) {
    let params = new HttpParams();
    params = params.set("fechaInicio", fechaInicio);
    params = params.set("fechaFin", fechaFin);
    return this.http.get<PlantaResponse[]>(`${this.apiUrl}/planta/plantas-programadas-arribo`,{params});
  }
  save(request: PlantaRequest): Observable<ApiResponse<PlantaResponse>> {
    return this.http.post<ApiResponse<PlantaResponse>>(`${this.apiUrl}/planta`, request);
  }

  update(request: PlantaRequest, id: number): Observable<PlantaResponse> {
    return this.http.put<PlantaResponse>(`${this.apiUrl}/planta/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/planta/${id}`);
  }
}
