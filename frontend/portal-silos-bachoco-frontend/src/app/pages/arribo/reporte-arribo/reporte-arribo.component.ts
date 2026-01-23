import { DecimalPipe } from '@angular/common';
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
import { ReporteArriboResponse } from '../../../models/reporte-program-arribo/Reporte.Arribo.Response';
import { ReporteArriboService } from '../../../services/reporteArribos/reporte-arribo.service';
import { response } from 'express';
import { SiloResponse } from '../../../models/catalogs/silo/Silo.Response';
import { SiloServiceService } from '../../../services/catalog/silo-service.service';
import Swal from 'sweetalert2';
import { UtilsExcelService } from '../../../services/shared/utils-excel.service';
import { notZeroStringValidator } from '../../../services/shared/validators/not-zero-string.validator';
import { UtilsService } from '../../../services/shared/utils.service';
import { dateRangeValidator } from '../../../utils/validations/date-range.validator';
import { AuthServiceService } from '../../../services/auth/auth-service.service';
import * as XLSX from 'xlsx';
import { EmpleadoExternoService } from '../../../services/emppleado-externo/empleado-externo.service';
import { EmpleadoExternoResponseDTO } from '../../../models/catalogs/Empleado-externo/Empleado.Response.DTO';




@Component({
  selector: 'app-reporte-arribo',
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
    HeaderComponent
  ],
  templateUrl: './reporte-arribo.component.html',
  styleUrl: './reporte-arribo.component.css'
})
export class ReporteArriboComponent implements OnInit {
  hide = true;
  page = 1;
  pageSize = 10;
  collectionSize = 0;
  listaReporteArribo: ReporteArriboResponse[] = [];
  listaReporteArriboFilters: ReporteArriboResponse[] = [];
  listSilo: SiloResponse[] = [];
  formReporteArribo!: FormGroup;
  siloIdEmpleado!: number;
  opcionSeleccionada!: number;
  isExternoEmployee!: boolean;
  listProveedores: EmpleadoExternoResponseDTO[] = [];



  constructor(private reporteArriboServ: ReporteArriboService,
    private siloService: SiloServiceService,
    private authService: AuthServiceService,
    private utilServ: UtilsService,
    private cdr: ChangeDetectorRef,
    private empleadoExternoService: EmpleadoExternoService // <-- NUEVO

  ) {

  }
  ngOnInit(): void {
    this.extractSiloId();
    this.initForm();
    this.getSilos();
    this.extractNomPuesto();
  }

  extractSiloId() {
    this.siloIdEmpleado = Number(this.authService.getSiloIdInLocalStorage());
    this.opcionSeleccionada = this.siloIdEmpleado;
  }

  extractNomPuesto() {
    let tipoEmpleado = this.authService.getNombrePuestonLocalStorage();
    if (tipoEmpleado) {
      if (tipoEmpleado == "EXTERNO") {
        this.isExternoEmployee = true;
        this.formReporteArribo.get('siloId')?.disable();
        this.cdr.detectChanges();
      }
    }
  }

  initForm() {
    this.formReporteArribo = new FormGroup({
      siloId: new FormControl('0', [Validators.required, notZeroStringValidator()]),
      idProveedor: new FormControl('0', [notZeroStringValidator()]), // <-- NUEVO (obligatorio)
      fechaI: new FormControl('', [Validators.required]),
      fechaF: new FormControl('', [Validators.required])
    }, {
      validators: [
        dateRangeValidator('fechaI', 'fechaF', 'rangoFechaInvalido'),
      ]
    });

     // ✅ Cuando cambia silo: reset proveedor + cargar proveedores del silo
  this.formReporteArribo.get('siloId')?.valueChanges.subscribe((val) => {
    const siloId = Number(val ?? 0);

    this.formReporteArribo.get('idProveedor')?.setValue('0');
    this.listProveedores = [];

    if (!siloId || siloId === 0) {
      this.cdr.detectChanges();
      return;
    }

    this.loadProveedoresBySilo(siloId);
  });

  }


  loadProveedoresBySilo(siloId: number) {
  this.empleadoExternoService.findAllBySilo(siloId).subscribe({
    next: (resp: EmpleadoExternoResponseDTO[]) => {
      this.listProveedores = resp ?? [];
      this.cdr.detectChanges();
    },
    error: (err) => {
      this.listProveedores = [];
      this.utilServ.showMessageError("Hubo un error al cargar proveedores por silo");
      console.log("ERROR PROVEEDORES POR SILO:", err);
      this.cdr.detectChanges();
    }
  });
}



