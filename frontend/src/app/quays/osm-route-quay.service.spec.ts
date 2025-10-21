import { TestBed } from '@angular/core/testing';

import OsmRouteQuayService from '@quays/osm-route-quay.service';

describe('OsmRouteQuayService', () => {
  let service: OsmRouteQuayService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OsmRouteQuayService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
