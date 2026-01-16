import { CommonModule, DecimalPipe } from '@angular/common';
import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { RouterModule } from '@angular/router';
import { NgbPaginationModule, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderComponent } from '../../../shared/header/header.component';
import { PedidoTrasladoService } from '../../../services/pedidoTraslado/pedido-traslado.service';
import { PedidoTrasladoResponse } from '../../../models/peido-traslado/PedidoTraslado.response';
import { SiloServiceService } from '../../../services/catalog/silo-service.service';
import { SiloResponse } from '../../../models/catalogs/silo/Silo.Response';
import { MaterialServiceService } from '../../../services/catalog/material-service.service';
import { MaterialResponse } from '../../../models/catalogs/Material/Material.Response';
import Swal from 'sweetalert2';
import { UtilsService } from '../../../services/shared/utils.service';
import { PedidoCompraService } from '../../../services/pedido/pedido-compra.service';
import { firstValueFrom } from 'rxjs';
import { PedidoCompraResponse } from '../../../models/PedidoCompraResponse';
import { DowloadDataService } from '../../../services/shared/dowload-data.service';
import { notZeroStringValidator } from '../../../services/shared/validators/not-zero-string.validator';
import { dateRangeValidator } from '../../../utils/validations/date-range.validator';
import { datePairValidator } from '../../../utils/validations/date-pair.validator';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';


@Component({
  selector: 'app-reporte-pedido-traslado',
  standalone: true,
  imports: [
    MatCardModule,
    MatIconModule,
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule, DecimalPipe,
    FormsModule,
    NgbTypeaheadModule,
    NgbPaginationModule,
    CommonModule,
    HeaderComponent,
  ],
  templateUrl: './reporte-pedido-traslado.component.html',
  styleUrl: './reporte-pedido-traslado.component.css'
})
export class ReportePedidoTrasladoComponent implements OnInit {
  hide = true;
  page = 1;
  pageSize = 10;
  listaPedidoTrasladoRequest: PedidoTrasladoResponse[] = [];
  listaPedidoTrasladoRequestFilter: PedidoTrasladoResponse[] = [];
  collectionSize!: number;
  formPedidoTraslado!: FormGroup;
  //listaFiltersPedidos:PedidoTrasladoResponse[]=[];
  //LISTAS DE CATALOGO
  listSilo: SiloResponse[] = [];
  listMateriales: MaterialResponse[] = [];

  showSaldoInfo = false;
  //Bandera para habilitar exportar a excel
  exportando = false;
  private datosCompartidosService = inject(DowloadDataService);
  //
  isSeccesPedCompra: number = 0;

  constructor(private pedidoTrasladoService: PedidoTrasladoService,
    private pedidoCompraService: PedidoCompraService,
    private siloService: SiloServiceService,
    private materialService: MaterialServiceService,
    private utilService: UtilsService,
    private cdr: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.initForm();
    this.getSilos();
    this.findAllMaterial();
  }

  initForm() {
    this.formPedidoTraslado = new FormGroup({
      silo: new FormControl('0', [Validators.required, Validators.required, notZeroStringValidator()]),
      material: new FormControl('0', [Validators.required, Validators.required, notZeroStringValidator()]),
      fechaInicio: new FormControl(this.utilService.getFechaHoyFormateada()),
      fechaFin: new FormControl(this.utilService.getFechaHoyFormateada())
    }, {
      validators: [
        datePairValidator('fechaInicio', 'fechaFin'),
        dateRangeValidator('fechaInicio', 'fechaFin', 'rangoFechaInvalido'),
      ]
    });
  }

  refreshPedidos() {
    this.listaPedidoTrasladoRequestFilter = this.listaPedidoTrasladoRequest.map((arribo, i) => ({ id: i + 1, ...arribo })).slice(
      (this.page - 1) * this.pageSize,
      (this.page - 1) * this.pageSize + this.pageSize,
    );
  }

  control(key: string): FormControl {
    return this.formPedidoTraslado.get(key) as FormControl;
  }

