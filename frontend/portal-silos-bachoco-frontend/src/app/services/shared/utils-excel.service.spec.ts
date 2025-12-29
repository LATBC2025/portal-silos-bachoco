import { TestBed } from '@angular/core/testing';

import { UtilsExcelService } from './utils-excel.service';

describe('UtilsExcelService', () => {
  let service: UtilsExcelService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UtilsExcelService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
