import { ComponentFixture, TestBed } from '@angular/core/testing';

import RouteMatchComponent from '@routes/route-match.component';

describe('RouteMatchComponent', () => {
  let component: RouteMatchComponent;
  let fixture: ComponentFixture<RouteMatchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouteMatchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RouteMatchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
