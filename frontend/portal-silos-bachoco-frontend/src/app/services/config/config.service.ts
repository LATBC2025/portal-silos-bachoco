import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  private config: any = {};
  private configLoaded = false;

  loadConfig(): Promise<void> {
    return new Promise((resolve) => {
      setTimeout(() => {
        this.setDynamicConfig();
        this.configLoaded = true;
        resolve();
      }, 0);
    });
  }

  private setDynamicConfig(): void {
    const hostname = window.location.hostname;
    const protocol = window.location.protocol;
    const port = window.location.port;

    // CONSTRUCCIÓN MEJORADA - DESARROLLO APUNTA A PUERTO 8080
    const baseUrl = this.buildBaseUrl(protocol, hostname, port);
    const contextPath = this.detectContextPath(hostname,port);
    const apiUrl = `${baseUrl}${contextPath}/v1`;
    
    // DETECCIÓN INTELIGENTE DEL ENTORNO
    const environmentType = this.detectEnvironmentType(hostname, protocol);
    const isProduction = environmentType === 'production';

    this.config = {
      apiUrl: apiUrl,
      baseUrl: apiUrl,
      production: isProduction,
      environment: environmentType,
      contextPath: contextPath,
      apiPath: '/v1'
    };
    this.updateEnvironment();
  }

  private buildBaseUrl(protocol: string, hostname: string, port: string): string {
    // CAMBIO CLAVE: En desarrollo, usar puerto 8080 del backend
    if (this.isDevelopmentEnvironment(hostname)) {
      return `${protocol}//${hostname}:8080`; // ← Backend en puerto 8080
    }
    
    let baseUrl = `${protocol}//${hostname}`;
    
    // Solo agregar puerto si no es el default
    if (port && !this.isDefaultPort(protocol, port)) {
      baseUrl += `:${port}`;
    }
    
    return baseUrl;
  }

  private detectContextPath(hostname: string,port: string): string {
    // Si es una IP interna, usa el contexto de aplicación
    if (this.isInternalIP(hostname)) {
      return '/Bachoco-silos';
    }
     if (this.isDevelopmentEnvironment(hostname) && port=="8080") {
      return '/Bachoco-silos'; // ← Backend en puerto 8080
    }if (this.isDevelopmentEnvironment(hostname) && port=="4200") {
      return ''; // ← Backend en puerto 8080
      //return '/Bachoco-silos'; 
    }
    // Para dominio público o desarrollo, no usar contexto adicional
    return '';
  }

  private detectEnvironmentType(hostname: string, protocol: string): string {
    // Basado en el hostname y protocolo
    if (hostname === 'bachoco-externo.net' || hostname === 'www.bachoco-externo.net') {
      return 'production';
    } else if (this.isInternalIP(hostname)) {
      return 'internal';
    } else if (this.isDevelopmentEnvironment(hostname)) {
      return 'development';
    } else {
      // Por defecto, si usa HTTPS es producción, sino desarrollo
      return protocol === 'https:' ? 'production' : 'development';
    }
  }

  private isDevelopmentEnvironment(hostname: string): boolean {
    // AMBIENTE DE DESARROLLO: localhost en cualquier puerto
    return hostname === 'localhost' || hostname === '127.0.0.1';
  }

  private isInternalIP(hostname: string): boolean {
    return hostname.startsWith('172.17.') || 
           hostname.startsWith('192.168.') || 
           hostname.startsWith('10.') ||
           hostname.startsWith('172.') ||
           /^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+$/.test(hostname);
  }

  private isDefaultPort(protocol: string, port: string): boolean {
    return (protocol === 'http:' && port === '80') || 
           (protocol === 'https:' && port === '443');
  }

  private updateEnvironment(): void {
    Object.assign(environment, {
      production: this.config.production,
      api: {
        protocol: this.extractProtocol(this.config.apiUrl),
        baseUrl: this.extractBaseUrl(this.config.apiUrl),
        fullUrl: this.config.apiUrl,
        authPath: '/auth/login',
        auth: 'Authorization', 
        requestTime: 3000000
      }
    });
  }

  private extractProtocol(url: string): string {
    return url.startsWith('https') ? 'https://' : 'http://';
  }

  private extractBaseUrl(url: string): string {
    return url.replace(/^https?:\/\//, '');
  }

  get apiUrl(): string {
    return this.config.apiUrl;
  }

  isLoaded(): boolean {
    return this.configLoaded;
  }

  getCurrentEnvironment(): string {
    return this.config.environment;
  }

  getConfigSummary() {
    return {
      accessedFrom: window.location.href,
      apiBaseUrl: environment.api.baseUrl,
      apiFullUrl: environment.api.baseUrl,
      environment: this.config.environment,
      production: this.config.production
    };
  }
}