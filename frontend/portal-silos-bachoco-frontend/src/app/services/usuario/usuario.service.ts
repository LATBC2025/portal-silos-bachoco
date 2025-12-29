import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { UpdatePasswordRequest } from '../../models/authentication/UpdatePasswordRequest';
import { UpdatePasswordExpiredRequest } from '../../models/authentication/Update.password.expired';
import { PasswordOtpRequest } from '../../models/PasswordOtpRequest';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private baseUrlAuth: string = environment.api.protocol.concat(environment.api.baseUrl);

  constructor(private http: HttpClient) { }

  aupdatePassword(credentials: UpdatePasswordRequest): Observable<any> {
    return this.http.post(this.baseUrlAuth.concat("/usuario/update-password"), credentials);
  }
  updatePasswordExprired(credentials: UpdatePasswordExpiredRequest): Observable<any> {
    return this.http.put(this.baseUrlAuth.concat("/usuario/update-password-expired"), credentials);
  }
  sendClaveByCorreo(credentials: PasswordOtpRequest): Observable<any> {
    return this.http.post(this.baseUrlAuth.concat("/usuario/send-clave-email"), credentials);
  }
}
