import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RouteIssueComponent } from './route-issue.component';

describe('RouteIssueComponent', () => {
  let component: RouteIssueComponent;
  let fixture: ComponentFixture<RouteIssueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouteIssueComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RouteIssueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
