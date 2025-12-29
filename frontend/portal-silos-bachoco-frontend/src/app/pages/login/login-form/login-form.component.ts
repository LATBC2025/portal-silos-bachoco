import { Component, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

import { Router } from '@angular/router';
import { AuthServiceService } from '../../../services/auth/auth-service.service';
import { ResponseRequest } from '../../../models/ResponseRequest';
import { LoginRequest } from '../../../models/AuthenticationRequest';
import { MatDialog } from '@angular/material/dialog';
import { UpdatePasswordComponent } from '../update-password/update-password.component';
import { UpdatePasswordModel } from '../../../models/authentication/UpdatePassword.model';
import { UsuarioService } from '../../../services/usuario/usuario.service';
import { UpdatePasswordRequest } from '../../../models/authentication/UpdatePasswordRequest';
import Swal from 'sweetalert2';
import { LoginTokeResponse } from '../../../models/LoginTokenResponse';
import { ConfirmDobleFactorAuthComponent } from '../confirm-doble-factor-auth/confirm-doble-factor-auth.component';
import { OtpRequest } from '../../../models/OtpRequest';
import { EmpleadoService } from '../../../services/catalog/empleado.service';
import { EmpleadoResponse } from '../../../models/catalogs/Empelado/Empleado.Response';
import { VeriryOtpRequest } from '../../../models/authentication/VerifyOtp.Request';
import { UtilsService } from '../../../services/shared/utils.service';
import { BackendError } from '../../../models/error/Backend.Errror.response';
import { UpdatePasswordExpiredRequest } from '../../../models/authentication/Update.password.expired';
import { UpdatePasswordExpirateComponent } from '../update-password-expirate/update-password-expirate.component';
import { UpdatePasswordExpiredModel } from '../../../models/authentication/Upate.Paswword.expired.model';
import { CommonModule, JsonPipe } from '@angular/common';
import { NetworkServiceService } from '../../../services/shared/network-service.service';
import { UpdatePasswordOlvidadoComponent } from '../update-password-olvidado/update-password-olvidado.component';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, ReactiveFormsModule, CommonModule],
  template: `
    <div class="container-login">
        <form [formGroup]="loginForm" >
            <mat-form-field appearance="outline" class="w-100 w-md-75 w-lg-50">
              <mat-label >Usuario</mat-label>
              <input matInput class="p-lg-0 input-md-height" formControlName="username" placeholder="Usuario" />
              <!-- ERROR REQUERIDO ESTÁNDAR -->
              <mat-error *ngIf="getControl('username').hasError('required') && getControl('username').touched">
                Campo usuario requerido.
              </mat-error>
            </mat-form-field>
            <mat-form-field appearance="outline" class="w-100 w-md-75 w-lg-50 p-1">
              <mat-label >Contraseña</mat-label>
              <input matInput class="p-lg-0 input-md-height" [type]="hide() ? 'password' : 'text'" formControlName="password" placeholder="Password"/>
                <mat-error *ngIf="getControl('password').hasError('required') && getControl('password').touched">
                  Campo contraseña requerido.
                </mat-error>
                <mat-error *ngIf="getControl('password').hasError('minlength') && getControl('password').touched">
                  Debe tener al menos 8 caracteres (actual: {{ getControl('password').errors?.['minlength']?.actualLength }}).
                </mat-error>
                <button 
                  mat-icon-button 
                  matSuffix 
                  type="button" 
                  (mousedown)="mostrarPassword()" 
                  (mouseup)="ocultarPassword()"
                  (mouseleave)="ocultarPassword()" 
                  [attr.aria-label]="'Toggle password visibility'"
                >
              <mat-icon>{{ hide() ? 'visibility' : 'visibility_off' }}</mat-icon>
              </button>
            </mat-form-field>
          <p class="link-olvide-password" (click)="openModalOlvidePassword()">¿Olvidé mi contraseña?</p> 
          <button class="btn-login w-100 p-3" (click)="singUp()">Iniciar sesión</button>
      </form>
     
    </div>
  `,
  styles: `
    :host {
      display: block;
    }
   .btn-login {
      background: #F37321 0% 0% no-repeat padding-box;
      box-shadow: 4px 8px 20px #1F1F1F1A;
      border-radius: 2px;
      opacity: 1;
      font-size:18px;
      border: none;
      color: white;
    }
    .container-login{
      height: 100%;
    }
    .link-olvide-password{
      cursor:pointer;
    }
    .link-olvide-password{
      text-align: center;
      font: normal normal medium 24px/26px Montserrat;
      letter-spacing: 0.56px;
      color: #F37321;
      opacity: 1;
    }

  @media (max-width: 1200px) {
    .btn-login {
      font-size:16px;
    }
    mat-form-field input.mat-input-element {
      font-size: 18px;
      height: 15px !important; 
    }
    mat-form-field .mat-mdc-floating-label {
      font-size: 16px;
      height: 15px !important; 
    }
    .input-md-height { height: 15px !important; 
       padding-top: 5px; /* Adjust padding for alignment */
      padding-bottom: 5px;
    }
     .container-login{
      height: 100%;
    }
     .link-olvide-password{
       font: normal normal medium 20px/16px Montserrat;
      letter-spacing: 0.56px;
    }
}
      /* Pantallas medianas y grandes (tablet/laptop) */
  @media (max-width: 768px) {
    mat-form-field input.mat-input-element {
      font-size: 16px !important;
      height:10px !important;
    }
    mat-form-field .mat-mdc-floating-label {
      font-size: 14px;
    }
    .input-md-height { height: 15px !important; }
    .btn-login {
        background: #F37321 0% 0% no-repeat padding-box;
        box-shadow: 4px 8px 20px #1F1F1F1A;
        border-radius: 2px;
        opacity: 1;
        border: none;
        color: white;
      }
      .link-olvide-password{
      font: normal normal normal 16px/0px Montserrat;
      margin: 10px 0px 30px 0px;
      margin-top:30px;
    }
     .container-login{
      height: 100%;
    }

}


  `
})
export class LoginFormComponent implements OnInit {
  hide = signal(true);
  loginForm!: FormGroup;
  passwordUpdateModel!: UpdatePasswordModel;
  passwordUpdateExpriredModel!: UpdatePasswordExpiredModel;
  usernameLogin:string="";

