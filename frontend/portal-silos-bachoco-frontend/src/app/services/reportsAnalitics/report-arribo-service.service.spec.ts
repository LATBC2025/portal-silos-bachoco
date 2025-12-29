import { TestBed } from '@angular/core/testing';

import { ReportArriboServiceService } from './report-arribo-service.service';

describe('ReportArriboServiceService', () => {
  let service: ReportArriboServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportArriboServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
