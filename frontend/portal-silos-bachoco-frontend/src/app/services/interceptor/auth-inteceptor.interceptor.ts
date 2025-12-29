import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { JwtExpirationService } from '../jwtExpiration/jwt-expiration.service';
import { AuthServiceService } from '../auth/auth-service.service';
import { ErrorModalServiceService } from '../modal-error/error-modal-service.service';

declare global {
  interface Window {
    __swalBlockedForErrors?: boolean;
  }
}
export const authInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService=inject(AuthServiceService);
  const router = inject(Router);
  const jwtExpirationService = inject(JwtExpirationService);
  const modalService=inject(ErrorModalServiceService);
  let authToken =authService.getTokenInLocalStorage();
  let modifiedReq = req;
  if (authToken) {
    modifiedReq = req.clone({
      setHeaders: {
        Authorization:` Bearer ${authToken}`
      }
    });
  }
return next(modifiedReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // 1. Manejo del error 403
      if (error.status === 403) {
        // 2. Lógica para diferenciar el error de "Contraseña Expirada"
        // Esta es la parte CLAVE. Necesitas un código o mensaje específico del servidor.
        // OPCIÓN A: Basado en un mensaje de error específico en el cuerpo del JSON.
        // Asume que el servidor envía un cuerpo de error JSON con una propiedad 'message' o 'code'.
        const errorBody = error.error; // 'error.error' es donde suele estar el cuerpo de la respuesta.
        if (errorBody && errorBody.message && errorBody.message.includes('password expired')) {
            // Por ejemplo, redirigir a una ruta de cambio de contraseña
            router.navigate(['/cambio-contrasena']); 
        }else if (errorBody && errorBody.message && error.error['error']=="error-code:no-auth-operation") {
            jwtExpirationService.showMessageForBiden()
        } 
        // OPCIÓN B: Lógica de expiración de JWT genérica (si el 403 significa token inválido)
        else if (jwtExpirationService.showMessage()) {
            console.warn('Token o sesión expirada/inválida. Redirigiendo a login...');
            router.navigate(['/login']);
        }
      } 
      // 3. Manejar otros errores (por ejemplo, 401 Unauthorized para un token no válido)
      else if (error.status === 401) {
         console.warn('Token inválido o no autorizado. Redirigiendo a login...');
         router.navigate(['/login']);
      }
      // Siempre propaga el error para que el componente que hizo la petición pueda manejarlo también.
      return throwError(() => error);
    })
  );
};