import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BodegaResponse } from '../../models/catalogs/bodega/Bodega.Response';
import { BodegaRequest } from '../../models/catalogs/bodega/Bodega.Request';

@Injectable({
  providedIn: 'root'
})
export class BodegaServiceService {

  private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"

  constructor(private http:HttpClient) { }

  findAll():Observable<BodegaResponse[]>{
    return this.http.get<BodegaResponse[]>(`${this.apiUrl}/bodega`);
  }

  findAllBySilo(siloId:number):Observable<BodegaResponse[]>{
     let params = new HttpParams();
    params = params.set("siloId", siloId);
    return this.http.get<BodegaResponse[]>(`${this.apiUrl}/bodega/filter-silo`,{params});
  }


  save(request:BodegaRequest):Observable<BodegaResponse>{
    return this.http.post<BodegaResponse>(`${this.apiUrl}/bodega`,request);
  }

  update(request:BodegaRequest,id:number):Observable<BodegaResponse>{
    return this.http.put<BodegaResponse>(`${this.apiUrl}/bodega/${id}`,request);
  }

  delete(id:number):Observable<void>{
    return this.http.delete<void>(`${this.apiUrl}/bodega/${id}`);
  }
}
