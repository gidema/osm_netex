import { ComponentFixture, TestBed } from '@angular/core/testing';

import LineMatchIssuesComponent from '@routes/route-match-issues.component';

describe('LineMatchIssuesComponent', () => {
  let component: LineMatchIssuesComponent;
  let fixture: ComponentFixture<LineMatchIssuesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LineMatchIssuesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LineMatchIssuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
