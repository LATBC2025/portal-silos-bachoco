import { CommonModule, DecimalPipe } from '@angular/common';
import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { RouterModule } from '@angular/router';
import { NgbPaginationModule, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderComponent } from '../../shared/header/header.component';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import * as XLSX from 'xlsx';
import { SiloResponse } from '../../models/catalogs/silo/Silo.Response';
import { SiloServiceService } from '../../services/catalog/silo-service.service';
import { MaterialServiceService } from '../../services/catalog/material-service.service';
import { PlantaServiceService } from '../../services/catalog/planta-service.service';
import { PlantaResponse } from '../../models/catalogs/planta/Planta.Response';
import { PedidoTrasladoService } from '../../services/pedidoTraslado/pedido-traslado.service';
import { PedidoTrasladoArriboResponse } from '../../models/peido-traslado/PedidoTrasladoArribo.response';
import { MaterialResponse } from '../../models/catalogs/Material/Material.Response';
import { ProgramArriboService } from '../../services/program-arribo/program-arribo.service';
import { ProgramPedTrasladoResponse } from '../../models/programacion-arribo/Program.Pedido.Traslado';
import Swal, { SweetAlertResult } from 'sweetalert2';
import { ProgramArriboRequest } from '../../models/programacion-arribo/Program.Arribo.Request';
import { P } from '@angular/cdk/platform.d-B3vREl3q';
import { notZeroStringValidator } from '../../services/shared/validators/not-zero-string.validator';
import { UtilsService } from '../../services/shared/utils.service';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRadioModule } from '@angular/material/radio';
import { ItemFormulario } from '../../models/form-arribo/ItemForm.Arribo';
import { ConfirmDespachoService } from '../../services/confirmacionDespacho/confirm-despacho.service';
import { DowloadDataService } from '../../services/shared/dowload-data.service';
import { firstValueFrom, lastValueFrom } from 'rxjs';
import { PedidoCompraService } from '../../services/pedido/pedido-compra.service';
import { PedidoCompraResponse } from '../../models/PedidoCompraResponse';
import { NumberFormatPipe } from '../../utils/formats/number-format.pipe';
import { EmpleadoExternoService } from '../../services/emppleado-externo/empleado-externo.service';
import { EmpleadoExternoResponseDTO } from '../../models/catalogs/Empleado-externo/Empleado.Response.DTO';
import { distinctUntilChanged } from 'rxjs/operators';


@Component({
	selector: 'app-programacion-arribo',
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
		MatRadioModule,
		MatCheckboxModule,
		NumberFormatPipe
	],
	templateUrl: './programacion-arribo.component.html',
	styleUrl: './programacion-arribo.component.css'
})
export class ProgramacionArriboComponent implements OnInit {

	hide = true;
	pagePedTraslado = 1;
	pageSizePedTraslado = 5;
	page = 1;
	pageSize = 5;
	collectionSizePedTraslado = 0;
	formArriboFilter!: FormGroup;
	formProgramAribo!: FormGroup;
	//LISTAS DE CATALOGO
	listSilo: SiloResponse[] = [];
	listMateriales: MaterialResponse[] = [];
	listPlantas: PlantaResponse[] = [];
	listPedidosTraslado: PedidoTrasladoArriboResponse[] = [];
	listProgramPedTraslado: ProgramPedTrasladoResponse[] = [];
	listProgramPedTrasladoFilter: ProgramPedTrasladoResponse[] = [];
	listaDeObjetosProgram: ItemFormulario[] = [];
	excelData: any[] = [];
	excelHeaders: string[] = [];
	private allItems: any[] = [];
	paginatedItems: any[] = [];
	visibleItems: FormGroup[] = [];
	//stock
	stock: number = 0;
	stockSilo: string = "0";
	promedioArriboCamiones = 0;
	materialSeleccionado!: number;
	actionsBottonsDisabled: boolean = true;
	filaSeleccionada: ProgramPedTrasladoResponse | null = null;
	private datosCompartidosService = inject(DowloadDataService);

  listProveedores: EmpleadoExternoResponseDTO[] = [];
  private lastSiloIdLoaded = 0;


	private _collectionSizeArribo = 0;
	get collectionSizeArribo(): number {
		return this._collectionSizeArribo;
	}

	set collectionSizeArribo(value: number) {
		this._collectionSizeArribo = value;
		this.cdr.markForCheck();
	}

	constructor(private library: FaIconLibrary, private siloService: SiloServiceService,
		private materialService: MaterialServiceService,
		private plantaService: PlantaServiceService,
		private pedidoCompraService: PedidoCompraService,
		private pedidoTrasladoService: PedidoTrasladoService,
		private programArriboServ: ProgramArriboService,
		private confDespacho: ConfirmDespachoService,
		private utilServ: UtilsService,
		private fb: FormBuilder,
    private empleadoExternoService: EmpleadoExternoService, //  nuevo
		private cdr: ChangeDetectorRef) {
		this.library.addIconPacks(fas);
	}

	ngOnInit(): void {
		this.initForm();
		this.initFormArribo();
		this.findAllSilos();
		this.findAllMaterial();
		this.findAllPlantas();
		this.findPromedioArribo();
	}

	getTodayAsString(): string {
		const hoy = new Date();
		return hoy.toISOString().substring(0, 10);
	}
	initForm() {
		this.formArriboFilter = new FormGroup({
			silo: new FormControl(0, [Validators.required, notZeroStringValidator()]),
			planta: new FormControl(0, [Validators.required, notZeroStringValidator()]),
			material: new FormControl(0, [Validators.required, notZeroStringValidator()]),
          proveedor: new FormControl(0, [Validators.required, notZeroStringValidator()]), //Campo nuevo para filtrar proveedores por silo

		});

  this.listenSiloChangeLoadProveedores(); // llamado para cargar proveedores

	}

