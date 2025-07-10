import { TestBed } from '@angular/core/testing';

import { RouteValidationStatusService } from './route-validation-status.service';

describe('RouteValidationStatusService', () => {
  let service: RouteValidationStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RouteValidationStatusService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
