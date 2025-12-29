// error-alert.interceptor.ts (¡NUEVO! - mínimo 15 líneas)
import { HttpInterceptorFn } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import Swal from 'sweetalert2';

export const errorAlertInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error) => {
      // Solo mostrar para los mismos errores que bloqueas
      if (error.status === 403 || error.status === 401 || error.status === 500) {
        
        // Este SweetAlert SIEMPRE se mostrará (no está bloqueado)
        Swal.fire({
          icon: 'error',
          title: getTitleByStatus(error.status),
          text: getMessageByStatus(error.status),
          confirmButtonText: 'Entendido',
          allowOutsideClick: false
        });
      }
      
      return throwError(() => error);
    })
  );
};

// Funciones helper para mensajes
function getTitleByStatus(status: number): string {
  switch(status) {
    case 403: return 'Acceso Denegado';
    case 401: return 'No Autenticado';
    case 500: return 'Error del Servidor';
    default: return 'Error';
  }
}

function getMessageByStatus(status: number): string {
  switch(status) {
    case 403: return 'Su rol no permite realizar esta operación';
    case 401: return 'Debe iniciar sesión para acceder a este recurso';
    case 500: return 'Error interno del servidor. Intente más tarde';
    default: return 'Ha ocurrido un error inesperado';
  }
}