	initFormArribo() {
		this.formProgramAribo = this.fb.group({
			items: this.fb.array([])
		});
		this.collectionSizeArribo = this.items.length;
	}
	onFileChangeWithEncabezados(event: any): void {
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
	}

	onFileChangeNoEncebzados(event: any): void {
		const file = event.target.files[0];
		if (!file) return;
		const reader = new FileReader();
		reader.onload = (e: any) => {
			const workbook = XLSX.read(e.target.result, { type: 'array' });
			const sheet = workbook.Sheets[workbook.SheetNames[0]];
			// Leer como array sin encabezados
			const dataArray: any[][] = XLSX.utils.sheet_to_json(sheet, {
				header: 1,
				defval: ''
			});
			//se toma las columnas de cada fila del excel
			this.excelData = dataArray.map(row => {
				const obj: any = {};
				row.forEach((cell, index) => {
					obj[`col-${index}`] = cell;
				});
				return obj;
			});
			this.printDataExcelInList();
			event.target.value = null;
		};
		reader.readAsArrayBuffer(file);
	}

	validaMaterial(nombre: string): boolean {
		const material = this.listMateriales.find(m => m.nombre == nombre);
		return material != undefined && material != null ? true : false;
	}
	validaMaterialSeleccionado(nombre: string): boolean {
		const idMaterialSeleccionadoForm = this.formArriboFilter.get("material")?.value;
		const material = this.listMateriales.find(m => m.id == idMaterialSeleccionadoForm);
		if (material) {
			return material.nombre == nombre ? true : false
		}
		return false;
	}

	printDataExcelInList() {
		let isValid = true;
		if (this.excelData.length > 0) {
			this.excelData.forEach((element) => {//,
				if (element["col-0"] == undefined || element["col-0"] == null) {
					this.utilServ.showMessageWarningInfo("El campo tonelada es obligatorio.");
					isValid = false;
				} else if (!this.utilServ.esNumero(element["col-0"])) {
					isValid = false;
					this.utilServ.showMessageWarningInfo("El campo tonelada debe ser numérico.");
				} else if (Number(element["col-0"]) <= 0) {
					isValid = false;
					this.utilServ.showMessageWarningInfo("El campo tonelada debe ser mayor a cero.");
				} else if (element["col-1"] == undefined || element["col-1"] == null) {
					isValid = false;
					this.utilServ.showMessageWarningInfo("El campo material es obligatorio.");
				} else if (!this.validaMaterialSeleccionado(element["col-1"])) {
					isValid = false;
					this.utilServ.showMessageWarningInfo("El campo material del Excel no coincide con el material seleccionado.");
				} else if (this.utilServ.esFechaValidaNoAnterior(this.utilServ.excelSerialToDate(element["col-2"]))) {
					isValid = false;
					this.utilServ.showMessageWarningInfo("Fecha inválida, no se permiten fechas anteriores a la actual.");
				}
			});
			if (isValid) {
				this.clearItems();
				this.excelData.forEach((element) => {//,
					this.items.push(this.fb.group({
						tonelada: [element["col-0"], [Validators.required, Validators.min(1)]],
						material: [{ value: this.materialSeleccionado, disabled: true }, [Validators.required]],
						fecha: [this.utilServ.excelSerialToDate(element["col-2"]), [Validators.required]]
					}));
				});
				this.collectionSizeArribo = this.items.length;
				this.updateVisibleItems();
				this.cdr.detectChanges();
			}
		}
	}

	clearItems(): void {
		this.items.clear();
		this.visibleItems=[];
	}

	refreshPedidos() {
		this.listProgramPedTrasladoFilter = this.listProgramPedTraslado.map((arribo, i) => ({ id: i + 1, ...arribo })).slice(
			(this.pagePedTraslado - 1) * this.pageSizePedTraslado,
			(this.pagePedTraslado - 1) * this.pageSizePedTraslado + this.pageSizePedTraslado,
		);
	}

	private createItem(): FormGroup {
		return this.fb.group({
			tonelada: [0, [Validators.required, Validators.min(1)]],
			material: [{ value: this.materialSeleccionado, disabled: true }, [Validators.required]],
			fecha: [this.getTodayAsString(), [Validators.required]]
		});
	}

	// Getter para acceder fácil al FormArray
	get items(): FormArray {
		return this.formProgramAribo.get('items') as FormArray;
	}

	// Agregar fila
	addItem() {
		const newFormGroup = this.createItem();
		this.items.push(newFormGroup);
		this.collectionSizeArribo = this.items.length;
		const totalPages = Math.ceil(this.collectionSizeArribo / this.pageSize);
		if (this.page === totalPages) {
			this.updateVisibleItems();
		}

		this.cdr.detectChanges();
	}

	// Eliminar por índice
	removeItemFromArray(index: number) {
		const globalIndex = (this.page - 1) * this.pageSize + index;
		this.items.removeAt(globalIndex);
		this.collectionSizeArribo = this.items.length;
		this.updateVisibleItems();
		this.adjustCurrentPage();
		this.cdr.detectChanges();
	}

