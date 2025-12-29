import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PuestoReponse } from '../../models/catalogs/Puesto/Puesto.Response';
import { DepartamentoResponse } from '../../models/catalogs/departamento/Catalog.Response';

@Injectable({
  providedIn: 'root'
})
export class CatalogService {
private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"

  constructor(private http:HttpClient) { }

  findAllDepartamento():Observable<DepartamentoResponse[]>{
    return this.http.get<DepartamentoResponse[]>(`${this.apiUrl}/catalog/departamento`);
  }

   findAllPuesto():Observable<PuestoReponse[]>{
    return this.http.get<PuestoReponse[]>(`${this.apiUrl}/catalog/puesto`);
  }
}
