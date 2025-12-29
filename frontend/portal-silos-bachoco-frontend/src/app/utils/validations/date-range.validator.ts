// src/app/shared/validators/date-range.validator.ts

import { AbstractControl, FormGroup, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Función Factoría que genera un ValidatorFn.
 * Esto permite pasar los nombres de los campos de fecha dinámicamente.
 * * @param fechaInicioKey El nombre del control de la fecha de inicio (ej: 'fechaI')
 * @param fechaFinKey El nombre del control de la fecha fin (ej: 'fechaF')
 * @param errorKey El nombre de la clave de error a usar (ej: 'dateRangeError')
 * @returns Un ValidatorFn que se aplica a nivel de FormGroup.
 */
export function dateRangeValidator(fechaInicioKey: string, fechaFinKey: string, errorKey: string = 'dateRangeError'): ValidatorFn {
  
  // La función interna es el ValidatorFn real
  return (control: AbstractControl): ValidationErrors | null => {
    
    // Casteamos el control a FormGroup
    const formGroup = control as FormGroup;
    
    // Obtenemos los controles usando las claves dinámicas
    const fechaInicioControl = formGroup.get(fechaInicioKey);
    const fechaFinControl = formGroup.get(fechaFinKey);

    // 1. Verificación básica y de valores
    if (!fechaInicioControl || !fechaFinControl || !fechaInicioControl.value || !fechaFinControl.value) {
      return null;
    }

    // 2. Comparar las fechas
    const fechaInicio = new Date(fechaInicioControl.value);
    const fechaFin = new Date(fechaFinControl.value);

    const isInvalid = fechaFin < fechaInicio;

    // 3. Manejo del error: Asignar o remover el error en el control de Fecha Fin
    if (isInvalid) {
      // Asignamos el error con la clave dinámica
      fechaFinControl.setErrors({ ...fechaFinControl.errors, [errorKey]: true });
      return null; 
    } else {
      // Si es válido, eliminamos el error si existe
      if (fechaFinControl.hasError(errorKey)) {
        // Obtenemos todos los errores
        const errors = fechaFinControl.errors || {};
        // Eliminamos la clave de error dinámica
        delete errors[errorKey];
        
        // Reasignamos los errores restantes o null
        fechaFinControl.setErrors(Object.keys(errors).length > 0 ? errors : null);
      }
    }

    return null;
  };
}