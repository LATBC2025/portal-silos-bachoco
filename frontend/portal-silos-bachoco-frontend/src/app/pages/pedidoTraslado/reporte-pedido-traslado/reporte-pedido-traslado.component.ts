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

}
