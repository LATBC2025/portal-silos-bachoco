import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { UtilsService } from '../shared/utils.service';

@Injectable({
  providedIn: 'root'
})
export class ErrorModalServiceService {

  constructor( private utilServ: UtilsService) { }

  showModalErrorNotAuthotize(){
    this.utilServ.showMessageError("Operacion no autorizada");
  }
}
