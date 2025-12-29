export interface BackendError {
  status: number;
  error: string; // Esto contiene el "error-code:1001"
  message: string; // Esto contiene "El correo ya existe"
  path: string;
  timestamp: string;
}