import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { RouterModule } from '@angular/router';
import {
  NgbPaginationModule,
  NgbTypeaheadModule,
} from '@ng-bootstrap/ng-bootstrap';
import { HeaderComponent } from '../../../shared/header/header.component';
import {
  FaIconLibrary,
  FontAwesomeModule,
} from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { ConfirmDespachoService } from '../../../services/confirmacionDespacho/confirm-despacho.service';
import { CommonModule, NumberSymbol } from '@angular/common';
import { SiloResponse } from '../../../models/catalogs/silo/Silo.Response';
import { MaterialResponse } from '../../../models/catalogs/Material/Material.Response';
import { PlantaResponse } from '../../../models/catalogs/planta/Planta.Response';
import { MaterialServiceService } from '../../../services/catalog/material-service.service';
import { PlantaServiceService } from '../../../services/catalog/planta-service.service';
import { PedidoTrasladoService } from '../../../services/pedidoTraslado/pedido-traslado.service';
import { SiloServiceService } from '../../../services/catalog/silo-service.service';
import { ConfirmacionDespachoRequest } from '../../../models/confirmacion-despacho/Confirmacion.Despacho.Request';
import { BodegaServiceService } from '../../../services/catalog/bodega-service.service';
import { BodegaResponse } from '../../../models/catalogs/bodega/Bodega.Response';
import { AuthServiceService } from '../../../services/auth/auth-service.service';
import * as XLSX from 'xlsx';
import { ConfDespachoTransactionResponse } from '../../../models/confirmacion-despacho/Confirmacion.Despacho.Transaction.response';
import { ProgramArriboService } from '../../../services/program-arribo/program-arribo.service';
import { ProgramPedTrasladoResponse } from '../../../models/programacion-arribo/Program.Pedido.Traslado';
import { PedTrasladoConfDespachoResponse } from '../../../models/confirmacion-despacho/Pedido.Traslado.Conf.Despacho';
import { UtilsService } from '../../../services/shared/utils.service';
import { firstValueFrom, lastValueFrom } from 'rxjs';
import { notZeroStringValidator } from '../../../services/shared/validators/not-zero-string.validator';
import { ConfirmacionDespachoResponse } from '../../../models/confirmacion-despacho/Confirmacion.Despacho.Response';
import { DateValidators } from '../../../utils/validations/date-DateValidators ';
import { DateUtils } from '../../../utils/date/DateUtils ';
import { dateRangeValidator } from '../../../utils/validations/date-range.validator';
import { ConfirmacionDespachoForm } from '../../../models/confirmacion-despacho/ConfirmacionDespacho-form.request';
import { ConfirmationDespachoMapper } from '../../../services/shared/mappers/confirmation-despacho.mapper';

interface ConfirmacionDespacho {
  id?: number;
  idBodega: string;
  idMaterial: string;
  fechaEmbargue: string;
  numeroBoleta: string;
  pesoBruto: number;
  pesoTara: number;
  humedad: string;
  chofer: string;
  placaJaula: string;
  lineaTransportista: string;
  destino: string;
  pedido: string;
}
const CONFIRMACION_DESPACHO: ConfirmacionDespacho[] = [];
@Component({
  selector: 'app-confirmacion-despacho',
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
    FontAwesomeModule,
    CommonModule,
  ],
  templateUrl: './confirmacion-despacho.component.html',
  styleUrl: './confirmacion-despacho.component.css',
})
export class ConfirmacionDespachoComponent implements OnInit {
  isExternoEmployee!: boolean;
  opcionSeleccionada!: number;
  formConfDespacho!: FormGroup;
  formFilter!: FormGroup;
  //LISTAS DE CATALOGO
  listSilo: SiloResponse[] = [];
  listMateriales: MaterialResponse[] = [];
  listPlantas: PlantaResponse[] = [];
  listaBodega: BodegaResponse[] = [];
  //listPedTraslado: PedidoTrasladoResponse[] = [];
  listPedTraslado: PedTrasladoConfDespachoResponse[] = [];
  listPedTrasladoFilter: PedTrasladoConfDespachoResponse[] = [];
  listProgramPedTraslado: ProgramPedTrasladoResponse[] = [];
  listaConfirmacionesDespacho: ConfirmacionDespachoResponse[] = [];
  listaConfirmacionesDespachoFilter: ConfirmacionDespachoResponse[] = [];
  siloIdEmpleado!: number;
  enableDowloadPd: boolean = true;
  disabledBtnActions: boolean = true;
  siloIdEnvioDeleteSap!: number;
  //variables para carga de excel
  excelData: any[] = [];
  excelHeaders: string[] = [];
  enabledBtnEdit = true;
  public isDownloading: boolean[] = [];
  public isIdCofingDespacho: boolean[] = [];
  public isAddItemFirst: boolean = false;
  public isModificable: boolean[] = [];
  public listFolioReporte: string[] = [];
  collectionSize!: number;
  hide = true;
  page = 1;
  pageSize = 10;
  private plantasFiltradasMap = new Map<number, any[]>();
  private VALUE_EXTERNO = 'EXTERNO';

  constructor(
    private library: FaIconLibrary,
    private confDespachoServ: ConfirmDespachoService,
    private cdr: ChangeDetectorRef,
    private siloService: SiloServiceService,
    private materialService: MaterialServiceService,
    private plantaService: PlantaServiceService,
    private bodegaService: BodegaServiceService,
    private authService: AuthServiceService,
    private pedidoTrasladoService: PedidoTrasladoService,
    private programArriboService: ProgramArriboService,
    private utilsServc: UtilsService,
    private fb: FormBuilder
  ) {
    this.library.addIconPacks(fas);
  }
  async ngOnInit() {
    this.extractSiloId();
    this.initFormFilter();
    this.initFormConfDespacho();
    this.findAllSilos();
    this.findAllMaterial();
    this.findAllBodegas(this.siloIdEmpleado);
    this.extractNomPuesto();
  }

  extractSiloId() {
    this.siloIdEmpleado = Number(this.authService.getSiloIdInLocalStorage());
    this.opcionSeleccionada = this.siloIdEmpleado;
  }

