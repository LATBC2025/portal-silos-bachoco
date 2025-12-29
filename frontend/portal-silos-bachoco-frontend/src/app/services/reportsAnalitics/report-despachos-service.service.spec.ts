import { TestBed } from '@angular/core/testing';

import { ReportDespachosServiceService } from './report-despachos-service.service';

describe('ReportDespachosServiceService', () => {
  let service: ReportDespachosServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportDespachosServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
