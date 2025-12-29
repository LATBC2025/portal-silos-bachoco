import { ScrollingModule } from '@angular/cdk/scrolling';
import { CommonModule, DecimalPipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { RouterModule } from '@angular/router';
import { NgbPaginationModule, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { BodegaServiceService } from '../../../services/catalog/bodega-service.service';
import { BodegaResponse } from '../../../models/catalogs/bodega/Bodega.Response';
import { BodegaRequest } from '../../../models/catalogs/bodega/Bodega.Request';
import { SiloServiceService } from '../../../services/catalog/silo-service.service';
import { SiloResponse } from '../../../models/catalogs/silo/Silo.Response';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import Swal, { SweetAlertResult } from 'sweetalert2';
import { HeaderComponent } from '../../../shared/header/header.component';
import { UtilsService } from '../../../services/shared/utils.service';
import { notZeroStringValidator } from '../../../services/shared/validators/not-zero-string.validator';


@Component({
  selector: 'app-registro-bodega',
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
    CommonModule,
    HeaderComponent
  ],
  templateUrl: './registro-bodega.component.html',
  styleUrl: './registro-bodega.component.css'
})
export class RegistroBodegaComponent implements OnInit {

  hide = true;
  page = 1;
  pageSize = 10;
  collectionSize = 0;
  listaBodega: BodegaResponse[];
  ListaFilterBodega: BodegaResponse[];
  listSilo: SiloResponse[] = [];
  showBtnUpdate: boolean = true;
  updateId: number = -1;
  //
  bodegaForm!: FormGroup;


  constructor(
    private library: FaIconLibrary,
    private bodegaService: BodegaServiceService,
    private siloService: SiloServiceService,
    private utilServ: UtilsService,
    private cdr: ChangeDetectorRef
  ) {
    this.library.addIconPacks(fas);
    this.listaBodega = [];
    this.ListaFilterBodega = [];
  }
  ngOnInit(): void {
    this.initForm();
    this.getSilos();
    this.findAll();
  }

  initForm() {
    this.bodegaForm = new FormGroup({
      bodega: new FormControl('', [Validators.required,Validators.min(1),Validators.pattern(/^[a-zA-Z0-9\s-]*$/)]),
      siloId: new FormControl('0', [Validators.required,notZeroStringValidator()]),
    });
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
        this.utilServ.showMessageError("Hubo un error en la descarga de datos para listar bodegas");
        console.log("ERROR DATA SILO: " + JSON.stringify(error));
      }
    })
  }

  findAll() {
    this.bodegaService.findAll().subscribe({
      next: (response: BodegaResponse[]) => {
        this.listaBodega = response;
        this.ListaFilterBodega = response;
        this.collectionSize = this.listaBodega.length;
        this.refreshCountries();
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.log("ERROR DATA FINDALL BODEGA", JSON.stringify(error));
      }
    });
  }

  getControl(key: string): FormControl {
    return (this.bodegaForm.get(key) as FormControl);
  }

  getValue(key: string): string {
    return (this.bodegaForm.get(key) as FormControl).value;
  }
  getValueNumber(key: string): number {
    return (this.bodegaForm.get(key) as FormControl).value;
  }
  toRequest(): BodegaRequest {
    return new BodegaRequest(this.getValue('bodega'), this.getValueNumber('siloId'));
  }

  submmit() {
    this.utilServ.markAllControlsAsTouched(this.bodegaForm);
    if (this.bodegaForm.valid) {
      const request: BodegaRequest = this.toRequest();
      this.bodegaService.save(request).subscribe({
        next: (response: BodegaResponse) => {
          this.findAll();
          this.showBtnUpdate = true;
          this.collectionSize = this.listaBodega.length;
          this.cdr.detectChanges();
          this.bodegaForm.reset();
          this.formReset();
          this.utilServ.showSuccessRegistro();
        }, error: (error) => {
          this.utilServ.showErrorRegistro();
        }
      });
    }
  }

  submmitUpdate() {
    if (this.bodegaForm.valid && this.updateId != -1) {
      const request: BodegaRequest = this.toRequest();
      this.bodegaService.update(request, this.updateId).subscribe({
        next: (response: BodegaResponse) => {
          this.findAll();
          this.showBtnUpdate = true;
          this.refreshCountries();
          this.cdr.detectChanges();
          this.formReset();
          this.utilServ.showSuccessActualizacion();
        }, error: (error) => {
          this.utilServ.showErrorActualizacion();
        }
      });
    }else{
      this.utilServ.markAllControlsAsTouched(this.bodegaForm);
    }
  }

  update(item: BodegaResponse) {
    this.showBtnUpdate = false;
    this.updateId = item.id;
    this.bodegaForm.patchValue({
      bodega: item.nombre,
      siloId: item.siloId
    });
  }

  async deleteSilo(id: number, index: number) {
    const result = await this.utilServ.confirmarEliminar();
    if (result.isConfirmed) {
      this.bodegaService.delete(id).subscribe({
        next: (response) => {
          this.findAll();
          this.refreshCountries();
          this.cdr.detectChanges();
          this.formReset();
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

  refreshCountries() {
    this.ListaFilterBodega = this.listaBodega.map((bodega, i) => ({ bodegaId: i + 1, ...bodega })).slice(
      (this.page - 1) * this.pageSize,
      (this.page - 1) * this.pageSize + this.pageSize,
    );
  }

  formReset() {
    this.bodegaForm.reset({
      bodega:'',
      siloId: '0',
    });
    this.utilServ.resetFormGroupState(this.bodegaForm);
  }
}
