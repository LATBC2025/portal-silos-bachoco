import { TestBed } from '@angular/core/testing';

import { ProgramArriboService } from './program-arribo.service';

describe('ProgramArriboService', () => {
  let service: ProgramArriboService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProgramArriboService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
