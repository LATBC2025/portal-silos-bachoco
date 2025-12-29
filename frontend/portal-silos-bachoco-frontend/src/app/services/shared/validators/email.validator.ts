import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Expresión Regular estándar para validar la mayoría de los formatos de correo electrónico.
 * (Una de las RegEx más comunes que cubre la mayoría de los casos de uso)
 */
const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

/**
 * Validador personalizado para asegurar que el valor del control sea un correo electrónico válido.
 */
export function customEmailValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    
    const email = control.value;

    // 1. Manejar valores nulos o vacíos
    // Si el campo está vacío, permitimos que Validators.required (si se aplica) lo maneje.
    if (!email) {
      return null;
    }

    // 2. Comprobar el formato usando la Expresión Regular
    const isValid = EMAIL_REGEX.test(email);

    // Si NO es válido, devolvemos el objeto de error { invalidEmail: true }
    return isValid ? null : { invalidEmail: true };
  };
}