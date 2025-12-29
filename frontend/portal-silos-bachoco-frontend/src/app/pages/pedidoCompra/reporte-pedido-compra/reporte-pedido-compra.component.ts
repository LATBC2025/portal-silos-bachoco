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
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { PedidoCompraResponse } from '../../../models/PedidoCompraResponse';
import { PedidoCompraService } from '../../../services/pedido/pedido-compra.service';
import { MaterialServiceService } from '../../../services/catalog/material-service.service';
import { SiloServiceService } from '../../../services/catalog/silo-service.service';
import { SiloResponse } from '../../../models/catalogs/silo/Silo.Response';
import { MaterialResponse } from '../../../models/catalogs/Material/Material.Response';
import { UtilsService } from '../../../services/shared/utils.service';
import { firstValueFrom } from 'rxjs';
import { DowloadDataService } from '../../../services/shared/dowload-data.service';
import { dateRangeValidator } from '../../../utils/validations/date-range.validator';
import { notZeroStringValidator } from '../../../services/shared/validators/not-zero-string.validator';
import { BackendError } from '../../../models/error/Backend.Errror.response';

@Component({
  selector: 'app-reporte-pedido-compra',
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
  templateUrl: './reporte-pedido-compra.component.html',
  styleUrl: './reporte-pedido-compra.component.css'
})
export class ReportePedidoCompraComponent implements OnInit {
  hide = true;
  page = 1;
  pageSize = 10;
  listaPedidosCompra!: PedidoCompraResponse[];
  listaPedidosCompraFilter!: PedidoCompraResponse[];
  collectionSize = 0;
  formProgramArribo!: FormGroup;
  //LISTAS DE CATALOGO
  listSilo: SiloResponse[] = [];
  listMateriales: MaterialResponse[] = [];
  selectedFile: File | null = null;
  rutaRecurso: string = "archivos_pps/pedido_compra/";
  private datosCompartidosService = inject(DowloadDataService);

  constructor(private library: FaIconLibrary,
    private siloService: SiloServiceService,
    private materialService: MaterialServiceService,
    private pedidoCompraService: PedidoCompraService,
    private utilsServc: UtilsService,
    private cdr: ChangeDetectorRef
  ) {
    this.library.addIconPacks(fas);
  }

  ngOnInit(): void {
    this.initForm();
    this.getSilos();
    this.findAllMaterial();
  }
  initForm() {
    this.formProgramArribo = new FormGroup({
      silo: new FormControl('0', [Validators.required, notZeroStringValidator()]),
      material: new FormControl('0', [Validators.required, notZeroStringValidator()]),
      fechaInicio: new FormControl(this.utilsServc.getFechaHoyFormateada()),
      fechaFin: new FormControl(this.utilsServc.getFechaHoyFormateada())
    }, {
      validators: [
        dateRangeValidator('fechaInicio', 'fechaFin', 'rangoFechaInvalido'),
      ]
    });
  }
  control(key: string): FormControl {
    return this.formProgramArribo.get(key) as FormControl;
  }

  getValueNumber(key: string): number {
    return ((this.formProgramArribo.get(key) as FormControl).value) as number;
  }

  getValue(key: string): string {
    return (this.formProgramArribo.get(key) as FormControl).value;
  }
  findClaveMaterialByid(id: number) {
    const material = this.listMateriales.find(m => m.id == id);
    if (material) {
      return material.nombre;
    }
    return "";
  }

  findClaveSiloByDesc(desc: string) {
    const material = this.listSilo.find(m => m.nombre == desc);
    if (material) {
      return material.silo;
    }
    return "";
  }

  findClaveSiloById(id:number){
      const silo = this.listSilo.find(s=> s.id == id);
    if (silo) {
      return silo.silo;
    }
    return "";
  }

  getPedidosCompra() {
    if (this.formProgramArribo.valid) {
      const claveMaterial = this.findClaveMaterialByid(this.getValueNumber("material"));
      const plantaDestino="";
      const claveSilo=this.findClaveSiloById(this.getValueNumber("silo"));
      this.pedidoCompraService.findAll(claveSilo, claveMaterial,plantaDestino, this.getValue("fechaInicio"), this.getValue("fechaFin")).subscribe({
        next: (response: PedidoCompraResponse[]) => {
          if (response != null && response != undefined && response.length > 0) {
            this.listaPedidosCompra = response;
            this.collectionSize = this.listaPedidosCompra.length;
            this.refreshCountries();
            this.cdr.detectChanges();
          } else {
            this.utilsServc.showMessageError("No existen registros con esos filtros");
          }
        },
        error: (error) => {
          console.log("ERROR: " + JSON.stringify(error));
          this.utilsServc.showMessageError("Hubo un error en el sistema");
        }
      });
    }
  }

