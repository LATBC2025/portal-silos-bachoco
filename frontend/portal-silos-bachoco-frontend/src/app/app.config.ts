import { ApplicationConfig, APP_INITIALIZER, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { ConfigService } from './services/config/config.service';
import { provideLottieOptions } from 'ngx-lottie';
import { authInterceptorInterceptor } from './services/interceptor/auth-inteceptor.interceptor';
import { loaderInterceptor } from './services/interceptor/load-service.interceptor';

// Función de inicialización
export function initializeApp(configService: ConfigService) {
  return (): Promise<any> => {
    return configService.loadConfig();
  };
}

export const appConfig: ApplicationConfig = {
  providers: [
   provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes),
    provideLottieOptions({
      player: () => import('lottie-web'),
    }),
    provideHttpClient(
      withFetch(),
      withInterceptors([authInterceptorInterceptor, loaderInterceptor])
    ),
    {
      provide: APP_INITIALIZER,
      useFactory: initializeApp,
      deps: [ConfigService],
      multi: true
    }
  ]
};