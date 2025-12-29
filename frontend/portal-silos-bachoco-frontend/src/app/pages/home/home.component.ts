import { Component } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HeaderComponent } from '../../shared/header/header.component';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

@Component({
    selector: 'app-home',
    standalone: true,
    imports: [MatCardModule, MatIconModule, RouterModule, HeaderComponent],
    template: `
    <div class="container-home">
          <app-header class="fade-in component-header" ></app-header>
         <div class="container-text">
<!--            <img
                src="./assets/images/shared/bachocoblanco.svg"
                alt="Logo"
                class="img-fluid"
              /> -->
              <p>Portal Silos Bachoco</p>
          </div>
    </div>
  `,
    styles: `
    :host {
      display: block;
      min-height: 100vh;
      background: var(--mdc-filled-text-field-container-color);
    }

    .bg-gradient-blue {
      background: linear-gradient(180deg, #206FB4 0%, #005AA9 50%, #002262 100%);
    }

    .lottie {
      height: 150px;
      cursor: pointer;
    }

    a {
      text-decoration: none;
    }
    .container-home{
      width:100%;
      height:100vh;
      z-index:1;
      background-image: 
      linear-gradient(to bottom, rgba(255,165,0,0.7), rgba(0,0,0,0.8)),
      url("../../../assets/images/shared/fondo.png");
      background-size: cover;
      background-position: center;
      background-repeat: no-repeat;

    }
    .component-header{
      position:absolute;
      width:97%;
      top:10px;
    }
     .container-text{
       width: 100%;
       height: 100%;
       display: flex;
       flex-direction: column;
       align-items: center; 
    }
    .container-text img {
      position:absolute;
      top: 400px;
      width: 500px;
      height: 200px;
    }

    .container-text p{
      margin-top:290px;
      position: absolute;
      font: normal normal 400 80px/78px Montserrat;
      letter-spacing: 1.07px;
      color: #FFFF;
      opacity: 1;
      text-align:center;
      width:500px;
      top:180px;
     }
    @media (max-width: 1400px) {
          .container-home{
          height:100vh;
          z-index:1;
          background-image: 
          linear-gradient(to bottom, rgba(255,165,0,0.7), rgba(0,0,0,0.8)),
          url("../../../assets/images/shared/fondo.png");
          background-size: cover;
          background-position: center;
          background-repeat: no-repeat;

        }
        .component-header{
          position:absolute;
          width:96%;
          top:10px;
        }
        .container-text{
          width: 100%;
          height: 100%;
          display: flex;
          flex-direction: column;
          align-items: center; 
        }
        .container-text p{
          margin-top:120px;
          position: absolute;
          font: normal normal 300 50px/58px Montserrat;
          letter-spacing: 1.07px;
          color: #FFFF;
          opacity: 1;
          text-align:center;
          width:500px;
          top:200px;
        }
        .container-text img {
          position:absolute;
          top: 270px;
          width: 431px;
          height: 188px;
          right:330px;
        }
    }

     @media (max-width: 768px){
           .component-header{
              position:relative;
              width:97%;
              top:10px;
            }
             .container-text img {
              top: 200px;
              width: 263px;
              height: 90px;
            }
            .container-text p{
              margin-top:50px;
              font: normal normal 200 28px/30px Montserrat;
              width:200px;
              top:250px;
            }
  }

  `
})
export class HomeComponent {
   showWelcomeCard: boolean;
  version: string;

  constructor() {
    this.version = environment.version;
    this.showWelcomeCard = true;
  }

  closeWelcomeCard(): void {
    this.showWelcomeCard = false;
  }
}
