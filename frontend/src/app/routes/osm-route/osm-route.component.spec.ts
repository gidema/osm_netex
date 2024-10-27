import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OsmRouteComponent } from './osm-route.component';

describe('OsmRouteComponent', () => {
  let component: OsmRouteComponent;
  let fixture: ComponentFixture<OsmRouteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OsmRouteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OsmRouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
