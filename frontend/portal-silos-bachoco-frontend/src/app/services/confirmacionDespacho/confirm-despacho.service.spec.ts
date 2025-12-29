import { TestBed } from '@angular/core/testing';

import { ConfirmDespachoService } from './confirm-despacho.service';

describe('ConfirmDespachoService', () => {
  let service: ConfirmDespachoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConfirmDespachoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
