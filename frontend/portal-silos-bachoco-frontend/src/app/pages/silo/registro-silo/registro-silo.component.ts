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
import { SiloServiceService } from '../../../services/catalog/silo-service.service';
import { SiloRequest } from '../../../models/catalogs/silo/Silo.Request';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import Swal, { SweetAlertResult } from 'sweetalert2';
import { SiloResponse } from '../../../models/catalogs/silo/Silo.Response';
import { Observable } from 'rxjs';
import { HeaderComponent } from '../../../shared/header/header.component';
import { ApiResponse } from '../../../models/ApiResponse';
import { UtilsService } from '../../../services/shared/utils.service';
import { OnlyLettersDirective } from '../../../utils/shared/only-letters.directive';
import { OnlyAlphanumericDirective } from '../../../utils/shared/only-alphanumeric.directive';
import { lettersOnlyValidator } from '../../../utils/validations/letter-only.validator';

@Component({
  selector: 'app-registro-silo',
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
    HeaderComponent,
    OnlyLettersDirective,
    OnlyAlphanumericDirective
  ],
  templateUrl: './registro-silo.component.html',
  styleUrl: './registro-silo.component.css'
})
export class RegistroSiloComponent implements OnInit {
  hide = true;
  page = 1;
  pageSize = 10;
  collectionSize = 0;
  listaSilo: SiloResponse[];
  listaSiloFilter: SiloResponse[];
  siloForm!: FormGroup;
  showBtnUpdate: boolean = true;
  updateId: number = -1;


  constructor(private siloService: SiloServiceService,
    private utilServ: UtilsService,
    private library: FaIconLibrary,
    private cdr: ChangeDetectorRef
  ) {
    this.listaSilo = [];
    this.listaSiloFilter = [];
    this.library.addIconPacks(fas);
  }

  ngOnInit(): void {
    this.initForm();
    this.findAll();
  }

  initForm() {
    this.siloForm = new FormGroup({
      silo: new FormControl('', [Validators.required, Validators.pattern(/^[a-zA-Z0-9\s-]*$/)]),
      nombre: new FormControl('', [Validators.required]),
      sociedad: new FormControl('', [Validators.required]),
    });
  }

  getControl(key: string): FormControl {
    return (this.siloForm.get(key) as FormControl);
  }

  getValue(key: string): string {
    return (this.siloForm.get(key) as FormControl).value;
  }

  findAll() {
    this.siloService.getSilos().subscribe({
      next: (response: SiloResponse[]) => {
        if ((response != null && response != undefined) && response.length > 0) {
          this.listaSilo = response;
          this.listaSiloFilter = response;
          this.collectionSize = this.listaSilo.length;
        }
        this.refreshCountries();
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.utilServ.showMessageError("Hubo un error en la descarga de datos para listar los silos");
        console.log("DATA LISTA SILO ERROR RESPONSE: " + JSON.stringify(error));
      }
    })
  }

  refreshCountries() {
    this.listaSiloFilter = this.listaSilo.map((arribo, i) => ({ itemId: i + 1, ...arribo })).slice(
      (this.page - 1) * this.pageSize,
      (this.page - 1) * this.pageSize + this.pageSize,
    );
    this.cdr.detectChanges();
  }

  toRequest(): SiloRequest {
    return new SiloRequest(this.getValue('silo'), this.getValue('nombre'), this.getValue('sociedad'));
  }

  submmit() {
    this.utilServ.markAllControlsAsTouched(this.siloForm);
    if (this.siloForm.valid) {
      const request: SiloRequest = this.toRequest();
      this.siloService.save(request).subscribe({
        next: (response: ApiResponse<SiloResponse>) => {
          if (response.code == "0") {
            this.findAll();
            this.showBtnUpdate = true;
            this.cdr.detectChanges();
            this.siloForm.reset();
            this.utilServ.resetFormGroupState(this.siloForm);
            this.refreshCountries();
            this.cdr.detectChanges();
            this.utilServ.showSuccessRegistro();
          } else {
            this.utilServ.showMessageError(response.message);
          }
        }, error: (error) => {
          this.utilServ.showErrorRegistro();
          console.log("DATA SAVE ERROR RESPONSE: " + JSON.stringify(error));
        }
      });
    }
  }

  submmitUpdate() {
    if (this.siloForm.valid && this.updateId != -1) {
      const request: SiloRequest = this.toRequest();
      this.siloService.update(request, this.updateId).subscribe({
        next: (response: SiloResponse) => {
          this.findAll();
          this.cdr.detectChanges();
          this.utilServ.showSuccessActualizacion();
          this.showBtnUpdate = true;
          this.siloForm.reset();
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
      this.siloService.delete(id).subscribe({
        next: (response) => {
          this.findAll();
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

  update(item: SiloResponse) {
    this.showBtnUpdate = false;
    this.updateId = item.id;
    this.siloForm.patchValue({
      silo: item.silo,
      nombre: item.nombre,
      sociedad: item.sociedad
    });
  }
}
