import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { EmpleadoInternoRequest } from '../../models/catalogs/Empleado-Interno/Empleado.Request';
import { Observable } from 'rxjs';
import { EmpleadoInternoResponse } from '../../models/catalogs/Empleado-Interno/Empleado-Interno.response';
import { EmpleadoInternoDtoResponse } from '../../models/catalogs/Empleado-Interno/Empleado.Interno.Response.dto';

@Injectable({
  providedIn: 'root'
})
export class EmpleadoInternoService {

  private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"

  constructor(private http: HttpClient) { }

  save(request: EmpleadoInternoRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/empleado-interno`, request);
  }

  findAll(): Observable<EmpleadoInternoResponse[]> {
    return this.http.get<EmpleadoInternoResponse[]>(`${this.apiUrl}/empleado-interno`);
  }

  findAllEmpleadoInterno(): Observable<EmpleadoInternoDtoResponse[]> {
    return this.http.get<EmpleadoInternoDtoResponse[]>(`${this.apiUrl}/empleado-interno`);
  }

  update(request: EmpleadoInternoRequest, id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/empleado-interno/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/empleado-interno/${id}`);
  }

}
