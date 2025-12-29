import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Validador personalizado para asegurar que el valor del control
 * no sea la cadena literal "0".
 * (Útil para Selects donde el valor "0" representa la opción de placeholder).
 */
export function notZeroStringValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    // Si el valor es nulo, indefinido o vacío, se asume que
    // otro validador (como Validators.required) lo manejará.
    if (control.value === null || control.value === undefined || control.value === '') {
      return null;
    }
    // Comprobamos si el valor es la cadena "0"
    const isZero = control.value === '0';
    // Si es "0", devolvemos el objeto de error con la clave 'zeroValue'
    return isZero ? { zeroValue: true } : null;
  };
}