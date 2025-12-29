import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PedidoTrasladoResponse } from '../../models/peido-traslado/PedidoTraslado.response';
import { PedidoTrasladoArriboResponse } from '../../models/peido-traslado/PedidoTrasladoArribo.response';
import { PedTrasladoConfDespachoResponse } from '../../models/confirmacion-despacho/Pedido.Traslado.Conf.Despacho';

@Injectable({
  providedIn: 'root'
})
export class PedidoTrasladoService {

  private baseUrl: string = environment.api.protocol.concat(environment.api.baseUrl);

  constructor(private http: HttpClient) { }

  getPedidosTraslado(claveSilo: string, claveMaterial: string, plantaDestino: string, fechaInicio: string, fechaFin: string): Observable<PedidoTrasladoResponse[]> {
    let params = new HttpParams();
    params = params.set("claveSilo", claveSilo);
    params = params.set("claveMaterial", claveMaterial);
    params = params.set("plantaDestino", plantaDestino);
    params = params.set("fechaInicio", fechaInicio);
    params = params.set("fechaFin", fechaFin);
    return this.http.get<PedidoTrasladoResponse[]>(`${this.baseUrl}/pedido-traslado/filters`, { params })
  }

  getPedidosTrasladoCantidad(claveSilo: number, claveMaterial: number, fechaInicio: string, fechaFin: string): Observable<PedidoTrasladoResponse[]> {
    let params = new HttpParams();
    params = params.set("siloId", claveSilo.toString());
    params = params.set("materialId", claveMaterial.toString());
    params = params.set("fechaInicio", fechaInicio);
    params = params.set("fechaFin", fechaFin);
    return this.http.get<PedidoTrasladoResponse[]>(`${this.baseUrl}/pedido-traslado/filters/cantidad-disponible`, { params })
  }

  savePedTrasladoBySap(claveSilo: string, claveMaterial: string, plantaDestino: string, fechaInicio: string, fechaFin: string): Observable<PedidoTrasladoResponse[]> {
    let params = new HttpParams();
    params = params.set("claveSilo", claveSilo);
    params = params.set("claveMaterial", claveMaterial);
    params = params.set("plantaDestino", plantaDestino);
    params = params.set("fechaInicio", fechaInicio);
    params = params.set("fechaFin", fechaFin);
    return this.http.get<PedidoTrasladoResponse[]>(`${this.baseUrl}/pedido-traslado/filters-dowload-ped-traslado`, { params })
  }

  findAllByMaterialAndSilo(siloId: number, materialId: number, planta: string): Observable<PedidoTrasladoArriboResponse[]> {
    let params = new HttpParams();
    params = params.set("siloId", siloId);
    params = params.set("materialId", materialId);
    params = params.set("planta", materialId);
    return this.http.get<PedidoTrasladoArriboResponse[]>(`${this.baseUrl}/pedido-traslado/filters/program`, { params })
  }

  findAllPedTraladoByConfDespacho(siloId: number, materialId: number, fechaInicio: string, fechaFin: string): Observable<PedTrasladoConfDespachoResponse[]> {
    let params = new HttpParams();
    params = params.set("siloId", siloId);
    params = params.set("materialId", materialId);
    params = params.set("planta", materialId);
    params = params.set("fechaInicio", fechaInicio);
    params = params.set("fechaFin", fechaFin);
    return this.http.get<PedTrasladoConfDespachoResponse[]>(`${this.baseUrl}/pedido-traslado/filters/conf-despacho`, { params })
  }
}
