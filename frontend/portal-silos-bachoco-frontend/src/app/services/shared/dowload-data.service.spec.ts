import { TestBed } from '@angular/core/testing';

import { DowloadDataService } from './dowload-data.service';

describe('DowloadDataService', () => {
  let service: DowloadDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DowloadDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
