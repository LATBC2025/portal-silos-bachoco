import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { EmpleadoResponse } from '../../models/catalogs/Empelado/Empleado.Response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmpleadoService {

  private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"
  
    constructor(private http: HttpClient) { }
  
  
    findByUsurioOrCorreo(value:string): Observable<EmpleadoResponse> {
         let params = new HttpParams();
         params = params.set("value", value);
      return this.http.get<EmpleadoResponse>(`${this.apiUrl}/empleado`,{params});
    }
}
