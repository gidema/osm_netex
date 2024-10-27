import { TestBed } from '@angular/core/testing';

import { NetexRouteService } from './netex-route.service';

describe('NetexRouteService', () => {
  let service: NetexRouteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NetexRouteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
