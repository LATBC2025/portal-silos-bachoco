import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { catchError, firstValueFrom, map, of, timeout } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NetworkServiceService {

  private apiUrl: string = environment.api.protocol.concat(environment.api.baseUrl);
  // private apiUrl: string = "http://172.17.7.116:8080/Bachoco-silos/v1"
  constructor(private http: HttpClient) {}

  /**
   * Verifica si el servidor está accesible a nivel HTTP
   * Retorna true si obtiene status code (200, 404, etc.), false si timeout o sin respuesta
   */
  async  checkConnection() {
    
      try {
      const result = await firstValueFrom(
        this.http.get(`${this.apiUrl}`.concat("/auth/login/ok"), { observe: 'response', responseType: 'text' }).pipe(
          timeout(2000),
          map(resp => {
            console.log('Servidor responde con código:', resp.status);
            return true;
          }),
          catchError(err => {
            console.warn('No se pudo conectar al servidor:', err.message || err);
            return of(false);
          })
        )
      );
      return result;
    } catch (error) {
      console.error('Error general al verificar conexión:', error);
      return false;
    }
  }
}
