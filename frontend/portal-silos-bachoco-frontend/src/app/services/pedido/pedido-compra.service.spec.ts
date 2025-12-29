import { TestBed } from '@angular/core/testing';

import { PedidoCompraService } from './pedido-compra.service';

describe('PedidoCompraService', () => {
  let service: PedidoCompraService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PedidoCompraService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
