import { TestBed } from '@angular/core/testing';

import NetexLineServiceService from '@lines/netex-line-service.service';

describe('NetexLineServiceService', () => {
  let service: NetexLineServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NetexLineServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
