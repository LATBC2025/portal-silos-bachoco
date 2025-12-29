import { TestBed } from '@angular/core/testing';

import { PedidoTrasladoService } from './pedido-traslado.service';

describe('PedidoTrasladoService', () => {
  let service: PedidoTrasladoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PedidoTrasladoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
