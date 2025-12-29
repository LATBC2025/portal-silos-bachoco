import { Injectable } from '@angular/core';
import { AbstractControl, FormArray, FormGroup } from '@angular/forms';
import { ConfirmacionDespachoRequest } from '../../models/confirmacion-despacho/Confirmacion.Despacho.Request';
import Swal, { SweetAlertResult } from 'sweetalert2';
import { DateUtils } from '../../utils/date/DateUtils ';

@Injectable({
  providedIn: 'root'
})
export class UtilsService {

  constructor() { }

  excelSerialToDate(serial: number): string {
    const date = new Date(Math.round((serial - 25569) * 86400 * 1000));
    return date.toISOString().split('T')[0];
  }

  // Convierte una fecha de Excel (número serial) o string (dd/mm/yy) a formato YYYY-MM-DD para input date
  convertExcelDate(value: any): string {
    if (value === '' || value == null || value === undefined) {
      return '';
    }

    // Si es número, es fecha serial de Excel
    if (typeof value === 'number') {
      return this.excelSerialToYYYYMMDD(value);
    }

    // Si es string, intentar parsear diferentes formatos
    const strValue = String(value).trim();

    // Si ya está en formato YYYY-MM-DD (input date), devolverlo
    if (this.isValidYYYYMMDD(strValue)) {
      return strValue;
    }

    // Intentar parsear como dd/mm/yy o dd/mm/yyyy
    return this.parseDateStringToYYYYMMDD(strValue);
  }

  /**
   * Convierte número serial de Excel a formato YYYY-MM-DD
   */
  private excelSerialToYYYYMMDD(serial: number): string {
    try {
      // Excel para Windows: 1 = 01/01/1900
      const excelEpoch = new Date(1899, 11, 30); // 30/12/1899
      const date = new Date(excelEpoch.getTime() + (serial - 1) * 86400000);

      // Corrección para el bug de Excel (1900 considerado bisiesto)
      if (serial > 60) {
        date.setTime(date.getTime() - 86400000);
      }

      if (isNaN(date.getTime())) {
        console.warn('Fecha serial de Excel inválida:', serial);
        return '';
      }

      return this.formatDateToYYYYMMDD(date);
    } catch (error) {
      console.error('Error en excelSerialToYYYYMMDD:', error);
      return '';
    }
  }

  /**
   * Parsea string de fecha a formato YYYY-MM-DD
   */
  private parseDateStringToYYYYMMDD(dateStr: string): string {
    if (!dateStr || dateStr.trim() === '') {
      return '';
    }

    const cleanStr = dateStr.trim();

    // Expresión regular para formatos comunes
    const dateRegex = /^(\d{1,2})[\/\-\.](\d{1,2})[\/\-\.](\d{2,4})$/;

    const match = cleanStr.match(dateRegex);

    if (!match) {
      // Intentar con el constructor Date nativo
      try {
        const date = new Date(cleanStr);
        if (!isNaN(date.getTime())) {
          return this.formatDateToYYYYMMDD(date);
        }
      } catch (e) {
        console.warn('No se pudo parsear la fecha:', cleanStr);
      }

      return ''; // Devolver vacío si no se puede parsear
    }

    const part1 = parseInt(match[1], 10);
    const part2 = parseInt(match[2], 10);
    let year = parseInt(match[3], 10);

    let day: number, month: number;

    // Determinar si es formato dd/mm o mm/dd
    if (part1 > 31 || part2 > 31) {
      return '';
    }

    if (part1 > 12 && part2 <= 12) {
      // part1 > 12 no puede ser mes, así que part1 es día
      day = part1;
      month = part2;
    } else if (part2 > 12 && part1 <= 12) {
      // part2 > 12 no puede ser mes, así que part2 es día
      day = part2;
      month = part1;
    } else if (part1 <= 12 && part2 <= 12) {
      // Ambos <= 12, asumir dd/mm (común en español)
      day = part1;
      month = part2;
    } else {
      return '';
    }

    // Validar día y mes
    if (day < 1 || day > 31 || month < 1 || month > 12) {
      return '';
    }

    // Convertir año de 2 dígitos a 4 dígitos
    if (year < 100) {
      year = 2000 + year; // "25" -> 2025
    }

    // Validar año
    if (year < 1900 || year > 2100) {
      return '';
    }

    // Crear fecha
    const date = new Date(year, month - 1, day);

    // Verificar si la fecha es válida
    if (isNaN(date.getTime()) ||
      date.getDate() !== day ||
      date.getMonth() + 1 !== month ||
      date.getFullYear() !== year) {
      return '';
    }

    return this.formatDateToYYYYMMDD(date);
  }

