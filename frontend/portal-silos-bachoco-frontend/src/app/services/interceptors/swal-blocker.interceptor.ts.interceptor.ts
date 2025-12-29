// swal-blocker.interceptor.ts (YA LO TIENES)
import { HttpInterceptorFn } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { ErrorModalServiceService } from '../modal-error/error-modal-service.service';
import { inject } from '@angular/core';

declare global {
  interface Window {
    __swalBlockedForErrors?: boolean;
  }
}

export const swalBlockerInterceptor: HttpInterceptorFn = (req, next) => {
  const originalSwalFire = (window as any).Swal?.fire;
    const modalService=inject(ErrorModalServiceService);
  return next(req).pipe(
    tap({
      error: (error) => {
        const errorBody = error.error;
        if (errorBody && errorBody.message && error.error['error']=="error-code:no-auth-operation") {
            console.log("ENTRO ERROR : "+JSON.stringify(error.error['error']));
             window.__swalBlockedForErrors = true;
          
          if ((window as any).Swal && originalSwalFire) {
            (window as any).Swal.fire = function(...args: any[]) {
              console.warn('SweetAlert bloqueado por error HTTP');
              return Promise.resolve({
                isConfirmed: false,
                isDenied: false,
                isDismissed: true,
                value: undefined
              });
            };
          }
          
          setTimeout(() => {
            window.__swalBlockedForErrors = false;
            if ((window as any).Swal && originalSwalFire) {
              (window as any).Swal.fire = originalSwalFire;
            }
          }, 2000);
            modalService.showModalErrorNotAuthotize();
        } 
      }
    })
  );
};