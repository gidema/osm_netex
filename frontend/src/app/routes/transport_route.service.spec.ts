import { TestBed } from '@angular/core/testing';

import { TransportRouteService } from './transport_route.service';

describe('RouteService', () => {
  let service: TransportRouteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransportRouteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
