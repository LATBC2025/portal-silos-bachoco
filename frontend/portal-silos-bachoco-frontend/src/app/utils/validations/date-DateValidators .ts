import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { DateUtils } from '../date/DateUtils ';

export class DateValidators {

static noFutura(mensaje?: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) return null;
    try {
      const fechaControl = new Date(control.value);
      const fechaHoy = new Date();
      // Validar que sea una fecha válida
      if (isNaN(fechaControl.getTime())) {
        return { fechaInvalida: true };
      }
      if (fechaControl > fechaHoy) {
        return {
          fechaFutura: {
            message: mensaje || 'La fecha no puede ser futura, incluyendo mañana',
            value: control.value,
            fechaMaximaPermitida: fechaHoy.toISOString().split('T')[0]
          }
        };
      }
      return null;
    } catch (error) {
      return { fechaInvalida: true };
    }
  };
}

 static fechaNoFuturaValidator(mensaje?: string): ValidatorFn {
   return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    try {
      // Usar el mismo formato YYYY-MM-DD para ambas fechas
      const fechaSeleccionada = control.value; // Ya está en formato YYYY-MM-DD
      const fechaHoy = DateUtils.getFechaHoy(); // También en YYYY-MM-DD
      // Comparación directa de strings (YYYY-MM-DD es comparable lexicográficamente)
      if (fechaSeleccionada > fechaHoy) {
        return {
          fechaFutura: {
            message: mensaje || `La fecha no puede ser posterior al ${fechaHoy}`,
            fechaSeleccionada: fechaSeleccionada,
            fechaHoy: fechaHoy
          }
        };
      }
      return null;
    } catch (error) {
      console.error('Error en validador de fecha:', error);
      return { fechaInvalida: { message: 'Error al validar la fecha' } };
    }
  };
}

  // Validador adicional: fecha no pasada
  static noPasada(mensaje?: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) return null;
      const fechaControl = new Date(control.value);
      const fechaHoy = new Date();
      fechaHoy.setHours(0, 0, 0, 0);
      fechaControl.setHours(0, 0, 0, 0);

      if (fechaControl < fechaHoy) {
        return {
          fechaPasada: {
            message: mensaje || 'La fecha no puede ser anterior al día de hoy',
            value: control.value
          }
        };
      }
      return null;
    };
  }
}