  extractNomPuesto() {
    let tipoEmpleado = this.authService.getNombrePuestonLocalStorage();
    if (tipoEmpleado) {
      if (tipoEmpleado == this.VALUE_EXTERNO) {
        this.isExternoEmployee = true;
        this.formFilter.get('siloId')?.disable();
        this.cdr.detectChanges();
      } else {
        this.opcionSeleccionada = 0;
      }
    }
  }
  initFormConfDespacho() {
    this.formConfDespacho = this.fb.group({
      items: this.fb.array([]),
    });
  }
  initFormFilter() {
    const hoy = DateUtils.getFechaHoy();
    this.formFilter = this.fb.group(
      {
        siloId: ['0', [Validators.required, notZeroStringValidator()]],
        materialId: ['0', [Validators.required, notZeroStringValidator()]],
        fechaI: [hoy, []],
        fechaF: [hoy, []],
      },
      {
        validators: [
          dateRangeValidator('fechaI', 'fechaF', 'rangoFechaInvalido'),
        ],
      }
    );
  }
  get items(): FormArray {
    return this.formConfDespacho.get('items') as FormArray;
  }
  getClaveSilo(id: number) {
    let siloRe = this.listSilo.find((s) => s.id == id);
    return siloRe?.nombre;
  }
  getClaveSiloById(id: number) {
    return this.listSilo.find((s) => s.id == id)?.silo;
  }
  getBodegaId(nombre: string) {
    return this.listaBodega.find((b) => b.nombre == nombre)?.id;
  }
  getClaveBodegaById(id: string) {
    return this.listaBodega.find((b) => b.id == Number(id))?.nombre;
  }
  getClaveMaterialById(id: string) {
    return this.listMateriales.find((m) => m.id == Number(id))?.nombre;
  }
  getClaveMaterial(desc: string) {
    let item = this.listMateriales.find(
      (m) => Number(m.nombre) == Number(desc)
    );
    return item?.nombre;
  }
  getClaveMaterialByClave(desc: string) {
    let item = this.listMateriales.find(
      (m) => String(m.descripcion).toLowerCase() == desc.toLowerCase()
    );
    return item?.nombre;
  }
  getClavesILObYdES(desc: string) {
    let item = this.listSilo.find(
      (m) => String(m.nombre).toLowerCase() == desc.toLowerCase()
    );
    return item?.silo;
  }
  findMaterialIdByclave(id: number) {
    const material = this.listMateriales.find((m) => m.id == id);
    if (material) {
      return material.nombre;
    } else {
      return '0';
    }
  }
  findMaterialIdByclaveV2(clave: string) {
    const material = this.listMateriales.find((m) => m.nombre == clave);
    if (material) {
      return material.id;
    } else {
      return '0';
    }
  }
  getDestinoId(nombre: string) {
    let item = this.listPlantas.find((p) => p.nombre == nombre);
    return item?.id;
  }
  getDestinoIdByPlantaDestino(planta: string) {
    let item = this.listPlantas.find((p) => p.planta == planta);
    return item?.id;
  }
  getClaveDestinoById(id: number) {
    let item = this.listPlantas.find((p) => p.id == id);
    return item?.planta;
  }
  getIdBodegaByClave(bodega: string) {
    let item = this.listaBodega.find(
      (b) => b.nombre.toLowerCase == bodega.toLowerCase
    );
    return item?.id;
  }
  getPedidoTrasladoIdByNumerTraslado(num: string) {
    return this.listPedTrasladoFilter.find((p) => p.numeroPedido == num)
      ?.pedidoTrasladoId;
  }
  addItem() {
    this.items.push(this.createItem());
    this.isDownloading.push(true);
    this.isModificable.push(false);
    this.isIdCofingDespacho.push(false);
    this.updateAlllPlantasDestino(this.items.length - 1);
    this.cdr.detectChanges();
  }
  addItemVacio() {
    this.items.push(this.createItem());
    this.isDownloading.push(true);
    this.isModificable.push(false);
    this.isIdCofingDespacho.push(false);
    this.cdr.detectChanges();
  }
  removeItem(index: number) {
    this.items.removeAt(index);
    this.cdr.detectChanges();
  }
  findClavePedTrasladoById(id: number) {
    const ped = this.listProgramPedTraslado.find(
      (p) => p.pedidoTrasladoId == id
    );
    if (ped) return ped.numeroPedido;
    return '';
  }
  buildConfirmacionDespachoRequest(index: number): ConfirmacionDespachoRequest {
    const f = this.items.at(index) as FormGroup; //getRawValue() incluye los campos deshabilitados también
    return this.utilsServc.buildConfirmacionDespachoRequest(
      this.items.at(index) as FormGroup
    );
  }
  buildConfirmacionDespachoRequestUpdate(
    index: number,
    id: string
  ): ConfirmacionDespachoRequest {
    const f = this.items.at(index) as FormGroup; //getRawValue() incluye los campos deshabilitados también
    f.get('tipoMovimiento')?.setValue('352');
    f.get('idConfDespacho')?.setValue(id);
    return this.utilsServc.buildConfirmacionDespachoRequest(
      this.items.at(index) as FormGroup
    );
  }

  buildConfirmacionDespachoRequestDDeleteSAP352(
    index: number,
    claveSilo: string,
    idConfDespacho: number
  ): ConfirmacionDespachoRequest {
    const f = this.items.at(index) as FormGroup; //getRawValue() incluye los campos deshabilitados también
    f.get('tipoMovimiento')?.setValue('352');
    f.get('claveSilo')?.setValue(claveSilo);
    return this.utilsServc.buildConfirmacionDespachoRequest(
      this.items.at(index) as FormGroup
    );
  }

  buildConfirmacionDespachoRequestDeelete(
    index: number,
    id: string
  ): ConfirmacionDespachoRequest {
    const f = this.items.at(index) as FormGroup; //getRawValue() incluye los campos deshabilitados también
    f.get('tipoMovimiento')?.setValue('352');
    f.get('idConfDespacho')?.setValue(id);
    return this.utilsServc.buildConfirmacionDespachoRequest(
      this.items.at(index) as FormGroup
    );
  }

  private createItem(): FormGroup {
    const hoy = DateUtils.getFechaHoy();
    let _emp_siloId =
      this.siloIdEmpleado != 0
        ? this.siloIdEmpleado
        : Number(this.formFilter.get('siloId')?.value ?? '0');
    let _claveSilo = this.getClaveSilo(_emp_siloId);
    return this.fb.group({
      claveBodega: ['0', [Validators.required, notZeroStringValidator()]],
      claveSilo: [_claveSilo, [Validators.required]],
      claveMaterial: [
        {
          value: this.findMaterialIdByclave(this.getValueNumber('materialId')),
          disabled: true,
        },
        [Validators.required],
      ],
      fechaEmbarque: [
        hoy,
        [Validators.required, DateValidators.fechaNoFuturaValidator()],
      ],
      numBoleta: [
        '',
        [Validators.required, Validators.pattern(/^\d+(\.\d+)?$/)],
      ],
      pesoBruto: [
        ,
        [
          Validators.required,
          Validators.min(0),
          Validators.pattern(/^\d+(\.\d+)?$/),
        ],
      ],
      pesoTara: [
        ,
        [
          Validators.required,
          Validators.min(0),
          Validators.pattern(/^\d+(\.\d+)?$/),
        ],
      ],
      humedad: ['', [Validators.required, Validators.pattern(/^\d+(\.\d+)?$/)]],
      chofer: ['', [Validators.required]],
      placaJaula: ['', [Validators.required]],
      lineaTransportista: ['', [Validators.required]],
      destinoId: ['0', [Validators.required, notZeroStringValidator()]],
      numPedidoTraslado: ['0', [Validators.required, notZeroStringValidator()]],
      tipoMovimiento: [351, [Validators.required]],
      idConfDespacho: ['0'],
      numeroSap: [''],
      folio: [''],
    });
  }

  /*   onFileChange(event: any): void {
      const file = event.target.files[0];
      if (!file) return;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const arrayBuffer = e.target.result;
        const workbook = XLSX.read(arrayBuffer, { type: 'array' });
        const sheet = workbook.Sheets[workbook.SheetNames[0]];
        const jsonData = XLSX.utils.sheet_to_json(sheet, { defval: '' });
        this.excelData = jsonData;
        this.printDataExcelInList();
        event.target.value = null;
      };
      reader.readAsArrayBuffer(file);
    } */

