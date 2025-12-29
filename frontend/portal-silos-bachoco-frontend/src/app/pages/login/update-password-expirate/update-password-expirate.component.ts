import { Component, Inject, OnInit, signal } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MAT_DIALOG_DATA, MatDialogClose, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { LoginFormComponent } from '../login-form/login-form.component';
import { CommonModule } from '@angular/common';

export const matchPasswordsValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('password');
  const passwordConfirm = control.get('passwordConfirm');
  if (!password || !passwordConfirm) {
    return null;
  }
  // Si los valores no coinciden, establece el error a nivel del control 'passwordConfirm'
  if (passwordConfirm.value && password.value !== passwordConfirm.value) {
    // Es clave que el error se establezca aquí para que mat-error lo pueda capturar
    passwordConfirm.setErrors({ ...passwordConfirm.errors, passwordsMismatch: true });
    // Retornamos el error a nivel de grupo para que el formulario sepa que es inválido
    return { passwordsMismatch: true };
  } 
  // Si coinciden, nos aseguramos de limpiar SÓLO el error de mismatch, si existía.
  if (passwordConfirm.hasError('passwordsMismatch')) {
    const { passwordsMismatch, ...rest } = passwordConfirm.errors || {};
    // Mantener otros errores si existen (ej. 'required')
    passwordConfirm.setErrors(Object.keys(rest).length ? rest : null);
  }
  return null;
};

@Component({
  selector: 'app-update-password-expirate',
  imports: [MatButtonModule, 
    MatDialogClose, MatCardModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatIconModule, 
    ReactiveFormsModule,
    CommonModule],
  templateUrl: './update-password-expirate.component.html',
  styleUrl: './update-password-expirate.component.css'
})
export class UpdatePasswordExpirateComponent implements OnInit {

  hidePasswordActual = signal(true);
  hidePassword = signal(true);
  hidePasswordConfirm = signal(true);
  updatePasswordForm!: FormGroup;

  constructor(public dialogRef: MatDialogRef<LoginFormComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit(): void {
    this.inicializarFormulario();
  }

    clickEventPasswordActual(event: MouseEvent) {
    this.hidePasswordActual.set(!this.hidePassword());
    event.stopPropagation();
  }


  clickEventPassword(event: MouseEvent) {
    this.hidePassword.set(!this.hidePassword());
    event.stopPropagation();
  }

  clickEventPasswordConfirm(event: MouseEvent) {
    this.hidePasswordConfirm.set(!this.hidePasswordConfirm());
    event.stopPropagation();
  }
  // Getters para acceder fácilmente a los FormControls en la plantilla
  get passwordControl() {
    return this.updatePasswordForm.get('password') as FormControl;
  }

  get passwordConfirmControl() {
    return this.updatePasswordForm.get('passwordConfirm') as FormControl;
  }

   get passwordActualControl() {
    return this.updatePasswordForm.get('passwordActual') as FormControl;
  }

  
  inicializarFormulario() {
    this.updatePasswordForm = new FormGroup({
      passwordActual: new FormControl('', [
        Validators.required,
        Validators.minLength(8)
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8)
      ]),
      passwordConfirm: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
      ])
    }, { 
    // Aplicación del validador personalizado a nivel de FormGroup
    validators: [matchPasswordsValidator] 
  });
  }

  togglePasswordVisibility(event: MouseEvent, type: 'password' | 'confirm') {
    event.preventDefault();
    if (type === 'password') {
      this.hidePassword.update(current => !current);
    } else {
      this.hidePasswordConfirm.update(current => !current);
    }
  }

  updatePassword() {
    this.updatePasswordForm.markAllAsTouched();
    if (this.updatePasswordForm.valid) {
      this.dialogRef.close(this.updatePasswordForm.value);
    }
  }
}
