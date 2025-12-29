import { ChangeDetectorRef, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ProfileInfoComponent } from './profile-info/profile-info.component';
import { ConfigBtnComponent } from './config-btn/config-btn.component';
import { MatIconModule } from '@angular/material/icon';
import { SidenavServiceService } from '../../services/sidenav-service.service';
import { AuthServiceService } from '../../services/auth/auth-service.service';
import { SiloServiceService } from '../../services/catalog/silo-service.service';
import { SiloResponse } from '../../models/catalogs/silo/Silo.Response';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [ConfigBtnComponent, ProfileInfoComponent, MatIconModule],
  template: `
   <div class="container-fluid card-header">
          <div class="row align-items-center">
              <div class="col-6">
                  <div class="contenedor_bienvenido">
                    <h2>¡Bienvenido!</h2>
                  </div>
              </div>
              <div class="col-6">
            <app-profile-info class="perfil" [userName]="userName" [userRole]="userRole"></app-profile-info>
     <!--       <button mat-icon-button (click)="toggleMenu.emit()">
          <mat-icon>menu</mat-icon>
      </button> -->
          <div class="container-btn-menu">
              <button mat-icon-button class="btn-visible" (click)="toggleSidenav()">
              <mat-icon >menu</mat-icon>
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: `
    :host {
      box-sizing: border-box;
      display: block;
      background: var(--mdc-filled-text-field-container-color);
    }
    .contenedor_bienvenido h2{
      display: flex;
      align-items: center;
      padding-left: 20px;
      font: normal normal medium 14px/20px Montserrat;
      letter-spacing: 0.25px;
    }
   .card-header{
       width:100%;
       border: 1px solid #e0e0e0;
       border-radius: 4px;
       box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
       background: white;
    }
    .btn-visible{
      display:none;
      border:none;
      background-color: #FFFF;
    }
    .container-btn-menu{
      display:flex;
      justify-content:end;
      align-items:center;
      padding-right:10px;
    }
     @media (max-width: 768px) {
        .contenedor_bienvenido{
          height: 100%;
          margin-top:10px;
        }
        .contenedor_bienvenido h2{
          font-size:14px;
        }
        .btn-visible{
          display:block;
        }
        .perfil{
          display:none;
        }
         .card-header{
          width:100%;
        }
          
    }
  `
})
export class HeaderComponent implements OnInit {

  userName!: string;
  userRole: string;
  listaSilo!: SiloResponse[];
  @Output() toggleMenu = new EventEmitter<void>();

  constructor(
    private sidenavService: SidenavServiceService,
    private siloService: SiloServiceService,
    private authService: AuthServiceService,
    private cdr: ChangeDetectorRef
  ) {
    this.userName = '';
    this.userRole = '';
  }

  ngOnInit(): void {
    this.findAll();
  }
  toggleSidenav(): void {
    this.sidenavService.toggle();
  }

  findClaveUsername(): string {
    const username = this.authService.getUsernameInLocalStorage();
    if (username) {
      return username;
    }
    return "";
  }

  findSilo(): string {
    const silo = this.authService.getSiloIdInLocalStorage();
    let nameSilo = "";
    if (silo) {
      const siloResponse = this.listaSilo.find(s => s.id == Number(silo));
      if (siloResponse) {
        nameSilo = siloResponse.nombre;
      }
    }
    return nameSilo;
  }
  findNombrePuesto(): string {
    return this.authService.getNombrePuestoInLocalStorage()??"";
  }

  findAll() {
    this.siloService.getSilos().subscribe({
      next: (response: SiloResponse[]) => {
        this.listaSilo = response;
        this.extractDataEmpleadoAndSilo();
      },
      error: (error) => {
        console.log("DATA LISTA SILO ERROR RESPONSE: " + JSON.stringify(error));
      }
    })
  }

  extractDataEmpleadoAndSilo() {
    const clave = localStorage.getItem('CLAVE_USER');
    if (clave) {
      this.userName = clave;
    }
    this.userName = this.findClaveUsername();
    this.userRole = this.findNombrePuesto();
    this.cdr.detectChanges();
  }

}
