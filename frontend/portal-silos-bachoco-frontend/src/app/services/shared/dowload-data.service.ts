import { Injectable, signal, Signal } from '@angular/core';

interface DatosTabla {
  // Define la estructura de tus datos
  id: number;
  nombre: string;
}
@Injectable({
  providedIn: 'root'
})
export class DowloadDataService {
// Signal para el estado de la carga de datos
  private _datosCargadosPedCompra = signal<boolean>(false);
  private _datosCargadosPedTraslado = signal<boolean>(false);
  
  // Signal para almacenar los datos (inicialmente un array vacío o null)
  private _datosTabla = signal<DatosTabla[] | null>(null);

  // Exponer los Signals como solo lectura (Solo Signal)
  datosCargadosPedCompra: Signal<boolean> = this._datosCargadosPedCompra.asReadonly();
  datosCargadosPedTraslado: Signal<boolean> = this._datosCargadosPedTraslado.asReadonly();

  constructor() { }

  // Método que llama la primera página para marcar y guardar los datos
  establecerDatosPedCompra(cargado: boolean): void {
    this._datosCargadosPedCompra.set(cargado);
  }
   establecerDatosPedTraslado(cargado: boolean): void {
    this._datosCargadosPedTraslado.set(cargado);
  }
  establecerDatos(datos: DatosTabla[]): void {
    this._datosTabla.set(datos);
  }

  // Opcional: Para resetear el estado si es necesario
  resetearEstado(): void {
    this._datosTabla.set(null);
  }
}