  onFileChange(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (e: ProgressEvent<FileReader>) => {
      const arrayBuffer = e.target?.result as ArrayBuffer;
      const workbook = XLSX.read(arrayBuffer, { type: 'array' });
      const sheet = workbook.Sheets[workbook.SheetNames[0]];
      // Definir tipo para nuestros datos
      interface ExcelRow {
        Bodega: string;
        Material: string;
        Fecha_Embarque: string;
        Numero_Boleta: string;
        Peso_bruto: string;
        Peso_tara: string;
        Humedad: string;
        Chofer: string;
        Placa_Jaula: string;
        Linea_transportista: string;
        Destino: string;
        Pedido: string;
        [key: string]: any;
      }
      const requiredHeaders = [
        'Bodega',
        'Material',
        'Fecha_Embarque',
        'Numero_Boleta',
        'Peso_bruto',
        'Peso_tara',
        'Humedad',
        'Chofer',
        'Placa_Jaula',
        'Linea_transportista',
        'Destino',
        'Pedido',
      ];

      const requiredHeadersLower = requiredHeaders.map((h) => h.toLowerCase());

      const rawData: unknown[][] = XLSX.utils.sheet_to_json(sheet, {
        header: 1,
        defval: '',
        raw: false,
      });

      const dataRows: any[][] = [];
      for (const row of rawData) {
        if (Array.isArray(row)) {
          const hasData = row.some((cell) => {
            if (cell == null) return false;
            const str = String(cell).trim();
            return str !== '' && str !== 'null' && str !== 'undefined';
          });
          if (hasData) {
            dataRows.push(row);
          }
        }
      }

      if (dataRows.length === 0) {
        this.excelData = [];
        this.printDataExcelInList();
        event.target.value = null;
        return;
      }

      // ANALIZAR PRIMERA FILA
      const firstRow = dataRows[0];
      const firstRowValues = firstRow.map((cell) => {
        if (cell == null) return '';
        return String(cell).trim();
      });

      const firstRowLower = firstRowValues.map((v) => v.toLowerCase());

      // Verificar si la primera fila tiene los encavezados
      let hasExactHeaders = true;
      const minLength = Math.min(firstRowValues.length, requiredHeaders.length);

      for (let i = 0; i < minLength; i++) {
        if (firstRowLower[i] !== requiredHeadersLower[i]) {
          hasExactHeaders = false;
          break;
        }
      }
      if (firstRowValues.length < requiredHeaders.length) {
        hasExactHeaders = false;
      }

      let matchCount = 0;
      for (const cellValue of firstRowLower) {
        if (requiredHeadersLower.includes(cellValue)) {
          matchCount++;
        }
      }
      const hasManyMatchingHeaders = matchCount >= 3;

      let textCells = 0;
      for (const cell of firstRow) {
        if (typeof cell === 'string' && String(cell).trim() !== '') {
          textCells++;
        }
      }

      const totalCells = firstRow.length || 1;
      const isMostlyText = textCells > totalCells / 2;

      let looksLikeData = false;
      for (const cell of firstRow) {
        if (typeof cell === 'number') {
          looksLikeData = true;
          break;
        }
        if (typeof cell === 'string') {
          const trimmed = String(cell).trim();
          if (!isNaN(Number(trimmed)) || !isNaN(Date.parse(trimmed))) {
            looksLikeData = true;
            break;
          }
        }
      }
      const firstRowIsHeader =
        (hasExactHeaders || hasManyMatchingHeaders || isMostlyText) &&
        !looksLikeData;
      const jsonData: ExcelRow[] = [];
      const startIndex = firstRowIsHeader ? 1 : 0;

      for (let i = startIndex; i < dataRows.length; i++) {
        const row = dataRows[i];
        const obj: ExcelRow = {
          Bodega: '',
          Material: '',
          Fecha_Embarque: '',
          Numero_Boleta: '',
          Peso_bruto: '',
          Peso_tara: '',
          Humedad: '',
          Chofer: '',
          Placa_Jaula: '',
          Linea_transportista: '',
          Destino: '',
          Pedido: '',
        };

        // Asignar valores de la fila a nuestras columnas
        for (let j = 0; j < requiredHeaders.length; j++) {
          const header = requiredHeaders[j];
          if (j < row.length && row[j] !== undefined && row[j] !== null) {
            (obj as any)[header] = String(row[j]);
          } else {
            (obj as any)[header] = '';
          }
        }

        jsonData.push(obj);
      }
      this.excelData = jsonData;
      this.printDataExcelInList();
      event.target.value = null;
    };
    reader.readAsArrayBuffer(file);
  }

  validaBodegaExiste(nombre: string) {
    const bodega = this.listaBodega.find(
      (b) => String(b.nombre).toLowerCase() == String(nombre).toLowerCase()
    );
    return bodega != undefined && bodega != null ? true : false;
  }
  validaBodegaExisteByNombre(nombre: string) {
    const bodega = this.listaBodega.find(
      (b) => String(b.nombre).toUpperCase() == String(nombre).toUpperCase()
    );
    return bodega != undefined && bodega != null ? true : false;
  }
  validaMaterialExiste(nombre: string) {
    const material = this.listMateriales.find(
      (b) => String(b.descripcion).toLowerCase() == String(nombre.toLowerCase())
    );
    return material != undefined && material != null ? true : false;
  }
  validaMaterialExisteById(nombre: string) {
    const materialSeleccionado = this.formFilter.get('materialId')?.value;
    const material = this.listMateriales.find(
      (b) => b.id == materialSeleccionado
    );
    if (material) {
      return String(material.descripcion).toLowerCase() ==
        String(nombre.toLowerCase())
        ? true
        : false;
    }
    return false;
  }
  validaMaterialExisteByClave(clave: string) {
    const materialSeleccionado = this.formFilter.get('materialId')?.value;
    const material = this.listMateriales.find(
      (b) => b.id == materialSeleccionado
    );
    if (material) {
      return Number(material.nombre) == Number(clave) ? true : false;
    }
    return false;
  }
  validaNumeroPedTrasladoExiste(numero: string) {
    const pedido = this.listPedTrasladoFilter.find(
      (ped) => ped.numeroPedido == numero
    );
    return pedido != undefined && pedido != null ? true : false;
  }
  validaDestinoExiste(nombre: string) {
    const destino = this.listPlantas.find(
      (b) => String(b.planta.toLowerCase()) == String(nombre).toLowerCase()
    );
    return destino != undefined && destino != null ? true : false;
  }
  validaPedTrasladoExiste(numeroPed: string) {
    const pedido = this.listPedTrasladoFilter.find(
      (ped) => ped.numeroPedido == numeroPed
    );
    return pedido != undefined && pedido != null ? true : false;
  }
  isValidExcelData = true;

  validationDataEcxel() {
    this.isValidExcelData = true;
    if (this.excelData.length === 0) {
      return;
    }
    const parseToNumber = (val: any) => {
      const num = Number(val);
      return isNaN(num) || !isFinite(num) ? NaN : num;
    };
    // Usa .some() para iterar y detenerse tan pronto como se encuentre un error (devuelve true)
    const foundError = this.excelData.some((element, index) => {
      const esVacio = (valor: any) =>
        valor === undefined || valor === null || this.utilsServc.isVacio(valor);
      const validations = [
        {
          field: 'Bodega',
          msg: 'La bodega es obligatoria',
          extraCheck: (val: any) =>
            !esVacio(val) && !this.validaBodegaExisteByNombre(val),
          extraMsg: 'La bodega no existe',
        },
        {
          field: 'Material',
          msg: 'El material es obligatorio',
          extraCheck: (val: any) =>
            !esVacio(val) && !this.validaMaterialExisteByClave(val),
          extraMsg: 'El material no existe',
        },
        {
          field: 'Fecha_Embarque',
          msg: 'La fecha de embarque es obligatoria',
        },
        {
          field: 'Numero_Boleta',
          extraCheck: (val: any) => !this.utilsServc.esNumero(val),
          msg: 'El número de boleta es obligatorio',
        },
        {
          field: 'Numero_Boleta',
          msg: 'El número de boleta es obligatorio',
        },
        {
          field: 'Peso_bruto',
          msg: 'El peso bruto es obligatorio',
          isNumeric: true,
          numericMsg: 'El peso bruto debe ser un valor numérico',
          isPositive: true,
          positiveMsg: 'El peso bruto no puede ser negativo',
        },
        {
          field: 'Peso_tara',
          msg: 'El peso tara es obligatorio',
          isNumeric: true,
          numericMsg: 'El peso tara debe ser un valor numérico',
          isPositive: true,
          positiveMsg: 'El peso tara no puede ser negativo',
        },
        {
          field: 'Humedad',
          msg: 'La humedad es obligatoria',
        },
        {
          field: 'Chofer',
          msg: 'El nombre del chofer es obligatorio',
        },
        {
          field: 'Placa_Jaula',
          msg: 'La placa/jaula es obligatoria',
        },
        {
          field: 'Linea_transportista',
          msg: 'La línea transportista es obligatoria',
        },
        {
          field: 'Destino',
          msg: 'Es destino es obligatoria',
        },
        {
          field: 'Destino',
          extraCheck: (val: any) => !this.validaDestinoExiste(val),
          msg: 'El destino no existe',
          extraMsg: 'El destino no existe',
        },
        {
          field: 'Pedido',
          msg: 'La pedido traslado es obligatoria',
        },
        {
          field: 'Pedido',
          extraCheck: (val: any) => !this.validaNumeroPedTrasladoExiste(val),
          extraMsg: 'El número pedido traslado no existe',
        },
      ];
      // Iterar sobre las validaciones para el elemento actual
      for (const validation of validations) {
        const value = element[validation.field];
        const rowInfo = ` (Fila ${index + 1} - Campo: ${validation.field})`;
        // Validación de campo vacío (obligatorio)
        if (esVacio(value)) {
          this.utilsServc.showMessageWarningInfo(`${validation.msg}${rowInfo}`);
          this.isValidExcelData = false;
          return true; // Detener la iteración de .some()
        }
        //Validación de tipo numérico (si la propiedad isNumeric existe)
        if (validation.isNumeric) {
          const numValue = parseToNumber(value);
          if (isNaN(numValue)) {
            this.utilsServc.showMessageWarningInfo(
              `${validation.numericMsg}${rowInfo}`
            );
            this.isValidExcelData = false;
            return true;
          }
          //Validación de no negatividad (si la propiedad isPositive existe)
          if (validation.isPositive && numValue < 0) {
            this.utilsServc.showMessageWarningInfo(
              `${validation.positiveMsg}${rowInfo}`
            );
            this.isValidExcelData = false;
            return true;
          }
        }
        // --- FIN: Nuevas validaciones ---
        // Validación de lógica de negocio adicional
        // Nota: Esta validación debe ir DESPUÉS de las validaciones de tipo si depende del valor.
        if (validation.extraCheck && validation.extraCheck(value)) {
          this.utilsServc.showMessageWarningInfo(
            `${validation.extraMsg}${rowInfo}`
          );
          this.isValidExcelData = false;
          return true;
        }
      }
      return false; // Continuar con la iteración de .some()
    });
    this.cdr.detectChanges();
  }

