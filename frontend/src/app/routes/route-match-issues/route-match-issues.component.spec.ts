import { ComponentFixture, TestBed } from '@angular/core/testing';

import RouteMatchIssuesComponent from '@routes/route-match-issues.component';

describe('RouteMatchIssuesComponent', () => {
  let component: RouteMatchIssuesComponent;
  let fixture: ComponentFixture<RouteMatchIssuesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouteMatchIssuesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RouteMatchIssuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
