import { ChangeDetectorRef, Component, Input } from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import { AuthServiceService } from '../../../services/auth/auth-service.service';

@Component({
    selector: 'app-profile-info',
    standalone: true,
    imports: [MatIconModule],
    template: `
    <div class="profile d-flex gap-2 align-items-center">

     <div class="profile__avatar">
       <mat-icon class="icon_persona">account_circle</mat-icon>
      </div> 
      <div class="profile__info d-flex flex-column">
        <span class="profile__name fw-semibold">{{ userName }}</span>
        <small class="profile__role fw-semibold">{{ userRole }}</small>
      </div>
    </div>
  `,
    styles: `
  :host {
      display: block;
    }
    .profile{
       height: 98px;
       display: flex;
       justify-content: flex-end;
       padding: 0px 50px 0px 0px;
    }
    .profile__avatar{
      width: 50px;
      height: 50px;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .icon_persona {
        font-size: 48px;   /* ajusta el tamaño */
        height: 48px;
        width: 48px;
    }
    .profile__name {
      font: normal normal medium 14px/20px Montserrat;
      letter-spacing: 0.25px;
    }
    .profile__role{
       font: normal normal medium 14px/20px Montserrat;
      letter-spacing: 0.25px;
    }
     @media (max-width: 768px) {
        .profile__name{
          display:none;
        }
         .profile__role{
          display:none;
        }
        .icon_persona{
          display:none;
        }
        .profile{
          height: 70px;
          display: flex;
          justify-content: flex-end;
          padding: 0px 50px 0px 0px;
        }
    }
  `
})
export class ProfileInfoComponent {
  @Input() userName: string;
  @Input() userRole: string;
  
  constructor() {
    this.userName = '';
    this.userRole = '';
  }
}
