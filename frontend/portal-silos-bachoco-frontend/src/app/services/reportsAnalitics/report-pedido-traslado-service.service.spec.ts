import { TestBed } from '@angular/core/testing';

import { ReportPedidoTrasladoServiceService } from './report-pedido-traslado-service.service';

describe('ReportPedidoTrasladoServiceService', () => {
  let service: ReportPedidoTrasladoServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportPedidoTrasladoServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