	private adjustCurrentPage(): void {
		const totalPages = Math.ceil(this.collectionSizeArribo / this.pageSize);

		if (this.page > totalPages && totalPages > 0) {
			this.page = totalPages;
			this.updateVisibleItems();
		}
		if (this.visibleItems.length === 0 && this.collectionSizeArribo > 0) {
			if (totalPages > 0) {
				this.page = totalPages;
				this.updateVisibleItems();
			}
		}
	}

	private updateVisibleItems(): void {
		const startIndex = (this.page - 1) * this.pageSize;
		const endIndex = Math.min(startIndex + this.pageSize, this.items.length);
		this.visibleItems = [];
		for (let i = startIndex; i < endIndex; i++) {
			this.visibleItems.push(this.items.at(i) as FormGroup);
		}
	}
 //Método para cambiar proveedores al cambiar silo
private listenSiloChangeLoadProveedores(): void {
  const siloCtrl = this.formArriboFilter.get('silo');
  if (!siloCtrl) return;

  siloCtrl.valueChanges.subscribe(val => {
    const siloId = Number(val ?? 0);

    // ✅ si NO cambió el silo, NO hagas nada (evita reset al buscar)
    if (siloId === this.lastSiloIdLoaded) return;

    // ✅ actualiza el "último silo"
    this.lastSiloIdLoaded = siloId;

    // ✅ reset proveedor SOLO cuando realmente cambió silo
    this.formArriboFilter.patchValue({ proveedor: 0 }, { emitEvent: false });

    // limpiar lista solo en cambio real
    this.listProveedores = [];

    if (!siloId || siloId === 0) return;

    this.empleadoExternoService.findAllBySilo(siloId).subscribe({
      next: (resp) => {
        this.listProveedores = resp ?? [];
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.listProveedores = [];
        this.utilServ.showMessageError("Hubo un error al cargar proveedores");
        console.log("ERROR DATA PROVEEDORES: " + JSON.stringify(error));
      }
    });
  });
}

	onPageChange(): void {
		this.updateVisibleItems();
		this.cdr.detectChanges();
	}

	onPageSizeChange(): void {
		this.page = 1; // Volver a página 1
		this.updateVisibleItems();
		this.cdr.detectChanges();
	}

	getAllData(): any[] {
		return this.allItems.map(formGroup => formGroup.value);
	}

	saveAllData(): void {
		this.allItems.map(formGroup => formGroup.value);
	}



	control(key: string): FormControl {
		return this.formArriboFilter.get(key) as FormControl;
	}

	getValueNumber(key: string): number {
		return ((this.formArriboFilter.get(key) as FormControl).value) as number;
	}

	getValue(key: string): string {
		return (this.formArriboFilter.get(key) as FormControl).value;
	}

