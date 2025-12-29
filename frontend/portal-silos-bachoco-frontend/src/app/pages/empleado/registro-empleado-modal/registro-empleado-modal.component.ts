import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import {
  MatDialogClose,
} from '@angular/material/dialog';

@Component({
    selector: 'app-registro-empleado-modal',
    standalone: true,
    imports: [MatButtonModule, MatDialogClose, MatCardModule],
    templateUrl: './registro-empleado-modal.component.html',
    styleUrl: './registro-empleado-modal.component.css'
})
export class RegistroEmpleadoModalComponent {

}
