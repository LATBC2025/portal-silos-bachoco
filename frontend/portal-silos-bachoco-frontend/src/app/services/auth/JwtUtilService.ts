export class JwtUtilService {
  isTokenExpired(token: string): boolean {
    if (!token) return true;

    try {
      // Decodificar la parte del payload del JWT (sin verificar firma)
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const payload = JSON.parse(window.atob(base64));

      // Verificar expiración (el campo 'exp' está en segundos)
      const currentTime = Math.floor(Date.now() / 1000);
      return payload.exp < currentTime;

    } catch (error) {
      console.error('Error al decodificar el token:', error);
      return true; // Considerar token inválido si no se puede decodificar
    }
  }

  decodeToken(token: string): any {
    if (!token) return null;

    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      return JSON.parse(window.atob(base64));
    } catch (error) {
      console.error('Error decodificando el token:', error);
      return null;
    }
  }
}