  getValueNumber(key: string): number {
    return ((this.formPedidoTraslado.get(key) as FormControl).value) as number;
  }

  getValue(key: string): string {
    return (this.formPedidoTraslado.get(key) as FormControl).value;
  }

  findClaveMaterialByid(id: number) {
    const material = this.listMateriales.find(m => m.id == id);
    if (material) {
      return material.nombre;
    }
    return "";
  }

  findClaveSiloByid(id: number) {
    const silo = this.listSilo.find(s => s.id == id);
    if (silo) {
      return silo.silo;
    }
    return "";
  }

  getPedidosTraslado() {
    if (this.formPedidoTraslado.valid) {
      const claveMaterial = this.findClaveMaterialByid(this.getValueNumber("material"));
      const plantaDestino="";
      const claveSilo=this.findClaveSiloByid(this.getValueNumber("silo"));
      this.pedidoTrasladoService.getPedidosTraslado(claveSilo,claveMaterial,plantaDestino, this.getValue("fechaInicio"), this.getValue("fechaFin")).subscribe({
        next: (response: PedidoTrasladoResponse[]) => {
          if (response != null && response != undefined) {
            if (response != null && response != undefined && response.length > 0) {
              this.listaPedidoTrasladoRequest = response;
              this.cdr.detectChanges();
            } else {
              this.utilService.showMessageError("No existen registros con esos filtros");
            }
          }
        }, error: (error) => {
          if (error) {
            this.utilService.showMessageError("Hubo un error en la consulta de pedidos de traslados.");
            console.log("ERROR PEDIDO TRASLADO getPedidosTraslado: " + JSON.stringify(error["error"]));
          }
        }
      });
    }
  }

  async findAllPedidosCompra(): Promise<void> { // Agrega 'async' y el tipo de retorno Promise<void>
    if (this.formPedidoTraslado.valid) {
      const claveMaterial = this.findClaveMaterialByid(this.getValueNumber("material"));
      const claveSilo=this.findClaveSiloByid(this.getValueNumber("silo"));
      const plantaDestino="";
      try {
        // 1. Usa firstValueFrom para convertir el Observable en una Promesa
        // 2. Usa 'await' para esperar la resolución de la Promesa
        const response: PedidoCompraResponse[] = await firstValueFrom(
          this.pedidoCompraService.findAll(
            claveSilo,
            claveMaterial,
            plantaDestino,
            this.getValue("fechaInicio"),
            this.getValue("fechaFin")
          )
        );
        // Bloque 'next' de la versión con subscribe
        if (response?.length > 0) {
          this.cdr.detectChanges();
          this.isSeccesPedCompra = 1;
        } else {
           this.cdr.detectChanges();
          this.isSeccesPedCompra = 1;
        }
        this.cdr.detectChanges();
      } catch (err) {
        // Bloque 'error' de la versión con subscribe
         const errorConPropiedad = err as any;
        if (err) {
          const errorHttp = errorConPropiedad["error"];
          if (errorHttp["error"] == "error-code:CON-SAP-01") {
            this.isSeccesPedCompra = -2;
          }else{
              this.utilService.showMessageError("Hubo un error en el sistema");
              this.isSeccesPedCompra = -1;
          }
          // En el catch, 'error' ya es el objeto de error.
          // Acceder a 'error["error"]' es común con HttpClient en caso de errores HTTP.
          console.log("ERROR PEDIDO COMPRA: " + JSON.stringify(err));
        }
      }
    }
  }


