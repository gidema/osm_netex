import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NetexRouteComponent } from './netex-route.component';

describe('NetexRouteComponent', () => {
  let component: NetexRouteComponent;
  let fixture: ComponentFixture<NetexRouteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NetexRouteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NetexRouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
