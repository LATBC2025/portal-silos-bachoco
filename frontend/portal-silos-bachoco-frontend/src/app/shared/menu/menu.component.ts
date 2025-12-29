import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { environment } from '../../../environments/environment';
import { CommonModule } from '@angular/common';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AuthServiceService } from '../../services/auth/auth-service.service';
import { MenuItem } from '../../models/menu/menu-item.model';
import { CoincidencePuestosMap } from '../../models/menu/Coincidencia.Puestos';
import { filter, Subscription } from 'rxjs';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBook, faChartBar, faCheckCircle, faGamepad, faPieChart, fas } from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../services/shared/utils.service';


@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    MatButtonModule,
    MatDividerModule,
    MatFormFieldModule,
    MatIconModule,
    MatExpansionModule,
    MatListModule,
    MatExpansionModule,
    FontAwesomeModule,
    RouterModule,
    CommonModule,
    MatSidenavModule,
    MatToolbarModule,
  ],
  template: `
<div class="menu container-fluid">
       <div class="contenedor_logo d-flex flex-column">
        <img
          src="./assets/images/login/imagenes-bachuco/Grupo_Bachoco_H_fondo_blanco.png"
          alt="Logo"
          class="img-fluid"
          style="max-width: 11rem"
        />
      </div>
       <p>Portal Silos Bachoco</p>
   <div class="menu__body">
        <div class="menu__content">
          <mat-divider></mat-divider>
          <p>Categoría</p>
            <div class=" my-8">
                @for (item of menuFiltrado; track item.id) {
                    @if (item.isGroup) {
                        <mat-accordion multi="true">
                            <mat-expansion-panel style="margin-top: 10px;">
                                <mat-expansion-panel-header style="pdding:5px 0px;" class="title__text">
                                    <mat-panel-title>
                                          <fa-icon *ngIf="item.icono" [icon]="item.icono"></fa-icon>
                                        <div class="ms-4 d-flex flex-column">
                                            <span >{{ item.titulo }}</span>
                                        </div>
                                    </mat-panel-title>
                                </mat-expansion-panel-header>
                                <mat-nav-list>
                                    @for (child of item.children; track child.id) {
                                        <a
                                            mat-list-item
                                            [routerLink]="[child.ruta]"
                                            routerLinkActive="active">
                                            <small>{{ child.titulo }}</small>
                                        </a>
                                    }
                                </mat-nav-list>
                            </mat-expansion-panel>
                        </mat-accordion>
                    }
                    @if (!item.isGroup) {
                        <div
                            class="mat-expansion-panel mat-expansion-panel-spacing"
                            [routerLink]="[item.ruta]"
                            routerLinkActive="active"
                        >
                            <div class="mat-expansion-panel-header">
                                <mat-panel-title>
                                    <fa-icon *ngIf="item.icono" [icon]="item.icono"></fa-icon>
                                    <div class="ms-4 d-flex flex-column title__text" >
                                        <span >{{ item.subtitulo }}</span>
                                    </div>
                                </mat-panel-title>
                            </div>
                        </div>
                    }
                }
            </div>
        </div>

        <div class="menu__footer">
          <mat-divider></mat-divider>
          <div
            class="mat-expansion-panel mat-expansion-panel-spacing"
            (click)="logout()"
          >
            <div class="mat-expansion-panel-header ">
              <mat-panel-title>
                <mat-icon>logout</mat-icon>
                <div class="ms-4 d-flex flex-column title__text">
                  <span >Cerrar Sesión</span>
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
    }

    .title__text span{
       font: normal normal normal 14px/18px Montserrat;
    }
    .active {
      background: #3B3C43 !important;
      * {
        color: white!important;
      }
    }
     .contenedor_logo{
      width:100%;
      height:auto;
    }
    .contenedor_logo img{
      width: 310px;
      height: 70px;
      margin-left:30px;
    }
    .menu__content{
      margin: 0px 0px;
    }
    .menu__content p{
      margin-top:12px !important;
      font: normal normal normal 14px/20px Montserrat;
      letter-spacing: 0.24px;
      color: #231F20;
    }
    .menu p{
      font: normal normal medium 14px/20px Montserrat;
      letter-spacing: 0.25px;
      color: #000000;
      margin-top:10px;
      margin-left:20px;
    }
    .mat-expansion-panel-spacing {
      margin: 16px 0;
    }

    .mat-expansion-panel-header {
      display: flex;
      align-items: center;
      padding: 0 24px;
      cursor: default;
    }

    .mat-expansion-panel-content {
      padding: 16px 24px;
    }

    .menu {
      height: 100%;
    }

    .menu__body {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }

    a {
      text-decoration: none;
    }
  `
})
export class MenuComponent implements OnInit, OnDestroy {
  version = environment.version;
  empleadoPuestoId: number = -1;
  tipoEmpleado!: number;
  nombrePuesto!: string;
  @Input() userPermissionId: number = 1;

