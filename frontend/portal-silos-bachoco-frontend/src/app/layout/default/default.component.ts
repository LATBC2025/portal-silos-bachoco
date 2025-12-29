
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { map, Observable, shareReplay } from 'rxjs';
import { ChangeDetectionStrategy, Component, inject, OnInit, ViewChild } from '@angular/core';
import { MenuComponent } from '../../shared/menu/menu.component';
import { CommonModule } from '@angular/common';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { RouterModule } from '@angular/router';
import { SidenavServiceService } from '../../services/sidenav-service.service';

@Component({
    selector: 'app-default',
    standalone: true,
    imports: [
        MenuComponent, 
        CommonModule,
        MatSidenavModule,
        MatToolbarModule,
        MatIconModule,
        MatExpansionModule,
        MatListModule,
        RouterModule
    ],
    template: `
        <mat-sidenav-container class="sidenav-container">
              <mat-sidenav
                #drawer
                class="sidenav"
                [ngClass]="{ 'mat-elevation-z4': true }"
                [attr.role]="(isHandset$ | async) ? 'dialog' : 'navigation'"
                [mode]="(isHandset$ | async) ? 'over' : 'side'"
                [opened]="!(isHandset$ | async)"
              >
                <button
                  type="button"
                  mat-icon-button
                  (click)="drawer.toggle()"
                  class="close-button"
                >
                <mat-icon>close</mat-icon>
                </button>
              <app-menu class="h-100 bg-white py-4 tilt-in-left-1"></app-menu>
            </mat-sidenav>
            <mat-sidenav-content class="content">
             <router-outlet></router-outlet>
            </mat-sidenav-content>
        </mat-sidenav-container>
  `,
    styles: `
   :host {
      display: block;
    }
    .aside {
      background-color: var(--white-color);
      min-width: 250px;
      min-height: 100vh;
    }
    .sidenav-container {
      height: 100vh;
      z-index:10;
    }

    .sidenav {
      width: 300px;
    }

    .sidenav .mat-toolbar {
      background: inherit;
    }

    .mat-toolbar.mat-primary {
      position: sticky;
      top: 0;
      z-index: 1;
    }
    /* Estilo para el botón de cierre */
    .close-button {
      position: absolute;
      top: 0;
      right: 0;
      margin: 10px;
      display:none;
    }

     @media (max-width: 768px) {
      .close-button {
        display:block;
        border:none;
        background-color:#FFFF;
        margin-top:20px;
      }
     }
  `,
   changeDetection: ChangeDetectionStrategy.OnPush
})
export class DefaultComponent implements OnInit {

   private breakpointObserver = inject(BreakpointObserver);
  @ViewChild('drawer')
  drawer!: MatSidenav;
  
  constructor(private sidenavService: SidenavServiceService){
  }

  isHandset$: Observable<boolean> = this.breakpointObserver
      .observe(Breakpoints.Handset)
      .pipe(
        map((result) => result.matches),
        shareReplay()
      );

  ngOnInit(): void {
    this.isHandset$.subscribe(isHandset => {
      // Registra el sidenav en el servicio una vez que esté disponible
      if (this.drawer) {
        this.sidenavService.setSidenav(this.drawer);
      }
    });
  }

  ngAfterViewInit(): void {
    // Es mejor registrarlo aquí para asegurarte de que el sidenav ya está en el DOM
    this.sidenavService.setSidenav(this.drawer);
  }
}
