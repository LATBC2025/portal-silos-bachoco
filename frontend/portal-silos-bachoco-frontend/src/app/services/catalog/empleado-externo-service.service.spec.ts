import { TestBed } from '@angular/core/testing';

import { EmpleadoExternoServiceService } from './empleado-externo-service.service';

describe('EmpleadoExternoServiceService', () => {
  let service: EmpleadoExternoServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmpleadoExternoServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