  async findAllPedidosTraslado(): Promise<void> {
    this.utilService.markAllControlsAsTouched(this.formPedidoTraslado);
    if (this.formPedidoTraslado.valid) {
      const claveMaterial = this.findClaveMaterialByid(this.getValueNumber("material"));
      const claveSilo=this.findClaveSiloByid(this.getValueNumber("silo"));
      try {
        await this.findAllPedidosCompra();
        // 1. Convertir el Observable del servicio en una Promesa con firstValueFrom
        // 2. Usar 'await' para esperar la respuesta
        if (this.isSeccesPedCompra == 2) {
          this.clearListas();
          this.utilService.showMessageWarningInfoNoExisteRegistrosFilters();
          return;
        } else if (this.isSeccesPedCompra == -2) {
          this.clearListas();
          this.utilService.showMessageError("Hubo un error de conexión a SAP.");
          return;
        } else if (this.isSeccesPedCompra == -1) {
          this.clearListas();
          this.utilService.showMessageError("Hubo un eror en la consulta de datos con esos filtros");
          return;
        }
        const plantaDestino="";
        const response: PedidoTrasladoResponse[] = await firstValueFrom(
          this.pedidoTrasladoService.getPedidosTraslado(
            claveSilo,
            claveMaterial,
            plantaDestino,
            this.getValue("fechaInicio"),
            this.getValue("fechaFin")
          )
        );
        // Bloque 'next' refactorizado
        if (response) {
          if (response.length > 0) {
            this.listaPedidoTrasladoRequest = response;
            this.showSaldoInfo = this.listaPedidoTrasladoRequest.some(x => x.saldoSeLiberaManana === true);


            this.datosCompartidosService.establecerDatosPedTraslado(true);
            this.refreshPedidos();
            this.collectionSize = this.listaPedidoTrasladoRequest.length;
            this.cdr.detectChanges();
          } else {
            this.clearListas();
            this.utilService.showMessageWarningInfoNoExisteRegistrosFilters();
          }
        }

      } catch (error) {
        const errorConPropiedad = error as any;
        if (error) {
          const errorHttp = errorConPropiedad["error"];
          if (errorHttp["error"] == "error-code:NO-EXISTE-PLANTAS") {
            this.utilService.showMessageError("Hubo un error al registrar los pedidos de traslados; no existen las plantas: " + errorHttp["message"]);
          } else if (errorHttp["error"] == "error-code:NO-EXISTE-MATERIALES") {
            this.utilService.showMessageError("Hubo un error al registrar los pedidos de traslados; no existen los materiales: " + errorHttp["message"]);
          }
          else if (errorHttp["error"] == "error-code:NO-EXISTE-PED-COMPRA") {
            this.utilService.showMessageError("Hubo un error al registrar los pedidos de traslados; no existen los pedidos de compra asociados. " + errorHttp["message"]);
          }else if(errorHttp["error"] == "error-code:EX-001"){
            this.utilService.showMessageError("Hubo un error en la consulta de pedidos de traslados.");
          }else{
            this.utilService.showMessageError("Hubo error en el servidor o conexión a SAP.");
          }
          // En el catch, 'error' ya es el objeto de error.
          // Acceder a 'error["error"]' es común con HttpClient en caso de errores HTTP.
          console.log("ERROR PEDIDO TRASLADO findAllPedidosTraslado: " + JSON.stringify(error));
        }
        // Opcionalmente, mostrar un mensaje de error genérico al usuario si el catch se dispara.
        //this.utilService.showMessageError("Hubo un error al obtener los pedidos de traslado.");
      }
    }
  }

