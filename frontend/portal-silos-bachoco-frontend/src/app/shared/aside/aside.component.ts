import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { RouterModule } from '@angular/router';
import { AsideMenuComponent } from './aside-menu/aside-menu.component';
import { environment } from '../../../environments/environment';

@Component({
    selector: 'app-aside',
    standalone: true,
    imports: [
        AsideMenuComponent,
        MatIconModule,
        MatDividerModule,
        MatExpansionModule,
        RouterModule,
    ],
    template: `
   <div class="aside__content container">
      <div class="aside__header py-4">
        <img src="assets/images/shared/telcel-logo.svg" alt="logo" class="logo img-fluid my-2"/>
        <h2 class="fs-6 fw-bolder text-uppercase">Cadenas <span class="fw-light text-muted">Comerciales</span></h2>
      </div>
      <div class="aside__menu">
        <app-aside-menu></app-aside-menu>
        <div class="menu__footer">
          <mat-divider></mat-divider>
          <div
            class="mat-expansion-panel mat-expansion-panel-spacing"
            [routerLink]="['/login']"
          >
            <div class="mat-expansion-panel-header">
              <mat-panel-title>
                <mat-icon>logout</mat-icon>
                <div class="ms-4 d-flex flex-column">
                  <span class="fs-6 fw-normal">Cerrar Sesión</span>
                </div>
              </mat-panel-title>
            </div>
          </div>
          <p class="text-muted text-center">
            <small>{{ version }}</small>
          </p>
        </div>
      </div>
    </div>
  `,
    styles: `
    :host {
      display: block;
      width: 100%;
      height: 100%;
    }

    .aside__header .logo {
      max-width: 100px;
    }
  `
})
export class AsideComponent {
 version = environment.version;
}
