import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { MatExpansionModule } from '@angular/material/expansion';
import { environment } from '../../../../environments/environment';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { Menu } from '../../../models/menu.interface';
import { MENU } from './menu.list';

@Component({
    selector: 'app-aside-menu',
    standalone: true,
    imports: [
        MatButtonModule,
        MatDividerModule,
        MatFormFieldModule,
        MatIconModule,
        MatExpansionModule,
        MatListModule,
        MatExpansionModule,
        RouterModule,
    ],
    template: `
    <mat-accordion multi="true">
      <mat-list-item class="menu__item" routerLinkActive="active" [routerLink]="['/dashboard']">
        <div class="d-flex align-items-center">
          <mat-icon class="me-2">dashboard</mat-icon>
          <span>Dashboard</span>
        </div>
      </mat-list-item>
      @for (menuItem of menuList; track menuItem.id) { @if (menuItem.children) {
      <mat-expansion-panel>
        <mat-expansion-panel-header>
          <mat-panel-title>
            <mat-icon>{{ menuItem.icon }}</mat-icon>
            {{ menuItem.name }}
          </mat-panel-title>
        </mat-expansion-panel-header>
        <mat-nav-list>
          @for( child of menuItem.children; track child.id) {
          <mat-list-item [routerLink]="menuItem.url+child.url" routerLinkActive="active">
            <div class="d-flex align-items-center">
              <span>{{ child.name }}</span>
            </div>
          </mat-list-item>
          }
        </mat-nav-list>
      </mat-expansion-panel>
      } @else {
      <mat-list-item class="menu__item" [routerLink]="menuItem.url" routerLinkActive="active">
        <div class="d-flex align-items-center">
          <mat-icon class="me-2">{{ menuItem.icon }}</mat-icon>
          <span>{{ menuItem.name }}</span>
        </div>
      </mat-list-item>
      } }
    </mat-accordion>
  `,
    styles: `
    :host {
      display: block;
    }

    .menu__item:hover {
      background-color: #f5f5f5;
      cursor: pointer;
    }

    .menu__item a {
      text-decoration: none;
      color: inherit;
    }

    .mat-expansion-panel:not([class*="mat-elevation-z"]) {
      box-shadow: none!important;
    }

    .mat-expansion-panel-header {
      padding: 0 16px;
    }

    .mat-expansion-panel-header {
      font-size: inherit;
      font-weight: inherit;
    }

    .material-icons {
      margin-right: 8px;
    }

    .active {
      border-right: 4px solid #4318FF;
      .mat-icon {
        color: #4318FF!important;
      }
      span {
        color: var(--black-color);
        font-weight: 800;
      }
    }

    a {
      text-decoration: none;
      color: inherit;
    }
  `
})
export class AsideMenuComponent {

   menuList: Menu[];

  constructor() {
    this.menuList = [];
  }

  ngOnInit(): void {
    this.menuList = MENU;
  }
}
