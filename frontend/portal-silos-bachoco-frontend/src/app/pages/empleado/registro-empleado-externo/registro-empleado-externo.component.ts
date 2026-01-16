import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { RouterModule } from '@angular/router';
import { NgbPaginationModule, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderComponent } from '../../../shared/header/header.component';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { EmpleadoExternoResponse } from '../../../models/catalogs/Empleado-externo/Empleado.Externo.Response';
import { EmpleadoExternoService } from '../../../services/emppleado-externo/empleado-externo.service';
import { SiloResponse } from '../../../models/catalogs/silo/Silo.Response';
import { SiloServiceService } from '../../../services/catalog/silo-service.service';
import { EmpleadoExternoRequest } from '../../../models/catalogs/Empleado-externo/Empleado.Externo.Request';
import { EmpleadoExternoResponseDTO } from '../../../models/catalogs/Empleado-externo/Empleado.Response.DTO';
import Swal, { SweetAlertResult } from 'sweetalert2';
import { customEmailValidator } from '../../../services/shared/validators/email.validator';
import { notZeroStringValidator } from '../../../services/shared/validators/not-zero-string.validator';
import { UtilsService } from '../../../services/shared/utils.service';
import { BodegaServiceService } from '../../../services/catalog/bodega-service.service';
import { BodegaResponse } from '../../../models/catalogs/bodega/Bodega.Response';


@Component({
  selector: 'app-registro-empleado-externo',
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
  templateUrl: './registro-empleado-externo.component.html',
  styleUrl: './registro-empleado-externo.component.css'
})
export class RegistroEmpleadoExternoComponent implements OnInit {
  hide = true;
  page = 1;
  pageSize = 10;
  collectionSize = 0;
  //listas de empleado
  listaEmpleadoExterno: EmpleadoExternoResponseDTO[] = [];
  listaEmpleadoExternoFilters: EmpleadoExternoResponseDTO[] = [];
  formEmpleadoExterno!: FormGroup;
  listSilo: SiloResponse[] = [];
  listBodegas: BodegaResponse[] = []; // nuevo atributo
  showBtnUpdate: boolean = true;
  updateId: number = -1;

  constructor(
    private empleadoExternoServ: EmpleadoExternoService,
    private library: FaIconLibrary,
    private siloService: SiloServiceService,
    private utilServ: UtilsService,
    private cdr: ChangeDetectorRef,
  private bodegaService: BodegaServiceService
) {
    this.library.addIconPacks(fas);
  }
  ngOnInit(): void {
    this.getSilos();
    this.initForm();
    this.findAllEmpExterno();
    //Inicia lógica para cargar informacion de bodegas por silo
    this.formEmpleadoExterno.get('siloId')?.valueChanges.subscribe((val) => {
      const siloId = Number(val);

      if (!siloId || siloId === 0) {
        this.listBodegas = [];
        this.formEmpleadoExterno.patchValue({ bodegasIds: [] });
        return;
      }

      this.loadBodegasBySilo(siloId);
});

  }

