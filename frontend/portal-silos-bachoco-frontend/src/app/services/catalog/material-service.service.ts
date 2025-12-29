import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { MaterialRequest } from '../../models/catalogs/Material/Material.Request';
import { MaterialResponse } from '../../models/catalogs/Material/Material.Response';

@Injectable({
  providedIn: 'root'
})
export class MaterialServiceService {

  private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"

  constructor(private http: HttpClient) { }


  findAll(): Observable<MaterialResponse[]> {
    return this.http.get<MaterialResponse[]>(`${this.apiUrl}/material`);
  }

  save(request: MaterialRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/material`, request);
  }

  update(request: MaterialRequest, id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/material/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/material/${id}`);
  }

}
