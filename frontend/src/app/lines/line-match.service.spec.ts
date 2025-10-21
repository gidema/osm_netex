import { TestBed } from '@angular/core/testing';

import LineMatchService from '@lines/line-match.service';

describe('LineMatchService', () => {
  let service: LineMatchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LineMatchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