  /**
   * Formatea Date a YYYY-MM-DD (formato para input date)
   */
  private formatDateToYYYYMMDD(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  /**
   * Valida si un string ya está en formato YYYY-MM-DD
   */
  private isValidYYYYMMDD(dateStr: string): boolean {
    const regex = /^(\d{4})-(\d{2})-(\d{2})$/;
    const match = dateStr.match(regex);

    if (!match) {
      return false;
    }

    const year = parseInt(match[1], 10);
    const month = parseInt(match[2], 10);
    const day = parseInt(match[3], 10);

    // Validaciones básicas
    if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900 || year > 2100) {
      return false;
    }

    // Validar fecha específica
    const date = new Date(year, month - 1, day);

    return !isNaN(date.getTime()) &&
      date.getDate() === day &&
      date.getMonth() + 1 === month &&
      date.getFullYear() === year;
  }

  cleanKgResponse(text: string): string {
    if (!text) return text;
    return text
      // Eliminar "KG" después de números con comas o puntos
      .replace(/(\d{1,3}(?:[.,]\d{3})*)\s*KG/gi, '$1')
      // Eliminar puntos de miles
      .replace(/(\d{1,3})\.(\d{3})/g, '$1$2')
      // Eliminar comas de miles
      .replace(/(\d{1,3}),(\d{3})/g, '$1$2')
      // Espacio adicional entre número y código siguiente
      .replace(/(\d+)\s+([A-Z])/g, '$1 $2');
  }

  cleanKgResponse2(text: string): string {
    if (!text) return text;
    return text
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
      .trim();
  }

  isPDFFile(file: File): boolean {
    return file.type === 'application/pdf' &&
      file.name.toLowerCase().endsWith('.pdf');
  }

  public buildConfirmacionDespachoRequest(form: FormGroup) {
    const f = form.getRawValue();
    return new ConfirmacionDespachoRequest(
      f.claveBodega,
      f.claveSilo,
      f.claveMaterial,
      f.fechaEmbarque,
      f.numBoleta,
      f.pesoBruto,
      f.pesoTara,
      f.humedad,
      f.chofer,
      f.placaJaula,
      f.lineaTransportista,
      f.destinoId.toString(),
      f.numPedidoTraslado,
      f.tipoMovimiento.toString(),
      '0',
      f.idPedTraslado
    );
  }