  validationDataEcxelSinEncabezado() {
    this.isValidExcelData = true;
    if (this.excelData.length === 0) {
      return;
    }
    const parseToNumber = (val: any) => {
      const num = Number(val);
      return isNaN(num) || !isFinite(num) ? NaN : num;
    };
    const esVacio = (valor: any) =>
      valor === undefined || valor === null || this.utilsServc.isVacio(valor);
    // Usa .some() para iterar y detenerse tan pronto como se encuentre un error (devuelve true)
    const foundError = this.excelData.some((element, index) => {
      // Crear un validador centralizado para simplificar las comprobaciones de null/undefined/vacío
      const esVacio = (valor: any) =>
        valor === undefined || valor === null || this.utilsServc.isVacio(valor);
      // Mapear los campos a validar con su mensaje de error y validación extra
      const validations = [
        {
          field: 'Bodega',
          msg: 'La bodega es obligatoria',
          extraCheck: (val: any) =>
            !esVacio(val) && !this.validaBodegaExiste(val),
          extraMsg: 'La bodega no existe',
        },
        {
          field: 'Material',
          msg: 'El material es obligatorio',
          extraCheck: (val: any) =>
            !esVacio(val) && !this.validaMaterialExiste(val),
          extraMsg: 'El material no existe',
        },
        {
          field: 'Fecha_Embarque',
          msg: 'La fecha de embarque es obligatoria',
        },
        {
          field: 'Numero_Boleta',
          extraCheck: (val: any) => !this.utilsServc.esNumero(val),
          msg: 'El número de boleta es obligatorio',
        },
        {
          field: 'Numero_Boleta',
          msg: 'El número de boleta es obligatorio',
        },
        {
          field: 'Peso_bruto',
          msg: 'El peso bruto es obligatorio',
          isNumeric: true,
          numericMsg: 'El peso bruto debe ser un valor numérico',
          isPositive: true,
          positiveMsg: 'El peso bruto no puede ser negativo',
        },
        {
          field: 'Peso_tara',
          msg: 'El peso tara es obligatorio',
          isNumeric: true,
          numericMsg: 'El peso tara debe ser un valor numérico',
          isPositive: true,
          positiveMsg: 'El peso tara no puede ser negativo',
        },
        {
          field: 'Humedad',
          msg: 'La humedad es obligatoria',
        },
        {
          field: 'Chofer',
          msg: 'El nombre del chofer es obligatorio',
        },
        {
          field: 'Placa_Jaula',
          msg: 'La placa/jaula es obligatoria',
        },
        {
          field: 'Linea_transportista',
          msg: 'La línea transportista es obligatoria',
        },
        {
          field: 'Destino',
          msg: 'Es destino es obligatoria',
        },
        {
          field: 'Destino',
          extraCheck: (val: any) => !this.validaDestinoExiste(val),
          msg: 'El destino no existe',
          extraMsg: 'El destino no existe',
        },
        {
          field: 'Pedido',
          msg: 'La pedido traslado es obligatoria',
        },
        {
          field: 'Pedido',
          extraCheck: (val: any) => !this.validaNumeroPedTrasladoExiste(val),
          extraMsg: 'El número pedido traslado no existe',
        },
      ];
      // Iterar sobre las validaciones para el elemento actual
      for (const validation of validations) {
        const value = element[validation.field];
        const rowInfo = ` (Fila ${index + 1} - Campo: ${validation.field})`;
        // Validación de campo vacío (obligatorio)
        if (esVacio(value)) {
          this.utilsServc.showMessageWarningInfo(`${validation.msg}${rowInfo}`);
          this.isValidExcelData = false;
          return true; // Detener la iteración de .some()
        }
        //Validación de tipo numérico (si la propiedad isNumeric existe)
        if (validation.isNumeric) {
          const numValue = parseToNumber(value);
          if (isNaN(numValue)) {
            this.utilsServc.showMessageWarningInfo(
              `${validation.numericMsg}${rowInfo}`
            );
            this.isValidExcelData = false;
            return true;
          }
          //Validación de no negatividad (si la propiedad isPositive existe)
          if (validation.isPositive && numValue < 0) {
            this.utilsServc.showMessageWarningInfo(
              `${validation.positiveMsg}${rowInfo}`
            );
            this.isValidExcelData = false;
            return true;
          }
        }
        // --- FIN: Nuevas validaciones ---
        // Validación de lógica de negocio adicional
        // Nota: Esta validación debe ir DESPUÉS de las validaciones de tipo si depende del valor.
        if (validation.extraCheck && validation.extraCheck(value)) {
          this.utilsServc.showMessageWarningInfo(
            `${validation.extraMsg}${rowInfo}`
          );
          this.isValidExcelData = false;
          return true;
        }
      }
      return false;
    });
    this.cdr.detectChanges();
  }

  refreshPedidos() {
    this.listaConfirmacionesDespachoFilter = this.listaConfirmacionesDespacho
      .map((confirm, i) => ({ id: i + 1, ...confirm }))
      .slice(
        (this.page - 1) * this.pageSize,
        (this.page - 1) * this.pageSize + this.pageSize
      );
    this.populateFormArray(this.listaConfirmacionesDespachoFilter);
  }

