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
import { HeaderComponent } from '../../../shared/header/header.component';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { PlantaRequest } from '../../../models/catalogs/planta/Planta.Request';
import { PlantaServiceService } from '../../../services/catalog/planta-service.service';
import { PlantaResponse } from '../../../models/catalogs/planta/Planta.Response';
import Swal, { SweetAlertResult } from 'sweetalert2';
import { ApiResponse } from '../../../models/ApiResponse';
import { UtilsService } from '../../../services/shared/utils.service';
import { notZeroStringValidator } from '../../../services/shared/validators/not-zero-string.validator';

@Component({
  selector: 'app-planta-registro',
  standalone: true,
  imports: [
    MatCardModule,
    MatIconModule,
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    DecimalPipe,
    FormsModule,
    NgbTypeaheadModule,
    NgbPaginationModule,
    HeaderComponent,
    FontAwesomeModule,
    CommonModule
  ],
  templateUrl: './planta-registro.component.html',
  styleUrl: './planta-registro.component.css'
})
export class PlantaRegistroComponent implements OnInit {

  hide = true;
  page = 1;
  pageSize = 10;
  collectionSize = 0;
  listaPlanta: PlantaResponse[];
  listaPlantaFilter: PlantaResponse[];
  plantaForm!: FormGroup;
  showBtnUpdate: boolean = true;
  updateId: number = -1;


  constructor(
    private library: FaIconLibrary,
    private cdr: ChangeDetectorRef,
    private plantaService: PlantaServiceService,
    private utilServ: UtilsService
  ) {
    this.library.addIconPacks(fas);
    this.listaPlanta = [];
    this.listaPlantaFilter = [];
  }
  ngOnInit(): void {
    this.initForm();
    this.findAll();
    this.refreshCountries();
  }

  initForm() {
    this.plantaForm = new FormGroup({
      planta: new FormControl('', [Validators.required,Validators.pattern(/^[a-zA-Z0-9\s-]*$/)]),
      nombre: new FormControl('', [Validators.required]),
      sociedad: new FormControl('', [Validators.required, notZeroStringValidator()]),
    });
  }

  getControl(key: string): FormControl {
    return (this.plantaForm.get(key) as FormControl);
  }

  getValue(key: string): string {
    return (this.plantaForm.get(key) as FormControl).value;
  }

  toRequest(): PlantaRequest {
    return new PlantaRequest(this.getValue('planta'), this.getValue('nombre'), this.getValue('sociedad'));
  }

  findAll() {
    this.plantaService.findAll().subscribe({
      next: (response: PlantaResponse[]) => {
        this.listaPlanta = response;
        this.listaPlantaFilter = response;
        this.collectionSize = this.listaPlanta.length;
        this.refreshCountries();
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.utilServ.showMessageError("Hubo un error en la descarga de datos para listar las plantas");
        console.log("DATA LISTA PLANTA ERROR RESPONSE: " + JSON.stringify(error));
      }
    })
  }

  submmit() {
    this.utilServ.markAllControlsAsTouched(this.plantaForm);
    if (this.plantaForm.valid) {
      const request: PlantaRequest = this.toRequest();
      this.plantaService.save(request).subscribe({
        next: (response: ApiResponse<PlantaResponse>) => {
          //this.listaPlanta.push(response);
          if (response.code == "0") {
            this.findAll();
            this.showBtnUpdate = true;
            this.collectionSize = this.listaPlanta.length;
            this.cdr.detectChanges();
            this.plantaForm.reset();
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
    if (this.plantaForm.valid && this.updateId != -1) {
      const request: PlantaRequest = this.toRequest();
      this.plantaService.update(request, this.updateId).subscribe({
        next: (response: PlantaResponse) => {
          this.findAll();
          this.cdr.detectChanges();
          this.utilServ.showSuccessActualizacion();
          this.showBtnUpdate = true;
          this.plantaForm.reset();
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
      this.plantaService.delete(id).subscribe({
        next: (response) => {
          console.log("DATA DELETE RESPONSE: " + JSON.stringify(response));
          this.findAll();
          this.cdr.detectChanges();
          this.plantaForm.reset({
            siloId: 'seleccione'
          });
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

  update(item: PlantaResponse) {
    this.showBtnUpdate = false;
    this.updateId = item.id;
    this.plantaForm.patchValue({
      planta: item.planta,
      nombre: item.nombre,
      sociedad: item.sociedad
    });
  }


  refreshCountries() {
    this.listaPlantaFilter = this.listaPlanta.map((planta, i) => ({ plantaId: i + 1, ...planta })).slice(
      (this.page - 1) * this.pageSize,
      (this.page - 1) * this.pageSize + this.pageSize,
    );
  }

}
