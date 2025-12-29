import { Component, inject, Inject, OnInit, signal, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogClose, MatDialogRef } from '@angular/material/dialog';
import { LoginFormComponent } from '../login-form/login-form.component';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { ConfirmDobleFactorAuthComponent } from '../confirm-doble-factor-auth/confirm-doble-factor-auth.component';
import { UsuarioService } from '../../../services/usuario/usuario.service';
import { PasswordOtpRequest } from '../../../models/PasswordOtpRequest';
import { UtilsService } from '../../../services/shared/utils.service';
import { BackendError } from '../../../models/error/Backend.Errror.response';
import { AuthServiceService } from '../../../services/auth/auth-service.service';
import { OtpRequest } from '../../../models/OtpRequest';
import { LoginTokeResponse } from '../../../models/LoginTokenResponse';
import { UpdatePasswordRequest } from '../../../models/authentication/UpdatePasswordRequest';

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
  selector: 'app-update-password-olvidado',
  imports: [MatButtonModule,
    MatDialogClose,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    ReactiveFormsModule,
    MatStepperModule,
    CommonModule],
  templateUrl: './update-password-olvidado.component.html',
  styleUrl: './update-password-olvidado.component.css'
})
export class UpdatePasswordOlvidadoComponent implements OnInit {

  @ViewChild('stepper') stepper!: MatStepper;
  hidePasswordActual = signal(true);
  hidePassword = signal(true);
  hidePasswordConfirm = signal(true);
  updatePasswordForm!: FormGroup;
  username: string = "";
  disableFormUpdatePassword:boolean=false;

  emailStepCompleted = signal(false);
  otpStepCompleted = signal(false);

  // Estados de carga para los botones
  isSendingEmail = signal(false);
  isVerifyingOtp = signal(false);



  private _formBuilder = inject(FormBuilder);

  formCorreo = this._formBuilder.group({
    correo: ['', Validators.required],
  });
  formClaveOtp = this._formBuilder.group({
    clave: ['', Validators.required],
  });

  constructor(public dialog: MatDialog,
    public dialogRef: MatDialogRef<LoginFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private utilService: UtilsService,
    private authService: AuthServiceService,
    private usuarioService: UsuarioService) {
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
  get claveOtp() {
    return this.formClaveOtp.get('clave') as FormControl;
  }

  get correoControl() {
    return this.formCorreo.get('correo') as FormControl;
  }
  get passwordControl() {
    return this.updatePasswordForm.get('password') as FormControl;
  }

  get passwordConfirmControl() {
    return this.updatePasswordForm.get('passwordConfirm') as FormControl;
  }

  inicializarFormulario() {
    this.updatePasswordForm = new FormGroup({
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8)
      ]),
      passwordConfirm: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
      ])
    }, {
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

  updatePassword(event: MouseEvent) {
    event.stopPropagation();
    this.utilService.markAllControlsAsTouched(this.updatePasswordForm);
    if(this.updatePasswordForm.invalid){
      return;
    }
    const password=this.updatePasswordForm.get("password")?.value??"";
    const updatePassword: UpdatePasswordRequest = new UpdatePasswordRequest(this.username, password);
    this.usuarioService.aupdatePassword(updatePassword).subscribe({
      next: (response) => {
        this.dialogRef.close();
        this.utilService.confirmarSuccess("Se actualizo el password correctamente");
      },
      error: (error) => {
        console.log("ERROR RESULTADO UPDATE PASSWORD: " + JSON.stringify(error));
        this.utilService.showMessageError("Hubo un error en la actualizacion de contraseña");
      }
    });
  }

  sendCorreo() {
    if (this.formCorreo.valid) {
      this.username = this.formCorreo.get("correo")?.value ?? "";
      const request: PasswordOtpRequest = new PasswordOtpRequest(this.username);
      this.usuarioService.sendClaveByCorreo(request).subscribe({
        next: (response) => {
          this.utilService.confirmarSuccess("Se envió la clave al correo");
          this.stepper.next();
        }, error: (err) => {
          if (err.error && typeof err.error === 'object') {
            // Castear para acceder a las propiedades con seguridad
            const authError: BackendError = err.error as BackendError;
            const errorCode = authError.error;
            const errorMessage = authError.message;
            if (errorCode === "error-code:SEND-C-001") {
              this.utilService.showMessageError("No existe el correo");
            } else {
              this.utilService.showMessageError("Hubo un error en el envío de la clave al correo");
            }
          }
          console.log("RESPONSE ERROR SEND CORREO: " + JSON.stringify(err));
        }
      })
    }
  }

  veryOtp() {
    this.utilService.markAllControlsAsTouched(this.formClaveOtp);
    if (!this.formClaveOtp.valid) {
      return;
    }
    const otp = this.formClaveOtp.get("clave")?.value ?? "";
    const otpRequest = new OtpRequest(otp, this.username);
    this.authService.verifyOtp(otpRequest).subscribe({
      next: (response: LoginTokeResponse) => {
        if (response.token != null && response.token != undefined) {
           this.otpStepCompleted.set(true);
          this.utilService.confirmarSuccess("Se verificó correctamente la clave");
          this.stepper.next();
        } else {
          this.deleteOpt(otpRequest.username);
          this.utilService.showMessageWarningInfo("Su clave no es correcta, Intente de nuevo");
        }
      },
      error: (error) => {
        if (error.error["error"] == "no-existe-password") {
        } else if (error.error["error"] == "error-code:EX-001") {
          this.utilService.showMessageWarningInfo("Su clave no es correcta, Intente de nuevo");
        } else {
          this.utilService.showMessageError("Hubo un error en la validacion de la clave OTP");
        }
        console.log('Login erroneo:', JSON.stringify(error.error));
      }
    });
  }

  deleteOpt(usuario: string) {
    this.authService.deleteOtp(-1, usuario).subscribe({
      next: (response) => {
      }, error: (err) => {
        console.log("Hubo un error en la verificacipon de su OTP de 4 digitos");
      }
    })
  }
}
