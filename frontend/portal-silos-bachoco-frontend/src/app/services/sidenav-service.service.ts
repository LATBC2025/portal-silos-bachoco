import { Injectable } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';

@Injectable({
  providedIn: 'root'
})
export class SidenavServiceService {
  private sidenav!: MatSidenav;

  public setSidenav(sidenav: MatSidenav): void {
    this.sidenav = sidenav;
  }

  public open(): void {
    this.sidenav.open();
  }

  public close(): void {
    this.sidenav.close();
  }

  public toggle(): void {
    this.sidenav.toggle();
  }
}
