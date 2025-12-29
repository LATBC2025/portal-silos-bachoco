import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoaderComponent } from './shared/loader/loader.component';
import { CommonModule } from '@angular/common';
import { loadingState } from './services/load/loading.function';


@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet,LoaderComponent,CommonModule],
    templateUrl: './app.component.html',
    styleUrl: './app.component.css'
})
export class AppComponent {
  loadState=loadingState;
  title = 'portal-proveedores-silo';
}
