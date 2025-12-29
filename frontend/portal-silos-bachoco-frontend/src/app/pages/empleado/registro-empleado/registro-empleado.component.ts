import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { RouterModule } from '@angular/router';
import { NgbPaginationModule, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { RegistroEmpleadoModalComponent } from '../registro-empleado-modal/registro-empleado-modal.component';
import Swal, { SweetAlertResult } from 'sweetalert2'
import { HeaderComponent } from '../../../shared/header/header.component';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { EmpleadoInternoRequest } from '../../../models/catalogs/Empleado-Interno/Empleado.Request';
import { EmpleadoInternoService } from '../../../services/emppleado-interno/empleado-interno.service';
import { EmpleadoInternoResponse } from '../../../models/catalogs/Empleado-Interno/Empleado-Interno.response';
import { CatalogService } from '../../../services/catalog/catalog.service';
import { DepartamentoResponse } from '../../../models/catalogs/departamento/Catalog.Response';
import { PuestoReponse } from '../../../models/catalogs/Puesto/Puesto.Response';
import { EmpleadoInternoDtoResponse } from '../../../models/catalogs/Empleado-Interno/Empleado.Interno.Response.dto';
import { UtilsService } from '../../../services/shared/utils.service';
import { customEmailValidator } from '../../../services/shared/validators/email.validator';
import { notZeroStringValidator } from '../../../services/shared/validators/not-zero-string.validator';
import { HttpErrorResponse } from '@angular/common/module.d-CnjH8Dlt';
import { BackendError } from '../../../models/error/Backend.Errror.response';
import { AuthServiceService } from '../../../services/auth/auth-service.service';

@Component({
  selector: 'app-registro-empleado',
  standalone: true,
  imports: [
    MatCardModule,
    MatIconModule,
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatSelectModule,
    ReactiveFormsModule,
    FormsModule,
    NgbTypeaheadModule,
    NgbPaginationModule,
    HeaderComponent,
    FontAwesomeModule
  ],
  templateUrl: './registro-empleado.component.html',
  styleUrl: './registro-empleado.component.css'
})
export class RegistroEmpleadoComponent implements OnInit {

  hide = true;
  page = 1;
  pageSize = 10;
  collectionSize = 0;
  listaEmpleado: EmpleadoInternoDtoResponse[] = [];
  listaEmpleadoFilters: EmpleadoInternoDtoResponse[] = [];
  listaDepto: DepartamentoResponse[] = [];
  listaPuesto: PuestoReponse[] = [];
  formEmpleadoInterno!: FormGroup;
  showBtnUpdate: boolean = true;
  updateId: number = -1;
  palabraClaveEmpExterno = 'usuario externo';


  constructor(
    private empleadoInternoServ: EmpleadoInternoService,
    private catalogService: CatalogService,
    private authService: AuthServiceService,
    private utilServ: UtilsService,
    public dialog: MatDialog,
    private cdr: ChangeDetectorRef,
    private library: FaIconLibrary) {
    this.library.addIconPacks(fas);
  }
  ngOnInit(): void {
    this.initForm();
    this.findAllEmpleadoInrternos();
    this.findAllDepattamento();
    this.findAllPuesto();
    this.refreshCountries();
  }

  openDialog(enterAnimationDuration: string, exitAnimationDuration: string): void {
    this.dialog.open(RegistroEmpleadoModalComponent, {
      enterAnimationDuration,
      exitAnimationDuration,
    });
  }

  initForm() {
    this.formEmpleadoInterno = new FormGroup({
      nombre: new FormControl('', [Validators.required,Validators.pattern(/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]*$/)]),
      email: new FormControl('', [Validators.required, customEmailValidator()]),
      usuario: new FormControl('', [Validators.required]),
      rol: new FormControl('0', [Validators.required, notZeroStringValidator()]),
      area: new FormControl('0', [Validators.required, notZeroStringValidator()])
    });
  }

  getControl(key: string): FormControl {
    return (this.formEmpleadoInterno.get(key) as FormControl);
  }

  getValue(key: string): string {
    return (this.formEmpleadoInterno.get(key) as FormControl).value;
  }

  getValueNumber(key: string): number {
    return (this.formEmpleadoInterno.get(key) as FormControl).value;
  }

  findNombreArea(id: number): string | undefined {
    return this.listaDepto.find(a => a.id === id)?.nombre;
  }

  findNombrePuesto(id: number): string | undefined {
    return this.listaPuesto.find(a => a.id === id)?.descripcion;
  }

  refreshCountries() {
    this.listaEmpleadoFilters = this.listaEmpleado.map((empleado, i) => ({ empleadoId: i + 1, ...empleado })).slice(
      (this.page - 1) * this.pageSize,
      (this.page - 1) * this.pageSize + this.pageSize,
    );
  }


  toRequest(): EmpleadoInternoRequest {
    return new EmpleadoInternoRequest(this.getValue('nombre'), this.getValue('email'), this.getValue('usuario'),
      2, this.getValueNumber('area'), this.getValueNumber('rol'));
  }

  findAllEmpleadoInrternos() {
    this.empleadoInternoServ.findAllEmpleadoInterno().subscribe({
      next: (response: EmpleadoInternoDtoResponse[]) => {
        this.listaEmpleado = response;
        this.listaEmpleadoFilters = response;
        this.collectionSize = this.listaEmpleadoFilters.length;
        this.refreshCountries();
        this.cdr.detectChanges();
      }, error: (error) => {
        this.utilServ.showMessageError("Hubo un error en la carga de datos de los empleados");
        console.log("DATA ERROR ESPONSE EMPLEADO INTERNO: " + JSON.stringify(error));
      }
    })
  }
  findAllDepattamento() {
    this.catalogService.findAllDepartamento().subscribe({
      next: (response: DepartamentoResponse[]) => {
        if (response != null && response != undefined && response.length > 0) {
          this.listaDepto = response;
          this.cdr.detectChanges();
        }
      }, error: (error) => {
        this.utilServ.showMessageError("Hubo un error en a carga de datos del departamento");
      }
    });
  }
  findAllPuesto() {
    this.catalogService.findAllPuesto().subscribe({
      next: (response: PuestoReponse[]) => {
        if (response != null && response != undefined && response.length > 0) {
          this.listaPuesto = response.filter(p =>
            !p.descripcion.toLowerCase().includes(this.palabraClaveEmpExterno)
          );
          this.cdr.detectChanges();
        }
      }, error: (error) => {
        this.utilServ.showMessageError("Hubo un error en la carga de datos de los puestos");
      }
    });
  }

  submmit() {
    if (this.formEmpleadoInterno.valid) {
      const request: EmpleadoInternoRequest = this.toRequest();
      this.empleadoInternoServ.save(request).subscribe({
        next: (response: any) => {
          this.findAllEmpleadoInrternos();
          this.formReset();
          this.refreshCountries();
          this.cdr.detectChanges();
          this.utilServ.showSuccessRegistro();
        }, error: (err: HttpErrorResponse) => {
          // 1. Verificar si hay un objeto de error en el cuerpo de la respuesta
          if (err.error && typeof err.error === 'object') {
            // 2. Castear el objeto de error para acceder a sus propiedades
            const backendError: BackendError = err.error as BackendError;
            // 3. Acceder al código y al mensaje del backend
            const errorCode = backendError.error;      // "error-code:1001"
            const errorMessage = backendError.message; // "El correo ya existe"
            // 4. Lógica de manejo de errores específica por código
            if (errorCode === "error-code:1001") {
              // Muestra el mensaje específico al usuario
              this.utilServ.showMessageError("El correo ya existe");
            } else {
              // Manejar otros errores (ej. error-code:2002)
              this.utilServ.showErrorRegistro();
            }
          }
        }
      });
    } else {
      this.utilServ.markAllControlsAsTouched(this.formEmpleadoInterno);
    }
  }

  submmitUpdate() {
    if (this.formEmpleadoInterno.valid && this.updateId != -1) {
      const request: EmpleadoInternoRequest = this.toRequest();
      this.empleadoInternoServ.update(request, this.updateId).subscribe({
        next: (response) => {
          this.showBtnUpdate = true;
          this.formReset();
          this.findAllEmpleadoInrternos();
          this.refreshCountries();
          this.cdr.detectChanges();
          this.utilServ.showSuccessActualizacion();
        }, error: (error) => {
          this.utilServ.showErrorActualizacion();
          console.log("DATA SAVE ERROR RESPONSE: " + JSON.stringify(error));
        }
      });
    } else {
      this.utilServ.markAllControlsAsTouched(this.formEmpleadoInterno);
    }
  }


  isEmpleadoEstaMismoSistema(id:number){
    const empleado= this.listaEmpleado.find(e=>e.id==id);
    let usuario= this.authService.getUsernameInLocalStorage() ?? '';
    if(empleado && empleado.usuario==usuario){
      return true;
    }
    return false;
  }

  async deleteEmpleado(id: number, index: number) {
    const result = await this.utilServ.confirmarEliminar();
    if (result.isConfirmed) {
      if(this.isEmpleadoEstaMismoSistema(id)){
        this.utilServ.showMessageError("No es posible eliminar su propia cuenta de usuario");
        return;
     }
      this.empleadoInternoServ.delete(id).subscribe({
        next: (response) => {
          this.formReset();
          this.findAllEmpleadoInrternos();
          this.refreshCountries();
          this.cdr.detectChanges();
          this.utilServ.showSuccesEliminacion();
        },
        error: (error) => {
          this.utilServ.showErrorEliminacion();
          console.log("DATA DELETE ERROR RESPONSE: " + JSON.stringify(error));
        }
      });
    }
  }

  resetForm() {
    this.formEmpleadoInterno.patchValue({
      nombre: '',
      email: '',
      usuario: '',
      rol: '0',
      area: '0'
    });
    this.utilServ.resetFormGroupState(this.formEmpleadoInterno);
  }
  update(item: EmpleadoInternoDtoResponse) {
    this.showBtnUpdate = false;
    this.updateId = item.id;
    this.formEmpleadoInterno.patchValue({
      nombre: item.nombre,
      email: item.correo,
      usuario: item.usuario,
      rol: item.puestoId,
      area: item.departamentoId
    });
  }

  formReset() {
    this.formEmpleadoInterno.reset({
      nombre: '',
      email: '',
      usuario: '',
      rol: '0',
      area: '0'
    });
    this.utilServ.resetFormGroupState(this.formEmpleadoInterno);
  }
}
