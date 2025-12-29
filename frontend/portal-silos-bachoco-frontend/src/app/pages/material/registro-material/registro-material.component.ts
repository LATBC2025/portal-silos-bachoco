import { ScrollingModule } from '@angular/cdk/scrolling';
import { DecimalPipe } from '@angular/common';
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
import { MaterialResponse } from '../../../models/catalogs/Material/Material.Response';
import { MaterialServiceService } from '../../../services/catalog/material-service.service';
import { SiloServiceService } from '../../../services/catalog/silo-service.service';
import { SiloResponse } from '../../../models/catalogs/silo/Silo.Response';
import { MaterialRequest } from '../../../models/catalogs/Material/Material.Request';
import { HeaderComponent } from '../../../shared/header/header.component';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import Swal, { SweetAlertResult } from 'sweetalert2';
import { UtilsService } from '../../../services/shared/utils.service';
import { notZeroStringValidator } from '../../../services/shared/validators/not-zero-string.validator';

@Component({
  selector: 'app-registro-material',
  standalone: true,
  imports: [
    MatCardModule,
    MatIconModule,
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    ReactiveFormsModule,
    DecimalPipe,
    FormsModule,
    NgbTypeaheadModule,
    NgbPaginationModule,
    FontAwesomeModule,
    HeaderComponent
  ],
  templateUrl: './registro-material.component.html',
  styleUrl: './registro-material.component.css'
})
export class RegistroMaterialComponent implements OnInit {
  hide = true;
  page = 1;
  pageSize = 10;
  listaMateriales: MaterialResponse[] = [];
  listaMaterialFilter: MaterialResponse[] = [];
  collectionSize = 0;
  formMaterial!: FormGroup;
  //catalogos
  listaSilo: SiloResponse[] = [];
  showBtnUpdate: boolean = true;
  updateId: number = -1;

  constructor(private materialService: MaterialServiceService,
    private siloService: SiloServiceService,
    private utilServ: UtilsService,
    private library: FaIconLibrary,
    private cdr: ChangeDetectorRef) {
    this.library.addIconPacks(fas);
  }

  ngOnInit(): void {
    this.initForm();
    this.findAllSilo();
    this.findAll();
  }

  initForm() {
    this.formMaterial = new FormGroup({
      numero: new FormControl('', [Validators.required, Validators.min(1), Validators.pattern(/^[a-zA-Z0-9\s-]*$/)]),
      descripcion: new FormControl('', [Validators.required, Validators.min(1)]),
    });
  }
  refreshCountries() {
    this.listaMaterialFilter = this.listaMateriales.map((arribo, i) => ({ materialId: i + 1, ...arribo })).slice(
      (this.page - 1) * this.pageSize,
      (this.page - 1) * this.pageSize + this.pageSize,
    );
  }
  findAllSilo() {
    this.siloService.getSilos().subscribe({
      next: (response: SiloResponse[]) => {
        this.listaSilo = response;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.log("DATA LISTA SILO ERROR RESPONSE: " + JSON.stringify(error));
      }
    })
  }
  findAll() {
    this.materialService.findAll().subscribe({
      next: (response: MaterialResponse[]) => {
        this.listaMateriales = response;
        this.listaMaterialFilter = response;
        this.collectionSize = this.listaMateriales.length;
        this.refreshCountries();
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.utilServ.showMessageError("Hubo un error en la descarga de datos para listar los materiales");
        console.log("DATA LISTA MATERIAL ERROR RESPONSE: " + JSON.stringify(error));
      }
    })
  }
  getControl(key: string): FormControl {
    return (this.formMaterial.get(key) as FormControl);
  }
  getValue(key: string): string {
    return (this.formMaterial.get(key) as FormControl).value;
  }
  getValueNumber(key: string): number {
    return (this.formMaterial.get(key) as FormControl).value;
  }
  toRequest(): MaterialRequest {
    return new MaterialRequest(this.getValue('numero'), this.getValue('descripcion'), -1);
  }
  submmit() {
    this.utilServ.markAllControlsAsTouched(this.formMaterial);
    if (this.formMaterial.valid) {
      const material: MaterialRequest = this.toRequest();
      this.materialService.save(material).subscribe({
        next: (response) => {
          this.findAll();
          this.refreshCountries();
          this.cdr.detectChanges();
          this.utilServ.showSuccessRegistro();
        }, error: (error) => {
          this.utilServ.showErrorRegistro();
          console.log(" DATA ERROR REGISTRO MATERIAL: " + JSON.stringify(error));
        }
      });
    }
  }
  update(item: MaterialResponse) {
    this.showBtnUpdate = false;
    this.updateId = item.id;
    this.formMaterial.patchValue({
      numero: item.nombre,
      descripcion: item.descripcion,
      siloId: item.siloId
    });
  }
  resetFormUpdate() {
    this.formMaterial.patchValue({
      numero: '',
      descripcion: '',
      siloId: '0'
    });
    this.utilServ.resetFormGroupState(this.formMaterial);
  }
  submmitUpdate() {
    this.utilServ.markAllControlsAsTouched(this.formMaterial);
    if (this.formMaterial.valid && this.updateId != -1) {
      const request: MaterialRequest = this.toRequest();
      this.materialService.update(request, this.updateId).subscribe({
        next: (response) => {
          this.showBtnUpdate = true;
          this.findAll();
          this.resetFormUpdate();
          this.cdr.detectChanges();
          this.utilServ.showSuccessActualizacion();
        }, error: (error) => {
          this.utilServ.showErrorActualizacion();
          console.log("DATA SAVE ERROR RESPONSE: " + JSON.stringify(error));
        }
      });
    }
  }
  async deleteSilo(id: number, index: number) {
    const result = await this.utilServ.confirmarEliminar();
    if (result.isConfirmed) {
      this.materialService.delete(id).subscribe({
        next: (response) => {
          this.findAll();
          this.resetFormUpdate();
          this.cdr.detectChanges();
          this.utilServ.showSuccesEliminacion();
        },
        error: (error) => {
          const resultError = error['error'];
          if (resultError['error'] != undefined && resultError['error'] == 'error-code:violation-integration') {
            this.utilServ.showMessageErrorDataIntagrationViolation();
          } else {
            this.utilServ.showErrorEliminacion();
          }
          console.log("DATA DELETE ERROR RESPONSE: " + JSON.stringify(error));
        }
      });
    }
  }
}
