import { TestBed } from '@angular/core/testing';

import OsmRouteService from '@routes/osm-route.service';

describe('OsmRouteService', () => {
  let service: OsmRouteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OsmRouteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
