import { TestBed } from '@angular/core/testing';

import { SiloServiceService } from './silo-service.service';

describe('SiloServiceService', () => {
  let service: SiloServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SiloServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