  buildPdf(name: string, data: Blob) {
    const blobUrl = window.URL.createObjectURL(data);
    const link = document.createElement('a');
    link.href = blobUrl;
    link.download = name;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(blobUrl);
  }
  markAllControlsAsTouched(control: AbstractControl | FormGroup | FormArray) {
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

  getFechaHoyFormateada(): string {
    const today = new Date();
    // Obtiene el año (YYYY)
    const year = today.getFullYear();
    // Obtiene el mes (0-11). Se suma 1 y se le añade un '0' si es menor a 10.
    const month = String(today.getMonth() + 1).padStart(2, '0');
    // Obtiene el día. Se le añade un '0' si es menor a 10.
    const day = String(today.getDate()).padStart(2, '0');
    // Concatena y retorna el formato esperado: YYYY-MM-DD
    return `${year}-${month}-${day}`;
  }

  convertExcelSerialToDateString(excelSerial: number): string {
    const excelEpoch = new Date(1900, 0, 1);
    const jsDate = new Date(excelEpoch.getTime() + (excelSerial - 2) * 24 * 60 * 60 * 1000);
    const d = String(jsDate.getDate()).padStart(2, '0');
    const m = String(jsDate.getMonth() + 1).padStart(2, '0');
    const y = jsDate.getFullYear();
    return `${d}/${m}/${y}`;
  }


  esNumero(value: string) {
    // Expresión regular para validar número: acepta números enteros o decimales (con punto)
    const patronNumero = /^-?\d+(\.\d+)?$/;
    if (patronNumero.test(value)) {
      // ✅ Es un número válido
      return true;
    } else {
      return false;
    }
  }
  toUpperCase(value: string) {
    return value.toLocaleLowerCase();
  }
  isVacio(value: any): boolean {
    // 1. Maneja null o undefined inmediatamente.
    if (value === null || value === undefined) {
      return true;
    }
    const stringValue = String(value);
    return stringValue.trim().length === 0;
  }
  convertExcelSerialToDate(excelSerial: number): Date {
    // Días entre la época de Excel (1900-01-01) y la de JavaScript (1970-01-01)
    const daysDifference = 25569;
    // Milisegundos en un día
    const msInDay = 24 * 60 * 60 * 1000;
    // Calcular los días transcurridos desde la época de JavaScript
    const daysSinceEpoch = excelSerial - daysDifference;
    // Calcular los milisegundos y crear el objeto Date
    const msSinceEpoch = daysSinceEpoch * msInDay;
    // El input type="date" solo necesita la fecha (sin hora/zona horaria),
    // por lo que este cálculo es directo.
    return new Date(msSinceEpoch);
  }
  esFechaValidaNoAnterior(fechaStr: string, permitirHoy: boolean = true): boolean {
    if (!fechaStr || fechaStr.trim() === '') {
      return false;
    }
    const fecha = fechaStr.trim();
    const fechaHoy = DateUtils.getFechaHoy(); // También en YYYY-MM-DD
    // Comparación directa de strings (YYYY-MM-DD es comparable lexicográficamente)
    if (fecha < fechaHoy) {
      return true;
    }
    return false;
  }
  //METODOS PARA ALERTAS MODAL
  confirmarSuccess(mensaje: string) {
    Swal.fire({
      title: mensaje,
      icon: "success",
      showCancelButton: false,
      confirmButtonColor: "#F37321",
      confirmButtonText: "Aceptar",
    });
  }

  showSuccessRegistro() {
    Swal.fire({
      title: "Registro agregado con éxito.",
      icon: "success",
      showCancelButton: false,
      confirmButtonColor: "#F37321",
      confirmButtonText: "Aceptar",
    });
  }


  showSuccess(mensaje: string) {
    Swal.fire({
      title: mensaje,
      icon: "success",
      showCancelButton: false,
      confirmButtonColor: "#F37321",
      confirmButtonText: "Aceptar",
    });
  }
  showErrorRegistro() {
    Swal.fire({
      title: "Ocurrio un error al agregar.",
      icon: "error",
      showCancelButton: false,
      confirmButtonColor: "#F37321",
      confirmButtonText: "Aceptar",
    });
  }

  showSuccessActualizacion() {
    Swal.fire({
      title: "Registro actualizado con éxito.",
      icon: "success",
      showCancelButton: false,
      confirmButtonColor: "#F37321",
      confirmButtonText: "Aceptar",
    });
  }

  showErrorActualizacion() {
    Swal.fire({
      title: "Ocurrio un error al actualizar.",
      icon: "error",
      showCancelButton: false,
      confirmButtonColor: "#F37321",
      confirmButtonText: "Aceptar",
    });
  }


  showErrorEliminacion() {
    Swal.fire({
      title: "Ocurrio un error al eliminar.",
      icon: "error",
      showCancelButton: false,
      confirmButtonColor: "#F37321",
      confirmButtonText: "Aceptar",
    });
  }

  showSuccesEliminacion() {
    Swal.fire({
      title: "Registro eliminado con éxito.",
      icon: "success",
      showCancelButton: false,
      confirmButtonColor: "#F37321",
      confirmButtonText: "Aceptar",
    });
  }




  confirmarEliminar(): Promise<SweetAlertResult> {
    return Swal.fire({
      title: "¿Estas seguro de eliminar?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#F37321",
      cancelButtonColor: "#000000",
      confirmButtonText: "Eliminar",
      cancelButtonText: 'Cancelar',
      customClass: {
        cancelButton: 'sweet-alert-cancel-white-text'
      }
    });
  }
  confirmarCloseCuenta(): Promise<SweetAlertResult> {
    return Swal.fire({
      title: "¿Estás seguro de que deseas salir del Portal Silos Bachoco?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#F37321",
      cancelButtonColor: "#000000",
      confirmButtonText: "Aceptar",
      cancelButtonText: 'Cancelar',
      customClass: {
        cancelButton: 'sweet-alert-cancel-white-text'
      }
    });
  }

  showMessageError(mensaje: string): Promise<SweetAlertResult> {
    return Swal.fire({
      icon: "error",
      text: mensaje,
      // --- Configuración del color del botón ---
      showCancelButton: false, // Oculta el botón de cancelar que tenías
      confirmButtonText: 'Aceptar',
      confirmButtonColor: '#F37321', // 👈 Color Naranja (código hexadecimal o nombre CSS)
      // --- Configuración del color del texto ---
      // Usamos customClass para modificar el texto del botón de confirmación
      customClass: {
        confirmButton: 'sweet-alert-custom-confirm-button'
      }
    });
  }

  showMessageErrorDataIntagrationViolation(): Promise<SweetAlertResult> {
    return Swal.fire({
      icon: "error",
      text: "El registro no puede ser eliminado porque tiene referencias activas en otros módulos del sistema.",
      // --- Configuración del color del botón ---
      showCancelButton: false, // Oculta el botón de cancelar que tenías
      confirmButtonText: 'Aceptar',
      confirmButtonColor: '#F37321', // 👈 Color Naranja (código hexadecimal o nombre CSS)
      // --- Configuración del color del texto ---
      // Usamos customClass para modificar el texto del botón de confirmación
      customClass: {
        confirmButton: 'sweet-alert-custom-confirm-button'
      }
    });
  }


  showMessageWarningInfo(mensaje: string): Promise<SweetAlertResult> {
    return Swal.fire({
      icon: "warning",
      text: mensaje,
      // --- Configuración del color del botón ---
      showCancelButton: false, // Oculta el botón de cancelar que tenías
      confirmButtonText: 'Aceptar',
      confirmButtonColor: '#F37321', // 👈 Color Naranja (código hexadecimal o nombre CSS)
      // --- Configuración del color del texto ---
      // Usamos customClass para modificar el texto del botón de confirmación
      customClass: {
        confirmButton: 'sweet-alert-custom-confirm-button'
      }
    });
  }

  showMessageWarningInfoNoExisteRegistrosFilters(): Promise<SweetAlertResult> {
    return Swal.fire({
      icon: "warning",
      text: "No hay coincidencias para tu búsqueda.",
      showCancelButton: false,
      confirmButtonText: 'Aceptar',
      confirmButtonColor: '#F37321',
      customClass: {
        confirmButton: 'sweet-alert-custom-confirm-button'
      }
    });
  }

  resetFormGroupState(formGroup: FormGroup | FormArray): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);

      if (!control) return;

      if (control instanceof FormGroup || control instanceof FormArray) {
        this.resetFormGroupState(control); // ✅ ya no da error
      } else {
        control.setErrors(null);
        control.markAsPristine();
        control.markAsUntouched();
        control.updateValueAndValidity({
          onlySelf: true,
          emitEvent: false
        });
      }
    });

    formGroup.markAsPristine();
    formGroup.markAsUntouched();
    formGroup.updateValueAndValidity({ emitEvent: false });
  }

}