  menuData: MenuItem[] = []; // Menú completo (mapeado del JSON)
  menuFiltrado: MenuItem[] = []; // Menú que se renderizará
  // Variable para almacenar la suscripción del Router
  private routerSubscription: Subscription = new Subscription();

  constructor(private authService: AuthServiceService,
     private utilServ: UtilsService,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private library: FaIconLibrary
  ) {
      this.library.addIconPacks(fas);
      library.addIcons(faGamepad,faPieChart,faCheckCircle,faChartBar,faBook);
  }
  ngOnDestroy(): void {
    // Limpiamos la suscripción para evitar memory leaks
    this.routerSubscription.unsubscribe();
  }

  async logout() {
    const result = await this.utilServ.confirmarCloseCuenta();
    if (result.isConfirmed) {
      localStorage.clear();
      this.router.navigate(['/login']);
    }
  }

  ngOnInit(): void {
    // 1. Cargamos el menú la primera vez que se inicializa el componente
    this.loadMenuData();
    // 2. Nos suscribimos a los eventos del Router
    this.routerSubscription.add(
      this.router.events.pipe(
        // Filtramos solo el evento de fin de navegación
        filter((event) => event instanceof NavigationEnd)
      ).subscribe(() => {
        // 3. Cuando finaliza una navegación, recargamos los datos del menú
        // Esto asegura que si el rol cambió (ej. después de un login), se actualice.
        this.loadMenuData();
      })
    );
  }

  private loadMenuData(): void {
    //La carga debe ser secuencial para obtener el rol actual.
    this.extractNombrePuesto();
    this.extractPuestoId();
    this.extractTipoEmpleado();
    // 1. Obtiene el ID de puesto basado en el nombre (puesto actual del usuario)
    const puestoId = this.findCoincidencePuestoId(this.nombrePuesto);
    // 2. Carga la estructura del menú
    this.menuData = this.getMenuStructure();
    // 3. Filtra el menú con el ID de puesto actual
    this.menuFiltrado = this.filterMenuByPermission(this.menuData, [puestoId]);
    // 4. Forzamos la detección de cambios (necesaria si se usa OnPush o si Angular no detecta la actualización)
    // 💡 Consejo: Elimina esta línea si no usas ChangeDetectionStrategy.OnPush para un mejor rendimiento.
    this.cdr.detectChanges();
  }

  extractPuestoId() {
    this.empleadoPuestoId = Number(this.authService.getPuestoIddInLocalStorage());
  }

  extractTipoEmpleado() {
    this.tipoEmpleado = Number(this.authService.getTipoEmpInLocalStorage());
  }

  extractNombrePuesto() {
    return this.nombrePuesto = this.authService.getNombrePuestonLocalStorage() ?? '';
  }
  private filterMenuByPermission(menu: MenuItem[], userPermissions: number[]): MenuItem[] {

    return menu
      // Filtramos el nivel actual (grupos o enlaces directos)
      .filter(item => {
        // El ítem es visible si:
        // Alguno de los permisos del usuario (userPermissions.some)
        // está incluido en la lista de permisos del ítem (item.permisosRequeridos)
        return item.permisosRequeridos.some(
          requiredId => userPermissions.includes(requiredId)
        );
      })
      // Mapeamos para filtrar recursivamente los hijos
      .map(item => {
        if (item.isGroup && item.children) {
          // Si es un grupo, filtra sus hijos recursivamente
          return {
            ...item,
            children: this.filterMenuByPermission(item.children, userPermissions)
          };
        }
        return item;
      });
  }

