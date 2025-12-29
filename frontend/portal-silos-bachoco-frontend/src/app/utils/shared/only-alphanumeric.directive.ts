import { Directive, HostListener } from '@angular/core';

@Directive({
  selector: '[appOnlyAlphanumeric]'
})
export class OnlyAlphanumericDirective {

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    const key = event.key;

    // 1. Permitir teclas de control para edición (Backspace, Tab, flechas, etc.)
    const controlKeys = [
      'Backspace', 'Tab', 'End', 'Home', 'ArrowLeft', 'ArrowRight',
      'Delete', 'Shift', 'Control', 'Alt', 'Meta'
    ];

    if (controlKeys.includes(key) || event.metaKey || event.ctrlKey) {
      return; // Permite la acción por defecto
    }

    // 2. Expresión regular:
    // Acepta letras (mayúsculas/minúsculas, con tildes y ñ), números (0-9) y espacios (\s).
    // Si NO quieres espacios, simplemente remueve '\s' de la expresión.
    const isAlphanumericOrSpace = /^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\s]$/.test(key);

    if (!isAlphanumericOrSpace) {
      event.preventDefault(); // Bloquea la entrada si no coincide con la regla
    }
  }
}
