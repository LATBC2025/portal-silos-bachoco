import { TestBed } from '@angular/core/testing';

import { EmpleadoInternoService } from './empleado-interno.service';

describe('EmpleadoInternoService', () => {
  let service: EmpleadoInternoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmpleadoInternoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
