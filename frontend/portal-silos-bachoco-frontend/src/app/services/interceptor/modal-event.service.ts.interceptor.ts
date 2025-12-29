import { HttpInterceptorFn } from '@angular/common/http';

export const modalEventServiceTsInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req);
};


