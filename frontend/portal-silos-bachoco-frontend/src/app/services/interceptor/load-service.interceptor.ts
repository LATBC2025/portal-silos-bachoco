import { finalize } from 'rxjs';
import { hideLoader, showLoader } from '../load/loading.function';
import { HttpInterceptorFn } from '@angular/common/http';

export const loaderInterceptor: HttpInterceptorFn = (req, next) => {
  showLoader();
  return next(req).pipe(
    finalize(() => {
      hideLoader();
    })
  );
};