	getClaveSiloById(id: number) {
		const silo = this.listSilo.find(s => s.id == id);
		return silo?.silo ?? "";
	}
	getPlantaDestinoById(id: number) {
		const planta = this.listPlantas.find(s => s.id == id);
		return planta?.planta ?? "";
	}
	findClaveMaterialById(id: number) {
		const material = this.listMateriales.find(m => m.id == id);
		return material?.nombre ?? "";
	}
	findClavePlantaById(id: number): string {
		const result = this.listPlantas.find(planta => planta.id == id);
		if (result) {
			return result.planta;
		}
		return "";
	}
	findAllSilos() {
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
		});
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
	findAllPlantas() {
		this.plantaService.findAll().subscribe({
			next: (response: PlantaResponse[]) => {
				if (response != null && response != undefined) {
					this.listPlantas = response;
					this.cdr.detectChanges();
				}
			}, error: (error) => {
				console.log("ERROR DATA SILO: " + JSON.stringify(error));
			}
		})
	}

	async findPromedioArribo(): Promise<void> {
		const siloId = this.getValueNumber('silo');
		try {
			const promedioPromise = lastValueFrom(
				this.confDespacho.findCantidadPromedioArribo(siloId)
			);
			const response: number = await promedioPromise;
			if (response) {
				this.promedioArriboCamiones = response;
				this.cdr.detectChanges();
			}
		} catch (err) {
			console.error("Error al buscar el promedio de carga de camiones por silo:", err);
		}
	}

	sumaCantidadPedTraslado() {
		return this.listProgramPedTraslado.reduce((acum, p) => acum + Number(p.cantidad), 0);
	}

	async findStock(): Promise<void> {

  const proveedorSeleccionado = this.formArriboFilter.get('proveedor')?.value; // 👈 guarda

		this.utilServ.markAllControlsAsTouched(this.formArriboFilter);
		if (!this.formArriboFilter.valid) {
			return;
		}
		const formValues = this.formArriboFilter.value;
		this.actionsBottonsDisabled = false;
		this.materialSeleccionado = formValues.material;
		const siloId = this.getValueNumber('silo');
		const materialId = this.getValueNumber('material');
		const plantaId = this.getValueNumber('planta');
		const claveSilo = this.getClaveSiloById(siloId) ?? "";
		const claveDestino = this.getPlantaDestinoById(plantaId) ?? "";
		//Conversión de Observable a Promesa y uso de async/await
		try {
			const claveMaterial: string = this.findClaveMaterialById(materialId);
			await this.savePedCompraBySap();
			const resultPedTraslado = await this.savePedTrasladoBySap();
			if (resultPedTraslado != "success") {
				this.utilServ.showMessageError(resultPedTraslado);
				return;
			}
			const stockObservable = this.programArriboServ.findStock(claveSilo, claveMaterial);
			const response: string = await lastValueFrom(stockObservable);
			if (response != null && response != undefined && response != "0") {
				this.stockSilo = response;
				await this.findAllPedidoTraslado(siloId.toString(),
					this.findClavePlantaById(plantaId), materialId.toString());
			} else {
				this.listProgramPedTraslado = [];
				this.listProgramPedTrasladoFilter = [];
				this.collectionSizePedTraslado = 0;
				this.clearItems();
				this.utilServ.showMessageWarningInfo("El stock del silo es 0.");
				//this.utilServ.showMessageWarningInfoNoExisteRegistrosFilters();
				this.stockSilo = '0';
			}
		} catch (error) {
			this.listProgramPedTraslado = [];
			this.listProgramPedTrasladoFilter = [];
			this.collectionSizePedTraslado = 0;
			this.clearItems();
			this.utilServ.showMessageError("Hubo un error en la carga del stock");
			console.error("ERROR AL EXTRAER STOCK DE SILO:", error);
		} finally {
          // restaura (si no quieres que se pierda por rerender o recarga de lista)

          this.formArriboFilter.patchValue({ proveedor: proveedorSeleccionado }, { emitEvent: false });

			this.cdr.detectChanges();
		}
	}

	async savePedCompraBySap(): Promise<void> {
		if (this.formArriboFilter.valid) {
			const claveMaterial = this.findClaveMaterialById(this.getValueNumber("material"));
			const claveSilo = this.getClaveSiloById(this.getValueNumber("silo"));

const proveedorId = this.getProveedorId();
const proveedor = this.listProveedores.find(p => p.id === proveedorId);
const sapVendor = (proveedor as any)?.sapVendor ?? ""; // si existe en el DTO


			const plantaId = this.getValueNumber('planta');
			const claveDestino = this.getPlantaDestinoById(plantaId) ?? "";
			try {
				await firstValueFrom(
					this.pedidoCompraService.savePedCompraDowloadBySap(
						claveSilo,
						claveMaterial,
						claveDestino,
						sapVendor,
						""
					)
				);
			} catch (error) {
				this.listProgramPedTraslado = [];
				this.clearItems();
				this.listProgramPedTrasladoFilter = [];
				console.error("ERROR EJECUTA SAVE PED COMPRA EN PROGRAM ARRIBO: ", error);
			}
		}
	}

  getProveedorId(): number {
  return Number(this.formArriboFilter.get('proveedor')?.value ?? 0);
}
	async savePedTrasladoBySap(): Promise<string> {
		if (!this.formArriboFilter.valid) {
			return "El formulario no es válido";
		}
		const claveMaterial = this.findClaveMaterialById(this.getValueNumber("material"));
		const claveSilo = this.getClaveSiloById(this.getValueNumber("silo"));
		const plantaDestino = this.getPlantaDestinoById(this.getValueNumber("planta"));

    const proveedorId = this.getProveedorId();
    const proveedor = this.listProveedores.find(p => p.id === proveedorId);
    const sapVendor = (proveedor as any)?.sapVendor ?? "";
		try {
			await firstValueFrom(
				this.pedidoTrasladoService.savePedTrasladoBySap(
					claveSilo,
					claveMaterial,
					plantaDestino,
					sapVendor,
					""
				)
			);
			return "success"; // O cualquier mensaje de éxito
		} catch (error) {
			const errorConPropiedad = error as any;
			if (error) {
				const errorHttp = errorConPropiedad["error"];

				if (errorHttp["error"] == "error-code:NO-EXISTE-PLANTAS") {
					const errorMessage = "Hubo un error al descargar la información de pedido traslado; no existen las plantas: " + errorHttp["message"];
					this.utilServ.showMessageError(errorMessage);
					return errorMessage;
				} else if (errorHttp["error"] == "error-code:NO-EXISTE-MATERIALES") {
					const errorMessage = "Hubo un error al descargar la información de pedido traslado; no existen los materiales: " + errorHttp["message"];
					this.utilServ.showMessageError(errorMessage);
					return errorMessage;
				} else if (errorHttp["error"] == "error-code:NO-EXISTE-PED-COMPRA") {
					const errorMessage = "Hubo un error al descargar la información de pedido traslado; no existen los pedidos de compra asociados. " + errorHttp["message"];
					this.utilServ.showMessageError(errorMessage);
					return errorMessage;
				}
			}
			this.listProgramPedTraslado = [];
			this.listProgramPedTrasladoFilter = [];
			this.pageSizePedTraslado = 0;
			this.clearItems();
			const errorMessage = "Error desconocido al procesar el pedido de traslado";
			this.utilServ.showMessageError(errorMessage);
			return errorMessage;
		}
	}

	//extracion de pedidos traslado para programar arribos
	async findAllPedidoTraslado(claveSilo: string, clavePlanta: string, claveMaterial: string): Promise<void> {
		const pedidoTrasladoPromise = lastValueFrom(
			this.programArriboServ.findPedidosTralados(claveSilo, clavePlanta, claveMaterial)
		);
		try {
			const response: ProgramPedTrasladoResponse[] = await pedidoTrasladoPromise;
			if (response?.length > 0) {
				this.listProgramPedTraslado = response;
				this.listProgramPedTrasladoFilter = response;
				this.collectionSizePedTraslado = this.listProgramPedTraslado.length;
				this.refreshPedidos();
				this.clearItems();
				this.cdr.detectChanges();
			} else {
				this.listProgramPedTraslado = [];
				this.listProgramPedTrasladoFilter = [];
				this.collectionSizePedTraslado = 0;
				this.refreshPedidos()
				this.clearItems();
				this.cdr.detectChanges();
				//this.utilServ.showMessageWarningInfoNoExisteRegistrosFilters();
        //this.utilServ.showMessageWarningInfo("No se encontró información con los filtros seleccionados.");
        await Swal.fire({
          icon: 'warning',
          title: 'Sin resultados',
          text: 'No se encontró información con los filtros seleccionados.',
          confirmButtonText: 'Aceptar'
        });

			}
		} catch (error) {
			this.listProgramPedTraslado = [];
			this.listProgramPedTrasladoFilter = [];
			this.collectionSizePedTraslado = 0;
			this.clearItems();
			this.refreshPedidos()
			this.cdr.detectChanges();
			// Manejar el error (equivalente al bloque 'error' del subscribe)
			console.error("DATA ERROR RESPONSE PROGRAM PED TRASLADO:", error); // Usar console.error es mejor práctica
		}
	}

	seleccionarFila(pedido: ProgramPedTrasladoResponse) {
		// Desactivar todos los demás checkboxes
		this.listProgramPedTrasladoFilter.forEach(p => {
			if (p !== pedido) {
				p.activo = false;
			}
		});
		// Actualizar el rastreador de la fila seleccionada
		if (pedido.activo) {
			this.filaSeleccionada = pedido;
		} else {
			this.filaSeleccionada = null;
		}
	}
	private crearItemFormGroup(tonelada: number = 0, material: number = 0, fecha: string = ''): FormGroup {
		return this.fb.group({
			tonelada: [tonelada, [Validators.required, Validators.min(1)]],
			material: [material, Validators.required],
			fecha: [fecha, Validators.required],
		});
	}
	public agregarItem(tonelada?: number, material?: number, fecha?: string): void {
		this.items.push(this.crearItemFormGroup(tonelada, material, fecha));
	}
	extractPedTrasladoSelect(): ProgramPedTrasladoResponse | undefined {
		return this.listProgramPedTrasladoFilter.find(ped => ped.activo == true);
	}
	resetearFormArray(): void {
		const VALORES_POR_DEFECTO = {
			tonelada: 0,
			material: this.materialSeleccionado,
			fecha: this.getTodayAsString()
		};
		this.items.controls.forEach((control: any) => {
			control.patchValue({
				tonelada: VALORES_POR_DEFECTO.tonelada,
				material: VALORES_POR_DEFECTO.material,
				fecha: VALORES_POR_DEFECTO.fecha
			});
			this.utilServ.resetFormGroupState(control);
		});
	}
	procesarFormularioProgramArribo() {
		this.markAllControlsAsTouched(this.items);
		if (this.listProgramPedTrasladoFilter.length == 0) {
			this.utilServ.showMessageWarningInfo("No existen pedidos de traslados para crear programación de arribo.");
			return;
		}
		if (!this.filaSeleccionada) {
			this.utilServ.showMessageWarningInfo("Debe seleccionar el pedido traslado");
			return;
		}
		if (this.items.invalid) {
			return;
		}
		const valoresDelFormulario: any[] = this.items.value;
		let pesoTotal_ = 0;
		this.listaDeObjetosProgram = valoresDelFormulario.map(item => ({
			tonelada: Number(item.tonelada),
			materialId: Number(item.material),
			fecha: item.fecha,
		}));
		pesoTotal_ = this.listaDeObjetosProgram.reduce((total, item) => total + item.tonelada, 0);
		const pedSeleccionado = this.extractPedTrasladoSelect();
		const restaCantidadPedido = pesoTotal_ < Number(pedSeleccionado?.cantidad) ? true : false;
		if (pedSeleccionado != undefined && pesoTotal_ > Number(pedSeleccionado.cantidad)) {
			this.utilServ.showMessageWarningInfo("La cantidad de toneladas de programación arribo supera al total de pedido traslado seleccionado");
		} else {
			this.buildRequestProgramArribo(restaCantidadPedido);
		}
	}
	buildRequestProgramArribo(esResta: boolean) {
		const siloId = this.getValueNumber("silo");
		const materialId = this.getValueNumber("material");
		const plantaId = this.getValueNumber("planta");
		const pedSeleccionado = this.extractPedTrasladoSelect();
		const listProgramRequest: ProgramArriboRequest[] = this.listaDeObjetosProgram.map(program => {
			return {
				numeroPedidoTraslado: pedSeleccionado?.numeroPedido,
				cantidad: Number(program.tonelada),
				fechaProgramada: program.fecha,
				claveSilo: "",
				siloId: siloId,
				materialId: materialId,
				plantaId: plantaId,
				pedidoTrasladoId: pedSeleccionado?.pedidoTrasladoId,
				isRestaCantidad: esResta ? 'R' : 'P'
			} as ProgramArriboRequest;
		});
		if (listProgramRequest.length > 0) {
			this.saveProgramArribo(listProgramRequest);
		}
	}

	saveProgramArribo(listProgramRequest: ProgramArriboRequest[]) {
		this.programArriboServ.save(listProgramRequest).subscribe({
			next: (response: string) => {
				this.deleteListPedTraslado(listProgramRequest);
				this.filaSeleccionada = null;
				this.resetearFormArray();
				this.clearItems();
				this.cdr.detectChanges();
				this.utilServ.confirmarSuccess("Operación  exitosa");
			}, error: (error) => {
				this.utilServ.showMessageError("Hubo un error en el registro de la programación de arribo");
				console.log("ERROR RESPONSE SAVE PROGRAM ARRIBO: " + JSON.stringify(error));
			}
		});
	}

	deleteListPedTraslado(listProgramRequest: ProgramArriboRequest[]) {
		for (const ped of listProgramRequest) {
			const index = this.listProgramPedTrasladoFilter.findIndex(p => p.pedidoTrasladoId == ped.pedidoTrasladoId);
			if (index !== -1) {
				this.listProgramPedTrasladoFilter.splice(index, 1);
			}
		}
	}
	private markAllControlsAsTouched(control: AbstractControl | FormGroup | FormArray) {
		if (control instanceof FormGroup || control instanceof FormArray) {
			// Recorre los controles internos (los FormGroups de cada ítem)
			Object.values(control.controls).forEach(c => {
				this.markAllControlsAsTouched(c); // Llamada recursiva para marcar los FormControls
			});
		} else {
			// Si es un FormControl (ej. 'tonelada', 'fecha'), lo marca.
			control.markAsTouched();
			control.updateValueAndValidity();
		}
	}

	//nueva forma de calcular programacion de arribos:
	//=================================================================================================================================================
	//=================================================================================================================================================
	//=================================================================================================================================================
	//=================================================================================================================================================


	procesarFormularioConRestasOptimizadas() {
		this.markAllControlsAsTouched(this.items);

		if (this.listProgramPedTrasladoFilter.length == 0) {
			this.utilServ.showMessageWarningInfo("No existen pedidos de traslados para crear programación de arribo.");
			return;
		}

		if (this.items.invalid) {
			return;
		}

		// Obtener valores del formulario
		const valoresDelFormulario: any[] = this.items.value;
		let pesoTotal_ = 0;

		this.listaDeObjetosProgram = valoresDelFormulario.map(item => ({
			tonelada: Number(item.tonelada),
			materialId: Number(item.material),
			fecha: item.fecha,
		}));

		pesoTotal_ = this.listaDeObjetosProgram.reduce((total, item) => total + item.tonelada, 0);

		console.log('🔍 BÚSQUEDA INICIADA:', {
			pesoTotalRequerido: pesoTotal_,
			pedidosDisponibles: this.listProgramPedTrasladoFilter.length,
			pedidos: this.listProgramPedTrasladoFilter.map(p => ({
				numero: p.numeroPedido,
				cantidad: p.cantidad
			}))
		});

		//  MEJORADO: Usar función más flexible
		const resultado = this.obtenerMejorCombinacionFlexible(
			this.listProgramPedTrasladoFilter.map(p => ({
				...p,
				cantidad: Number(p.cantidad)
			})),
			'cantidad',
			pesoTotal_
		);

		console.log('🔍 RESULTADO BÚSQUEDA:', resultado);

		if (resultado.seleccionados.length === 0) {
			this.utilServ.showMessageWarningInfo("No se encontró una combinación adecuada.");
			return;
		}

		//  MOSTRAR INFORMACIÓN DETALLADA DEL AJUSTE
		if (resultado.objetoAjustado) {
			console.log('🔍 AJUSTE REALIZADO:');
			console.log(`- Pedido ajustado: ${resultado.objetoAjustado.numeroPedido}`);
			console.log(`- Cantidad original: ${resultado.objetoAjustado.cantidadOriginal}`);
			console.log(`- Cantidad restada: ${resultado.objetoAjustado.cantidadRestada}`);
			console.log(`- Sobrante final: ${resultado.objetoAjustado.sobranteFinal}`);

			this.utilServ.showSuccess(
				`Combinación encontrada. Se ajustó el pedido ${resultado.objetoAjustado.numeroPedido} ` +
				`(resta: ${resultado.objetoAjustado.cantidadRestada} ton, sobra: ${resultado.objetoAjustado.sobranteFinal} ton)`
			);
		} else {
			this.utilServ.showSuccess(
				`Combinación encontrada. Se utilizaron ${resultado.seleccionados.length} pedidos sin ajustes.`
			);
		}

		// Proceder con el guardado
		this.buildRequestConRestasOptimizadas(resultado.seleccionados, resultado.objetoAjustado);
	}

	// Algoritmo más flexible que prioriza pedidos individuales
	private obtenerMejorCombinacionFlexible(
		items: any[],
		campo: string,
		objetivo: number
	) {
		console.log(' Objetivo:', objetivo);

		// ESTRATEGIA 1: Buscar un solo pedido que sea suficiente
		const pedidoIndividual = this.buscarPedidoIndividualOptimo(items, campo, objetivo);
		if (pedidoIndividual) {
			console.log('✅ Encontrado pedido individual óptimo:', pedidoIndividual);
			return pedidoIndividual;
		}

		// ESTRATEGIA 2: Buscar combinación de múltiples pedidos
		const combinacionMultiple = this.buscarCombinacionMultiple(items, campo, objetivo);
		if (combinacionMultiple.seleccionados.length > 0) {
			console.log('✅ Encontrada combinación múltiple:', combinacionMultiple);
			return combinacionMultiple;
		}

		// ESTRATEGIA 3: Último recurso - pedido más pequeño que cubra el objetivo
		const pedidoMinimo = this.buscarPedidoMinimoSuficiente(items, campo, objetivo);
		if (pedidoMinimo) {
			console.log('✅ Encontrado pedido mínimo suficiente:', pedidoMinimo);
			return pedidoMinimo;
		}

		console.log('❌ No se encontró combinación adecuada');
		return { seleccionados: [], total: 0, objetoAjustado: null };
	}

	// Buscar un solo pedido que sea óptimo
	private buscarPedidoIndividualOptimo(items: any[], campo: string, objetivo: number) {
		let mejorPedido: any = null;
		let mejorDiferencia = Number.MAX_SAFE_INTEGER;
		let mejorSobrante = Number.MAX_SAFE_INTEGER;

		for (const item of items) {
			const cantidad = item[campo];
			const diferencia = Math.abs(cantidad - objetivo);

			// Si el pedido cubre el objetivo, evaluar cuál es mejor
			if (cantidad >= objetivo) {
				const sobrante = cantidad - objetivo;

				// PRIORIDAD 1: Menor sobrante (más eficiente)
				// PRIORIDAD 2: Menor diferencia si hay empate
				if (sobrante < mejorSobrante ||
					(sobrante === mejorSobrante && diferencia < mejorDiferencia)) {
					mejorPedido = item;
					mejorDiferencia = diferencia;
					mejorSobrante = sobrante;
				}
			}
		}

		if (mejorPedido) {
			const cantidadOriginal = mejorPedido[campo];
			const diferencia = cantidadOriginal - objetivo;

			let objetoAjustado = null;
			if (diferencia > 0) {
				objetoAjustado = {
					...mejorPedido,
					cantidadOriginal: cantidadOriginal,
					cantidadReducida: objetivo,
					cantidadRestada: diferencia,
					sobranteFinal: 0 // Se usa todo para las programaciones
				};
			}

			return {
				seleccionados: [mejorPedido],
				total: objetivo, // Usamos exactamente lo necesario
				objetoAjustado
			};
		}

		return null;
	}

	//  Buscar combinación de múltiples pedidos
	private buscarCombinacionMultiple(items: any[], campo: string, objetivo: number) {
		// Para objetivos pequeños, buscar combinación de los pedidos más pequeños
		const pedidosPequenos = [...items]
			.filter(item => item[campo] <= objetivo * 3) // Solo pedidos razonables
			.sort((a, b) => a[campo] - b[campo]);

		if (pedidosPequenos.length === 0) {
			return { seleccionados: [], total: 0, objetoAjustado: null };
		}

		// Usar algoritmo greedy simple
		let suma = 0;
		const seleccionados: any[] = [];

		for (const pedido of pedidosPequenos) {
			if (suma >= objetivo) break;

			seleccionados.push(pedido);
			suma += pedido[campo];
		}

		if (seleccionados.length > 0 && suma >= objetivo) {
			// Ajustar si nos pasamos
			let objetoAjustado = null;
			if (suma > objetivo) {
				const diferencia = suma - objetivo;
				const resultadoAjuste = this.elegirMejorPedidoParaAjustar(seleccionados, campo, diferencia);
				return {
					seleccionados: resultadoAjuste.combinacionAjustada,
					total: resultadoAjuste.nuevaSuma,
					objetoAjustado: resultadoAjuste.objetoAjustado
				};
			}

			return {
				seleccionados,
				total: suma,
				objetoAjustado
			};
		}

		return { seleccionados: [], total: 0, objetoAjustado: null };
	}

	//   Último recurso - pedido más pequeño que cubra el objetivo
	private buscarPedidoMinimoSuficiente(items: any[], campo: string, objetivo: number) {
		const pedidosSuficientes = items.filter(item => item[campo] >= objetivo);

		if (pedidosSuficientes.length === 0) {
			return null;
		}

		// Encontrar el más pequeño que cubra el objetivo
		const pedidoMinimo = pedidosSuficientes.reduce((min, item) =>
			item[campo] < min[campo] ? item : min
		);

		const cantidadOriginal = pedidoMinimo[campo];
		const diferencia = cantidadOriginal - objetivo;

		const objetoAjustado = {
			...pedidoMinimo,
			cantidadOriginal: cantidadOriginal,
			cantidadReducida: objetivo,
			cantidadRestada: diferencia,
			sobranteFinal: 0
		};

		return {
			seleccionados: [pedidoMinimo],
			total: objetivo,
			objetoAjustado
		};
	}


	private elegirMejorPedidoParaAjustar(combinacion: any[], campo: string, diferenciaNecesaria: number) {
		// Estrategia 1: Buscar el pedido que al restarle deje el mayor sobrante
		let mejorOpcion: any = null;
		let mayorSobrante = -1;
		let mejorCombinacion: any[] = [];
		let mejorNuevaSuma = 0;

		// Probar ajustando cada pedido individualmente
		for (let i = 0; i < combinacion.length; i++) {
			const pedido = combinacion[i];
			const cantidadOriginal = pedido[campo];

			// Verificar si este pedido puede absorber la diferencia completa
			if (cantidadOriginal >= diferenciaNecesaria) {
				const sobranteDespues = cantidadOriginal - diferenciaNecesaria;

				// ESTRATEGIA: Preferir el pedido que quede con MAYOR sobrante
				if (sobranteDespues > mayorSobrante) {
					mayorSobrante = sobranteDespues;
					mejorOpcion = { ...pedido, indice: i };

					// Crear nueva combinación ajustada
					mejorCombinacion = combinacion.map((item, idx) => {
						if (idx === i) {
							return { ...item, [campo]: sobranteDespues };
						}
						return { ...item };
					});

					mejorNuevaSuma = combinacion.reduce((sum, item, idx) =>
						sum + (idx === i ? sobranteDespues : item[campo]), 0);
				}
			}
		}

		// Si encontramos un pedido que pueda absorber la diferencia y dejar buen sobrante
		if (mejorOpcion) {
			const objetoAjustado = {
				...mejorOpcion,
				cantidadOriginal: mejorOpcion[campo],
				cantidadReducida: mayorSobrante,
				cantidadRestada: diferenciaNecesaria,
				sobranteFinal: mayorSobrante
			};

			return {
				combinacionAjustada: mejorCombinacion,
				nuevaSuma: mejorNuevaSuma,
				objetoAjustado
			};
		}

		// ESTRATEGIA 2: Si ningún pedido individual puede absorberlo,
		// eliminar pedidos empezando por los MÁS PEQUEÑOS para concentrar el sobrante en los grandes
		const combinacionOrdenada = [...combinacion].sort((a, b) => a[campo] - b[campo]);
		let diferenciaRestante = diferenciaNecesaria;
		const combinacionAjustada = combinacion.map(obj => ({ ...obj }));
		let objetoAjustado: any = null;

		for (let i = 0; i < combinacionOrdenada.length && diferenciaRestante > 0; i++) {
			const pedidoMasPequeno = combinacionOrdenada[i];
			const indexOriginal = combinacion.findIndex(p => p === pedidoMasPequeno);

			if (pedidoMasPequeno[campo] <= diferenciaRestante) {
				// Eliminar pedido pequeño completo
				diferenciaRestante -= pedidoMasPequeno[campo];
				combinacionAjustada[indexOriginal][campo] = 0;
				// No marcamos como objetoAjustado porque se elimina completo
			} else {
				// Ajustar pedido (este será el que quede con sobrante)
				const cantidadAnterior = pedidoMasPequeno[campo];
				combinacionAjustada[indexOriginal][campo] = cantidadAnterior - diferenciaRestante;

				objetoAjustado = {
					...pedidoMasPequeno,
					cantidadOriginal: cantidadAnterior,
					cantidadReducida: cantidadAnterior - diferenciaRestante,
					cantidadRestada: diferenciaRestante,
					sobranteFinal: cantidadAnterior - diferenciaRestante
				};
				diferenciaRestante = 0;
			}
		}

		// Filtrar pedidos que no fueron eliminados (cantidad > 0)
		const combinacionFinal = combinacionAjustada.filter(item => item[campo] > 0);
		const nuevaSumaFinal = combinacionFinal.reduce((sum, item) => sum + item[campo], 0);

		return {
			combinacionAjustada: combinacionFinal,
			nuevaSuma: nuevaSumaFinal,
			objetoAjustado
		};
	}

	//  MEJORADO: Build request - SOLO UN pedido debe tener 'R'
	private buildRequestConRestasOptimizadas(pedidosSeleccionados: any[], ajuste: any) {
		const siloId = this.getValueNumber("silo");
		const materialId = this.getValueNumber("material");
		const plantaId = this.getValueNumber("planta");

		const listProgramRequest: ProgramArriboRequest[] = [];

		//  CORREGIDO: Asignar SOLO UN pedido para las programaciones
		if (pedidosSeleccionados.length > 0) {
			const pedidoPrincipal = pedidosSeleccionados[0]; // Usar solo el primer pedido

			// Asignar TODAS las programaciones al pedido principal
			this.listaDeObjetosProgram.forEach(programacion => {
				// SOLO el pedido principal usa 'R' si hay ajuste
				const esResta = pedidosSeleccionados.length === 1 && ajuste;

				const request: ProgramArriboRequest = {
					numeroPedidoTraslado: pedidoPrincipal.numeroPedido,
					cantidad: programacion.tonelada,
					fechaProgramada: programacion.fecha,
					claveSilo: "",
					siloId: siloId,
					materialId: materialId,
					plantaId: plantaId,
					pedidoTrasladoId: pedidoPrincipal.pedidoTrasladoId,
					isRestaCantidad: esResta ? 'R' : 'P'
				};

				listProgramRequest.push(request);
			});
		}

		//  CORREGIDO: El ajuste SOLO si existe y es diferente a las programaciones
		if (ajuste && ajuste.cantidadRestada > 0) {
			const requestAjuste: ProgramArriboRequest = {
				numeroPedidoTraslado: ajuste.numeroPedido,
				cantidad: ajuste.cantidadRestada,
				fechaProgramada: new Date().toISOString().split('T')[0],
				claveSilo: "",
				siloId: siloId,
				materialId: materialId,
				plantaId: plantaId,
				pedidoTrasladoId: ajuste.pedidoTrasladoId,
				isRestaCantidad: 'R'
			};
			listProgramRequest.push(requestAjuste);
		}

		console.log('📦 REQUESTS A GUARDAR:', listProgramRequest);

		if (listProgramRequest.length > 0) {
			this.saveProgramArribo(listProgramRequest);
		}
	}

}


/*findStock() {
	this.markAllControlsAsTouched(this.formArriboFilter);
	if (this.formArriboFilter.valid) {
		this.actionsBottonsDisabled = false;
		this.materialSeleccionado = this.formArriboFilter.get('material')?.value;
		this.formArriboFilter.get('nombre')?.setValue('Juan Pérez');
		const siloId = this.getValueNumber('silo');
		const materialId = this.getValueNumber('material');
		const claveSilo = this.getClaveSiloById(this.getValueNumber('silo'));
		const plantaId = this.getValueNumber('planta');
		this.programArriboServ.findStock(claveSilo != null ? claveSilo : "").subscribe({
			next: (response: string) => {
				if (response != null && response != undefined) {
					this.stockSilo = response;
					this.cdr.detectChanges();
					this.findPedidoTraslado(siloId.toString(), this.findClavePlantaById(plantaId), materialId.toString());
				}
			}, error: (error) => {
				this.utilServ.showMessageError("Hubo un error en la carga del stock");
				console.log("ERROR AL EXTRAER STOCK DE SILO: " + JSON.stringify(error));
			}
		});
	}
}*/
