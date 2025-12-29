// src/app/shared/validators/letter-only.validator.ts
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Validador que acepta solo letras (mayúsculas, minúsculas) y espacios.
 */
export const lettersOnlyValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const value = control.value;

  if (!value) {
    return null; // Permitimos que el validador 'required' se encargue.
  }

  // Acepta letras (con tildes y ñ) y espacios.
  const lettersOnly = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]*$/.test(value);

  return lettersOnly ? null : { lettersOnly: true };
};