import { TestBed } from '@angular/core/testing';

import { BodegaServiceService } from './bodega-service.service';

describe('BodegaServiceService', () => {
  let service: BodegaServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BodegaServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
