import { isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, OnDestroy, PLATFORM_ID } from '@angular/core';
import { BehaviorSubject, fromEvent, Subscription } from 'rxjs';
import { AuthServiceService } from './auth-service.service';
import { JwtUtilService } from './JwtUtilService';

export interface JwtSession {
  sessionId: string;
  token: string;
  userData: {
    claveUser: string;
    empSiloId: string;
    empDeptoId: string;
    empNomPuesto: string;
    empTipo: string;
    ubicacion?: string;
    empPuestoId?: string;
    username?:string;
  };
  lastUsed: number;
  createdAt: number;
}

@Injectable({
  providedIn: 'root'
})
export class JwtSessionManagerService implements OnDestroy {
  private readonly SESSIONS_STORAGE_KEY = 'multi_sessions_data';
  private readonly MAX_SESSIONS = 5;

  private currentSessionId$ = new BehaviorSubject<string>('');
  private sessionsMap$ = new BehaviorSubject<Map<string, JwtSession>>(new Map());
  private storageListener$: Subscription | null = null;
  private cleanupInterval: any;

  private jwtHelper = new JwtUtilService();

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    if (isPlatformBrowser(this.platformId)) {
      this.initializeService();
    }
  }

  ngOnDestroy(): void {
    if (this.storageListener$) {
      this.storageListener$.unsubscribe();
    }
    if (this.cleanupInterval) {
      clearInterval(this.cleanupInterval);
    }
  }

  private initializeService(): void {
    const tabId = this.generateTabId();
    sessionStorage.setItem('current_tab_id', tabId);

    this.loadSessionsFromStorage();
    this.setupCrossTabCommunication();
    this.startTokenCleanup();
  }

  private generateTabId(): string {
    let tabId = sessionStorage.getItem('persistent_tab_id');
    if (!tabId) {
      tabId = `tab_${Date.now()}_${Math.random().toString(36).substring(2, 9)}`;
      sessionStorage.setItem('persistent_tab_id', tabId);
    }
    return tabId;
  }

  private loadSessionsFromStorage(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    try {
      const stored = localStorage.getItem(this.SESSIONS_STORAGE_KEY);
      const sessionsArray: JwtSession[] = stored ? JSON.parse(stored) : [];

      const sessionsMap = new Map<string, JwtSession>();
      const now = Date.now();

      sessionsArray.forEach(session => {
        // Solo cargar sesiones válidas (no expiradas y no muy antiguas)
        if (!this.jwtHelper.isTokenExpired(session.token)) {
          sessionsMap.set(session.sessionId, session);
        }
      });

      this.sessionsMap$.next(sessionsMap);
      this.saveSessionsToStorage(); // Re-guardar sin expirados
    } catch (error) {
      console.error('Error cargando sesiones:', error);
      this.sessionsMap$.next(new Map());
    }
  }

  private saveSessionsToStorage(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    const sessionsArray = Array.from(this.sessionsMap$.value.values());

    // Ordenar por último uso y limitar cantidad
    const sorted = sessionsArray.sort((a, b) => b.lastUsed - a.lastUsed);
    const toKeep = sorted.slice(0, this.MAX_SESSIONS);

    const newMap = new Map<string, JwtSession>();
    toKeep.forEach(session => newMap.set(session.sessionId, session));

    this.sessionsMap$.next(newMap);

    localStorage.setItem(
      this.SESSIONS_STORAGE_KEY,
      JSON.stringify(toKeep)
    );
  }

  private setupCrossTabCommunication(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    // Escuchar cambios en localStorage de otras pestañas
    this.storageListener$ = fromEvent<StorageEvent>(window, 'storage')
      .subscribe(event => {
        if (event.key === this.SESSIONS_STORAGE_KEY) {
          this.loadSessionsFromStorage();
        }
      });
  }

  private startTokenCleanup(): void {
    this.cleanupInterval = setInterval(() => {
      const expiredSessions = Array.from(this.sessionsMap$.value.values())
        .filter(session => this.jwtHelper.isTokenExpired(session.token));

      if (expiredSessions.length > 0) {
        expiredSessions.forEach(session => {
          this.sessionsMap$.value.delete(session.sessionId);
        });
        this.saveSessionsToStorage();
      }
    }, 60000);
  }

  // ========== API PÚBLICA ==========

  createSession(token: string, userData: any): string {
    const sessionId = `sess_${Date.now()}_${Math.random().toString(36).substring(2, 7)}`;

    try {
      const decodedToken = this.jwtHelper.decodeToken(token);

      const newSession: JwtSession = {
        sessionId,
        token,
        userData: {
          claveUser: userData.clave || userData.username || '',
          empSiloId: userData.empSiloId || userData.siloId || '',
          empDeptoId: userData.empDeptoId || userData.depto || '',
          empNomPuesto: userData.empNomPuesto || userData.nombrePuesto || '',
          empTipo: userData.empTipo || userData.tipoEmpleado || '',
          ubicacion: userData.ubicacion || '',
          empPuestoId: userData.empPuestoId || userData.puestoId || ''
        },
        lastUsed: Date.now(),
        createdAt: Date.now()
      };

      // Agregar al mapa
      const currentMap = this.sessionsMap$.value;
      currentMap.set(sessionId, newSession);
      this.sessionsMap$.next(currentMap);

      // Establecer como sesión activa para esta pestaña
      sessionStorage.setItem('active_session_id', sessionId);
      this.currentSessionId$.next(sessionId);

      // Guardar en storage
      this.saveSessionsToStorage();

      return sessionId;
    } catch (error) {
      console.error('Error creando sesión:', error);
      throw new Error('Token inválido');
    }
  }

  getActiveSession(): JwtSession | null {
    const sessionId = sessionStorage.getItem('active_session_id') ||
      this.currentSessionId$.value;

    if (sessionId) {
      const session = this.sessionsMap$.value.get(sessionId);
      if (session && !this.jwtHelper.isTokenExpired(session.token)) {
        // Actualizar último uso
        session.lastUsed = Date.now();
        return session;
      }
    }

    return null;
  }

  getActiveToken(): string | null {
    const session = this.getActiveSession();
    return session?.token || null;
  }

  getUserData(): any {
    const session = this.getActiveSession();
    return session?.userData || null;
  }

  getAllSessions(): JwtSession[] {
    return Array.from(this.sessionsMap$.value.values())
      .filter(session => !this.jwtHelper.isTokenExpired(session.token))
      .sort((a, b) => b.lastUsed - a.lastUsed);
  }

  removeCurrentSession(): void {
    const sessionId = sessionStorage.getItem('active_session_id');
    if (sessionId) {
      const currentMap = this.sessionsMap$.value;
      currentMap.delete(sessionId);
      this.sessionsMap$.next(currentMap);
      this.saveSessionsToStorage();
    }

    sessionStorage.removeItem('active_session_id');
    this.currentSessionId$.next('');
  }

  clearAllSessions(): void {
    this.sessionsMap$.next(new Map());
    sessionStorage.removeItem('active_session_id');
    this.currentSessionId$.next('');

    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.SESSIONS_STORAGE_KEY);
    }
  }

  // Métodos de utilidad para compatibilidad
  getCurrentTabId(): string {
    return sessionStorage.getItem('current_tab_id') || '';
  }

  getActiveSessionId(): string {
    return sessionStorage.getItem('active_session_id') || '';
  }

  updateSessionData(sessionId: string, updates: Partial<JwtSession['userData']>): boolean {
    if (!isPlatformBrowser(this.platformId)) return false;

    const session = this.sessionsMap$.value.get(sessionId);

    if (!session) {
      console.warn(`Sesión ${sessionId} no encontrada para actualizar`);
      return false;
    }

    try {
      // Actualizar los datos del usuario
      session.userData = { ...session.userData, ...updates };
      session.lastUsed = Date.now(); // Actualizar último uso

      // Actualizar en el mapa
      const currentMap = new Map(this.sessionsMap$.value);
      currentMap.set(sessionId, session);
      this.sessionsMap$.next(currentMap);

      // Guardar cambios en localStorage
      this.saveSessionsToStorage();

      console.log(`Sesión ${sessionId} actualizada:`, updates);
      return true;

    } catch (error) {
      console.error(`Error actualizando sesión ${sessionId}:`, error);
      return false;
    }
  }

  /**
   * Actualiza solo el token de una sesión específica (útil para refresh tokens)
   */
  updateSessionToken(sessionId: string, newToken: string): boolean {
    if (!isPlatformBrowser(this.platformId)) return false;

    const session = this.sessionsMap$.value.get(sessionId);

    if (!session) {
      console.warn(`Sesión ${sessionId} no encontrada para actualizar token`);
      return false;
    }

    try {
      // Verificar que el nuevo token sea válido
      if (this.jwtHelper.isTokenExpired(newToken)) {
        console.error('El nuevo token está expirado');
        return false;
      }

      session.token = newToken;
      session.lastUsed = Date.now();

      // Actualizar en el mapa
      const currentMap = new Map(this.sessionsMap$.value);
      currentMap.set(sessionId, session);
      this.sessionsMap$.next(currentMap);

      // Guardar cambios
      this.saveSessionsToStorage();

      return true;

    } catch (error) {
      console.error(`Error actualizando token de sesión ${sessionId}:`, error);
      return false;
    }
  }

  /**
   * Actualiza un campo específico en los datos de usuario
   */
  updateUserField(sessionId: string, field: keyof JwtSession['userData'], value: any): boolean {
    return this.updateSessionData(sessionId, { [field]: value });
  }
}