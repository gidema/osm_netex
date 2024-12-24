import { TestBed } from '@angular/core/testing';

import { NetexQuayInSequenceService } from './netex-quay-in-sequence.service';

describe('NetexQuayInSequenceService', () => {
  let service: NetexQuayInSequenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NetexQuayInSequenceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