  printDataExcelInList() {
    let _emp_siloId =
      this.siloIdEmpleado != 0
        ? this.siloIdEmpleado
        : Number(this.formFilter.get('siloId')?.value ?? '0');
    let _claveSilo = this.getClaveSilo(_emp_siloId);
    if (this.excelData.length > 0) {
      this.validationDataEcxel();
      if (this.isValidExcelData) {
        this.excelData.forEach((element) => {
          this.items.push(
            this.fb.group({
              claveBodega: [
                element['Bodega'],
                [Validators.required, notZeroStringValidator()],
              ],
              claveSilo: [_claveSilo, [Validators.required]],
              claveMaterial: [
                {
                  value: this.getClaveMaterial(element['Material']),
                  disabled: true,
                },
                [Validators.required, notZeroStringValidator()],
              ],
              fechaEmbarque: [
                this.utilsServc.convertExcelDate(element['Fecha_Embarque']),
                [Validators.required, DateValidators.fechaNoFuturaValidator()],
              ],
              numBoleta: [element['Numero_Boleta'], [Validators.required]],
              pesoBruto: [element['Peso_bruto'], [Validators.required]],
              pesoTara: [
                element['Peso_tara'],
                [Validators.required, Validators.min(1)],
              ],
              humedad: [
                element['Humedad'],
                [Validators.required, Validators.pattern(/^[0-9]+$/)],
              ],
              chofer: [
                element['Chofer'],
                [
                  Validators.required,
                  Validators.pattern(/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]*$/),
                ],
              ],
              placaJaula: [element['Placa_Jaula'], [Validators.required]],
              lineaTransportista: [
                element['Linea_transportista'],
                [Validators.required],
              ],
              destinoId: [
                this.getDestinoIdByPlantaDestino(element['Destino']),
                [Validators.required, notZeroStringValidator()],
              ],
              numPedidoTraslado: [
                element['Pedido'],
                [Validators.required, notZeroStringValidator()],
              ],
              tipoMovimiento: [351, [Validators.required]],
              idConfDespacho: ['0'],
              folio: [''],
            })
          );
          this.updateAlllPlantasDestinoByFilter(
            this.items.length - 1,
            element['Destino']
          );
        });
        this.cdr.detectChanges();
      }
      this.isValidExcelData = true;
    }
  }

  activeBottons() {
    this.enableDowloadPd = false;
    this.enabledBtnEdit = false;
    this.cdr.detectChanges();
  }
  private async _updateSinSap(
    data: ConfirmacionDespachoRequest
  ): Promise<ConfDespachoTransactionResponse> {
    // firstValueFrom convierte el Observable en una Promise que se resuelve con la respuesta.
    return firstValueFrom(this.confDespachoServ.updateSinSap(data));
  }
  private async _callUpdateService(
    data: ConfirmacionDespachoRequest
  ): Promise<ConfDespachoTransactionResponse> {
    return firstValueFrom(this.confDespachoServ.updateSap(data));
  }
  async executeSequentialUpdatesSap(
    data: ConfirmacionDespachoRequest
  ): Promise<void> {
    const TYPE_OPERATION_CANCEL = '352'; //envio 352 para cancelar la confirmacion despacho con el peso neto
    const TYPE_OPERATION_ALTA = '351'; //envio 351 para dar de alta la confirmacion despacho con el peso neto
    try {
      const claveSilo = this.getClaveSiloById(this.siloIdEnvioDeleteSap);
      data.claveSilo = claveSilo ?? '';
      const data352 = { ...data, tipoMovimiento: TYPE_OPERATION_CANCEL };
      const responseFrist = await this._callUpdateService(data352);
      if (responseFrist?.code !== '0') {
        this.utilsServc.showErrorActualizacion();
        return;
      }
      // --- Petición 2: 351 (Solo si la P1 fue '0') ---
      const data351 = { ...data, tipoMovimiento: TYPE_OPERATION_ALTA };
      const responseSecond = await this._callUpdateService(data351);
      if (responseSecond?.code === '0') {
        this.utilsServc.showSuccessActualizacion();
      } else {
        this.utilsServc.showErrorActualizacion();
      }
    } catch (error) {
      console.log('error en actualizacion de confirmacion despacho: ' + JSON.stringify(error));
      this.utilsServc.showErrorActualizacion();
    }
  }

  async executeUpdateSinSapBd(
    data: ConfirmacionDespachoRequest
  ): Promise<void> {
    try {
      const response: ConfDespachoTransactionResponse =
        await this._updateSinSap(data);
      // El operador ?. es una forma limpia de verificar null/undefined
      if (response?.code === '0') {
        this.utilsServc.confirmarSuccess('Se actualizó con éxito');
      } else {
        this.utilsServc.showMessageError('Error al actualizar');
      }
    } catch (error) {
      console.log('ERROR AL ACTUALIZAR SIN SAP: ' + JSON.stringify(error));
      this.utilsServc.showErrorActualizacion();
    }
  }

  getIdPedTrasladoBy(clave: string) {
    const ped = this.listPedTrasladoFilter.find((p) => p.numeroPedido == clave);
    if (ped) return ped.pedidoTrasladoId;
    return -1;
  }

  async validatePesosToneladas(
    id: string,
    pesobruto: string,
    pesotara: string
  ): Promise<ConfDespachoTransactionResponse> {
    return new Promise((resolve, reject) => {
      this.confDespachoServ.validatePessos(id, pesobruto, pesotara).subscribe({
        next: resolve,
        error: reject,
      });
    });
  }

  cleanKgResponse2(text: string): string {
    if (!text) return text;
    return (
      text
        // Patrón para números con coma y ceros (40,000 -> 40)
        .replace(/(\d{1,3}),0{2,}(\d*)\s*(KG|kg)/gi, (match, p1, p2) => {
          return p2 ? `${p1}${p2}` : `${p1}`;
        })
        // Patrón para números con coma y dígitos (40,500 -> 40500)
        .replace(/(\d{1,3}),(\d{2,})\s*(KG|kg)/gi, '$1$2')
        // Patrón para números con punto y ceros (40.000 -> 40)
        .replace(/(\d{1,3})\.0{2,}(\d*)\s*(KG|kg)/gi, (match, p1, p2) => {
          return p2 ? `${p1}${p2}` : `${p1}`;
        })
        // Patrón para números con punto y dígitos (40.500 -> 40500)
        .replace(/(\d{1,3})\.(\d{2,})\s*(KG|kg)/gi, '$1$2')
        // Eliminar cualquier "KG" o "kg" restante
        .replace(/\s*(KG|kg)\s*/gi, ' ')
        // Limpiar espacios extras
        .replace(/\s+/g, ' ')
        .trim()
    );
  }

  async findTotalProgramArriboByPedTraslado(
    itemConfDespacho: ConfirmacionDespachoForm
  ): Promise<number> {
    try {
      const fechaInicio = this.formFilter.get('fechaI')?.value ?? '';
      const fechaFin = this.formFilter.get('fechaF')?.value ?? '';
      const claveDestino =
        this.getClaveDestinoById(Number(itemConfDespacho.destinoId)) ?? '';
      const total = await lastValueFrom(
        this.programArriboService.findTotalProgramArriboByNumPedTraslado(
          itemConfDespacho.numPedidoTraslado,
          itemConfDespacho.claveSilo,
          itemConfDespacho.claveMaterial,
          claveDestino,
          fechaInicio,
          fechaFin
        )
      );
      return total;
    } catch (error) {
      console.log(
        'ERROR EXTRACION TOTAL DE PROGRAMACION ARRIBO POR NUMERO DE PEDIDO TRASLADO: ' +
        JSON.stringify(error)
      );
      return -1;
    }
  }

  async validateFormDespacho(itemForm: FormGroup): Promise<string> {
    const f: ConfirmacionDespachoForm = itemForm.getRawValue();
    itemForm.markAllAsTouched();
    itemForm.markAsDirty();
    let pesobruto = f.pesoBruto;
    let pesotara = f.pesoTara;
    if (itemForm.invalid) {
      this.utilsServc.showMessageError(
        'Hay campos obligatorios o con formato inválido.'
      );
      return '-1';
    } else if (Number(pesobruto) - Number(pesotara) <= 0) {
      this.utilsServc.showMessageError(
        'El peso neto no puede ser negativo ni cero.'
      );
      return '-1';
    } else {
      const total = await this.findTotalProgramArriboByPedTraslado(f);
      if (total != -1) {
        const totaPesoneto = Number(f.pesoBruto) - Number(f.pesoTara);
        if (totaPesoneto > total) {
          this.utilsServc.showMessageError(
            'El peso neto es mayor al programado.'
          );
          return '-1';
        }
      }
    }
    return '0';
  }
  validateFormAndBindingData(
    itemForm: FormGroup
  ): ConfirmacionDespachoForm | null {
    const f: ConfirmacionDespachoForm = itemForm?.getRawValue();
    itemForm.markAllAsTouched();
    itemForm.markAsDirty();
    let pesobruto = f.pesoBruto;
    let pesotara = f.pesoTara;
    if (itemForm.invalid) {
      this.utilsServc.showMessageError(
        'Hay campos obligatorios o con formato inválido.'
      );
      return null;
    } else if (Number(pesobruto) - Number(pesotara) <= 0) {
      this.utilsServc.showMessageError(
        'El peso neto no puede ser negativo ni cero.'
      );
      return null;
    }
    return f;
  }
  async executeProcess(index: number) {
    const itemForm = this.items.at(index) as FormGroup;
    const resultValid = await this.validateFormDespacho(itemForm);
    if (resultValid != '0') {
      return;
    }
    const f: ConfirmacionDespachoRequest = itemForm.getRawValue();
    //const request:ConfirmacionDespachoRequest=f;
    const request: ConfirmacionDespachoRequest = { ...f };
    request.claveDestino = this.getClaveDestinoById(Number(f.destinoId)) ?? '';
    request.claveSilo =
      this.getClaveSiloById(
        Number(this.formFilter.get('siloId')?.value ?? '0')
      ) ?? '';
    request.idPedTraslado = this.getPedidoTrasladoIdByNumerTraslado(
      f.numPedidoTraslado
    );

    if (f.idConfDespacho != null && f.idConfDespacho != undefined && f.idConfDespacho != '0') {
      const response = await this.validatePesosToneladas(f.idConfDespacho.toString(), f.pesoBruto.toString(), f.pesoTara.toString());
      if (response.code == "0") {
        await this.executeUpdateSinSapBd(request);
      } else if (response.code == "-1") {
        this.executeSequentialUpdatesSap(request);
      }
    } else {
      this.guardarDespachov2(index);
    }
  }

  async resetMovimientoSapNuevoPesoNeto(index: number) {
    const itemForm = this.items.at(index) as FormGroup;
    const f = itemForm.getRawValue();
    itemForm.markAllAsTouched();
    itemForm.markAsDirty();
    let pesobruto = f.pesoBruto;
    let pesotara = f.pesoTara;
    let id = f.idConfDespacho;
    if (itemForm.invalid) {
      this.utilsServc.showMessageError(
        'Hay campos obligatorios o con formato inválido.'
      );
      return;
    }
    //this.validatePesosToneladas(id, pesobruto, pesotara, index);
  }

  convertResponseToRequest(
    response: ConfirmacionDespachoResponse
  ): ConfirmacionDespachoRequest {
    return ConfirmationDespachoMapper.responseToRequest(response);
  }

  // Ejemplo de uso: convertir Request a Response
  convertRequestToResponse(
    request: ConfirmacionDespachoRequest
  ): ConfirmacionDespachoResponse {
    return ConfirmationDespachoMapper.requestToResponse(request);
  }

  guardarDespachov2(index: number) {
    const itemForm = this.items.at(index) as FormGroup;
    itemForm.markAllAsTouched();
    itemForm.markAsDirty();
    const claveSilo = this.getClaveSiloById(this.siloIdEnvioDeleteSap);
    const filaData = this.buildConfirmacionDespachoRequest(index);
    const isPesoNetoNegativo = filaData.pesoBruto - filaData.pesoTara;
    filaData.claveSilo = claveSilo ?? '';
    this.confDespachoServ.save(filaData).subscribe({
      next: (response: ConfDespachoTransactionResponse) => {
        if (response != undefined && response != null) {
          if (response.code === 'S') {
            this.utilsServc.confirmarSuccess('Transacción exitosa');
            this.activeBottons();
            itemForm.get('idConfDespacho')?.setValue(response.id);
            itemForm.get('numeroSap')?.setValue(response.numeroSap);
            itemForm.get('folio')?.setValue(response.mensaje);
            filaData.idConfDespacho = response.id ?? '';
            this.listFolioReporte[index] = response.mensaje;
            this.isDownloading[index] = false;
            this.isIdCofingDespacho[index] = true;
            this.isModificable[index] = true;
            const pedTraslado = this.convertRequestToResponse(filaData);
            pedTraslado.claveBodega =
              String(this.getIdBodegaByClave(pedTraslado.claveBodega)) ?? '0';
            pedTraslado.claveMaterial =
              String(this.findMaterialIdByclaveV2(pedTraslado.claveMaterial)) ??
              '0';
            this.listaConfirmacionesDespacho.push(pedTraslado);
            this.createItemFromResponse(pedTraslado);
            this.collectionSize = this.listaConfirmacionesDespacho.length;
            this.refreshPedidos();
            this.cdr.detectChanges();
            this.items.at(index).patchValue(itemForm);
            this.cdr.detectChanges();
          } else {
            if (response.code != null && response.mensaje != null) {
              this.utilsServc.showMessageError(
                'Hubo un error en la transacción: ' +
                this.cleanKgResponse(response.mensaje)
              );
            } else {
              this.utilsServc.showMessageError(
                'Hubo una falla en la transacción de confirmación despacho'
              );
            }
          }
        }
      },
      error: (error) => {
        this.utilsServc.showErrorRegistro();
        console.log(
          'Error al crear la transaccion de confirmacion despacho: ' +
          JSON.stringify(error)
        );
      },
    });
  }

  guardarDespacho(index: number) {
    const itemForm = this.items.at(index) as FormGroup;
    itemForm.markAllAsTouched();
    itemForm.markAsDirty();
    if (itemForm.invalid) {
      this.utilsServc.showMessageError(
        'Hay campos obligatorios o con formato inválido.'
      );
    } else {
      const claveSilo = this.getClaveSiloById(this.siloIdEnvioDeleteSap);
      const filaData = this.buildConfirmacionDespachoRequest(index);
      const isPesoNetoNegativo = filaData.pesoBruto - filaData.pesoTara;
      //const claveSilo=this.getClaveSiloById(Number(this.formFilter.get("siloId")));
      filaData.claveSilo = claveSilo ?? '';
      if (isPesoNetoNegativo && isPesoNetoNegativo < 0) {
        this.utilsServc.showMessageError('El peso neto no puede ser negativo');
        return;
      }
      this.confDespachoServ.save(filaData).subscribe({
        next: (response: ConfDespachoTransactionResponse) => {
          if (response != undefined && response != null) {
            if (response.code === 'S') {
              this.utilsServc.confirmarSuccess('Transacción exitosa');
              this.activeBottons();
              itemForm.get('idConfDespacho')?.setValue(response.id);
              itemForm.get('numeroSap')?.setValue(response.numeroSap);
              itemForm.get('folio')?.setValue(response.mensaje);
              filaData.idConfDespacho = response.id ?? '';
              this.isDownloading[index] = false;
              this.isIdCofingDespacho[index] = true;
              this.isModificable[index] = true;
              this.cdr.detectChanges();
              this.items.at(index).patchValue(itemForm);
              this.cdr.detectChanges();
            } else {
              if (response.code != null && response.mensaje != null) {
                this.utilsServc.showMessageError(
                  'Hubo un error en la transacción: ' +
                  this.cleanKgResponse(response.mensaje)
                );
              } else {
                this.utilsServc.showMessageError(
                  'Hubo una falla en la transacción de confirmación despacho'
                );
              }
            }
          }
        },
        error: (error) => {
          this.utilsServc.showErrorRegistro();
          console.log(
            'Error al crear la transaccion de confirmacion despacho: ' +
            JSON.stringify(error)
          );
        },
      });
    }
  }

  cleanKgResponse(text: string): string {
    if (!text) return text;
    return (
      text
        // Eliminar "KG" después de números con comas o puntos
        .replace(/(\d{1,3}(?:[.,]\d{3})*)\s*KG/gi, '$1')
        // Eliminar puntos de miles
        .replace(/(\d{1,3})\.(\d{3})/g, '$1$2')
        // Eliminar comas de miles
        .replace(/(\d{1,3}),(\d{3})/g, '$1$2')
        // Espacio adicional entre número y código siguiente
        .replace(/(\d+)\s+([A-Z])/g, '$1 $2')
    );
  }

  async deleteConfirmacionDespachoSAP532(index: number) {
    const itemForm = this.items.at(index) as FormGroup;
    const claveSilo = this.getClaveSiloById(this.siloIdEnvioDeleteSap);
    const IdConfDespacho = itemForm.get('idConfDespacho')?.value;
    const filaData = this.buildConfirmacionDespachoRequestDDeleteSAP352(
      index,
      claveSilo ?? '',
      1
    );
    filaData.idConfDespacho = IdConfDespacho;
    const result = await this.utilsServc.confirmarEliminar();
    if (result.isConfirmed) {
      this.confDespachoServ.delete(filaData).subscribe({
        next: (response: ConfDespachoTransactionResponse) => {
          if (response != undefined && response != null) {
            if (response.code === 'S') {
              this.items.removeAt(index);
              this.listaConfirmacionesDespacho.splice(index, 1);
              this.collectionSize = this.listaConfirmacionesDespacho.length;
              this.refreshPedidos();
              this.cdr.detectChanges();
              this.utilsServc.showSuccesEliminacion();
            } else {
              console.log(JSON.stringify(response.mensaje));
              this.utilsServc.showMessageError(
                'Hubo un error en la eliminacion del registro: ' +
                this.cleanKgResponse(response.mensaje)
              );
            }
          }
        },
        error: (error) => {
          this.utilsServc.showErrorEliminacion();
          console.log(
            'Hubo un error en la eliminacion del registro: ' +
            JSON.stringify(error)
          );
        },
      });
    }
  }

  downloadPdf(index: number) {
    const f = (this.items.at(index) as FormGroup).getRawValue();
    if (f) {
      const idConfDespacho = f.idConfDespacho;
      const itemForm = this.items.at(index) as FormGroup;
      this.confDespachoServ.dowloadPdf(idConfDespacho).subscribe({
        next: (data: Blob) => {
          if (f.folio != undefined && f.folio != null) {
            if (f.folio.length > 0) {
              const folio = f.folio ?? 'reporte';
              this.utilsServc.buildPdf(folio, data);
            } else {
              const folio = this.listFolioReporte[index] ?? 'reporte';
              this.utilsServc.buildPdf(folio, data);
            }
          } else {
            this.utilsServc.buildPdf('reporte', data);
          }
        },
        error: (error) => {
          this.utilsServc.showMessageError(
            'Hubo un error al descargar el reporte.'
          );
          console.error('Error en la descarga del PDF:', JSON.stringify(error));
        },
      });
    }
  }

  async search() {
    if (this.formFilter.valid) {
      let siloId = this.getValueNumber('siloId');
      this.siloIdEnvioDeleteSap = siloId;
      let materialId = this.getValueNumber('materialId');
      let fechaInicio = this.getValue('fechaI');
      let fechaFin = this.getValue('fechaF');
      const _siloId = this.getValue('siloId');
      await this.findAllPlantas();
      await this.findAllPedidoTraslado(
        _siloId,
        materialId,
        fechaInicio,
        fechaFin
      );
      this.findAllConfirmacionesDespacho(
        siloId,
        materialId,
        fechaInicio,
        fechaFin
      );
    } else {
      this.utilsServc.markAllControlsAsTouched(this.formFilter);
    }
  }

  findAllConfirmacionesDespacho(
    silo: number,
    material: number,
    fechaInicio: string,
    fechaFin: string
  ) {
    this.confDespachoServ
      .findAllConfirmDespachos(silo, material, fechaInicio, fechaFin)
      .subscribe({
        next: (response: ConfirmacionDespachoResponse[]) => {
          if (
            response != null &&
            response != undefined &&
            response.length > 0
          ) {
            this.listaConfirmacionesDespacho = response;
            this.disabledBtnActions = false;
            this.populateFormArray(this.listaConfirmacionesDespacho);
            this.refreshPedidos();
            this.collectionSize = this.listaConfirmacionesDespacho.length;
            this.cdr.detectChanges();
          } else {
            this.listaConfirmacionesDespacho = [];
            this.collectionSize = this.listaConfirmacionesDespacho.length;
          }
        },
        error: (error) => {
          console.log(
            'DATA ERROR RESPONSE PROGRAM PED TRASLADO: ' + JSON.stringify(error)
          );
        },
      });
  }

  // Método para poblar el FormArray con los datos de la respuesta
  private populateFormArray(
    confirmaciones: ConfirmacionDespachoResponse[]
  ): void {
    // Limpiar el FormArray existente
    this.clearFormArray();
    // Agregar cada confirmación como un nuevo item en el FormArray
    confirmaciones.forEach((confirmacion, index) => {
      this.isIdCofingDespacho[index] = true;
      this.isDownloading[index] = false;
      this.isModificable[index] = true;
      this.addItemFromResponse(confirmacion, index);
    });
  }

  // Método para agregar un item basado en la respuesta
  private addItemFromResponse(
    confirmacion: ConfirmacionDespachoResponse,
    index: number
  ): void {
    const formGroup = this.createItemFromResponse(confirmacion);
    this.items.push(formGroup);
    this.isDownloading.push(true);
    this.updateFilteredPlantas(index);
    this.cdr.detectChanges();
  }

  // Método para crear el FormGroup desde la respuesta
  private createItemFromResponse(
    confirmacion: ConfirmacionDespachoResponse
  ): FormGroup {
    const formGroup = this.fb.group({
      claveBodega: [
        this.getClaveBodegaById(confirmacion.claveBodega),
        [Validators.required, notZeroStringValidator()],
      ],
      claveSilo: [confirmacion.claveSilo, [Validators.required]],
      claveMaterial: [
        {
          value: this.getClaveMaterialById(confirmacion.claveMaterial),
          disabled: true,
        },
        [Validators.required],
      ],
      fechaEmbarque: [
        confirmacion.fechaEmbarque,
        [Validators.required, DateValidators.fechaNoFuturaValidator()],
      ],
      numBoleta: [confirmacion.numBoleta || '', [Validators.required]],
      pesoBruto: [
        confirmacion.pesoBruto || 0,
        [
          Validators.required,
          Validators.min(0),
          Validators.pattern(/^\d+(\.\d+)?$/),
        ],
      ],
      pesoTara: [
        confirmacion.pesoTara || 0,
        [
          Validators.required,
          Validators.min(0),
          Validators.pattern(/^\d+(\.\d+)?$/),
        ],
      ],
      humedad: [
        confirmacion.humedad || '',
        [Validators.required, Validators.pattern(/^[+-]?(\d+\.?\d*|\.\d+)$/)],
      ],
      chofer: [confirmacion.chofer || '', [Validators.required]],
      placaJaula: [confirmacion.placaJaula || '', [Validators.required]],
      lineaTransportista: [
        confirmacion.lineaTransportista || '',
        [Validators.required],
      ],
      destinoId: [
        confirmacion.claveDestino || '0',
        [Validators.required, notZeroStringValidator()],
      ],
      numPedidoTraslado: [
        confirmacion.numPedidoTraslado?.toString() || '0',
        [Validators.required, notZeroStringValidator()],
      ],
      tipoMovimiento: [
        confirmacion.tipoMovimiento || 351,
        [Validators.required],
      ],
      idConfDespacho: [confirmacion.idconfDespacho || '0'],
      numeroSap: [confirmacion.numeroSap || ''],
      folio: [confirmacion.folio || ''],
    });
    formGroup.disable();
    return formGroup;
  }

  // MRETODOS PARA FILTRAR

  // Método público para obtener las plantas filtradas por índice
  getFilteredPlantas(index: number): any[] {
    return this.plantasFiltradasMap.get(index) || [];
  }

  // Método para actualizar la lista filtrada cuando cambian los datos
  private updateFilteredPlantas(index: number): void {
    const itemData = this.items.at(index).getRawValue();
    const claveDestino = this.getClaveDestinoById(Number(itemData.destinoId));
    const filtered = this.listPedTraslado.filter(
      (pedido) => pedido.plantaDestino == claveDestino
    );
    this.plantasFiltradasMap.set(index, filtered);
  }

  private updateAlllPlantasDestino(index: number): void {
    const filtered = this.listPedTraslado;
    this.plantasFiltradasMap.set(index, filtered);
  }

  private updateAlllPlantasDestinoByFilter(
    index: number,
    claveDestino: string
  ): void {
    if (claveDestino == '') {
      const filtered = this.listPedTraslado;
      console.log("PEDIDO TRASLADO: " + JSON.stringify(filtered));
      this.plantasFiltradasMap.set(index, filtered);
    } else {
      const filtered = this.listPedTraslado.filter(
        (pedido) => pedido.plantaDestino == claveDestino
      );
      this.plantasFiltradasMap.set(index, filtered);
    }
  }

  private shouldIncludePlanta(planta: any, itemData: any): boolean {
    return planta.plantaDestino === itemData.claveBodega;
  }

  private isMaterialSupported(planta: any, materialId: string): boolean {
    if (!planta.supportedMaterials || planta.supportedMaterials.length === 0) {
      return true;
    }
    return planta.supportedMaterials.includes(materialId);
  }

  onFieldChange(index: number): void {
    this.updateFilteredPlantas(index);
  }

  // Método para limpiar el FormArray
  private clearFormArray(): void {
    while (this.items.length !== 0) {
      this.items.removeAt(0);
      this.isDownloading.pop();
      this.isIdCofingDespacho.pop();
    }
  }

  //metodos de consultas de catalogos
  filterPedidoTraslado(clave: string) {
    this.listPedTrasladoFilter = this.listPedTraslado.filter(
      (item) => (item.plantaDestino = clave)
    );
  }

  async findAllPedidoTraslado(
    siloId: string,
    materialiId: number,
    fechaI: string,
    fechaF: string
  ): Promise<void> {
    this.initFormConfDespacho();
    try {
      //se traen los pedido traslado que se se han programado por fecha de acuerdo al silo y material
      const response = await firstValueFrom(
        this.pedidoTrasladoService.findAllPedTraladoByConfDespacho(
          Number(siloId),
          materialiId,
          fechaI,
          fechaF
        )
      );
      if (response && response.length > 0) {
        this.listPedTraslado = response;
        this.listPedTrasladoFilter = response;
        if (this.items.length === 0) {
          this.addItem();
        }
        this.disabledBtnActions = false;
      } else {
        this.listPedTraslado = [];
        this.listPedTrasladoFilter = [];
        this.disabledBtnActions = true;
      }
      this.cdr.detectChanges();
    } catch (error) {
      console.log(
        'DATA ERROR RESPONSE PROGRAM PED TRASLADO: ' + JSON.stringify(error)
      );
    }
  }
  onSeleccionar(event: Event) {
    const idSeleccionado = (event.target as HTMLSelectElement).value;
    this.findAllBodegas(Number(idSeleccionado));
  }

  onSeleccionarPlantaDestino(event: Event, index: number) {
    const idSeleccionado = (event.target as HTMLSelectElement).value;
    const claveDestino = this.getClaveDestinoById(Number(idSeleccionado));
    this.updateAlllPlantasDestinoByFilter(index, claveDestino ?? '');
    const itemData = this.items
      .at(index)
      ?.get('numPedidoTraslado')
      ?.setValue('0');
    this.cdr.detectChanges();
  }

  onSeleccionarMaterial(event: Event) {
    const idSeleccionado = (event.target as HTMLSelectElement).value;
    if (idSeleccionado != '0') {
      this.items.controls.forEach((itemControl: AbstractControl) => {
        const itemGroup = itemControl as FormGroup;
        const campoB = itemGroup.get('claveMaterial');
        if (campoB) {
          let nomClave = this.findMaterialIdByclave(
            this.getValueNumber('materialId')
          );
          campoB.setValue(nomClave);
        }
      });
    } else {
      this.items.controls.forEach((itemControl: AbstractControl) => {
        const itemGroup = itemControl as FormGroup;
        const campoB = itemGroup.get('claveMaterial');
        if (campoB) {
          campoB.disable();
        }
      });
    }
  }

  findPedidoTraslado() {
    let siloId = this.getValue('siloId');
    let materialId = this.getValue('materialId');
    this.programArriboService
      .findPedidosTralados(siloId, '1', materialId)
      .subscribe({
        next: (response: ProgramPedTrasladoResponse[]) => {
          if (
            response != null &&
            response != undefined &&
            response.length > 0
          ) {
            this.listProgramPedTraslado = response;
            this.cdr.detectChanges();
          } else {
            this.listProgramPedTraslado = [];
            this.cdr.detectChanges();
          }
        },
        error: (error) => {
          console.log(
            'DATA ERROR RESPONSE PROGRAM PED TRASLADO: ' + JSON.stringify(error)
          );
          this.utilsServc.showMessageError('Hubo un error en el sistema');
        },
      });
  }
  async findAllSilos(): Promise<SiloResponse[] | null> {
    try {
      const response: SiloResponse[] = await lastValueFrom(
        this.siloService.getSilos()
      );

      if (response != null && response !== undefined) {
        this.listSilo = response;
        this.cdr.detectChanges();
        return response;
      }

      return null;
    } catch (error) {
      console.log('ERROR DATA SILO: ' + JSON.stringify(error));
      this.utilsServc.showMessageError('Hubo un error en el sistema');
      return null;
    }
  }
  findAllBodegas(siloId: number) {
    this.bodegaService.findAllBySilo(siloId).subscribe({
      next: (response: BodegaResponse[]) => {
        this.listaBodega = response;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.log('ERROR DATA FINDALL BODEGA', JSON.stringify(error));
        this.utilsServc.showMessageError('Hubo un error en el sistema');
      },
    });
  }
  findAllMaterial() {
    this.materialService.findAll().subscribe({
      next: (response: MaterialResponse[]) => {
        if (response != null && response != undefined) {
          this.listMateriales = response;
          this.cdr.detectChanges();
        }
      },
      error: (error) => {
        console.log('ERROR DATA SILO: ' + JSON.stringify(error));
      },
    });
  }
  async findAllPlantas(): Promise<void> {
    try {
      const fechaInicio = this.formFilter.get('fechaI')?.value ?? '';
      const fechaFin = this.formFilter.get('fechaF')?.value ?? '';
      const response: PlantaResponse[] = await lastValueFrom(
        this.plantaService.findAllPlantasByProgramArribo(fechaInicio, fechaFin)
      );
      if (response != null && response !== undefined) {
        this.listPlantas = response;
        this.cdr.detectChanges();
      }
    } catch (error) {
      console.log('ERROR DATA PLANTAS: ' + JSON.stringify(error));
    }
  }

  enableFormItem(index: number) {
    const itemForm = this.items.at(index) as FormGroup;
    this.isModificable[index] = !this.isModificable[index];
    if (this.isModificable[index] == false) {
      this.enableFormItemDespacho(itemForm);
    } else {
      itemForm.disable();
    }
    this.cdr.detectChanges();
  }

  enableFormItemDespacho(itemForm: FormGroup) {
    itemForm.enable();
    itemForm.get('claveMaterial')?.disable();
  }

  enableFormConfDespacho(index: number) {
    const itemForm = this.items.at(index) as FormGroup;
    const controlesADeshabilitar = [
      'numBfechaEmbarqueoleta',
      'numBoleta',
      'pesoBruto',
      'pesoTara',
      'humedad',
      'chofer',
      'placaJaula',
      'lineaTransportista',
    ];
    controlesADeshabilitar.forEach((nombre) => {
      itemForm.get(nombre)?.disable();
    });
  }
  getControl(key: string): FormControl {
    return this.formFilter.get(key) as FormControl;
  }
  getValue(key: string): string {
    return (this.formFilter.get(key) as FormControl).value;
  }
  getValueNumber(key: string): number {
    return (this.formFilter.get(key) as FormControl).value;
  }
  getIdDespacho(index: number): boolean {
    const itemForm = this.items.at(index) as FormGroup;
    const f = itemForm.getRawValue();
    if (
      f.idConfDespacho != null &&
      f.idConfDespacho != undefined &&
      f.idConfDespacho != '0'
    ) {
      return true;
    } else {
      return false;
    }
  }
}
