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
import { Despacho } from '../../../models/Despacho.interface';
import { ReportDespachosServiceService } from '../../../services/reportsAnalitics/report-despachos-service.service';
import { ReporteDespachoResponse } from '../../../models/reporte-despacho/Reporte.Despacho.Response';
import { SiloServiceService } from '../../../services/catalog/silo-service.service';
import { SiloResponse } from '../../../models/catalogs/silo/Silo.Response';
import { PlantaResponse } from '../../../models/catalogs/planta/Planta.Response';
import { PlantaServiceService } from '../../../services/catalog/planta-service.service';
import { UtilsService } from '../../../services/shared/utils.service';
import { notZeroStringValidator } from '../../../services/shared/validators/not-zero-string.validator';
import { dateRangeValidator } from '../../../utils/validations/date-range.validator';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-despachos',
  standalone: true,
  imports: [
    MatCardModule,
    MatIconModule,
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    FormsModule,
    NgbTypeaheadModule,
    NgbPaginationModule,
    HeaderComponent,
    CommonModule
  ],
  templateUrl: './despachos.component.html',
  styleUrl: './despachos.component.css'
})
export class DespachosComponent implements OnInit {
  isExternoEmployee: boolean;
  opcionSeleccionada: number;
  hide = true;
  page = 1;
  pageSize = 10;
  collectionSize = 0;
  listaReporteDespacho: ReporteDespachoResponse[] = [];
  listaReporteDespachoFilter: ReporteDespachoResponse[] = [];
  listSilo: SiloResponse[] = [];
  listPlanta: PlantaResponse[] = [];
  formReporteDespacho!: FormGroup;

  constructor(private reporteDespachoServ: ReportDespachosServiceService,
    private siloService: SiloServiceService,
    private plantaService: PlantaServiceService,
    private utilServ: UtilsService,
    private cdr: ChangeDetectorRef
  ) {
    this.isExternoEmployee = true;
    this.opcionSeleccionada = 1;

  }
  ngOnInit(): void {
    this.initForm();
    this.getSilos();
    this.findAllPlanta();
  }

  initForm() {
    this.formReporteDespacho = new FormGroup({
      bodegaId: new FormControl('0', [Validators.required, notZeroStringValidator()]),
      fechaI: new FormControl('', [Validators.required]),
      fechaF: new FormControl('', [Validators.required])
    }, {
      validators: [
        dateRangeValidator('fechaI', 'fechaF', 'rangoFechaInvalido'),
      ]
    });
  }

  getSilos() {
    this.siloService.getSilos().subscribe({
      next: (response: SiloResponse[]) => {
        if (response != null && response != undefined) {
          this.listSilo = response;
          console.log("DATA RESPUESTA SILOS: "+JSON.stringify(this.listPlanta));
          this.cdr.detectChanges();
        }
      },
      error: (error) => {
        this.utilServ.showMessageError("Hubo un error en la carga de datos de silos");
        console.log("ERROR DATA SILO: " + JSON.stringify(error));
      }
    })
  }
  findAllPlanta() {
    this.plantaService.findAll().subscribe({
      next: (response: PlantaResponse[]) => {
        if (response != undefined && response != undefined && response.length > 0) {
          this.listPlanta = response;
          this.cdr.detectChanges();
        }
      }, error: (error) => {
        this.utilServ.showMessageError("Hubo un error en la carga de datos de plantas");
        console.log("ERROR DATA PLANTA: " + JSON.stringify(error));
      }
    })
  }

  findSiloIdByClave(clave: string) {
    return this.listSilo.find(s => s.nombre == clave)?.id;
  }

  refreshDespachos() {
    this.listaReporteDespachoFilter = this.listaReporteDespacho.map((despacho, i) => ({ despachoId: i + 1, ...despacho })).slice(
      (this.page - 1) * this.pageSize,
      (this.page - 1) * this.pageSize + this.pageSize,
    );
  }

  getControl(key: string): FormControl {
    return (this.formReporteDespacho.get(key) as FormControl);
  }

  getValue(key: string): string {
    return (this.formReporteDespacho.get(key) as FormControl).value;
  }
  getValueNumber(key: string): number {
    return (this.formReporteDespacho.get(key) as FormControl).value;
  }

  submmit() {
    if (this.formReporteDespacho.valid) {
      this.reporteDespachoServ.findAllByFilters(this.getValueNumber('bodegaId'), this.getValue("fechaI"), this.getValue("fechaF")).subscribe({
        next: (response: ReporteDespachoResponse[]) => {
          this.clearListas();
          if (response != null && response != undefined && response.length > 0) {
            this.listaReporteDespacho = response;
            this.listaReporteDespachoFilter = response;
            this.collectionSize = this.listaReporteDespacho.length;
            this.refreshDespachos();
            this.cdr.detectChanges();
          } else {
            this.refreshDespachos();
            this.cdr.detectChanges();
            this.utilServ.showMessageWarningInfoNoExisteRegistrosFilters();
          }
        }, error: (error) => {
          this.clearListas();
          this.utilServ.showMessageError("Hubo un error en la carga de datos de Despachos");
          console.log("ERROR DATA REPORTE DESPACHO: " + JSON.stringify(error));
        }
      });
    } else {
      this.utilServ.markAllControlsAsTouched(this.formReporteDespacho);
    }
  }

  clearListas() {
    this.listaReporteDespacho = [];
    this.listaReporteDespachoFilter = [];
  }
}
