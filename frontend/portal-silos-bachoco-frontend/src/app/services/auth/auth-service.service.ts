import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AuthenticationUtil } from '../../models/AutenticationUtil';
import { Observable, Subject } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest } from '../../models/AuthenticationRequest';
import { ResponseRequest } from '../../models/ResponseRequest';
import { OtpRequest } from '../../models/OtpRequest';

@Injectable({
  providedIn: 'root',
})
export class AuthServiceService {
  public getUbicacionObserver = new Subject<String>();
  private baseUrlAuth: string = environment.api.protocol.concat(environment.api.baseUrl);
  //private sessionManager: JwtSessionManagerService

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: Object) { }

  login(user: LoginRequest): Observable<any> {
    return this.http.post<ResponseRequest>(`${this.baseUrlAuth}`.concat(environment.api.authPath), user);;
  }
  verifyOtp(veriryOtp: OtpRequest): Observable<any> {
    return this.http.post<ResponseRequest>(`${this.baseUrlAuth}`.concat("/auth/verity-otp"), veriryOtp);
  }
  deleteOtp(id: number, usuario: string): Observable<any> {
    return this.http.delete<ResponseRequest>(`${this.baseUrlAuth}`.concat(`/otp/${id}/${usuario}`));
  }
  saveUsernameAndTokenUInLocalStorage(clave: string, token: string) {
    if (isPlatformBrowser(this.platformId)) {
      this.setItem(AuthenticationUtil.CLAVE_KEY_USER, clave);
      this.setItem(AuthenticationUtil.TOKEN_KEY_USER, token);
      /*  localStorage.setItem(AuthenticationUtil.CLAVE_KEY_USER, clave);
       localStorage.setItem(AuthenticationUtil.TOKEN_KEY_USER, token); */
    }
  }
  saveValuesEmpleadoUInLocalStorage(siloId: string, depto: string) {
    if (isPlatformBrowser(this.platformId)) {
      /*     localStorage.setItem(AuthenticationUtil.EMP_SILO_ID, siloId);
          localStorage.setItem(AuthenticationUtil.EMP_DEPTO_ID, depto); */
      this.setItem(AuthenticationUtil.EMP_SILO_ID, siloId);
      this.setItem(AuthenticationUtil.EMP_DEPTO_ID, depto);
    }
  }
  saveValuesPueatoAnTipoEmpInLocalStorage(tipoEmpleado: string, nombrePuesto: string) {
    if (isPlatformBrowser(this.platformId)) {
      /*  localStorage.setItem(AuthenticationUtil.EMP_TIPO, tipoEmpleado);
       localStorage.setItem(AuthenticationUtil.EMP_NOM_PUESTO, nombrePuesto); */
      this.setItem(AuthenticationUtil.EMP_TIPO, tipoEmpleado);
      this.setItem(AuthenticationUtil.EMP_NOM_PUESTO, nombrePuesto);
    }
  }
  saveUbicacionUserUInLocalStorage(ubicacion: string) {
    if (isPlatformBrowser(this.platformId)) {
      //localStorage.setItem(AuthenticationUtil.UBICACION_USUARIO, ubicacion);
      this.setItem(AuthenticationUtil.UBICACION_USUARIO, ubicacion);
    }
  }
  getClaveInLocalStorage(): string | null {
    /*  if (isPlatformBrowser(this.platformId)) {
       return localStorage.getItem(AuthenticationUtil.CLAVE_KEY_USER);
     } */
    return this.getItem(AuthenticationUtil.CLAVE_KEY_USER);
  }
  getTipoEmpInLocalStorage(): string | null {
    /*  if (isPlatformBrowser(this.platformId)) {
       return localStorage.getItem(AuthenticationUtil.EMP_TIPO);
     } */
    return this.getItem(AuthenticationUtil.EMP_TIPO);
  }
  getTokenInLocalStorage(): string | null {
    /*  if (isPlatformBrowser(this.platformId)) {
       return localStorage.getItem(AuthenticationUtil.TOKEN_KEY_USER);
     } */
    return this.getItem(AuthenticationUtil.TOKEN_KEY_USER);
    return null;
  }
  getUsernameInLocalStorage(): string | null {
    /* if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem(AuthenticationUtil.CLAVE_KEY_USER);
    }
    return null; */
    // 1. Obtener la sesión ACTIVA de esta pestaña
    return this.getItem(AuthenticationUtil.CLAVE_KEY_USER);
  }
  getNombrePuestoInLocalStorage(): string | null {
    /*     if (isPlatformBrowser(this.platformId)) {
          return localStorage.getItem(AuthenticationUtil.EMP_NOM_PUESTO_OBJ);
        } */
    return this.getItem(AuthenticationUtil.EMP_NOM_PUESTO);
  }
  getSiloIdInLocalStorage(): string | null {
    /*    if (isPlatformBrowser(this.platformId)) {
         return localStorage.getItem(AuthenticationUtil.EMP_SILO_ID);
       } */
    return this.getItem(AuthenticationUtil.EMP_SILO_ID);
  }
  getPuestoIddInLocalStorage(): string | null {
    /*     if (isPlatformBrowser(this.platformId)) {
          return localStorage.getItem(AuthenticationUtil.EMP_PUESTO_ID);
        }
        return null; */
    return this.getItem(AuthenticationUtil.EMP_PUESTO_ID);
  }
  getNombrePuestonLocalStorage(): string | null {
    /*    if (isPlatformBrowser(this.platformId)) {
         return localStorage.getItem(AuthenticationUtil.EMP_NOM_PUESTO);
       } */
    return this.getItem(AuthenticationUtil.EMP_NOM_PUESTO);
  }

  getUbicacionUsuarioInLocalStorage(): Observable<String> {
    if (isPlatformBrowser(this.platformId)) {
      let ubicacionValue = localStorage.getItem(AuthenticationUtil.UBICACION_USUARIO);
      if (ubicacionValue) {
        this.getUbicacionObserver.next(ubicacionValue);
      } else {
        this.getUbicacionObserver.next("");
      }
    }
    this.getUbicacionObserver.complete();
    return this.getUbicacionObserver.asObservable();
  }

  clearLocalStorage(): void {
   if (isPlatformBrowser(this.platformId)) {
      try {
        sessionStorage.clear();
      } catch (e) {
        console.error('Error al limpiar sessionStorage:', e);
      }
    }
  }

  isAuthenticated(): boolean | null {
    if (isPlatformBrowser(this.platformId)) {
      return !!this.getTokenInLocalStorage();
    }
    return null;
  }
  setItem(key: string, value: string): void {
    if (isPlatformBrowser(this.platformId)) {
      try {
        sessionStorage.setItem(key, value);
      } catch (e) {
        console.error('Error al guardar en sessionStorage:', e);
      }
    }
  }

  getItem(key: string): string | null {
    if (isPlatformBrowser(this.platformId)) {
      try {
        return sessionStorage.getItem(key);
      } catch (e) {
        console.error('Error al obtener de sessionStorage:', e);
        return null;
      }
    }
    return null;
  }
}
