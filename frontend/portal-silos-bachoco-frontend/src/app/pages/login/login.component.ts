import { Component } from '@angular/core';
import { environment } from '../../../environments/environment';
import { LoginFormComponent } from './login-form/login-form.component';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [LoginFormComponent],
    template: `
       <div clas="container">
      <div class="contenedor">
        <div class="col-md-6">
          <div class="container-text">
                 <h1>BIENVENIDO</h1>
                  <p>Portal Silos Bachoco</p>
          </div>
        </div>
          <div class="col-md-6">
          <div class="login__container">
            <div class="login__header">
              <img
                src="./assets/images/login/imagenes-bachuco/Grupo_Bachoco_H_fondo_blanco.png"
                alt="Logo"
                class="img-fluid"
              />
            </div>
            <div class="text-dashboard">
                <br/><br/><br/><br/>
            </div>
            <app-login-form></app-login-form>
            <div class="login__footer mt-2">
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
    styles: `
     :host {
      display: block;
      box-sizing: border-box;
    }
    .contenedor{
       display: flex;
       flex-direction: row;
       width: 100%;
        background-image: 
        linear-gradient(to bottom, rgba(255, 168, 7, 0.7), rgba(0,0,0,0.8)),
        url("../../../assets/images/shared/fondo.png");
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
    }
    .container-text{
        width: 100%;
        height: 100%;
         display: flex;
       flex-direction: column;
       align-items: center; 
    }
     .container-text h1{
       position: relative;
      top: 270px;
      height: 80px;
      color: var(--unnamed-color-ffffff);
      text-align: center;
      font: normal normal 900 90px/44px Montserrat;
      letter-spacing: 0px;
      color: #FFFFFF;
      text-transform: uppercase;
     }
    .container-text p{
      margin-top:270px;
      position: relative;
      font: normal normal 300 80px/80px Montserrat;
      letter-spacing: 1.07px;
      color: #FFFF;
      opacity: 1;
      text-align:center;
     }

    .login__container {
      max-width: 650px;
      height: 100vh;
      padding: 50px 80px;
      margin: 0 auto;
      border: 1px solid #e0e0e0; 
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); 
      background: white;
    }
    .login__container p{
      text-align: left;
      font: normal normal normal 25px/34px Montserrat;
      letter-spacing: 0px;
      color: #000000DE;
      opacity: 1;
    }

    .login__header img {
      margin-top: 150px;
      left: 772px;
      width: 326px;
      height: 112px;
    }
    .text-dashboard p{
      font: normal normal normal 100 18px/24px Montserrat;
      padding: 20px 0px 30px 0px;
    }
  @media (max-width: 1400px) {
    .contenedor{
       width: 100%;
        background-image: 
        linear-gradient(to bottom, rgba(255, 168, 7, 0.7), rgba(0,0,0,0.8)),
        url("../../../assets/images/shared/fondo.png");
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
    }
        .login__header img {
      margin-top: 80px;
      left: 772px;
      width: 326px;
      height: 112px;
    }
    .container-text h1{
        top: 150px; /* Reducir un poco */
        font: normal normal 900 80px/34px Montserrat;
        font-size: 80px; /* Reducir el tamaño de fuente */
        line-height: 70px;
    }
    .container-text p{
        margin-top: 150px;
        font-size: 65px;
        font: normal normal 300 70px/70px Montserrat;
        line-height: 70px;
    }
    .login__container {
      max-width: 512px;
      height: 100vh;
      padding: 50px 80px;
      margin: 0 auto;
      border: 1px solid #e0e0e0; 
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); 
      background: white;
    }
    .login__container p{
      text-align: left;
      font: normal normal normal 14px/24px Montserrat;
      letter-spacing: 0px;
      color: #000000DE;
      opacity: 1;
    }

    .login__header img {
      margin-top: 80px;
      left: 772px;
      width: 326px;
      height: 112px;
    }
    .text-dashboard p{
      font: normal normal normal 100 14px/24px Montserrat;
      padding: 20px 0px 30px 0px;
    }
}
    @media (max-width: 768px) {
        .container-text{
            width: 100%;
            height: 100%;
            display: flex;
          flex-direction: column;
          align-items: center; 
            margin-bottom:30px;
        }
        .container-text h1{
          top:60px;
          font: normal normal 900 35px/34px Montserrat;
        }
        .container-text p{
          margin-top:10px;
          width:200px;
          text-align: center;
          font: normal normal 300 28px/25px Montserrat;
          letter-spacing: 1.07px;
        }
      .contenedor{
            top:60px;
            display: flex;
            flex-direction: column;
            width: 100%;
            height: 100vh;
            background-image: 
            linear-gradient(to bottom, rgba(133, 89, 7, 0.7), rgba(0,0,0,0.8)),
            url("../../../assets/images/shared/fondo.png");
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
          }
        .login__container {
          top:0px;
          width: 80%;
          padding: 20px 20px;
          height: auto;
        }
        .login__container p{
          text-align: left;
          font: normal normal normal 14px/24px Montserrat;
          letter-spacing: 0px;
          color: #000000DE;
          opacity: 1;
        }

        
        .login__header {
          width: 100%;
          text-align:center;
        }
        .login__header img {
          margin-top: 0px;
          width: 200px;
          height: 80px;
        }
        .text-dashboard p{
          font: normal normal medium 100 10px/12px Montserrat;
          padding: 20px 0px 20px 0px;
        }
    }
  `,
})
export class LoginComponent {
  version = environment.version;
}
