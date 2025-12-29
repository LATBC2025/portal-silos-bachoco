import { Routes } from '@angular/router';
import { BlankComponent } from './layout/blank/blank.component';
import { DashboardComponent } from './pages/dashboard/dashboard/dashboard.component';
import { DefaultComponent } from './layout/default/default.component';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { ProgramacionArriboComponent } from './pages/programacion-arribo/programacion-arribo.component';
import { RegistroEmpleadoComponent } from './pages/empleado/registro-empleado/registro-empleado.component';
import { ReporteArriboComponent } from './pages/arribo/reporte-arribo/reporte-arribo.component';
import { RegistroMaterialComponent } from './pages/material/registro-material/registro-material.component';
import { RegistroBodegaComponent } from './pages/bodega/registro-bodega/registro-bodega.component';
import { RegistroSiloComponent } from './pages/silo/registro-silo/registro-silo.component';
import { ConfirmacionDespachoComponent } from './pages/despacho/confirmacion-despacho/confirmacion-despacho.component';
import { ReportePedidoCompraComponent } from './pages/pedidoCompra/reporte-pedido-compra/reporte-pedido-compra.component';
import { ReportePedidoTrasladoComponent } from './pages/pedidoTraslado/reporte-pedido-traslado/reporte-pedido-traslado.component';
import { RegistroEmpleadoExternoComponent } from './pages/empleado/registro-empleado-externo/registro-empleado-externo.component';
import { PlantaRegistroComponent } from './pages/planta/planta-registro/planta-registro.component';
import { DespachosComponent } from './pages/reportes/despachos/despachos.component';

export const routes: Routes = [
    {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    title: 'Bachoco silos',
    component: BlankComponent,
    children: [{ path: '', component: LoginComponent }],
  },
  {
    path: 'home',
    title: 'Bachoco silos| Home',
    component: DefaultComponent,
    children: [{ path: '', component: HomeComponent }],
  },
      {
        path: 'program-arribo',
        title: 'Bachoco silos | Reportes',
        component: DefaultComponent,
        children: [
            { path: '', component: ProgramacionArriboComponent },
        ]
    },
    {
        path: 'reporte-arribo',
        title: 'Bachoco silos | Reporte de Arribos',
        component: DefaultComponent,
        children: [
            { path: '', component: ReporteArriboComponent },
        ]
    },
     {
        path: 'reporte-despachos',
        title: 'Bachoco silos | Reporte Despachos',
        component: DefaultComponent,
        children: [
            { path: '', component: DespachosComponent },
        ]
    },
    {
        path: 'reporte-pedido-compra',
        title: 'Bachoco silos | Gestion de Reportes',
        component: DefaultComponent,
        children: [
            { path: '', component: ReportePedidoCompraComponent },
        ]
    },
    {
        path: 'reporte-pedido-traslado',
        title: 'Bachoco silos | Gestion de Reportes',
        component: DefaultComponent,
        children: [
            { path: '', component: ReportePedidoTrasladoComponent },
        ]
    },
     {
        path: 'registro-empleado-interno',
        title: 'Bachoco silos | Gestion de catalogos',
        component: DefaultComponent,
        children: [
            { path: '', component: RegistroEmpleadoComponent },
        ]
    },
    {
        path: 'registro-empleado-externo',
        title: 'Bachoco silos | Gestion de catalogos',
        component: DefaultComponent,
        children: [
            { path: '', component: RegistroEmpleadoExternoComponent },
        ]
    },
     {
        path: 'registro-material',
        title: 'Bachoco silos | Gestion de catalogos',
        component: DefaultComponent,
        children: [
            { path: '', component: RegistroMaterialComponent },
        ]
    },
    {
        path: 'registro-bodega',
        title: 'Bachoco silos | Gestion de catalogos',
        component: DefaultComponent,
        children: [
            { path: '', component: RegistroBodegaComponent },
        ]
    },
    {
        path: 'registro-silo',
        title: 'Bachoco silos | Gestion de catalogos',
        component: DefaultComponent,
        children: [
            { path: '', component: RegistroSiloComponent },
        ]
    },
     {
        path: 'registro-planta',
        title: 'Bachoco silos | Gestion de catalogos',
        component: DefaultComponent,
        children: [
            { path: '', component: PlantaRegistroComponent },
        ]
    },
    {
         path: 'confirmacion-despacho',
        title: 'Bachoco silos | Gestion de procesos',
        component: DefaultComponent,
        children: [
            { path: '', component: ConfirmacionDespachoComponent },
        ]
    },
];