  // --- Estructura de Datos (Mapeando tu HTML con Arrays) ---
  private getMenuStructure(): MenuItem[] {
    return [
      // 1. GRUPO: Analitica y Reportes (Visible para 1 y 2)
      {
        id: 'reportes-group',
        permisosRequeridos: [1, 2, 3, 4,5],
        titulo: 'Analítica y Reportes',
        subtitulo: '',
        icono: ['fas','pie-chart'],
        isGroup: true,
        children: [
          // Arribos: Solo para Nivel 2 (Ejemplo)
          { id: 'arribos', permisosRequeridos: [1, 2, 3, 4,5], titulo: 'Arribos', ruta: '/reporte-arribo', icono: null, isGroup: false },
          // Pedido Traslado: Para Nivel 1 y 2
          { id: 'pedido', permisosRequeridos: [1, 2, 3,5], titulo: 'Pedido Traslado', ruta: '/reporte-pedido-traslado', icono: null, isGroup: false },
          // Despachos: Solo para Nivel 1 (Admin)
          { id: 'despachos', permisosRequeridos: [1, 2, 3,4,5], titulo: 'Despachos', ruta: '/reporte-despachos', icono: null, isGroup: false },
        ]
      },
      // 2. GRUPO: Gestion de Catalogos (Solo para Nivel 1)
      {
        id: 'catalogos-group',
        permisosRequeridos: [1,5], 
        titulo: 'Gestión de Catálogos',
        subtitulo: '',
        icono:  ['fas', 'book'],
        isGroup: true,
        children: [
          { id: 'emp-int', permisosRequeridos: [1,5], titulo: 'Empleado Interno', ruta: '/registro-empleado-interno', icono:null, isGroup: false },
          { id: 'emp-ext', permisosRequeridos: [1,5], titulo: 'Empleado Externo', ruta: '/registro-empleado-externo', icono:null, isGroup: false },
          { id: 'material', permisosRequeridos: [1,5], titulo: 'Material', ruta: '/registro-material', icono: null, isGroup: false },
          { id: 'bodega', permisosRequeridos: [1,5], titulo: 'Bodega', ruta: '/registro-bodega', icono:null, isGroup: false },
          { id: 'planta', permisosRequeridos: [1,5], titulo: 'Planta', ruta: '/registro-planta', icono: null, isGroup: false },
          { id: 'silo', permisosRequeridos: [1,5], titulo: 'Silo', ruta: '/registro-silo', icono: null, isGroup: false }
        ]

      },
      // 3. ENLACE DIRECTO: Programacion de Arribos
      {
        id: 'program-arribo',
        permisosRequeridos: [1, 2, 3,5], 
        titulo: '',
        subtitulo: 'Programación de Arribos',
        icono:  ['fas', 'gamepad'],
        ruta: '/program-arribo',
        isGroup: false
      },
      {
        id: 'conf-despachos',
        permisosRequeridos: [1, 2, 4,5],
        titulo: '',
        subtitulo: 'Confirmación de Despachos',
        icono:  ['fas', 'check-circle'],
        ruta: '/confirmacion-despacho',
        isGroup: false
      },
      {
        id: 'report-pedido-compra',
        permisosRequeridos: [1, 2, 3,5],
        titulo: '',
        subtitulo: 'Reporte de Pedido Compra',
        icono:  ['fas', 'bar-chart'],
        ruta: '/reporte-pedido-compra',
        isGroup: false
      }
      // ...
    ];
  }

  public findCoincidencePuestoId(inputName: string): number {
    if (!inputName) {
      return 0;
    }
    // 1. Normaliza el nombre de entrada a mayúsculas y elimina espacios innecesarios.
    const normalizedInput = inputName.trim().toUpperCase();
    // 2. Itera sobre el mapa de coincidencias
    const matchedItem = this.coincidences.find(item => {
      // 3. Usa .some() para verificar si alguna de las 'keywords' del grupo
      //    está CONTENIDA en el nombre normalizado o viceversa.
      return item.keywords.some(keyword => {
        const normalizedKeyword = keyword.trim().toUpperCase();
        // Opción optimizada: Verifica si la palabra clave (ej. 'ADMIN')
        // está contenida en el nombre de entrada (ej. 'SUPER ADMINISTRADOR')
        // O si el nombre de entrada (ej. 'EXT') está contenido en la palabra clave ('EXTERNO').
        return normalizedInput.includes(normalizedKeyword) || normalizedKeyword.includes(normalizedInput);
      });
    });
    // 4. Devuelve el ID si se encontró una coincidencia, o 0 si no se encontró.
    this.cdr.detectChanges();
    return matchedItem ? matchedItem.id : 0;
  }

  private readonly coincidences: CoincidencePuestosMap[] = [
    {
      id: 1,
      name: 'ADMINISTRADOR',
      keywords: ['ADMIN', 'ADM', 'ADMINISTRADOR', 'DIRECTOR']
    },
    {
      id: 2,
      name: 'LOGISTICA',
      keywords: ['LOGISTICA', 'LOGISTIC', 'LOGIS']
    },
    {
      id: 3,
      name: 'USUARIO INTERNO',
      keywords: ['USUARIO INTERNO', 'INTERNO', 'INTER']
    },
    {
      id: 4,
      name: 'USUARIO EXTERNO',
      keywords: ['USUARIO EXTERNO', 'EXT', 'OUTSIDE', 'CONTRATISTA', "EXTERNO"]
    },
        {
      id: 5,
      name: 'TI',
      keywords: ['TI','DEDPARTAMENTO TI','MANTENIMIENTO']
    },
  ];

}
