import { TestBed } from '@angular/core/testing';

import { EmpleadoInternoServiceService } from './empleado-interno-service.service';

describe('EmpleadoInternoServiceService', () => {
  let service: EmpleadoInternoServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmpleadoInternoServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
