// src/app/shared/directives/only-letters.directive.ts
import { Directive, HostListener } from '@angular/core';

@Directive({
  selector: '[appOnlyLetters]',
  standalone: true // Si trabajas con componentes standalone
})
export class OnlyLettersDirective {

  // HostListener escucha el evento 'keydown' en el elemento donde se aplica la directiva
  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    const key = event.key;

    // 1. Permitir teclas de control para edición (Backspace, Tab, flechas, etc.)
    const controlKeys = [
      'Backspace', 'Tab', 'End', 'Home', 'ArrowLeft', 'ArrowRight', 
      'Delete', 'Shift', 'Control', 'Alt', 'Meta' // Meta es la tecla Windows/Command
    ];

    if (controlKeys.includes(key) || event.metaKey || event.ctrlKey) {
      return; // Permite la acción por defecto (navegar, borrar, etc.)
    }

    // 2. Expresión regular que acepta letras (mayúsculas, minúsculas), tildes, 'ñ' y espacios.
    // El '$' al final de la expresión regular solo permite un solo carácter por pulsación de tecla.
    const isLetterOrSpace = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]$/.test(key);

    if (!isLetterOrSpace) {
      event.preventDefault(); // Bloquea el carácter si no es una letra/espacio válido
    }
  }
}