  constructor(private router: Router, private authService: AuthServiceService,
    private usuarioService: UsuarioService,
    private empService: EmpleadoService,
    private health: NetworkServiceService,
    private utilServ: UtilsService,
    public dialog: MatDialog) { }

  ngOnInit(): void {
    this.inicializarFormularioLogin();
  }

  mostrarPassword(): void {
    this.hide = signal(false);
  }
  ocultarPassword(): void {
    this.hide = signal(true);
  }
  inicializarFormularioLogin() {
    this.loginForm = new FormGroup({
      username: new FormControl('', [
        Validators.required
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(6)
      ])
    });
  }

  getControl(value: string) {
    return this.loginForm.get(value) as FormControl;
  }
  openDialog(enterAnimationDuration: string, exitAnimationDuration: string): void {
    const dialogRef = this.dialog.open(UpdatePasswordComponent, {
      width: '80vw',
      maxWidth: '350px',
      enterAnimationDuration,
      exitAnimationDuration,
      panelClass: 'custom-dialog-container'
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (typeof result !== "string" && result != "true") {
          if (Object.keys(result).length > 0) {
            this.passwordUpdateModel = new UpdatePasswordModel(result['password'], result["passwordConfirm"]);
            this.aupdatePassword(this.passwordUpdateModel.password);
          } else {
            this.resetFormAuthLogin();
          }
        }
      }
    });
  }

  openDialogUpdateExpired(enterAnimationDuration: string, exitAnimationDuration: string): void {
    const dialogRef = this.dialog.open(UpdatePasswordExpirateComponent, {
      width: '80vw',
      maxWidth: '350px',
      enterAnimationDuration,
      exitAnimationDuration,
      panelClass: 'custom-dialog-container'
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (typeof result !== "string" && result != "true") {
          if (Object.keys(result).length > 0) {
            this.updatePasswordExprired(result['passwordActual'], result["passwordConfirm"]);
          }
        }
      }
    });
  }

  openDialogConfirmDobleFactor(enterAnimationDuration: string, exitAnimationDuration: string): void {
    const dialogRef = this.dialog.open(ConfirmDobleFactorAuthComponent, {
      width: '80vw',
      maxWidth: '450px',
      enterAnimationDuration,
      exitAnimationDuration,
      panelClass: 'custom-dialog-container'
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result != undefined && result != "true") {
          if (Object.keys(result).length > 0) {
            this.veryOtp(result);
          }
        }
      }
    });
  }

  openDialogOlvidePassword(enterAnimationDuration: string, exitAnimationDuration: string): void {
    const dialogRef = this.dialog.open(UpdatePasswordOlvidadoComponent, {
      width: '80vw',
      maxWidth: '380px',
      enterAnimationDuration,
      exitAnimationDuration,
      panelClass: 'custom-dialog-container'
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result != undefined && result != "true") {
          if (Object.keys(result).length > 0) {
            this.veryOtp(result);
          }
        }
      }
    });
  }

  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }

  deleteOpt(usuario: string) {
    this.authService.deleteOtp(-1, usuario).subscribe({
      next: (response) => {
      }, error: (err) => {
        console.log("Hubo un error en la verificacipon de su OTP de 4 digitos");
      }
    })
  }

  veryOtp(otp: string) {
    let username: string | null = "";
    if (this.authService.getUsernameInLocalStorage() != null) {
      username = this.authService.getUsernameInLocalStorage();
    }
    const otpRequest = new OtpRequest(otp, username != null ? username : "");
    this.authService.verifyOtp(otpRequest).subscribe({
      next: (response: LoginTokeResponse) => {
        if (response.token != null && response.token != undefined) {
          this.authService.saveUsernameAndTokenUInLocalStorage(response.username, response.token);
          this.extracionDatosEmpleado(response.username);
        } else {
          this.deleteOpt(otpRequest.username);
          this.utilServ.showMessageWarningInfo("Su clave no es correcta, Intente de nuevo");
        }
      },
      error: (error) => {
        if (error.error["error"] == "no-existe-password") {
          this.openDialog('0ms', '0ms')
        } else if (error.error["error"] == "error-code:EX-001") {
          this.deleteOpt(otpRequest.username);
          this.utilServ.showMessageWarningInfo("Su clave no es correcta, Intente de nuevo");
        } else {
          this.resetFormAuthLogin();
          this.utilServ.showMessageError("Hubo un error en la validacion de la clave OTP");
        }
      }
    });
  }

  resetFormAuthLogin() {
    this.loginForm.reset();
  }

  openModalOlvidePassword() {
    this.openDialogOlvidePassword('0ms', '0ms');
  }

  async singUp() {
    this.utilServ.markAllControlsAsTouched(this.loginForm);
    if (!this.loginForm.valid) {
      return;
    }
    const isServerAlive = await this.health.checkConnection();
    if (!isServerAlive) {
      this.utilServ.showMessageError("Error de Conexión: No se pudo establecer comunicación con el servidor");
      return; // detener ejecución
    }
    const login: LoginRequest = new LoginRequest(this.loginForm.get('username')?.value, this.loginForm.get('password')?.value);
    this.usernameLogin=this.loginForm.get('username')?.value;
    this.authService.login(login).subscribe({
      next: (response: LoginTokeResponse) => {
        this.authService.saveUsernameAndTokenUInLocalStorage(response.username, response.token);
        this.openDialogConfirmDobleFactor('0ms', '0ms');
      },
      error: (err) => {
        if (err.error["error"] == "no-existe-password") {
          this.openDialog('0ms', '0ms');
          return;
        }
        if (err.error && typeof err.error === 'object') {
          const authError: BackendError = err.error as BackendError;
          const errorCode = authError.error;
          const errorMessage = authError.message;
          if (errorCode === "error-code:CREENTIALS-NOVALIDAS-1") {
            this.resetFormAuthLogin();
            this.utilServ.showMessageError(errorMessage);
          } else if (errorCode == "no-existe-password") {

          } else if (errorCode == "error-code:EXP-PWD") {
            this.openDialogUpdateExpired('0ms', '0ms');
          } else if (err["status"] != undefined && err["status"] != null && err["status"] == 0) {
            this.utilServ.showMessageError("Error de Conexión: No se pudo establecer comunicación con el servidor");
          } else {
            this.utilServ.showMessageError("Hubo un error en la autenticación");
          }
          this.resetFormAuthLogin();
          console.log('Login erroneo:', JSON.stringify(authError));
        }
      }
    });
  }

  extracionDatosEmpleado(value: string) {
    this.empService.findByUsurioOrCorreo(value).subscribe({
      next: (response: EmpleadoResponse) => {
         this.authService.saveValuesEmpleadoUInLocalStorage(response.siloId.toString(),
          response.departamentoId.toString());
        this.authService.saveValuesPueatoAnTipoEmpInLocalStorage(response.tipoEmpleado.toString(),
          response.nombrePuesto); 
        this.router.navigate(['/home']);
      }, error: (error) => {
        this.utilServ.showMessageError("Hubo un error en los accesos del usuario");
      }
    })
  }
  aupdatePassword(password: string) {
    const updatePassword: UpdatePasswordRequest = new UpdatePasswordRequest(this.usernameLogin, password);
    this.usuarioService.aupdatePassword(updatePassword).subscribe({
      next: (response) => {
        this.utilServ.confirmarSuccess("Se actualizó la contraseña correctamente.");
      },
      error: (error) => {
        if (error.error["error"] == "no-existe-password") {
          this.openDialog('0ms', '0ms')
        } else {
          this.utilServ.showMessageError("Hubo un error en la actualizacion de contraseña");
        }
      }
    });
  }
  updatePasswordExprired(passwordActual: string, nuevoPassword: string) {
    const updateReq: UpdatePasswordExpiredRequest = new UpdatePasswordExpiredRequest(this.usernameLogin, passwordActual, nuevoPassword);
    this.usuarioService.updatePasswordExprired(updateReq).subscribe({
      next: (response: ResponseRequest) => {
        this.utilServ.confirmarSuccess("Se actualizo el password correctamente");
      },
      error: (error) => {
        if (error.error["error"] == "no-existe-password") {
          this.openDialog('0ms', '0ms')
        } else {
          this.utilServ.showMessageError("Hubo un error en la actualizacion de contraseña");
        }
      }
    });
  }
}
