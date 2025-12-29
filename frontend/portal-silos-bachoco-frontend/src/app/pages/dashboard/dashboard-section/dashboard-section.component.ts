import { Component, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Menu } from '../../../models/menu.interface';

@Component({
    selector: 'app-dashboard-section',
    standalone: true,
    imports: [MatIconModule],
    templateUrl: './dashboard-section.component.html',
    styleUrl: './dashboard-section.component.css'
})
export class DashboardSectionComponent {

  @Input() menu: Menu;
  constructor() {
    this.menu = {
      id: 1,
      name: 'Dashboard',
      description: 'Módulo de inicio',
      icon: 'dashboard',
      url: '/dashboard',
    };
  }
}
