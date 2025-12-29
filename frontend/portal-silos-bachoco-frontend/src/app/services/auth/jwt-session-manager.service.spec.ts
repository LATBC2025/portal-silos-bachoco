import { TestBed } from '@angular/core/testing';

import { JwtSessionManagerService } from './jwt-session-manager.service';

describe('JwtSessionManagerService', () => {
  let service: JwtSessionManagerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JwtSessionManagerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
