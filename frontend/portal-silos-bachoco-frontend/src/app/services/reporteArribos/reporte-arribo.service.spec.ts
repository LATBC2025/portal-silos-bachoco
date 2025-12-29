import { TestBed } from '@angular/core/testing';

import { ReporteArriboService } from './reporte-arribo.service';

describe('ReporteArriboService', () => {
  let service: ReporteArriboService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReporteArriboService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