  async findAllPedidosCompra(): Promise<void> {
    this.utilsServc.markAllControlsAsTouched(this.formProgramArribo);
    if (this.formProgramArribo.valid) {
      const plantaDestino="";
      const claveMaterial = this.findClaveMaterialByid(this.getValueNumber("material"));
     const claveSilo=this.findClaveSiloById(this.getValueNumber("silo"));
      try {
        const response: PedidoCompraResponse[] = await firstValueFrom(
          this.pedidoCompraService.findAll(
            claveSilo,
            claveMaterial,
            plantaDestino,
            this.getValue("fechaInicio"),
            this.getValue("fechaFin")
          )
        );
        if (response?.length > 0) {
          this.clearListas();
          this.listaPedidosCompra = response;
          this.collectionSize = this.listaPedidosCompra.length;
          this.datosCompartidosService.establecerDatosPedCompra(true);
          this.refreshCountries();
          this.cdr.detectChanges();
        } else {
          this.clearListas();
          this.refreshCountries();
          this.cdr.detectChanges();
          this.utilsServc.showMessageWarningInfoNoExisteRegistrosFilters();
        }
      } catch (err) {
        this.clearListas();
        console.error("ERROR: ", err);
       const errorConPropiedad = err as any;
        if (err) {
          const errorHttp = errorConPropiedad["error"];
          if (errorHttp["error"] == "error-code:CON-SAP-01") {
            this.utilsServc.showMessageError("Hubo un error de conexión a SAP.");
          } else{
            this.utilsServc.showMessageError("Hubo un eror en la consulta de datos con esos filtros");
          }
          // En el catch, 'error' ya es el objeto de error. 
          // Acceder a 'error["error"]' es común con HttpClient en caso de errores HTTP.
          console.log("ERROR PEDIDO COMPRA: " + JSON.stringify(err));
        }
      }
    }
  }

  deleteArchivo(compraId: number, urlArchivo: string) {
    this.pedidoCompraService.delete(compraId, urlArchivo.split("/")[2]).subscribe({
      next: (response: any) => {
        if (response == "1") {
          this.uploadPdf(compraId);
        }
      }, error: (error) => {

      }
    });
  }
  uploadPdf(compraId: number): void {
    if (!this.selectedFile) return;
    const formData = new FormData();
    formData.append('file', this.selectedFile);
    formData.append('pedidoCompraId', compraId.toString());
    this.pedidoCompraService.saveFile(formData).subscribe({
      next: (response: any) => {
        const respuesta = JSON.parse(response);
        if (respuesta["status"] != null && respuesta["status"]) {
          this.replaceUrlToCompra(compraId, respuesta["uuid"]);
          this.utilsServc.confirmarSuccess("Se cargo exitosamente el archivo");
          this.addFileToPedidoCompra(compraId, respuesta["uuid"]);
          this.refreshCountries();
          this.findAllPedidosCompra();
          this.cdr.detectChanges();
        }
      }, error: (error) => {
        this.utilsServc.showMessageError("Hubo un error al cargar el archivo.");
        console.log("DATA ERROR RESPONSE SAVE FILE: " + JSON.stringify(error));
      }
    });
    this.selectedFile = null;
  }

  downloadPdf(urlfilename: string,nombreArvhivo:string): void {
    const filename = urlfilename.split('/')[2];
    this.pedidoCompraService.dowloadPdf(filename).subscribe({
      next: (data: Blob) => {
        this.utilsServc.buildPdf(nombreArvhivo, data);
      },
      error: (error) => {
        console.error('Error en la descarga del PDF:', error);
        this.utilsServc.showMessageError("Hubo un error en la descarga del archivo.");
      }
    });
  }

  refreshCountries() {
    this.listaPedidosCompraFilter = this.listaPedidosCompra.map((arribo, i) => ({ id: i + 1, ...arribo })).slice(
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

  //metodos para cargar archivos
  onFileSelected(event: Event, compraId: number, urlArchivo: string): void {
    const input = event.target as HTMLInputElement;
    event.stopPropagation();
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      if (!this.utilsServc.isPDFFile(this.selectedFile)) {
        this.utilsServc.showMessageError('El archivo debe ser un PDF.');
        return;
      }
      if (urlArchivo != null && urlArchivo != undefined) {
        this.deleteArchivo(compraId, urlArchivo);
      } else {
        this.uploadPdf(compraId);
      }
    }
  }

  addFileToPedidoCompra(compraId: number, urlCertificado: string) {
    const pedidoCompra = this.listaPedidosCompra.find(p => p.pedidoCompraId == compraId);
    if (pedidoCompra) {
      pedidoCompra.urlCertificadoDeposito = urlCertificado;
    }
  }

  clearListas() {
    this.listaPedidosCompra = [];
    this.listaPedidosCompraFilter = [];
    this.cdr.detectChanges();
  }

  replaceUrlToCompra(compraId: number, recurso: string) {
    let index = this.listaPedidosCompra.findIndex(p => p.pedidoCompraId == compraId);
    if (index !== -1) {
      let pathReurso = this.rutaRecurso.concat(recurso);
      this.listaPedidosCompra[index] = { ...this.listaPedidosCompra[index], urlCertificadoDeposito: pathReurso }; // nueva referencia
      this.cdr.detectChanges();
    }
  }
}
