import { TestBed } from '@angular/core/testing';

import NetexRouteQuaysService from '@quays/netex-route-quays.service';

describe('NetexRouteQuaysService', () => {
  let service: NetexRouteQuaysService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NetexRouteQuaysService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