  clearListas(){
    this.listaPedidoTrasladoRequest=[];
    this.listaPedidoTrasladoRequestFilter=[];
    this.showSaldoInfo = false;
    this.cdr.detectChanges();
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
        console.log("ERROR DATA SILO: " + JSON.stringify(error));
      }
    })
  }

  findAllMaterial() {
    this.materialService.findAll().subscribe({
      next: (response: MaterialResponse[]) => {
        if (response != null && response != undefined) {
          this.listMateriales = response;
          this.cdr.detectChanges();
        }
      }, error: (error) => {
        console.log("ERROR DATA SILO: " + JSON.stringify(error));
      }
    })
  }
  exportar(): void {
  const data = this.listaPedidoTrasladoRequest; // TODO el resultado
  if (!data?.length) return;

  this.exportando = true;

  try {
    // ---- 1) Texto de filtros (2da línea) ----
    const siloId = Number(this.getValue("silo"));
    const materialId = Number(this.getValue("material"));

    const siloTxt =
      this.listSilo.find(s => s.id === siloId)?.nombre
      ?? this.listSilo.find(s => s.id === siloId)?.silo
      ?? String(siloId);

    const materialTxt =
      this.listMateriales.find(m => m.id === materialId)?.descripcion
      ?? this.listMateriales.find(m => m.id === materialId)?.nombre
      ?? String(materialId);

    const fechaInicio = this.getValue("fechaInicio");
    const fechaFin = this.getValue("fechaFin");

    const titulo = "Reporte Pedido Traslado";
    const filtros = `Filtros: Silo=${siloTxt} | Material=${materialTxt} | Fecha Inicio=${fechaInicio} | Fecha Fin=${fechaFin}`;

    // ---- 2) Tabla (filas) ----
    const rows = data.map((x) => ({
      "Id": x.pedidoTrasladoId ?? "",
      "Planta Destino": x.nombrePlantaDestino ?? "",
      "Pedido Traslado": x.numPedidoTraslado ?? "",
      "Cantidad pedido": x.cantidadPedido ?? 0,
      "Cantidad Traslado": x.cantidadTraslado ?? 0,
      "Cantidad Programada": x.cantidadEmbarcadaReal ?? 0,
      "Cantidad Pendiente por Programar": x.cantidadPendientePorProgramar ?? 0,
      "Cantidad Recibida": x.cantidadRecibidaPa ?? 0,
      "Cantidad Pendiente Traslado": x.cantidadPendienteTraslado ?? 0,
      "Pedido Compras Asociado": x.numCompraAsociado ?? "",
      "Traslado Pendiente Facturas": x.trasladosPendFact ?? 0,
    }));

    // ---- 3) Hoja: título + filtros + blanco ----
    // A1: Título
    // A2: Filtros
    // A3: en blanco
    // A4: tabla
    const ws: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet([
      [titulo],
      [filtros],
      []
    ]);

    XLSX.utils.sheet_add_json(ws, rows, {
      origin: "A4",
      skipHeader: false
    });

    // ---- 4) Anchos de columna ----
    ws["!cols"] = [
      { wch: 10 }, // Id
      { wch: 22 }, // Planta
      { wch: 18 }, // Pedido Traslado
      { wch: 16 }, // Cant pedido
      { wch: 16 }, // Cant traslado
      { wch: 22 }, // embarcada real
      { wch: 30 }, // pendiente por programar
      { wch: 16 }, // recibida
      { wch: 26 }, // pendiente traslado
      { wch: 22 }, // pedido compra
      { wch: 26 }, // pendiente facturas
    ];

    // ---- 5) Fusionar título y filtros hasta la última columna ----
    // Última columna = 11 columnas => A..K (0..10)
    // Título: A1:K1, Filtros: A2:K2
    ws["!merges"] = [
      { s: { r: 0, c: 0 }, e: { r: 0, c: 10 } }, // A1:K1
      { s: { r: 1, c: 0 }, e: { r: 1, c: 10 } }, // A2:K2
    ];

    // ---- 6) Workbook + descarga ----
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, "PedidoTraslado");

    const excelBuffer: ArrayBuffer = XLSX.write(wb, {
      bookType: "xlsx",
      type: "array",
    });

    const now = new Date();
    const yyyy = now.getFullYear();
    const mm = String(now.getMonth() + 1).padStart(2, "0");
    const dd = String(now.getDate()).padStart(2, "0");
    const hh = String(now.getHours()).padStart(2, "0");
    const mi = String(now.getMinutes()).padStart(2, "0");

    const fileName = `reporte_pedido_traslado_${yyyy}${mm}${dd}_${hh}${mi}.xlsx`;

    const blob = new Blob([excelBuffer], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8",
    });

    saveAs(blob, fileName);

  } catch (e) {
    console.error("Error exportando Excel:", e);
    this.utilService.showMessageError("No se pudo exportar el archivo Excel.");
  } finally {
    this.exportando = false;
    this.cdr.detectChanges();
  }
}

}
