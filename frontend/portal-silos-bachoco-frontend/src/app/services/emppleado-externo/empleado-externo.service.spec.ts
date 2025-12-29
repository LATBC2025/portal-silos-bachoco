import { TestBed } from '@angular/core/testing';

import { EmpleadoExternoService } from './empleado-externo.service';

describe('EmpleadoExternoService', () => {
  let service: EmpleadoExternoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmpleadoExternoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
