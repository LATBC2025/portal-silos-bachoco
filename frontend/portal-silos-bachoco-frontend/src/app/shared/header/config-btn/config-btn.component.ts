import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { MatMenuModule } from '@angular/material/menu';

@Component({
    selector: 'app-config-btn',
     standalone: true,
    imports: [MatButtonModule, MatMenuModule, MatIconModule, RouterModule],
    template: `
   <button mat-icon-button [matMenuTriggerFor]="menu" aria-label="Example icon-button with a menu">
      <mat-icon>more_vert</mat-icon>
    </button>
    <mat-menu #menu="matMenu">
      <button mat-menu-item (click)="logout()">
        <mat-icon>logout</mat-icon>
        <span class="fw-semibold">Cerrar Sesion y Salir</span><br>
        <small>Salir de la cuenta</small>
      </button>
    </mat-menu>
  `,
    styles: `
     :host {
      display: block;
    }
  `
})
export class ConfigBtnComponent {

  constructor(private router: Router
  ) {}
  logout() {
    this.router.navigate(['/login']);
  }
}
