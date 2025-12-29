import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialogClose, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { LoginFormComponent } from '../login-form/login-form.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-doble-factor-auth',
  imports: [MatButtonModule, MatDialogClose, MatCardModule, MatFormFieldModule, MatInputModule, MatIconModule, ReactiveFormsModule,CommonModule],
  templateUrl: './confirm-doble-factor-auth.component.html',
  styleUrl: './confirm-doble-factor-auth.component.css'
})
export class ConfirmDobleFactorAuthComponent implements OnInit {

  dobleFactorForm!: FormGroup;
  
  // Referencia a todos los inputs del OTP
  @ViewChildren('otpInput') otpInputs!: QueryList<ElementRef<HTMLInputElement>>;

  constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<LoginFormComponent>) { }

  ngOnInit(): void {
    this.inicializarFormulario();
  }

  inicializarFormulario() {
    this.dobleFactorForm = this.fb.group({
      otp: this.fb.array(
        new Array(4).fill('').map(() => new FormControl('', [
          Validators.required,
          Validators.pattern(/^[0-9]$/) // Solo permite un dígito numérico
        ]))
      )
    });
  }

  confirmClave() {
    if (this.dobleFactorForm.valid) {
      const otpValue = this.dobleFactorForm.value.otp.join(''); 
      this.dialogRef.close(otpValue);
    }
  }

  get otpControls(): FormArray {
    return this.dobleFactorForm.get('otp') as FormArray;
  }

  onSubmit(): void {
    if (this.dobleFactorForm.valid) {
      const otpValue = this.dobleFactorForm.value.otp.join(''); 
      this.dialogRef.close(otpValue);
    } else {
      console.log('El formulario no es válido. Asegúrate de llenar todos los campos.');
      this.otpControls.controls.forEach(control => control.markAsTouched());
    }
  }

  onInput(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    const value = input.value;
    
    // Limitar a un solo carácter
    if (value.length > 1) {
      input.value = value.charAt(0);
      this.otpControls.at(index).setValue(value.charAt(0));
    }

    // Auto-focus al siguiente input si hay valor
    if (input.value && index < this.otpControls.length - 1) {
      this.focusInput(index + 1);
    }

    // Si es el último input y tiene valor, enviar automáticamente
    /*if (input.value && index === this.otpControls.length - 1 && this.dobleFactorForm.valid) {
      this.onSubmit();
    }*/
  }

  onKeydown(event: KeyboardEvent, index: number): void {
    const input = event.target as HTMLInputElement;
    
    // Manejar Backspace
    if (event.key === 'Backspace') {
      if (!input.value && index > 0) {
        // Si el campo está vacío, retroceder al anterior
        event.preventDefault();
        this.focusInput(index - 1);
      } else if (input.value) {
        // Si hay valor, limpiar el campo actual
        this.otpControls.at(index).setValue('');
      }
    }
    
    // Manejar flechas
    else if (event.key === 'ArrowLeft' && index > 0) {
      event.preventDefault();
      this.focusInput(index - 1);
    }
    else if (event.key === 'ArrowRight' && index < this.otpControls.length - 1) {
      event.preventDefault();
      this.focusInput(index + 1);
    }
    
    // Prevenir caracteres no numéricos
    else if (!/^[0-9]$/.test(event.key) && 
             !['Backspace', 'ArrowLeft', 'ArrowRight', 'Tab', 'Delete'].includes(event.key)) {
      event.preventDefault();
    }
  }

  onPaste(event: ClipboardEvent, index: number): void {
    event.preventDefault();
    const pasteData = event.clipboardData?.getData('text').trim();
    
    if (pasteData && /^\d+$/.test(pasteData)) {
      const digitsArray = pasteData.split('').slice(0, this.otpControls.length);
      
      // Llenar los campos con los dígitos pegados
      digitsArray.forEach((digit, digitIndex) => {
        const targetIndex = index + digitIndex;
        if (targetIndex < this.otpControls.length) {
          this.otpControls.at(targetIndex).setValue(digit);
        }
      });
      
      // Enfocar el siguiente campo después del último dígito pegado
      const nextIndex = Math.min(index + digitsArray.length, this.otpControls.length - 1);
      this.focusInput(nextIndex);
      
      // Si se completó todo el código, enviar automáticamente
      if (this.dobleFactorForm.valid) {
        setTimeout(() => this.onSubmit(), 100);
      }
    }
  }

  // Método para enfocar un input específico
  private focusInput(index: number): void {
    setTimeout(() => {
      const inputsArray = this.otpInputs.toArray();
      if (inputsArray[index] && inputsArray[index].nativeElement) {
        inputsArray[index].nativeElement.focus();
        inputsArray[index].nativeElement.select();
      }
    }, 10);
  }

  // Método para limpiar todos los campos
  clearForm(): void {
    this.otpControls.controls.forEach(control => control.setValue(''));
    this.focusInput(0);
  }
}