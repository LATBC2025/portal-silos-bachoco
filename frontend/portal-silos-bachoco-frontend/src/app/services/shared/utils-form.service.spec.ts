import { TestBed } from '@angular/core/testing';

import { UtilsFormService } from './utils-form.service';

describe('UtilsFormService', () => {
  let service: UtilsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UtilsFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