  getSilos() {
    this.siloService.getSilos().subscribe({
      next: (response: SiloResponse[]) => {
        if (response != null && response != undefined) {
          this.listSilo = response;
          this.refreshCountries();
          this.cdr.detectChanges();
        }
      },
      error: (error) => {
        this.utilServ.showMessageError("Hubo un error al cargar los datos de silos");
        console.log("ERROR DATA SILO: " + JSON.stringify(error));
      }
    })
  }


  refreshCountries() {
    this.listaReporteArriboFilters = this.listaReporteArribo.map((arribo, i) => ({ arriboId: i + 1, ...arribo })).slice(
      (this.page - 1) * this.pageSize,
      (this.page - 1) * this.pageSize + this.pageSize,
    );
  }

  getControl(key: string): FormControl {
    return (this.formReporteArribo.get(key) as FormControl);
  }

  getValue(key: string): string {
    return (this.formReporteArribo.get(key) as FormControl).value;
  }
  getValueNumber(key: string): number {
    return (this.formReporteArribo.get(key) as FormControl).value;
  }

  submmit() {
    if (this.formReporteArribo.valid) {
const raw = this.formReporteArribo.getRawValue();
    const idProveedor = Number(raw.idProveedor);


      this.reporteArriboServ.findAllByFilters(this.getValueNumber('siloId'), idProveedor,this.getValue("fechaI"), this.getValue("fechaF")).subscribe({
        next: (response: ReporteArriboResponse[]) => {
          this.clearListas();
          if (response != null && response != undefined && response.length > 0) {
            this.listaReporteArribo = response;
            this.listaReporteArriboFilters = response;
            this.collectionSize = this.listaReporteArribo.length;
            this.refreshCountries();
            this.cdr.detectChanges();
          } else {
            this.listaReporteArribo = [];
            this.listaReporteArriboFilters = [];
            this.collectionSize = 0;
            this.refreshCountries();
            this.cdr.detectChanges();
            this.utilServ.showMessageWarningInfoNoExisteRegistrosFilters();
          }
        }, error: (error) => {
          this.utilServ.showMessageError("Hubo un error al consultar la informacion");
        }
      });
    } else {
      this.utilServ.markAllControlsAsTouched(this.formReporteArribo);
    }
  }
  downloadExcel(): void {
  console.log('[EXCEL] Click exportar');

  const data = this.listaReporteArribo;

  if (!data || data.length === 0) {
    this.utilServ.showMessageWarningInfo('No hay datos en la tabla para exportar');
    return;
  }

  // ✅ Nombre dinámico del proveedor (según el select)
  const raw = this.formReporteArribo.getRawValue();
  const idProveedor = Number(raw.idProveedor ?? 0);

  const proveedorSeleccionado = this.listProveedores?.find(p => Number(p.id) === idProveedor);
  const nombreProveedorTitulo = proveedorSeleccionado?.nombre?.trim() || 'Proveedor';

  // ✅ Header dinámico (lo que se verá como título en Excel)
  const proveedorHeader = `Proveedor: ${nombreProveedorTitulo}`;

  const rows = data.map(r => ({
    Toneladas: r.toneladas,
    Material: r.material,
    Fecha: r.fecha,
    Pedido: r.numeroPedido,
    'Planta Destino': r.destinoPlanta,
    [proveedorHeader]: r.nombreProveedor ?? '' // ✅ Columna con header dinámico
  }));

  const ws = XLSX.utils.json_to_sheet(rows);

  ws['!cols'] = [
    { wch: 12 },
    { wch: 25 },
    { wch: 14 },
    { wch: 18 },
    { wch: 25 },
    { wch: 35 } // ✅ ancho para la columna dinámica
  ];

  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, 'Arribos');

  const siloId = raw.siloId ?? '0';
  const fechaI = raw.fechaI ?? '';
  const fechaF = raw.fechaF ?? '';

  const fileName = `Reporte_Arribos_Silo_${siloId}_${fechaI}_a_${fechaF}.xlsx`;
  XLSX.writeFile(wb, fileName);
}

  clearListas() {
    this.listaReporteArribo = [];
    this.listaReporteArriboFilters = [];
  }
}

