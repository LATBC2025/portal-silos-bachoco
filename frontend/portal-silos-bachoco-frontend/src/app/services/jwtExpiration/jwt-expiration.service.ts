import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class JwtExpirationService {

  constructor(private snackBar: MatSnackBar){}

  private messageShown = false;
   private messageShownForbiden = false;

  showMessage(): boolean {
    if (!this.messageShown) {
      this.messageShown = true;
      this.snackBar.open("Tu sesión ha expirado. Por favor, inicia sesión nuevamente.", 'Cerrar', {
        duration: 5000,
        panelClass: ['error-snackbar'],
      });
      return true;
    }
    return false;
  }

  showMessageForBiden(): boolean {
    if (!this.messageShownForbiden) {
      this.messageShownForbiden = true;
      this.snackBar.open("No tienes permisos para realizar esta operación.", 'Cerrar', {
        duration: 9000,
        panelClass: ['error-snackbar'],
        verticalPosition: 'top',
        horizontalPosition: 'right' 
      });
      return true;
    }
    return false;
  }

  resetMessage(): void {
    this.messageShown = false;
  }
}