  initForm() {
    this.formEmpleadoExterno = new FormGroup({
      nombre: new FormControl('', [Validators.required,Validators.pattern(/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]*$/)]),
      rfc: new FormControl('', [Validators.required,Validators.pattern(/^[A-ZÑ&]{3,4}\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])[A-Z\d]{2}[A-Z0-9]$/)]),
      correo: new FormControl('', [Validators.required, Validators.min(1), customEmailValidator()]),
      siloId: new FormControl('0', [Validators.required, notZeroStringValidator()]),
      //  NUEVOS CAMPOS
    sapVendor: new FormControl('', [
      Validators.required,
      Validators.maxLength(30),
      Validators.pattern(/^[A-Za-z0-9-]+$/)]),
      bodegasIds: new FormControl([], [Validators.required]),

    });
  }


  getControl(key: string): FormControl {
    return (this.formEmpleadoExterno.get(key) as FormControl);
  }
  getValue(key: string): string {
    return (this.formEmpleadoExterno.get(key) as FormControl).value;
  }
  getValueNumber(key: string): number {
    return (this.formEmpleadoExterno.get(key) as FormControl).value;
  }
  refreshCountries() {
    this.listaEmpleadoExternoFilters = this.listaEmpleadoExterno.map((empleado, i) => ({ empId: i + 1, ...empleado })).slice(
      (this.page - 1) * this.pageSize,
      (this.page - 1) * this.pageSize + this.pageSize,
    );
  }
  getSilos() {
    this.siloService.getSilos().subscribe({
      next: (response: SiloResponse[]) => {
        if (response != null && response != undefined) {
          this.listSilo = response;
          this.cdr.detectChanges();
        }
      },
      error: (error) => {
        this.utilServ.showMessageError("Hubo un error en la carga de silos");
        console.log("ERROR DATA SILO: " + JSON.stringify(error));
      }
    })
  }

  toRequest(): EmpleadoExternoRequest {
      const bodegasIds = (this.formEmpleadoExterno.get('bodegasIds')?.value ?? []) as number[];


    return new EmpleadoExternoRequest(this.getValue('nombre'), this.getValue('rfc'), this.getValue('correo'),
      '', this.getValueNumber('siloId'),
       this.getValue('sapVendor'),//  nuevo
       bodegasIds //  nuevo
  )


  }

  extractNameSilo(siloId: number): string {
    return this.listSilo.find(i => i.id === siloId)?.nombre ?? "sin nombre";
  }

  submmit() {
    if (this.formEmpleadoExterno.valid) {
      const request: EmpleadoExternoRequest = this.toRequest();
      this.empleadoExternoServ.save(request).subscribe({
        next: (response: any) => {
          this.findAllEmpExterno();
          this.utilServ.showSuccessRegistro();
          this.cdr.detectChanges();
          this.resetForm();
        }, error: (error) => {
          const resultError = error['error'];
          if (resultError['error'] != undefined && resultError['error'] == 'error-code:1001') {
            this.utilServ.showMessageError('El correo ya existe');
          }else{
            this.utilServ.showErrorRegistro();
          }
        }
      });
    }else{
      this.utilServ.markAllControlsAsTouched(this.formEmpleadoExterno);
    }
  }
  resetForm() {
    this.formEmpleadoExterno.patchValue({
      nombre: '',
      rfc: '',
      correo: '',
      siloId: '0',
      sapVendor: '', // nuevo
      bodegasIds: [] // nuevo
    });
    this.utilServ.resetFormGroupState(this.formEmpleadoExterno);
  }
  findAllEmpExterno() {
    this.empleadoExternoServ.findAll().subscribe({
      next: (response: EmpleadoExternoResponseDTO[]) => {
        this.listaEmpleadoExterno = response;
        this.collectionSize = this.listaEmpleadoExterno.length;
        this.refreshCountries();
        this.cdr.detectChanges();
      }, error: (error) => {
        this.utilServ.showMessageError("Hubo un error en la carga de datos de los empleados externos");
        console.log("DATA ERROR RESPONSE EMP EXTERNO: " + JSON.stringify(error));
      }
    });
  }

  async deleteSilo(id: number, index: number) {
    const result = await this.utilServ.confirmarEliminar();
    if (result.isConfirmed) {
      this.empleadoExternoServ.delete(id).subscribe({
        next: (response) => {
          this.cdr.detectChanges();
          this.formEmpleadoExterno.reset();
          this.findAllEmpExterno();
          this.refreshCountries();
          this.utilServ.showSuccesEliminacion();
        },
        error: (error) => {
          this.utilServ.showErrorEliminacion();
          console.log("DATA DELETE ERROR RESPONSE: " + JSON.stringify(error));
        }
      });
    }
  }

  submmitUpdate() {
    if (this.formEmpleadoExterno.valid && this.updateId != -1) {
      const request: EmpleadoExternoRequest = this.toRequest();
      this.empleadoExternoServ.update(request, this.updateId).subscribe({
        next: (response) => {
          this.showBtnUpdate = true;
          this.resetForm();
          this.findAllEmpExterno();
          this.cdr.detectChanges();
          this.utilServ.showSuccessActualizacion();
        }, error: (error) => {
          this.utilServ.showErrorActualizacion();
          console.log("DATA SAVE ERROR RESPONSE: " + JSON.stringify(error));
        }
      });
    }else{

      this.utilServ.markAllControlsAsTouched(this.formEmpleadoExterno);
    }
  }
  update(item: EmpleadoExternoResponseDTO) {
    this.showBtnUpdate = false;
    this.updateId = item.id;
    this.formEmpleadoExterno.patchValue({
      nombre: item.nombre,
      rfc: item.rfc,
      correo: item.correo,
      siloId: String(item.siloId),      //  CLAVE
      sapVendor: (item as any).sapVendor ?? '', // temporal
      bodegasIds: (item as any).bodegasIds ?? []    });
  }

  loadBodegasBySilo(siloId: number) {
  this.bodegaService.findAllBySilo(siloId).subscribe({
    next: (response: BodegaResponse[]) => {
      this.listBodegas = response ?? [];
      // resetea selección al cambiar silo
      this.formEmpleadoExterno.patchValue({ bodegasIds: [] });
      this.cdr.detectChanges();
    },
    error: (error) => {
      this.utilServ.showMessageError("Hubo un error en la carga de bodegas");
      console.log("ERROR DATA BODEGAS: " + JSON.stringify(error));
      this.listBodegas = [];
      this.formEmpleadoExterno.patchValue({ bodegasIds: [] });
    }
  });
}

}
