import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SiloResponse } from '../../models/catalogs/silo/Silo.Response';
import { SiloRequest } from '../../models/catalogs/silo/Silo.Request';
import { ApiResponse } from '../../models/ApiResponse';

@Injectable({
  providedIn: 'root'
})
export class SiloServiceService {
 private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"

  constructor(private http:HttpClient) { }

  getSilos():Observable<SiloResponse[]>{
    return this.http.get<SiloResponse[]>(`${this.apiUrl}/silo`);
  }

  save(request:SiloRequest):Observable<ApiResponse<SiloResponse>>{
    return this.http.post<ApiResponse<SiloResponse>>(`${this.apiUrl}/silo`,request);
  }

  update(request:SiloRequest,id:number):Observable<SiloResponse>{
    return this.http.put<SiloResponse>(`${this.apiUrl}/silo/${id}`,request);
  }

  delete(id:number):Observable<void>{
    return this.http.delete<void>(`${this.apiUrl}/silo/delete/${id}`);
  }

  findStockById(id:number):Observable<number>{
     let params = new HttpParams();
    params = params.set("siloId", id);
     return this.http.get<number>(`${this.apiUrl}/silo/stock`,{params});
  }
}
