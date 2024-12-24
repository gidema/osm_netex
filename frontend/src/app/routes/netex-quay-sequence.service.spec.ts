import { TestBed } from '@angular/core/testing';

import { NetexQuaySequenceService } from './NetexQuaySequenceService';

describe('NetexQuaySequenceService', () => {
  let service: NetexQuaySequenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NetexQuaySequenceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
