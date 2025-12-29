import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { EmpleadoExternoRequest } from '../../models/catalogs/Empleado-externo/Empleado.Externo.Request';
import { EmpleadoExternoResponseDTO } from '../../models/catalogs/Empleado-externo/Empleado.Response.DTO';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class EmpleadoExternoService {
  private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"

  constructor(private http: HttpClient) { }

  save(request: EmpleadoExternoRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/empleado-externo`, request);
  }

  update(request: EmpleadoExternoRequest, id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/empleado-externo/${id}`, request);
  }

  findAll(): Observable<EmpleadoExternoResponseDTO[]> {
    return this.http.get<EmpleadoExternoResponseDTO[]>(`${this.apiUrl}/empleado-externo`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/empleado-externo/${id}`);
  }
}
