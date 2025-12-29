import { Component } from '@angular/core';
import { DashboardSectionComponent } from '../dashboard-section/dashboard-section.component';
import { Menu } from '../../../models/menu.interface';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [DashboardSectionComponent],
    template: `
       @for(menu of menuList; track menu.id) {
      <app-dashboard-section [menu]="menu" class="mb-4"></app-dashboard-section>
    }
  `,
    styles: ``
})
export class DashboardComponent {
  menuList: Menu[] = [
  {
    id: 1,
    name: "Gestión de Cobranza",
    url: "/gestion-cobranza",
    icon: "dashboard",
    description: "Módulo para la gestión y control de la cobranza",
    children: [
        { id: 2, name: "Seleccion Cadena Comercial", url: "/seleccion-cadena-comercial", icon: null, description: "Seleccionar la cadena comercial" },
        { id: 3, name: "Conciliación Cuenta por Cobrar", url: "/conciliacion-cuenta-por-cobrar", icon: null, description: "Conciliar cuentas por cobrar" },
        { id: 4, name: "Cobranza de Cuenta por Cobrar", url: "/cobranza-cuenta-por-cobrar", icon: null, description: "Gestionar la cobranza de cuentas por cobrar" },
        { id: 5, name: "Contabilización de Pagos", url: "/contabilizacion-pagos", icon: null, description: "Contabilizar los pagos recibidos" },
        { id: 6, name: "Carga FBL5N", url: "/carga-fbl5n", icon: null, description: "Cargar datos FBL5N" },
        { id: 7, name: "Depósitos Bancarios", url: "/depositos-bancarios", icon: null, description: "Gestionar depósitos bancarios" },
        { id: 8, name: "Envío de Notificaciones", url: "/envio-notificaciones", icon: null, description: "Enviar notificaciones a los clientes" },
        { id: 9, name: "Carga IMR-NAR", url: "/carga-imr-nar", icon: null, description: "Cargar datos IMR-NAR" },
        { id: 10, name: "Contabilización Automática", url: "/contabilizacion-automatica", icon: null, description: "Contabilización automática de transacciones" },
        { id: 11, name: "Conciliación Filiales", url: "/conciliacion-filiales", icon: null, description: "Conciliar cuentas con filiales" }
    ]
  }]
}
