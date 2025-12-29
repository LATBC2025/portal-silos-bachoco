import { TestBed } from '@angular/core/testing';

import { PlantaServiceService } from './planta-service.service';

describe('PlantaServiceService', () => {
  let service: PlantaServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlantaServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
