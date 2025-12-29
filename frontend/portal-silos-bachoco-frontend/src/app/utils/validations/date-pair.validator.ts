import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Validador personalizado para asegurar que o ambas fechas están vacías, o ambas están llenas.
 * No se permite que solo una de ellas tenga valor.
 * * @param controlName1 Nombre del control de la primera fecha (ej: 'fechaInicio')
 * @param controlName2 Nombre del control de la segunda fecha (ej: 'fechaFin')
 * @returns Función ValidatorFn que devuelve un objeto de error (si la validación falla) o null (si es válida).
 */
export function datePairValidator(controlName1: string, controlName2: string): ValidatorFn {
  return (formGroup: AbstractControl): ValidationErrors | null => {
    // 1. Obtener los controles de fecha
    const control1 = formGroup.get(controlName1);
    const control2 = formGroup.get(controlName2);

    // Si los controles no existen, salir (esto no debería pasar)
    if (!control1 || !control2) {
      return null;
    }

    // Convertir los valores a booleanos para saber si tienen contenido
    // Usamos !!control.value para convertir el valor a booleano (true si tiene algo, false si es null, undefined o cadena vacía)
    const hasValue1 = !!control1.value;
    const hasValue2 = !!control2.value;

    // 2. Lógica de Validación:
    // Si *solo* una de las fechas tiene valor, es un error.
    // Esto se cumple si (una tiene valor Y la otra NO) O (la otra tiene valor Y la primera NO).
    const onlyOneHasValue = (hasValue1 && !hasValue2) || (!hasValue1 && hasValue2);

    if (onlyOneHasValue) {
      // 3. Devolver el error
      return { 
        datesMustBePaired: true // Clave del error
      };
    }

    // 4. Si la condición se cumple (ambas vacías O ambas llenas), la validación es exitosa
    return null;
  };
}